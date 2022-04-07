(ns clarktown.core-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clojure.java.io :as io]
    [clarktown.core :as core]))


(deftest overall-test
  (testing "Overall"
    (is (= (core/render (slurp (io/file (io/resource "test/core.md"))))
           (slurp (io/file (io/resource "test/core_result.html")))))))