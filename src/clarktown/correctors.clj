(ns clarktown.correctors
  (:require
    [clarktown.correctors.code-block :as code-block]
    [clarktown.correctors.atx-heading-block :as atx-heading-block]))


(def block-separation-correctors
  {:empty-line-above?
   [code-block/empty-line-above?
    atx-heading-block/empty-line-above?]
   :empty-line-below?
   [code-block/empty-line-below?
    atx-heading-block/empty-line-below?]})


(def default-correctors
  {:block-separations block-separation-correctors})