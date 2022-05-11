(ns clarktown.parsers
  (:require
    [clarktown.matchers.empty-block]
    [clarktown.renderers.empty-block]
    [clarktown.matchers.horizontal-line-block]
    [clarktown.renderers.horizontal-line-block]
    [clarktown.matchers.heading-block]
    [clarktown.renderers.heading-block]
    [clarktown.matchers.quote-block]
    [clarktown.renderers.quote-block]
    [clarktown.matchers.fenced-code-block]
    [clarktown.renderers.fenced-code-block]
    [clarktown.matchers.indented-code-block]
    [clarktown.renderers.indented-code-block]
    [clarktown.matchers.list-block]
    [clarktown.renderers.list-block]
    [clarktown.renderers.paragraph-block]
    [clarktown.renderers.link-and-image]
    [clarktown.renderers.bold]
    [clarktown.renderers.italic]
    [clarktown.renderers.inline-code]
    [clarktown.renderers.strikethrough]))


(def
  ^{:doc "Detects, parses and renders a empty block."}
  empty-block-parser
  {:matcher clarktown.matchers.empty-block/match?
   :renderers [clarktown.renderers.empty-block/render]})


(def
  ^{:doc "Detects, parses and renders a horizontal line block."}
  horizontal-line-block-parser
  {:matcher clarktown.matchers.horizontal-line-block/match?
   :renderers [clarktown.renderers.horizontal-line-block/render]})


(def
  ^{:doc "Detects, parses and renders a heading block."}
  heading-block-parser
  {:matcher clarktown.matchers.heading-block/match?
   :renderers [clarktown.renderers.link-and-image/render
               clarktown.renderers.bold/render
               clarktown.renderers.italic/render
               clarktown.renderers.inline-code/render
               clarktown.renderers.strikethrough/render
               clarktown.renderers.heading-block/render]})


(def
  ^{:doc "Detects, parses and renders a quote block."}
  quote-block-parser
  {:matcher clarktown.matchers.quote-block/match?
   :renderers [clarktown.renderers.quote-block/render]})


(def
  ^{:doc "Detects, parses and renders a fenced code block."}
  fenced-code-block-parser
  {:matcher clarktown.matchers.fenced-code-block/match?
   :renderers [clarktown.renderers.fenced-code-block/render]})


(def
  ^{:doc "Detects, parses and renders a indented code block."}
  indented-code-block-parser
  {:matcher clarktown.matchers.indented-code-block/match?
   :renderers [clarktown.renderers.indented-code-block/render]})


(def
  ^{:doc "Detects, parses and renders a list block."}
  list-block-parser
  {:matcher clarktown.matchers.list-block/match?
   :renderers [clarktown.renderers.link-and-image/render
               clarktown.renderers.bold/render
               clarktown.renderers.italic/render
               clarktown.renderers.inline-code/render
               clarktown.renderers.strikethrough/render
               clarktown.renderers.list-block/render]})


(def
  ^{:doc "Parses and renders a quote block."}
  paragraph-block-parser
  {:renderers [clarktown.renderers.link-and-image/render
               clarktown.renderers.bold/render
               clarktown.renderers.italic/render
               clarktown.renderers.inline-code/render
               clarktown.renderers.strikethrough/render
               clarktown.renderers.paragraph-block/render]})


(def
  ^{:doc "A set of default parsers."}
  default-parsers
  [empty-block-parser
   horizontal-line-block-parser
   heading-block-parser
   quote-block-parser
   fenced-code-block-parser
   indented-code-block-parser
   list-block-parser
   paragraph-block-parser])
