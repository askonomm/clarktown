(ns clarktown.renderers.code-block
  (:require
    [clojure.string :as string]))


(defn render
  "Renders the code block."
  [block _]
  (let [language (->> block
                      (re-find #"\`\`\`(\w+)")
                      second)
        lines (string/split-lines block)
        block* (->> (next lines)
                    (take (- (count lines) 2))
                    (string/join \newline))
        code (-> block*
                 (string/replace #"&" "&amp;")
                 (string/replace #"<" "&lt;")
                 (string/replace #">" "&gt;")
                 string/trim)]
    (if language
      (str "<pre><code class=\"language-" language "\">" code "</code></pre>")
      (str "<pre><code>" code "</code></pre>"))))
