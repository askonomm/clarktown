(ns clarktown.parsers.code-block-test
  (:require [clojure.test :refer [deftest testing is]]
            [clarktown.parsers.code-block :as code-block]))


(deftest code-block-test
  (testing "Code block"
    (is (= (slurp "./resources/test/parsers/code_block_result.html")
           (code-block/render (slurp "./resources/test/parsers/code_block.md") nil)))))