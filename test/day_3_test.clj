(ns day-3-test
  (:require
   [clojure.test :refer [use-fixtures deftest testing is]]
   [day-3 :refer :all]))

(deftest part-1-test
  (testing "part 1"
    (is (= 157 (part-1 "day/3/test.txt")))))

(deftest part-2-test
  (testing "part 2"
    (is (= 70 (part-2 "day/3/test.txt")))))