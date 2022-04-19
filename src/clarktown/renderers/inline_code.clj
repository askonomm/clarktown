(ns clarktown.renderers.inline-code
  (:require
    [clojure.string :as string]))


(defn render
  "Renders all occurring inline code."
  [block _]
  (loop [block block
         matches (-> (re-seq #"\`.*?\`" block)
                     distinct)]
    (if (empty? matches)
      block
      (let [match (first matches)
            value (-> (subs match 1 (- (count match) 1))
                      (string/replace #"&" "&amp;")
                      (string/replace #"<" "&lt;")
                      (string/replace #">" "&gt;"))
            replacement (str "<code>" value "</code>")]
        (recur (string/replace block match replacement)
               (drop 1 matches))))))
