(ns clarktown.core-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clarktown.core :as core]))


(deftest overall-test
  (testing "Overall"
    (is (= (core/render (slurp "./resources/test/core.md"))
           (slurp "./resources/test/core_result.html")))))