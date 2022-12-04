(ns day-4-test
  (:require
   [clojure.test :refer [use-fixtures deftest testing is]]
   [day-4 :refer :all]))

(deftest part-1-test
  (testing "part 1"
    (is (= 2 (part-1 "day/4/test.txt")))))

(deftest part-2-test
  (testing "part 2"
    (is (= 4 (part-2 "day/4/test.txt")))))