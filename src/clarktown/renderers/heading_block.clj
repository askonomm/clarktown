(ns clarktown.renderers.heading-block
  (:require
    [clojure.string :as string]
    [clarktown.matchers.heading-block :as matcher]))


(defn render-atx-heading
  "Renders the hashbang heading block."
  [block]
  (let [single-line-block (-> (string/replace block #"\n" "")
                              string/trim)
        size (-> (string/split single-line-block #" ")
                 first
                 string/trim
                 count)
        value (->> (string/split single-line-block #" ")
                   next
                   (string/join " ")
                   string/trim)]
    (str "<h" size ">" value "</h" size ">")))


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
                       first))]
    (if h1?
      (str "<h1>" value "</h1>")
      (str "<h2>" value "</h2>"))))


(defn render
  "Renders the heading block."
  [block _ _]
  (if (matcher/is-atx-heading? block)
    (render-atx-heading block)
    (render-settext-heading block)))
