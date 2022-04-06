# Clarktown

A zero-dependency Markdown parser for Clojure projects.

## Usage example

```clojure
(ns myapp.core
  (:require [clarktown.core :as clarktown]))

(clarktown/render "**Hello, world!**")
```