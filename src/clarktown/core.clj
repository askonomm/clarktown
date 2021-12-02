(ns clarktown.core
  (:require
    [clojure.string :as string]
    [clarktown.parsers.empty-block :as empty-block]
    [clarktown.parsers.heading-block :as heading-block]))


(def default-parsers
  [{:matcher empty-block/is?
    :renderers [empty-block/render]}
   {:matcher heading-block/is?
    :renderers [heading-block/render]}])


(defn find-parser-by-block
  "Find a parser from `parsers` that matches the given `block`."
  [parsers block]
  (->> parsers
       (filter
         (fn [{:keys [matcher]}]
           (matcher block)))
       first))


(defn parse-block-with-known-parser
  "Parses a given `block` with a known `parser`."
  [parser block]
  (loop [block block
         renderers (:renderers parser)]
    (if (empty? renderers)
      block
      (let [renderer (first renderers)]
        (recur (renderer block)
               (drop 1 renderers))))))


(defn- parse-block-with-unknown-parsers
  "Parses the given `block` with all the parsers that do not have
  a matcher function, useful for any fallback parsing one might want
  to do."
  [parsers block]
  (loop [block block
         parsers (filter #(= nil (:matcher %)) parsers)]
    (if (empty? parsers)
      block
      (loop [block block
             renderers (:renderers (first parsers))]
        (if (empty? renderers)
          block
          (let [renderer (first renderers)]
            (recur (renderer block)
                   (drop 1 renderers))))))))


(defn- parse-blocks
  "Parses each individual Markdown block, given as `blocks`, with
  the list of `parsers`."
  [blocks parsers]
  (for [block blocks]
    (if-let [parser (find-parser-by-block parsers block)]
      (parse-block-with-known-parser parser block)
      (parse-block-with-unknown-parsers parsers block))))


(defn- parse
  "Parses given `markdown` with `parsers`."
  [markdown parsers]
  (let [blocks (string/split markdown #"\n\n")
        parsed-blocks (parse-blocks blocks parsers)]
    (string/join "" parsed-blocks)))


(defn render
  "Renders the given `markdown` into a consumable HTML form. Optionally,
  a second argument can be passed that is made out of a vector of parsers.

  A parser is a map that consists of two things;
  - A matcher (optional) , which returns a truthy or falsey value
  - Renderers, which will be run on the a block when matcher returns true.
    There can be any number of renderers.

  Each matcher, and each renderer have to be a function that take a single
  argument, which is a given Markdown block.

  An example parser:
  ```
  {:matcher (fn [block] ...)
   :renderers [(fn [block] ...) (fn [block] ...)]}
  ```"
  ([markdown]
   (render markdown default-parsers))
  ([markdown parsers]
   (parse markdown parsers)))


(render (slurp "/Users/asko/Documents/work/clarktown/test.md"))
