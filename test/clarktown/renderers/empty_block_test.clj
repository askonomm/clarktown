(ns clarktown.renderers.empty-block-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clarktown.renderers.empty-block :as empty-block]))


(deftest empty-block-renderer-test
  (testing "Rendering an empty block"
    (is (= (empty-block/render "" nil nil)
           ""))))
