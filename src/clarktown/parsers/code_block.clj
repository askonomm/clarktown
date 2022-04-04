(ns clarktown.parsers.code-block
  (:require
    [clojure.string :as string]))


(defn is?
  "Determines whether we're dealing with a code block."
  [block]
  (and (string/starts-with? block "```")
       (string/ends-with? block "```")))


(defn render
  "Renders the code block."
  [block _]
  (let [language (->> block
                      (re-find #"\`\`\`(\w+)")
                      second)
        code (as-> block n
                   (string/replace-first n #"\`\`\`(\w+)?\n" "")
                   (subs n 0 (- (count n) 5))
                   (string/replace n #"&" "&amp;")
                   (string/replace n #"<" "&lt;")
                   (string/replace n #">" "&gt;")
                   (string/replace n #"\n" "<br>")
                   (string/replace n #"\tab" "<tab>")
                   (string/trim n))]
    (if language
      (str "<pre class=\"language-" language "\"><code>" code "</code></pre>")
      (str "<pre><code>" code "</code></pre>"))))
