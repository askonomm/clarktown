(ns clarktown.matchers.horizontal-line-block-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clarktown.matchers.horizontal-line-block :as horizontal-line-block]))


(deftest horizontal-line-block-matcher-test
  (testing "Is a horizontal line block"
    (is (true? (horizontal-line-block/match? "***")))
    (is (true? (horizontal-line-block/match? "    ***")))
    (is (false? (horizontal-line-block/match? "Test *** 123")))
    (is (true? (horizontal-line-block/match? "---")))
    (is (true? (horizontal-line-block/match? "    ---")))
    (is (false? (horizontal-line-block/match? "Test --- 123")))))