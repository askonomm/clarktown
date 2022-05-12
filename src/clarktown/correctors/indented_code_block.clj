(ns clarktown.correctors.indented-code-block
  (:require
    [clojure.string :as string]
    [clarktown.matchers.list-block :as list-block-matcher]))


(defn- in-code-block?
  "Determines whether the current `line` is within a code block."
  [lines index]
  (->> (take index lines)
       (filter #(string/starts-with? (string/trim %) "```"))
       count
       odd?))


(defn newline-above?
  "Determines whether there's a need for an empty new line
  above the `line` at the current `index`. In the case of a
  code block, which starts with three backticks (```), if there's
  no empty newline above, we need to create one, and so this
  function must then return `true`."
  [lines line index]
  (let [space-count (count (take-while #{\space} line))
        not-first? (> index 0)
        prev-line (when not-first?
                    (nth lines (- index 1)))
        space-count-prev-line (count (take-while #{\space} prev-line))]
    (and (>= space-count 4)
         not-first?
         (not (in-code-block? lines index))
         (not (list-block-matcher/match? line))
         (not (list-block-matcher/match? prev-line))
         (not (>= space-count-prev-line 4))
         (not (= (-> prev-line
                     string/trim)
                 "")))))

(defn newline-below?
  "Determines whether there's a need for an empty new line
  below the `line` at the current `index`. In the case of a
  code block, which ends with three backticks (```), if there's
  no empty newline above, we need to create one, and so this
  function must then return `true`."
  [lines line index]
  (let [space-count (count (take-while #{\space} line))
        not-last? (< index (- (count lines) 1))
        next-line (when not-last?
                    (nth lines (+ index 1)))
        space-count-next-line (count (take-while #{\space} next-line))]
    (and (>= space-count 4)
         not-last?
         (not (in-code-block? lines index))
         (not (list-block-matcher/match? line))
         (not (list-block-matcher/match? next-line))
         (not (>= space-count-next-line 4))
         (not (= (-> next-line
                     string/trim) "")))))
