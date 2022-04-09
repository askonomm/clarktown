(ns clarktown.parsers.link-and-image
  (:require
    [clojure.string :as string]))


(defn render
  "Renders all occurring links and images."
  [block _]
  (loop [block block
         matches (-> (re-seq #"\!?\[(\w+( \w+|\.|\,)*)\]\((.*?)\)" block)
                     distinct)]
    (if (empty? matches)
      block
      (let [[whole-match label _ href] (first matches)
            image? (string/starts-with? whole-match "!")
            image (str "<img src=\"" href "\" alt=\"" label "\">")
            link (str "<a href=\"" href "\">" label "</a>")]
        (recur (if image?
                 (string/replace block whole-match image)
                 (string/replace block whole-match link))
               (drop 1 matches))))))