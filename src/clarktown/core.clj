(ns clarktown.core
  (:require
    [clojure.string :as string]
    [clarktown.parsers.bold :as bold]
    [clarktown.parsers.italic :as italic]
    [clarktown.parsers.inline-code :as inline-code]
    [clarktown.parsers.strikethrough :as strikethrough]
    [clarktown.parsers.link-and-image :as link-and-image]
    [clarktown.parsers.empty-block :as empty-block]
    [clarktown.parsers.horizontal-line-block :as horizontal-line-block]
    [clarktown.parsers.heading-block :as heading-block]))


(def default-parsers
  [{:matcher empty-block/is?
    :renderers [empty-block/render]}
   {:matcher horizontal-line-block/is?
    :renderers [horizontal-line-block/render]}
   {:matcher heading-block/is?
    :renderers [bold/render
                italic/render
                inline-code/render
                strikethrough/render
                link-and-image/render
                heading-block/render]}
   {:renderers [bold/render
                italic/render
                inline-code/render
                strikethrough/render
                link-and-image/render]}])


(defn- stitch-code-blocks
  "Since code blocks can span multiple blocks (a block is separated by
  two line breaks from another block) , we need to stitch them together
  into one block in order for a block parser to be able to do anything
  with it."
  [blocks]
  (loop [stitched-blocks []
         code-block-started? false
         blocks blocks]
    (if (empty? blocks)
      stitched-blocks
      (let [block (first blocks)]
        (if (and (string/starts-with? (string/trim block) "```")
                 (not (string/ends-with? (string/trim block) "```")))
          (recur (conj stitched-blocks block)
                 true
                 (drop 1 blocks))
          (if code-block-started?
            (let [last-block (last stitched-blocks)
                  last-block-index (- (count stitched-blocks) 1)]
              (if (string/ends-with? (string/trim block) "```")
                (recur (assoc stitched-blocks last-block-index (str last-block "\n\n" block))
                       false
                       (drop 1 blocks))
                (recur (assoc stitched-blocks last-block-index (str last-block "\n\n" block))
                       true
                       (drop 1 blocks))))
            (recur (conj stitched-blocks block)
                   false
                   (drop 1 blocks))))))))


(defn- find-parser-by-block
  "Find a parser from `parsers` that matches the given `block`."
  [parsers block]
  (->> parsers
       (filter
         (fn [{:keys [matcher]}]
           (matcher block)))
       first))


(defn- parse-block-with-known-parser
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
  (let [blocks (-> (string/split markdown #"\n\n")
                   stitch-code-blocks)
        parsed-blocks (parse-blocks blocks parsers)]
    (string/join "" parsed-blocks)))


(defn render
  "Renders the given `markdown` into a consumable HTML form. Optionally,
  a second argument can be passed that is made out of a vector of parsers.

  A parser is a map that consists of two things;
  - A matcher (optional) , which returns a truthy or falsey value
  - Renderers, which will be run on the a block when matcher returns true.
    There can be any number of renderers. Each renderer must return a string.

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
