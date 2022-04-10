(ns clarktown.parsers.horizontal-line-block
  (:require
    [clojure.string :as string]))


(defn is?
  "Determines whether the given block is a horizontal line block."
  [block]
  (or (= "***" (string/trim block))
      (= "---" (string/trim block))))


(defn render
  "Renders the horizontal line block."
  [_ _]
  "<hr>")
