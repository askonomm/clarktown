(ns clarktown.parsers
  (:require
    [clarktown.parsers.bold :as bold]
    [clarktown.parsers.italic :as italic]
    [clarktown.parsers.inline-code :as inline-code]
    [clarktown.parsers.strikethrough :as strikethrough]
    [clarktown.parsers.link-and-image :as link-and-image]
    [clarktown.parsers.empty-block :as empty-block]
    [clarktown.parsers.horizontal-line-block :as horizontal-line-block]
    [clarktown.parsers.quote-block :as quote-block]
    [clarktown.parsers.heading-block :as heading-block]
    [clarktown.parsers.code-block :as code-block]
    [clarktown.parsers.list-block :as list-block]
    [clarktown.parsers.paragraph-block :as paragraph-block]))


(def parsers
  [{:matcher empty-block/is?
    :renderers [empty-block/render]}
   {:matcher horizontal-line-block/is?
    :renderers [horizontal-line-block/render]}
   {:matcher heading-block/is?
    :renderers [link-and-image/render
                bold/render
                italic/render
                inline-code/render
                strikethrough/render
                heading-block/render]}
   {:matcher quote-block/is?
    :renderers [quote-block/render]}
   {:matcher code-block/is?
    :renderers [code-block/render]}
   {:matcher list-block/is?
    :renderers [link-and-image/render
                bold/render
                italic/render
                inline-code/render
                strikethrough/render
                list-block/render]}
   {:renderers [link-and-image/render
                bold/render
                italic/render
                inline-code/render
                strikethrough/render
                paragraph-block/render]}])
