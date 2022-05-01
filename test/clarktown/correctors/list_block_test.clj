(ns clarktown.correctors.list-block-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clarktown.correctors.list-block :as corrector]))


(deftest line-block-corrector-test
  (testing "Empty line above"
    (let [line "1. Hello"
          lines ["Some text goes here" line] 
          index 1]
      (is (true? (corrector/empty-line-above? lines line index)))))

  (testing "Empty line above II"
    (let [line "* Hello"
          lines ["Some text goes here" line]
          index 1]
      (is (true? (corrector/empty-line-above? lines line index)))))
              
  (testing "No empty line above"
    (let [line "1. Hello" 
          lines ["Some text goes here" "\n" line]
          index 2]    
      (is (false? (corrector/empty-line-above? lines line index)))))

  (testing "No empty line above II"
    (let [line "* Hello"
          lines ["Some text goes here" "\n" line]
          index 2]
      (is (false? (corrector/empty-line-above? lines line index)))))

  (testing "No empty line above III"
    (let [line "* Hello"
          lines ["Some text here" "* Asd" line]
          index 2]
      (is (false? (corrector/empty-line-above? lines line index)))))
       
  (testing "Empty line below"
    (let [line "1. Hello"
          lines [line "Some text goes here"]
          index 0]
      (is (true? (corrector/empty-line-below? lines line index)))))

  (testing "Empty line below II"
    (let [line "* Hello"
          lines [line "Some text goes here"]
          index 0]
      (is (true? (corrector/empty-line-below? lines line index)))))
                          
  (testing "No empty line below"
    (let [line "1. Hello" 
          lines [line "\n" "Some text goes here"]
          index 0]
      (is (false? (corrector/empty-line-below? lines line index)))))
    
  (testing "No empty line below II"
    (let [line "* Hello"
          lines [line "\n" "Some text goes here"]
          index 0]
      (is (false? (corrector/empty-line-below? lines line index)))))
    
  (testing "No empty line below III"
    (let [line "* Hello"
          lines [line "* Asd" "Some text goes here"]
          index 0]
      (is (false? (corrector/empty-line-below? lines line index))))))
