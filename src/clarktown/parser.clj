(ns clarktown.parser
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


(defn- needs-empty-line-above?
  "Determines whether the current line needs an empty line correction
  above."
  [lines line index]
  (cond
    ; code block
    (and (= (string/trim line) "```")
         (> index 0)
         (->> (take index lines)
              (filter #(= (string/trim %) "```"))
              count
              odd?)
         (not (= (-> (nth lines (- index 1))
                     string/trim) "")))
    true

    ; ATX heading block
    (and (string/starts-with? (string/trim line) "#")
         (> index 0)
         (not (= (-> (nth lines (- index 1))
                     string/trim) "")))
    true


    ; everything else stays normal
    :else false))


(defn- needs-empty-line-below?
  "Determines whether the current line needs an empty line correction
  below."
  [lines line index]
  (cond
    ; code block
    (and (= (string/trim line) "```")
         (< index (- (count lines) 1))
         (->> (take index lines)
              (filter #(= (string/trim %) "```"))
              count
              even?)
         (not (= (-> (nth lines (+ index 1))
                     string/trim) "")))
    true

    ; ATX heading block
    (and (string/starts-with? (string/trim line) "#")
         (< index (- (count lines) 1))
         (not (= (-> (nth lines (+ index 1))
                     string/trim) "")))
    true

    ; everything else stays normal
    :else false))


(defn- correct-block-separations
  "Corrects block separations and adds newlines above or
  below a block where needed."
  [lines]
  (->> lines
       (map-indexed
         (fn [index line]
           (let [add-line-above? (needs-empty-line-above? lines line index)
                 add-line-below? (needs-empty-line-below? lines line index)]
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
  [markdown]
  (let [lines (string/split-lines markdown)]
    (->> lines
         correct-block-separations
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
  [markdown parsers]
  (let [blocks (-> (correct-markdown markdown)
                   (string/split #"\n\n")
                   stitch-code-blocks)
        parsed-blocks (parse-blocks blocks parsers)]
    (string/join "\n\n" parsed-blocks)))
