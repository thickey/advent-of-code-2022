(ns day-3
  (:require
   [clojure.java.io :as io]
   [clojure.set :as set]
   [utils.edn :as edn]))

(defn read-input [filename]
  (->> (edn/read-all-edn-lines
        (-> filename io/resource io/reader))
       (map #(str (first %)))))

(defn prio [c] 
  (let [i (int c)]
    (if (> i 90) (- i 96) (- i 38))))

(defn compartments
  [rucksack]
  (partition (/ (count rucksack) 2) rucksack))

(defn common 
  [& grps]
   (->> grps (map set) (apply set/intersection)))

(defn prioritize
  [rucksacks]
  (->> rucksacks
       (map (fn [rucksack]
              (->> (compartments rucksack) (apply common) first prio)))
       (reduce +)))

(defn part-1
  [filename]
  (let [input (read-input filename)]
    (prioritize input)))

(defn prioritize-badges
  [rucksacks]
  (let [groups (partition 3 rucksacks)]
    (->> groups
         (map (fn [g]
                (->> g (apply common) first prio)))
         (reduce +))))

(defn part-2
  [filename]
  (let [input (read-input filename)]
    (prioritize-badges input)))
