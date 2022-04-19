(ns clarktown.matchers.code-block
  (:require
    [clojure.string :as string]))


(defn match?
  "Determines whether we're dealing with a code block."
  [block]
  (and (string/starts-with? block "```")
       (string/ends-with? block "```")))