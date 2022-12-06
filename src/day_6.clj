(ns day-6)

(defn first-unique-batch
  [len s]
  (->> (partition len 1 s)
       (take-while (fn [cs]
                     (not= (count (distinct cs)) len)))
       count
       (+ len)))

(defn part-1
  [input]
  (first-unique-batch 4 input))

(defn part-2
  [input]
  (first-unique-batch 14 input))

