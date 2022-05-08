(ns clarktown.matchers.fenced-code-block-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clarktown.matchers.fenced-code-block :as code-block]))


(deftest fenced-code-block-matcher-test
  (testing "Checking a fenced code block"
    (is (true? (code-block/match? "```\nblabla```")))
    (is (true? (code-block/match? "```language-spec\nblabla```")))))
