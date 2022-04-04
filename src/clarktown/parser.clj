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
      (loop [block block
             renderers (:renderers (first parsers))]
        (if (empty? renderers)
          block
          (let [renderer (first renderers)]
            (recur (renderer block parsers)
                   (drop 1 renderers))))))))


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
  (let [blocks (-> (string/split markdown #"\n\n")
                   stitch-code-blocks)
        parsed-blocks (parse-blocks blocks parsers)]
    (string/join "" parsed-blocks)))
