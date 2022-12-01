(ns day-1-test
  (:require
   [clojure.test :refer [use-fixtures deftest testing is]]
   [day-1 :refer :all]))

(deftest group-calories-test
  (testing "grouping"
    (let [input [1 2 3 nil 4 5 6 nil 7 8 nil 9]]
      (is (= (group-calories input)
             [[1 2 3] [4 5 6] [7 8] [9]])))))

(deftest count-calories-test
  (testing "summing"
    (let [input [[1 2 3] [4 5 6] [7 8] [9]]]
      (is (= (count-calories input)
             [6 15 15 9])))))

(deftest read-input-test
  (testing "reading"
    (is (= (read-input "day/1/test.txt"))
        [1000 2000 3000
         nil
         4000
         nil
         5000 6000
         nil
         7000 8000 9000
         nil
         10000])))

(deftest part-1-test
  (testing "part 1"
    (is (= (->> (read-input "day/1/test.txt") group-calories)
           [[1000 2000 3000]
            [4000]
            [5000 6000]
            [7000 8000 9000]
            [10000]]))
    (is (= (->> (read-input "day/1/test.txt") group-calories count-calories)
           [6000 4000 11000 24000 10000]))
    (is (= (find-max-calories (read-input "day/1/test.txt"))
           24000))))

(deftest part-2-test
  (testing "part 2"
    (is (= (top-3-calories (read-input "day/1/test.txt"))
           [24000 11000 10000]))
    (is (= (sum-top-3-calories (read-input "day/1/test.txt"))
           45000))))
