(ns clarktown.renderers.quote-block
  (:require
    [clojure.string :as string]
    [clarktown.parser :as parser]))


(defn render
  "Renders a quote block."
  [block parsers]
  (let [matches (re-seq #">.*" block)
        blocks (->> (for [match matches]
                      (-> (subs match 1)
                          string/trim))
                    (string/join "\n"))
        block (parser/parse blocks parsers)]
    (str "<blockquote>" block "</blockquote>")))
