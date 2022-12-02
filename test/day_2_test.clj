(ns day-2-test
  (:require
   [clojure.test :refer [use-fixtures deftest testing is]]
   [day-2 :refer :all]))

(deftest beats-test
  (testing "wins"
    (is (= :win (beats :paper :rock)))
    (is (= :win (beats :scissors :paper)))
    (is (= :win (beats :rock :scissors))))
  (testing "loses"
    (is (= :lose (beats :paper :scissors)))
    (is (= :lose (beats :scissors :rock)))
    (is (= :lose (beats :rock :paper))))
  (testing "draw"
    (is (= :draw (beats :paper :paper)))
    (is (= :draw (beats :scissors :scissors)))
    (is (= :draw (beats :rock :rock)))))

(deftest result-test
  (testing "result"
    (let [input '[[A Y]
                  [B X]
                  [C Z]]]
      (is (= :win (plays->result (first input))))
      (is (= :lose (plays->result (second input))))
      (is (= :draw (plays->result (nth input 2)))))))

(deftest score-test
  (testing "scores"
    (let [input '[[A Y]
                  [B X]
                  [C Z]]]
      (is (= 8 (score (first input))))
      (is (= 1 (score (second input))))
      (is (= 6 (score (nth input 2)))))))

(deftest part-1-test
  (testing "part 1"
    (is (= 15 (part-1 "day/2/test.txt")))))

(deftest outcome->score-test
  (testing "outcome->score"
    (let [input '[[A Y]
                  [B X]
                  [C Z]]]
      (is (= 4 (outcome->score (first input))))
      (is (= 1 (outcome->score (second input))))
      (is (= 7 (outcome->score (nth input 2)))))))

(deftest part-2-test
  (testing "part 2"
    (is (= 12 (part-2 "day/2/test.txt")))))