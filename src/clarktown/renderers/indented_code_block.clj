(ns clarktown.renderers.indented-code-block
  (:require
    [clojure.string :as string]))


(defn render
  "Renders the indented code block."
  [block _ _]
  (str
    "<pre><code>"
    (-> (->> (string/split-lines block)
             (map #(subs % 4))
             (string/join \newline))
        (string/replace #"&" "&amp;")
        (string/replace #"<" "&lt;")
        (string/replace #">" "&gt;"))
    "</code></pre>"))