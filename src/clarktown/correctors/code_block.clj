(ns clarktown.correctors.code-block
  (:require
    [clojure.string :as string]))


(defn empty-line-above?
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
  [lines line index]
  (and (string/starts-with? (string/trim line) "```")
       (< index (- (count lines) 1))
       (->> (take index lines)
            (filter #(string/starts-with? (string/trim %) "```"))
            count
            odd?)
       (not (= (-> (nth lines (+ index 1))
                   string/trim) ""))))
