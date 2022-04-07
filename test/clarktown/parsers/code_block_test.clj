(ns clarktown.parsers.code-block-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clojure.java.io :as io]
    [clarktown.parsers.code-block :as code-block]))


(deftest code-block-test
  (testing "Code block"
    (is (= (slurp (io/file (io/resource "test/parsers/code_block_result.html")))
           (code-block/render (slurp (io/file (io/resource "test/parsers/code_block.md"))) nil)))))