(ns day-5
  (:require [clojure.java.io :as io]))

(defn transpose
  [matrix]
  (apply map list matrix))

(defn stacks-n-procs [filename]
  (with-open [rdr (io/reader (io/resource filename))]
    (doall
     (let [ls (line-seq rdr)
           stack-lines (take-while #(not (empty? %)) ls)
           len (->> stack-lines (map count) (apply max))
           parse-int #(Integer/parseInt (str %))
           ;; create matrix from the stacks to transpose and get char seqs of each stack
           stacks (->> stack-lines
                       ;; pad to same length
                       (map (fn [s] 
                              (take len (concat (seq s) (repeat \space)))))
                       reverse
                       transpose
                       (remove (fn [[c]] (= \space c))))
           stacks (zipmap (map (fn [[c]] (parse-int c)) stacks)
                          (map (fn [[_ & crate]]
                                 (vec (remove #(= \space %) crate))) stacks))
           procs (->> ls (drop-while #(not (empty? %))) rest)]
       {:stacks stacks
        :procs (->> procs 
                    (map (fn [p]
                           (->> (re-seq #"\d+" p)
                                (map parse-int))))
                    vec)}))))
;; yields:
;; {:stacks {1 [\Z \N]
;;           2 [\M \C \D]
;;           3 [\P]}
;;  :procs [(1 2 1) (3 1 3) (2 2 1) (1 1 2)]}

(defn process-bunch [stx [count from to]]
  (let [f (get stx from)]
    (assoc stx
           from (vec (drop-last count f))
           to (vec (concat (get stx to) (take-last count f))))))

(defn process [stx [count from to]]
  (loop [s stx
         rem count]
    (if (zero? rem)
      s
      (let [f (get s from)]
        (recur (assoc s
                      from (pop f)
                      to (conj (get s to) (peek f)))
               (dec rem))))))

(defn part-1
  [filename]
  (let [{:keys [stacks procs]} (stacks-n-procs filename)
        final-stacks (reduce process stacks procs)]
    (->> final-stacks
         (into (sorted-map))
         vals
         (map peek)
         (apply str))))

(defn part-2
  [filename]
  (let [{:keys [stacks procs]} (stacks-n-procs filename)
        final-stacks (reduce process-bunch stacks procs)]
    (->> final-stacks
         (into (sorted-map))
         vals
         (map peek)
         (apply str))))

(comment
  (part-1 "day/5/test.txt")
  (part-1 "day/5/input.txt")
  
  (part-2 "day/5/test.txt")
  (part-2 "day/5/input.txt")
  )