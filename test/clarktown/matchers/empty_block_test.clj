(ns clarktown.matchers.empty-block-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clarktown.matchers.empty-block :as empty-block]))

(deftest empty-block-matcher-test
  (testing "Checking an empty block"
    (is (true? (empty-block/match? "")))
    (is (true? (empty-block/match? "     ")))))