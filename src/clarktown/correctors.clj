(ns clarktown.correctors
  (:require
    [clarktown.correctors.code-block :as code-block]
    [clarktown.correctors.atx-heading-block :as atx-heading-block]
    [clarktown.correctors.list-block :as list-block]))


(def
  ^{:doc "The default block separation correctors."}
  default-block-separation-correctors
  {:newline-above
   [code-block/empty-line-above?
    atx-heading-block/empty-line-above?
    list-block/empty-line-above?]
   :newline-below
   [code-block/empty-line-below?
    atx-heading-block/empty-line-below?
    list-block/empty-line-below?]})


(def
  ^{:doc "The default correctors."}
  default-correctors
  {:block-separations default-block-separation-correctors})
