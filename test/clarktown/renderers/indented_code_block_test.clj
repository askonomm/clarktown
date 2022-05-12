(ns clarktown.renderers.indented-code-block-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clarktown.renderers.indented-code-block :as indented-code-block]))


(deftest indented-code-block-renderer-test
  (testing "Creating a single-line code block"
    (is (= "<pre><code>code goes here</code></pre>"
           (indented-code-block/render "    code goes here" nil nil))))

  (testing "Creating a multi-line code block"
    (is (= "<pre><code>code goes here\nand here</code></pre>"
           (indented-code-block/render "    code goes here\n    and here" nil nil)))))