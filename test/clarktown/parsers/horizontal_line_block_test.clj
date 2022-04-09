(ns clarktown.parsers.horizontal-line-block-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clarktown.parsers.horizontal-line-block :as horizontal-line-block]))


(deftest horizontal-line-block-test
  (testing "Creating a horizontal line"
    (is (= "<hr>"
           (horizontal-line-block/render "***" nil)))))