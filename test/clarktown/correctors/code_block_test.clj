(ns clarktown.correctors.code-block-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clarktown.correctors.code-block :as corrector]))


(deftest code-block-corrector
  (testing "Empty line above"
    (let [line "```clojure"
          lines ["Some text goes here" line "some code here" "```"]
          index 1]
      (is (true? (corrector/empty-line-above? lines line index)))))       
           
  (testing "No empty line above"
    (let [line "```"
          lines ["Some text goes here" "\n" line "some code" "```"]
          index 2]
      (is (false? (corrector/empty-line-above? lines line index)))))
  
  (testing "Empty line below"
    (let [line "```"
          lines ["Some text goes here" line "some code" line "some text"]
          index 3]
      (is (true? (corrector/empty-line-below? lines line index)))))
       
  (testing "No empty line below"
    (let [line "```"
          lines ["Some text goes here" line "some code" line "\n" "some text"]
          index 3]
      (is (false? (corrector/empty-line-below? lines line index)))))
    
  (testing "No empty line below when ending with code block"
    (let [line "```"
          lines ["Some text goes here" line "some code" line]
          index 3]
      (is (false? (corrector/empty-line-below? lines line index))))))
    
