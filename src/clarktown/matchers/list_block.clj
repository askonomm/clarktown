(ns clarktown.matchers.list-block
  (:require
    [clojure.string :as string]))


(defn match?
  "Determines whether we're dealing with a list block or not."
  [block]
  (->> (string/trim block)
       (re-matches #"(?s)^(\d\.\s|\*{1}\s|\-{1}\s).*$")))