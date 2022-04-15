(defproject com.github.askonomm/clarktown "1.1.2"
  :description "A zero-dependency, pure-clojure Markdown parser."
  :url "https://github.com/askonomm/clarktown"
  :license {:name "MIT"
            :url "https://github.com/askonomm/clarktown/blob/master/LICENSE.txt"}
  :deploy-repositories [["releases"  {:sign-releases false :url "https://repo.clojars.org"}]
                        ["snapshots" {:sign-releases false :url "https://repo.clojars.org"}]]
  :dependencies [[org.clojure/clojure "1.11.1"]]
  :plugins [[lein-auto "0.1.3"]
            [lein-cloverage "1.2.3"]]
  :repl-options {:init-ns clarktown.core})
