(ns clarktown.parsers.quote-block
  (:require
    [clojure.string :as string]
    [clarktown.parser :as parser]))


(defn is?
  "Determines whether the given block is a quote block."
  [block]
  (-> (string/replace block #"\n" "")
      string/trim
      (string/starts-with? ">")))


(defn render
  "Renders a quote block."
  [block parsers]
  (let [matches (re-seq #">.*" block)
        blocks (for [match matches]
                  (-> (subs match 1)
                      string/trim
                      (parser/parse parsers)))]
    (str "<blockquote>" (string/join "\n" blocks) "</blockquote>")))
