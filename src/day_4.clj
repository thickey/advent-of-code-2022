(ns day-4
  (:require
   [clojure.java.io :as io]
   [clojure.set :as set]
   [clojure.string :as str]))

(defn spread [s]
  (let [[from to] (str/split s #"\-")]
    (vec (range (Integer/parseInt from) (inc (Integer/parseInt to))))))

(defn decode-assignment
  "e.g., \"2-4,6-8\" => [[2 3 4] [6 7 8]]"
  [s]
  (let [[a b] (str/split s #"\,")]
    [(spread a) (spread b)]))

(defn fully-contains?
  [a b]
  (let [sa (set a)
        sb (set b)
        intsxn (set/intersection sa sb)]
    (boolean (or (= intsxn sa)
                 (= intsxn sb)))))

(defn overlaps?
  [a b]
  (let [sa (set a)
        sb (set b)
        intsxn (set/intersection sa sb)]
    (not (empty? intsxn))))

(defn assignments [filename]
  (with-open [rdr (io/reader (io/resource filename))]
    (doall (map decode-assignment (line-seq rdr)))))

(defn part-1
  [filename]
  (->> filename
       assignments
       (filter #(apply fully-contains? %))
       count))

(defn part-2
  [filename]
  (->> filename
       assignments
       (filter #(apply overlaps? %))
       count))

(comment 
  (part-1 "day/4/input.txt")
  (part-2 "day/4/input.txt")
  )