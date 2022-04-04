(ns clarktown.parsers.ordered-list-block
  (:require
    [clojure.string :as string]
    [clarktown.parser :as parser]))


(defn is?
  "Determines whether we're dealing with a list block or not."
  [block]
  (re-matches #"(?s)^\d\..*$" (string/trim block)))


(defn render
  "Renders the ordered list block"
  [block parsers]
  (loop [result ""
         items (string/split-lines block)]
    (if (empty? items)
      (str "<ol>" result "</ol>")
      (let [value (-> (first items)
                      (string/replace-first #"\d\." "")
                      string/trim
                      (parser/parse parsers))]
        (recur (str result "<li>" value "</li>")
               (drop 1 items))))))
