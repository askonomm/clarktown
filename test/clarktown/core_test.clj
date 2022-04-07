(ns clarktown.core-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clarktown.core :as core]))


(deftest overall-test
  (testing "Overall"
    (is (= (slurp "./resources/test/core_result.html")
           (core/render (slurp "./resources/test/core.md"))))))