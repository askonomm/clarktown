(ns clarktown.renderers.horizontal-line-block-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clarktown.renderers.horizontal-line-block :as horizontal-line-block]))


(deftest horizontal-line-block-renderer-test
  (testing "Creating a horizontal line"
    (is (= (horizontal-line-block/render "***" nil)
           "<hr>"))

    (is (= (horizontal-line-block/render "---" nil)
           "<hr>"))))
