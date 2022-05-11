(ns clarktown.correctors
  (:require
    [clarktown.correctors.fenced-code-block :as fenced-code-block]
    [clarktown.correctors.indented-code-block :as indented-code-block]
    [clarktown.correctors.atx-heading-block :as atx-heading-block]
    [clarktown.correctors.list-block :as list-block]))


(def
  ^{:doc "The default block separation correctors."}
  default-block-separation-correctors
  {:newline-above
   [fenced-code-block/newline-above?
    indented-code-block/newline-above?
    atx-heading-block/newline-above?
    list-block/newline-above?]
   :newline-below
   [fenced-code-block/newline-below?
    indented-code-block/newline-below?
    atx-heading-block/newline-below?
    list-block/newline-below?]})


(def
  ^{:doc "The default correctors."}
  default-correctors
  {:block-separations default-block-separation-correctors})
