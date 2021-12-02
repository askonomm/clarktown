(ns clarktown.parsers.heading-block
  (:require
    [clojure.string :as string]))

(defn is?
  [block]
  (= true (-> (clojure.string/trim block)
              (string/starts-with? "#"))))


(defn render
  [block]
  "headingblock")
