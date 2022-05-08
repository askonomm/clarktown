(ns clarktown.correctors.atx-heading-block
  (:require
    [clojure.string :as string]
    [clarktown.matchers.heading-block :refer [is-atx-heading?]]))


(defn- in-code-block?
  "Determines whether the current `line` is within a code block."
  [lines index]
  (->> (take index lines)
       (filter #(string/starts-with? (string/trim %) "```"))
       count
       odd?))


(defn newline-above?
  "Determines whether there's a need for an empty new line 
  above the `line` at the current `index`. In the case of a 
  ATX heading block that starts with the `#` character, if 
  there's no empty newline above, we need to create one, and 
  so this function must then return `true`."
  [lines line index]
  (and (is-atx-heading? (string/trim line))
       (> index 0)
       (not (= (-> (nth lines (- index 1))
                   string/trim) ""))
       (not (in-code-block? lines index))))


(defn newline-below?
  "Determines whether there's a need for an empty new line
  below the `line` at the current `index`. In the case of a 
  ATX heading block that starts with the `#` character, if
  there's no empty newline below, we need to create one, and
  so this function must then return `true`."
  [lines line index]
  (and (is-atx-heading? line)
       (< index (- (count lines) 1))
       (not (= (-> (nth lines (+ index 1))
                   string/trim) ""))
       (not (in-code-block? lines index))))
