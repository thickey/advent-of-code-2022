(ns day-1-verbose
  (:require
   [clojure.java.io :as io]
   [utils.data :as data]
   [utils.edn :as edn]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Verbose approach to Parts 1 & 2, straightforward way is in `day-1 ns

(def sum (partial reduce +))

(defn group-calories
  [input]
  (data/pred-seq int? input))

(defn count-calories
  [cal-groups]
  (map sum cal-groups))

(defn input->calories [input]
  (->> input
       group-calories
       count-calories))

(defn find-max-calories
  [input]
  (->> input
       input->calories
       (reduce max)))

(defn top-3-calories
  [input]
  (->> input
       input->calories
       (sort >)
       (take 3)))

(defn sum-top-3-calories
  [input]
  (->> input
       top-3-calories
       sum))

(defn read-input [filename]
  (->> (edn/read-all-edn-lines
        (-> filename io/resource io/reader))
       (map first)))

(defn part-1
  []
  (->> (read-input "day/1/input.txt")
       find-max-calories))

(defn part-2
  []
  (->> (read-input "day/1/input.txt")
       sum-top-3-calories))

(comment
  (part-1)
  (part-2)
  )
