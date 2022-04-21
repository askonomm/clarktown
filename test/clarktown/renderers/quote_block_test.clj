(ns clarktown.renderers.quote-block-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clarktown.renderers.quote-block :as quote-block]))


(deftest quote-block-block-renderer-test
  (testing "Creating a quote block line"
    (is (= (quote-block/render "> First line\n> second line" nil nil)
           "<blockquote>First line\nsecond line</blockquote>"))))