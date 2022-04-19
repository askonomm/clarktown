(ns clarktown.matchers.empty-block
  (:require
    [clojure.string :as string]))


(defn match?
  "Determines if the current block is an empty block or not."
  [block]
  (-> (string/replace block #"\n" "")
      string/trim
      string/blank?))