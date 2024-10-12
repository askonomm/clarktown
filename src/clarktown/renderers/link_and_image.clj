(ns clarktown.renderers.link-and-image
  (:require
    [clojure.string :as string]))


(defn encode-href
  [href]
  (-> href
      (string/replace "_" "&#95;")))


(defn render
  "Renders all occurring links and images."
  [block _ _]
  (loop [block block
         matches (-> (re-seq #"\!?\[([a-zA-Z0-9\-\_\.\,\']*( [a-zA-Z0-9\-\_\.\,\']+)*)\]\((.*?)\)" block)
                     distinct)]
    (if (empty? matches)
      block
      (let [[whole-match label _ href] (first matches)
            image? (string/starts-with? whole-match "!")
            image (str "<img src=\"" (encode-href href) "\" alt=\"" label "\">")
            link (str "<a href=\"" (encode-href href) "\">" label "</a>")]
        (recur (if image?
                 (string/replace block whole-match image)
                 (string/replace block whole-match link))
               (drop 1 matches))))))
