(ns clarktown.parsers.heading-block
  (:require
    [clojure.string :as string]))


(defn is?
  "Determines whether the given block is a heading block or not."
  [block]
  (= true (-> (string/replace block #"\n" "")
              string/trim
              (string/starts-with? "#"))))


(defn render
  "Renders the heading block."
  [block]
  (let [single-line-block (-> (string/replace block #"\n" "")
                              string/trim)
        size-indicators (-> (string/split single-line-block #" ")
                            first
                            string/trim)
        size (count size-indicators)
        value (->> (string/split single-line-block #" ")
                   next
                   (string/join " ")
                   string/trim)]
    (str "<h" size ">" value "</h" size ">")))
