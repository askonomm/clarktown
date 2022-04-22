(ns clarktown.correctors.code-block
  (:require
    [clojure.string :as string]))


(defn empty-line-above?
  "Determines whether there's a need for an empty new line
  above the `line` at the current `index`. In the case of a 
  code block, which starts with three backticks (```), if there's
  no empty newline above, we need to create one, and so this
  function must then return `true`."
  [lines line index]
  (and (string/starts-with? (string/trim line) "```")
       (> index 0)
       (->> (take index lines)
            (filter #(string/starts-with? (string/trim %) "```"))
            count
            even?)
       (not (= (-> (nth lines (- index 1))
                   string/trim) ""))))


(defn empty-line-below?
  "Determines whether there's a need for an empty new line
  below the `line` at the current `index`. In the case of a 
  code block, which ends with three backticks (```), if there's
  no empty newline above, we need to create one, and so this 
  function must then return `true`."
  [lines line index]
  (and (string/starts-with? (string/trim line) "```")
       (< index (- (count lines) 1))
       (->> (take index lines)
            (filter #(string/starts-with? (string/trim %) "```"))
            count
            odd?)
       (not (= (-> (nth lines (+ index 1))
                   string/trim) ""))))
