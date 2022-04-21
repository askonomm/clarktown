(ns clarktown.renderers.code-block-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clojure.java.io :as io]
    [clarktown.renderers.code-block :as code-block]))


(deftest code-block-renderer-test
  (testing "Code block with language specification"
    (is (= (slurp (io/file (io/resource "test/parsers/code_block_result.html")))
           (code-block/render (slurp (io/file (io/resource "test/parsers/code_block.md"))) nil nil))))

  (testing "Code block with NO language specification"
    (is (= (slurp (io/file (io/resource "test/parsers/code_block_no_language_result.html")))
           (code-block/render (slurp (io/file (io/resource "test/parsers/code_block_no_language.md"))) nil nil)))))