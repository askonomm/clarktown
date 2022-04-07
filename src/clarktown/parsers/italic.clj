(ns clarktown.parsers.italic
  (:require
    [clojure.string :as string]))


(defn render
  "Renders all occurring italic text as italic."
  [block _]
  (loop [block block
         matches (-> (re-seq #"(\*{1,}?|\_{1,}?)(.*?)(\*{1,}?|\_{1,}?)" block)
                     distinct)]
    (if (empty? matches)
      block
      (let [match (ffirst matches)
            value (subs match 1 (- (count match) 1))
            replacement (str "<em>" value "</em>")]
        (recur (string/replace block match replacement)
               (drop 1 matches))))))
