(ns clarktown.renderers.strikethrough-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clarktown.renderers.strikethrough :as strikethrough]))


(deftest strikethrough-renderer-test
  (testing "Creating strikethrough text"
    (is (= (strikethrough/render "~~This is strikethrough text.~~" nil nil)
           "<del>This is strikethrough text.</del>")))

  (testing "Creating strikethrough text mixed with regular text"
    (is (= (strikethrough/render "Some other text, ~~This is strikethrough text.~~ And more text." nil nil)
           "Some other text, <del>This is strikethrough text.</del> And more text."))))