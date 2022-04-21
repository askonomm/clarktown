(ns clarktown.renderers.quote-block
  (:require
    [clojure.string :as string]
    [clarktown.engine :as engine]))


(defn render
  "Renders a quote block."
  [block parsers correctors]
  (let [matches (re-seq #">.*" block)
        blocks (->> (for [match matches]
                      (-> (subs match 1)
                          string/trim))
                    (string/join "\n"))
        block (engine/render blocks parsers correctors)]
    (str "<blockquote>" block "</blockquote>")))
