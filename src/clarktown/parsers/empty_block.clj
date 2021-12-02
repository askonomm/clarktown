(ns clarktown.parsers.empty-block
  (:require
    [clojure.string :as string]))


(defn is?
  [block]
  (string/blank? (string/trim block)))


(defn render
  [block]
  "emptyblockgoeshere")
