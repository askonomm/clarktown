# Clarktown

[![Tests](https://github.com/askonomm/clarktown/actions/workflows/tests.yml/badge.svg)](https://github.com/askonomm/clarktown/actions/workflows/tests.yml)

An extensible and modular zero-dependency, pure-Clojure Markdown parser.

## Installation

#### Leiningen/Boot

```
[com.github.askonomm/clarktown "2.0.0"]
```

#### Clojure CLI/deps.edn

```
com.github.askonomm/clarktown {:mvn/version "2.0.0"}
```

## Basic usage example

```clojure
(ns myapp.core
  (:require [clarktown.core :as clarktown]))

(clarktown/render "**Hello, world!**") ; => <strong>Hello, world!</strong>
```

## Built-in parsers

At its core, Clarktown is nothing more than a collection of parsers that collectively make up a Markdown parser. Those parsers are:

- `bold`
- `code-block`
- `empty-block`
- `heading-block`
- `horizontal-line-block`
- `inline-code`
- `italic`
- `link-and-image`
- `list-block`
- `paragraph-block`
- `quote-block`
- `strikethrough`

So whenever you call `clarktown.core/render`, those parsers are applied to the content you give it, and are defined collectively in 
`clarktown.renderers/parsers`. If you want to remove certain parsers feel free to duplicate that vector with whatever combination of 
parsers that works best for you, and pass it as the second parameter to the `clarktown.core/render` function, like so:

```clojure
(clarktown.core/render "**Hello, world!**" [..parsers-go-here..])
```

## Create your own parsers

As Clarktown is modular, you can easily create your own custom parsers as well. To see how the parsers are made, I really recommend 
checking any of the existing parsers you can find in the `clarktown.renderers/*` namespaces and how they are used in the `clarktown.renderers/parsers` variable, 
but overall the idea is very simple: there are (potential) matchers, and renderers.

However, before we get into matchers and renderers, let me quickly explain how Clarktown does its thing. Clarktown splits the entire Markdown 
content you give it into blocks. Each paragraph would be its own block, each quote or heading would be its own block, and so on, basically 
whenever you create a newline separation between text, that creates a new block. 

And so if you create a parser that does something with the entirety of a block, you want it to have a matcher. A function that returns a boolean `true`
when it detects that it's the kind of block that the parser should run on. And then there's the renderer, the parser does its magical transformations 
in the renderer function.

There are also inline parsers, such as for bold or italic text, and those do not need a matcher because they are intended to be mixed with block parsers
as a supplement rather than the main thing. Case in point: code blocks. You want a code block to have a block-level parser that parses it, but you don't 
want a code block to be able to turn text bold or italic, so you wouldn't pass it those inline parsers, whereas to a quote block you would.

### Example block parser

```clojure
(defn is?
  "Determines whether the given block is a X block or not."
  [block]
  true)


(defn render
  "Renders the X block."
  [block parsers]
  "")
```

In the above example the `is?` function is the matcher, and the `render` function makes sure the given `block` gets rendered into the sort of HTML that
it should. 

You may also notice that the `render` function takes in a second argument called `parsers`, and that's the list of parsers passed down to 
Clarktown, so if you needed to run the whole Clarktown on just a subset of your block or something, you could call `clarktown.parser/parse` on that piece 
and pass those `parsers` to it. In most cases, you probably don't need to do that.

### Example inline parser

```clojure
(defn render
  "Renders all occurring italic text as italic."
  [block _]
  (loop [block block
         matches (-> (re-seq #"_.*?_" block)
                     distinct)]
    (if (empty? matches)
      block
      (let [match (first matches)
            value (subs match 1 (- (count match) 1))
            replacement (str "<em>" value "</em>")]
        (recur (string/replace block match replacement)
               (drop 1 matches))))))
```

In the above example there is no matcher function, just the `render` function. This render function goes over the entire `block` and turns all string instances
that match `_some text goes here_` into `<em>some text goes here</em>`. 

### Register your parser

Now that you have a new parser (or a set of ones) you can add it to Clarktown. Remember that the second argument of `clarktown.core/render` takes in a 
vector of maps, each map representing one collection. And so to register you could simply `conj` to the default ones, like so:

```clojure
(ns myapp.core
  (:require 
    [clarktown.core :as clarktown]
    [clarktown.renderers :refer [parsers]]))

(def my-parser
  {:matcher heading-block/is?
   :renderers [bold/render
               italic/render
               inline-code/render
               strikethrough/render
               link-and-image/render
               heading-block/render]})

(clarktown/render "**Hello, world!**" (conj parsers my-parser))
```

And then `my-parser` will run if the matcher returns `true`, and it will run through all the renderer functions you give it, in order from top to bottom.
