(ns clarktown.parsers.code-block-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clojure.java.io :as io]
    [clarktown.parsers.code-block :as code-block]))


(deftest code-block-test
  (testing "Code block with language specification"
    (is (= (slurp (io/file (io/resource "test/parsers/code_block_result.html")))
           (code-block/render (slurp (io/file (io/resource "test/parsers/code_block.md"))) nil))))

  (testing "Code block with NO language specification"
    (is (= (slurp (io/file (io/resource "test/parsers/code_block_no_language_result.html")))
           (code-block/render (slurp (io/file (io/resource "test/parsers/code_block_no_language.md"))) nil)))))