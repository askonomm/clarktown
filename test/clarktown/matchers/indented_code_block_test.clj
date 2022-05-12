(ns clarktown.matchers.indented-code-block-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clarktown.matchers.indented-code-block :as code-block]))


(deftest indented-code-block-matcher-test
  (testing "Checking a indented code block"
    (is (true? (code-block/match? "    blabla")))
    (is (true? (code-block/match? "    abc\n     dfg\n    blabla")))
    (is (false? (code-block/match? "    abc\n   asdasd")))))


