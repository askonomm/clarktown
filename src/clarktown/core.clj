(ns clarktown.core
  (:require
    [clarktown.parser :as parser]
    [clarktown.parsers :as parsers]))


(defn render
  "Renders the given `markdown` into a consumable HTML form. Optionally,
  a second argument can be passed that is made out of a vector of parsers.

  A parser is a map that consists of two things;
  - A matcher (optional) , which returns a truthy or falsey value
  - Renderers, which will be run on the a block when matcher returns true.
    There can be any number of renderers. Each renderer must return a string.

  Each matcher, and each renderer have to be a function that take a single
  argument, which is a given Markdown block.

  An example parser:
  ```
  {:matcher (fn [block] ...)
   :renderers [(fn [block] ...) (fn [block] ...)]}
  ```"
  ([markdown]
   (render markdown parsers/parsers))
  ([markdown given-parsers]
   (parser/parse markdown given-parsers)))

(comment
  (render (slurp "./test.md")))