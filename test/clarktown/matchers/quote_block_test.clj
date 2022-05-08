(ns clarktown.matchers.quote-block-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clarktown.matchers.quote-block :as quote-block]))


(deftest quote-block-matcher-test
  (testing "Checking a quote block"
    (is (true? (quote-block/match? "> Test")))
    (is (true? (quote-block/match? "    > Test")))
    (is (true? (quote-block/match? ">")))))