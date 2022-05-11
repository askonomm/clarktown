(ns clarktown.renderers.heading-block
  (:require
    [clojure.string :as string]
    [clarktown.matchers.heading-block :as matcher])
  (:import
    (java.text Normalizer Normalizer$Form)))


(defn- slugify
  "Turn `input` in a URL slug."
  [input]
  (let [split-s(-> (Normalizer/normalize input Normalizer$Form/NFD)
                   (string/replace #"[\P{ASCII}]+" "")
                   string/lower-case
                   string/triml
                   (string/split #"[\p{Space}\p{P}]+"))
        combined (string/join "-" split-s)]
    (apply str (take 250 combined))))
  

(defn render-atx-heading
  "Renders the hashbang heading block."
  [block]
  (let [single-line-block (string/trim block)
        size (-> (string/split single-line-block #" ")
                 first
                 string/trim
                 count)
        value (->> (string/split single-line-block #" ")
                   next
                   (string/join " ")
                   string/trim)
        id (slugify value)]
    (str "<h" size " id=\"" id "\">" value "</h" size ">")))


(defn render-settext-heading
  "Renders the settext heading block."
  [block]
  (let [lines (string/split-lines block)
        value (->> (split-at (- (count lines) 1) lines)
                   first
                   (string/join "\n"))
        h1? (= "=" (-> (last lines)
                       string/trim
                       (string/split #"")
                       first))
        id (slugify value)]
    (if h1?
      (str "<h1 id=\"" id "\">" value "</h1>")
      (str "<h2 id=\"" id "\">" value "</h2>"))))


(defn render
  "Renders the heading block."
  [block _ _]
  (cond (matcher/is-atx-heading? block)
        (render-atx-heading block)
        
        (matcher/is-settext-heading? block)
        (render-settext-heading block)
        
        :else block))
