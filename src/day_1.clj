(ns day-1
  (:require
   [clojure.java.io :as io]
   [utils.data :as data]
   [utils.edn :as edn]))

(defn read-input [filename]
  (->> (edn/read-all-edn-lines
        (-> filename io/resource io/reader))
       (map first)))

(defn part-1
  []
  (->> (read-input "day/1/input.txt") ;; read file
       (data/pred-seq int?)           ;; group by elf
       (map (partial reduce +))       ;; sum each elf's calories
       (reduce max)))                 ;; get max

(defn part-2
  []
  (->> (read-input "day/1/input.txt") ;; read file
       (data/pred-seq int?)           ;; group by elf
       (map (partial reduce +))       ;; sum each elf's calories
       (sort >)                       ;; sort greatest
       (take 3)                       ;; top 3
       (reduce +)))                   ;; sum

(comment
  (part-1)
  (part-2)
  )