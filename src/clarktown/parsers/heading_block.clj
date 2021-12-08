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
  [block _]
  (let [single-line-block (-> (string/replace block #"\n" "")
                              string/trim)
        size (-> (string/split single-line-block #" ")
                 first
                 string/trim
                 count)
        value (->> (string/split single-line-block #" ")
                   next
                   (string/join " ")
                   string/trim)]
    (str "<h" size ">" value "</h" size ">")))
