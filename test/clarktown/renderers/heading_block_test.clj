(ns clarktown.renderers.heading-block-test
  (:require
    [clojure.test :refer [deftest testing is]]
    [clarktown.renderers.heading-block :as heading-block]))


(deftest atx-heading-renderer-test
  (testing "ATX heading block that's a H1"
    (is (= "<h1 id=\"this-is-a-heading-block\">This is a heading block.</h1>"
           (heading-block/render "# This is a heading block." nil nil))))

  (testing "ATX heading block that's a H2"
    (is (= "<h2 id=\"this-is-a-heading-block\">This is a heading block.</h2>"
           (heading-block/render "## This is a heading block." nil nil))))

  (testing "ATX heading block that's a H3"
    (is (= "<h3 id=\"this-is-a-heading-block\">This is a heading block.</h3>"
           (heading-block/render "### This is a heading block." nil nil))))

  (testing "ATX heading block that's a H4"
    (is (= "<h4 id=\"this-is-a-heading-block\">This is a heading block.</h4>"
           (heading-block/render "#### This is a heading block." nil nil))))

  (testing "ATX heading block that's a H5"
    (is (= "<h5 id=\"this-is-a-heading-block\">This is a heading block.</h5>"
           (heading-block/render "##### This is a heading block." nil nil))))
    
  (testing "ATX heading block that's a H6"
    (is (= "<h6 id=\"this-is-a-heading-block\">This is a heading block.</h6>"
           (heading-block/render "###### This is a heading block." nil nil))))
  
  (testing "No H tag when 7 or more # characters"
    (is (= "####### This is not a heading block."
           (heading-block/render "####### This is not a heading block." nil nil))))
  
  (testing "No H tag when there is no space between # characters and value"
    (is (= "#This is not a heading block."
           (heading-block/render "#This is not a heading block." nil nil))))
  
  (testing "ATX heading can precede up to 3 spaces"
    (is (= "<h1 id=\"this-is-a-heading\">This is a heading.</h1>"
           (heading-block/render " # This is a heading." nil nil)))
    (is (= "<h1 id=\"this-is-a-heading\">This is a heading.</h1>"
           (heading-block/render "  # This is a heading." nil nil)))
    (is (= "<h1 id=\"this-is-a-heading\">This is a heading.</h1>"
           (heading-block/render "   # This is a heading." nil nil))))
  
  (testing "But no more than 3 spaces"
    (is (= "    # This is a heading."
           (heading-block/render "    # This is a heading." nil nil)))))    
    


(deftest settext-heading-renderer-text
  (testing "Settext heading block that's a H1"
    (is (= "<h1 id=\"this-is-a-heading-block\">This is a heading block.</h1>"
           (heading-block/render "This is a heading block.\n=========" nil nil))))

  (testing "Settext heading block that's a H1 spanning multiple lines"
    (is (= "<h1 id=\"this-is-a-heading-block-spanning-multiple-lines\">This is a \nheading block spanning multiple lines.</h1>"
           (heading-block/render "This is a \nheading block spanning multiple lines.\n========" nil nil))))

  (testing "Settext heading block that's a H2"
    (is (= "<h2 id=\"this-is-a-heading-block\">This is a heading block.</h2>"
           (heading-block/render "This is a heading block.\n---------" nil nil))))

  (testing "Settext heading block that's a H2 spanning multiple lines"
    (is (= "<h2 id=\"this-is-a-heading-block-spanning-multiple-lines\">This is a \nheading block spanning multiple lines.</h2>"
           (heading-block/render "This is a \nheading block spanning multiple lines.\n--------" nil nil)))))
