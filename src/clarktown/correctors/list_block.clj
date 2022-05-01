(ns clarktown.correctors.list-block
  (:require
    [clojure.string :as string]
    [clarktown.matchers.list-block :as matcher]))


(defn empty-line-above?
  "Determines whether there's a need for an empty new line
  above the `line` at the current `index`. In the list block 
  case that's when the above line is not a newline and is not
  a list block line."
  [lines line index]
  (and (matcher/match? line)
       (> index 0)
       (nil? (-> (nth lines (- index 1))
                 string/trim
                 matcher/match?))
       (not (= (-> (nth lines (- index 1))
                   string/trim)     
               "")))) 
 
               
(defn empty-line-below?
  "Determines whether there's a need for an empty new line
  above the `line` at the current `index`. In the list block 
  case that's when the below line is not a newline and is not
  a list block line."
  [lines line index]
  (and (matcher/match? line)
       (> (- (count lines) 1) index)
       (nil? (-> (nth lines (+ index 1))
                 string/trim
                 matcher/match?))
       (not (= (-> (nth lines (+ index 1))
                   string/trim)
               ""))))
