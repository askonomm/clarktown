(ns clarktown.renderers.bold-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clarktown.renderers.bold :as bold]))


(deftest bold-renderer-test
  (testing "Creating bold text with two surrounding asterisk characters"
    (is (= "<strong>This is bold.</strong>"
           (bold/render "**This is bold.**" nil nil))))

  (testing "Creating bold text with two surrounding underscore characters"
    (is (= "<strong>This is bold.</strong>"
           (bold/render "__This is bold.__" nil nil))))

  (testing "Creating bold text with both underscores and asterisks mixed"
    (is (= "Hi, my name is <strong>John</strong>, what is <strong>your name?</strong>"
           (bold/render "Hi, my name is **John**, what is __your name?__" nil nil)))))