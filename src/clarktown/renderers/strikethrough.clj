(ns clarktown.renderers.strikethrough
  (:require
    [clojure.string :as string]))


(defn render
  "Renders all occurring strikethrough text."
  [block _]
  (loop [block block
         matches (-> (re-seq #"~~.*?~~" block)
                     distinct)]
    (if (empty? matches)
      block
      (let [match (first matches)
            value (subs match 2 (- (count match) 2))
            replacement (str "<del>" value "</del>")]
        (recur (string/replace block match replacement)
               (drop 1 matches))))))
