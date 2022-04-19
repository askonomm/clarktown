(ns clarktown.renderers.paragraph-block
  (:require
    [clojure.string :as string]))


(defn render
  "Renders the paragraph block."
  [block _]
  (str "<p>" (string/trim block) "</p>"))
