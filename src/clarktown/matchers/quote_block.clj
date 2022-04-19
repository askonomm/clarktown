(ns clarktown.matchers.quote-block
  (:require
    [clojure.string :as string]))


(defn match?
  "Determines whether the given block is a quote block."
  [block]
  (-> (string/replace block #"\n" "")
      string/trim
      (string/starts-with? ">")))