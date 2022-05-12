(ns clarktown.correctors.indented-code-block-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clarktown.correctors.indented-code-block :as corrector]))


(deftest indented-code-block-corrector-test
  (testing "Empty line above"
    (let [line "    Test"
          lines ["Some text goes here" line]
          index 1]
      (is (true? (corrector/newline-above? lines line index)))))

  (testing "No empty line above"
    (let [line "    Hello"
          lines ["Some text goes here" "\n" line]
          index 2]
      (is (false? (corrector/newline-above? lines line index)))))

  (testing "Empty line below"
    (let [line "    Hello"
          lines [line "Some text goes here"]
          index 0]
      (is (true? (corrector/newline-below? lines line index)))))

  (testing "No empty line below"
    (let [line "    Hello"
          lines [line "\n" "Some text goes here"]
          index 0]
      (is (false? (corrector/newline-below? lines line index))))))
