(ns clarktown.parsers.bold
  (:require
    [clojure.string :as string]))


(defn render
  "Renders all occurring bold text as bold."
  [block _]
  (loop [block block
         matches (-> (re-seq #"(\*{2}|\_{2})(.*?)(\*{2}|\_{2})" block)
                     distinct)]
    (if (empty? matches)
      block
      (let [match (ffirst matches)
            value (subs match 2 (- (count match) 2))
            replacement (str "<strong>" value "</strong>")]
        (recur (string/replace block match replacement)
               (drop 1 matches))))))
