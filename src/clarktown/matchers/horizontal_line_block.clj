(ns clarktown.matchers.horizontal-line-block
  (:require
    [clojure.string :as string]))


(defn match?
  "Determines whether the given block is a horizontal line block."
  [block]
  (or (= "***" (string/trim block))
      (= "---" (string/trim block))))