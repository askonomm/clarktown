(ns clarktown.correctors.atx-heading-block-test
  (:require 
    [clojure.test :refer [deftest testing is]]
    [clarktown.correctors.atx-heading-block :as corrector]))


(deftest atx-heading-block-corrector
  (testing "Empty line above"
    (let [line "# Hello"
          lines ["Some text goes here" line]
          index 1]
      (is (true? (corrector/empty-line-above? lines line index)))))
    
  (testing "No empty line above"
    (let [line "# Hello" 
          lines ["Some text goes here" "\n" line]
          index 2]
      (is (false? (corrector/empty-line-above? lines line index)))))
    
  (testing "Empty line below"
    (let [line "# Hello"
          lines [line "Some text goes here"]
          index 0]
      (is (true? (corrector/empty-line-below? lines line index)))))
    
  (testing "No empty line below"
    (let [line "# Hello" 
          lines [line "\n" "Some text goes here"]
          index 0]
      (is (false? (corrector/empty-line-below? lines line index))))))
