(ns clarktown.parsers.empty-block
  (:require
    [clojure.string :as string]))


(defn is?
  "Determines if the current block is an empty block or not."
  [block]
  (-> (string/replace block #"\n" "")
      string/trim
      string/blank?))


(defn render
  "Renders an empty block."
  [_ _]
  "")
