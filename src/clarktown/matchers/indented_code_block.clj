(ns clarktown.matchers.indented-code-block
  (:require
    [clojure.string :as string]))


(defn match?
  "Determines whether the given block is a indented code block."
  [block]
  (->> (string/split-lines block)
       (every? #(>= (count (take-while #{\space} %)) 4))))