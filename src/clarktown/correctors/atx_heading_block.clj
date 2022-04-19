(ns clarktown.correctors.atx-heading-block
  (:require
    [clojure.string :as string]))


(defn empty-line-above?
  [lines line index]
  (and (string/starts-with? (string/trim line) "#")
       (> index 0)
       (not (= (-> (nth lines (- index 1))
                   string/trim) ""))))


(defn empty-line-below?
  [lines line index]
  (and (string/starts-with? (string/trim line) "#")
       (< index (- (count lines) 1))
       (not (= (-> (nth lines (+ index 1))
                   string/trim) ""))))