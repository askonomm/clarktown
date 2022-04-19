(ns clarktown.core
  (:require
    [clojure.string :as string]
    [clarktown.parsers :as parsers]
    [clarktown.correctors :as correctors]))


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


(defn- correct-block-separations
  "Corrects block separations and adds newlines above or
  below a block where needed."
  [correctors lines]
  (->> lines
       (map-indexed
         (fn [index line]
           (let [add-line-above? (some #(true? (% lines line index)) (:empty-line-above? correctors))
                 add-line-below? (some #(true? (% lines line index)) (:empty-line-below? correctors))]
             (cond
               ; If code block starts but there is no empty newline
               ; above, let's fix that
               (and add-line-above?
                    (not add-line-below?))
               (str \newline line)

               ; If the code block ends, but there is no empty newline
               ; below, let's fix that.
               (and add-line-below?
                    (not add-line-above?))
               (str line \newline)

               ; If the code block needs a newline both above and below,
               ; let's fix that.
               (and add-line-above?
                    add-line-below?)
               (str \newline line \newline)

               ; otherwise is what it is
               :else line))))))


(defn- correct-markdown
  "Corrects invalid Markdown for the parser."
  [markdown given-correctors]
  (let [lines (string/split-lines markdown)]
    (->> lines
         (correct-block-separations (:block-separations given-correctors))
         (string/join \newline))))


(defn- find-parser-by-block
  "Find a parser from `parsers` that matches the given `block`."
  [parsers block]
  (->> parsers
       (filter
         (fn [{:keys [matcher]}]
           (when matcher
             (matcher block))))
       first))


(defn- parse-block-with-known-parser
  "Parses a given `block` with a known `parser`."
  [parser parsers block]
  (loop [block block
         renderers (:renderers parser)]
    (if (empty? renderers)
      block
      (let [renderer (first renderers)]
        (recur (renderer block parsers)
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
      (recur (loop [block block
                    renderers (:renderers (first parsers))]
               (if (empty? renderers)
                 block
                 (let [renderer (first renderers)]
                   (recur (renderer block parsers)
                          (drop 1 renderers)))))
             (drop 1 parsers)))))


(defn- parse-blocks
  "Parses each individual Markdown block, given as `blocks`, with
  the list of `parsers`."
  [blocks parsers]
  (for [block blocks]
    (if-let [parser (find-parser-by-block parsers block)]
      (->> (string/trim block)
           (parse-block-with-known-parser parser parsers))
      (->> (string/trim block)
           (parse-block-with-unknown-parsers parsers)))))


(defn parse
  "Parses given `markdown` with `parsers`."
  [markdown given-parsers given-correctors]
  (let [blocks (-> (correct-markdown markdown given-correctors)
                   (string/split #"\n\n")
                   stitch-code-blocks)
        parsed-blocks (parse-blocks blocks given-parsers)]
    (string/join "\n\n" parsed-blocks)))


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
   (render markdown parsers/default-parsers))
  ([markdown given-parsers]
   (render markdown given-parsers correctors/default-correctors))
  ([markdown given-parsers given-correctors]
   (parse markdown given-parsers given-correctors)))