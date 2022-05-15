(ns clarktown.core
  (:require
    [clarktown.engine :as engine]
    [clarktown.parsers :as parsers]
    [clarktown.correctors :as correctors]))


(defn render
  "Renders the given `markdown` into a consumable HTML form. Optionally,
  a second argument can be passed to overwrite default parsers and
  correctors.`"
  ([markdown]
   (render markdown {}))
  ([markdown {:keys [parsers correctors] :or
              {parsers parsers/default-parsers
               correctors correctors/default-correctors}}]
   (engine/render markdown parsers correctors)))
