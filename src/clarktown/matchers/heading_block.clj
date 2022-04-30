(ns clarktown.matchers.heading-block
  (:require
    [clojure.string :as string]))


(defn is-atx-heading?
  "Determines whether the given block is a atx heading."
  [block]
  (re-matches #"^\#{1,6}\s.*" block))


(defn is-settext-heading?
  "Determines whether the given block is a settext heading."
  [block]
  (let [lines (-> (string/split-lines block))
        chars (-> (last lines)
                  string/trim
                  (string/split #""))]
    (and (> (count lines) 1)
         (every? #{"-" "="} chars))))


(defn match?
  "Determines whether the given block is a heading block."
  [block]
  (or (is-atx-heading? block)
      (is-settext-heading? block)))
