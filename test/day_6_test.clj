(ns day-6-test
  (:require
   [clojure.test :refer [use-fixtures deftest testing is]]
   [day-6 :refer :all]))

(deftest part-1-test
  (testing "part 1"
    (is (= [7 5 6 10 11]
           (map part-1 ["mjqjpqmgbljsphdztnvjfqwrcgsmlb"
                        "bvwbjplbgvbhsrlpgdmjqwftvncz"
                        "nppdvjthqldpwncqszvftbrmjlhg"
                        "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"
                        "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"])))))

(deftest part-2-test
  (testing "part 2"
    (is (= [19 23 23 29 26]
           (map part-2 ["mjqjpqmgbljsphdztnvjfqwrcgsmlb"
                        "bvwbjplbgvbhsrlpgdmjqwftvncz"
                        "nppdvjthqldpwncqszvftbrmjlhg"
                        "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"
                        "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"])))))