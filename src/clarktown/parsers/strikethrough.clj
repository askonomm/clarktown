(ns clarktown.parsers.strikethrough
  (:require
    [clojure.string :as string]))


(defn render
  "Renders all occuring strikethrough text."
  [block]
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
