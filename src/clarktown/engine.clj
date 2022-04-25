(ns clarktown.engine
  (:require
    [clojure.string :as string]))


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
  (let [above-correctors (:newline-above correctors)
        below-correctors (:newline-below correctors)]
    (map-indexed
      (fn [index line]
        (let [add-newline-above? (some #(true? (% lines line index)) above-correctors)
              add-newline-below? (some #(true? (% lines line index)) below-correctors)]
          (cond
            ; If code block starts but there is no empty newline
            ; above, let's fix that
            (and add-newline-above?
                 (not add-newline-below?))
            (str \newline line)

            ; If the code block ends, but there is no empty newline
            ; below, let's fix that.
            (and add-newline-below?
                 (not add-newline-above?))
            (str line \newline)

            ; If the code block needs a newline both above and below,
            ; let's fix that.
            (and add-newline-above?
                 add-newline-below?)
            (str \newline line \newline)

            ; otherwise is what it is
            :else line)))
      lines)))


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
  [parser given-parsers given-correctors block]
  (loop [block block
         renderers (:renderers parser)]
    (if (empty? renderers)
      block
      (let [renderer (first renderers)]
        (recur (renderer block given-parsers given-correctors)
               (drop 1 renderers))))))


(defn- parse-block-with-unknown-parsers
  "Parses the given `block` with all the parsers that do not have
  a matcher function, useful for any fallback parsing one might want
  to do."
  [given-parsers given-correctors block]
  (loop [block block
         parsers (filter #(= nil (:matcher %)) given-parsers)]
    (if (empty? parsers)
      block
      (recur (loop [block block
                    renderers (:renderers (first parsers))]
               (if (empty? renderers)
                 block
                 (let [renderer (first renderers)]
                   (recur (renderer block parsers given-correctors)
                          (drop 1 renderers)))))
             (drop 1 parsers)))))


(defn- parse-blocks
  "Parses each individual Markdown block, given as `blocks`, with
  the list of `parsers`."
  [blocks given-parsers given-correctors]
  (for [block blocks]
    (if-let [parser (find-parser-by-block given-parsers block)]
      (->> (string/trim block)
           (parse-block-with-known-parser parser given-parsers given-correctors))
      (->> (string/trim block)
           (parse-block-with-unknown-parsers given-parsers given-correctors)))))


(defn render
  "Parses given `markdown` with `parsers`."
  [markdown given-parsers given-correctors]
  (let [blocks (-> (correct-markdown markdown given-correctors)
                   (string/split #"\n\n")
                   stitch-code-blocks)
        parsed-blocks (parse-blocks blocks given-parsers given-correctors)]
    (string/join "\n\n" parsed-blocks)))
