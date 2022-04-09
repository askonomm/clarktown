(ns clarktown.parsers.heading-block
  (:require
    [clojure.string :as string]))


(defn is-hashbang-heading?
  "Determines whether the given block is a hashbang heading."
  [block]
  (-> (string/replace block #"\n" "")
      string/trim
      (string/starts-with? "#")))


(defn is-settext-heading?
  "Determines whether the given block is a settext heading."
  [block]
  (let [lines (-> (string/split-lines block))
        chars (-> (last lines)
                  string/trim
                  (string/split #""))]
    (and (> (count lines) 1)
         (every? #{"-" "="} chars))))


(defn is?
  "Determines whether the given block is a heading block."
  [block]
  (or (is-hashbang-heading? block)
      (is-settext-heading? block)))


(defn render-hashbang-heading
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


(render-settext-heading "Hello world\nAnd you too\n===")


(defn render
  "Renders the heading block."
  [block _]
  (if (is-hashbang-heading? block)
    (render-hashbang-heading block)
    (render-settext-heading block)))
