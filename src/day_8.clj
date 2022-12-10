(ns day-8
  (:require [clojure.java.io :as io]
            [utils.data :refer [take-until]]))

(defn max-prev
  [items]
  (loop [prev-maxes [nil]
         items items]
    (let [[item & others] items]
      (if (and item others)
        (let [prev (peek prev-maxes)]
          (recur (conj prev-maxes 
                       (if-not prev 
                         item 
                         (max item prev)))
                 others))
        prev-maxes))))

(defn visibility
  [row]
  (let [ml (max-prev row)
        mr (reverse (max-prev (reverse row)))]
    (mapv (fn [o l r]
            (boolean (or (nil? l)
                         (nil? r)
                         (> o l)
                         (> o r))))
          row ml mr)))

(defn transpose
  [matrix]
  (apply map list matrix))

(defn matrix-visibility
  [matrix]
  (let [h-vis (mapv visibility matrix)
        v-vis (->> matrix transpose (mapv visibility))
        col-count (count (first matrix))
        row-count (count (first v-vis))
        v-vis (->> v-vis transpose (mapv vec))]
    (->> (for [x (range col-count) y (range row-count)]
           (let [hv (nth (nth h-vis y) x)
                 vv (nth (nth v-vis y) x)]
             (boolean (or hv vv)))))))

(defn read-input [filename]
  (with-open [rdr (io/reader (io/resource filename))]
    (doall (map (fn [s]
                  (->> s
                       (into [])
                       (mapv #(Character/getNumericValue %))))
                (line-seq rdr)))))

(defn part-1
  []
  (->> (read-input "day/8/input.txt")
       matrix-visibility
       (filter identity)
       count))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Part 2

(defn seen-prev
  [items]
  (loop [prev-seen []
         hist []
         [item & others] items]
    (if item
      (let [prev-visible-dist (peek prev-seen)
            prev (peek hist)]
        (recur (conj prev-seen
                     (if (not prev) 0
                         (->> hist reverse (take-until (fn [x]
                                                         (or (> x item)
                                                             (= x item))))
                              count)))
               (conj hist item)
               others))
      prev-seen)))

(defn view-score
  [row]
  (let [ml (seen-prev row)
        mr (reverse (seen-prev (reverse row)))]
    (mapv (fn [l r] [l r])
          ml mr)))

(defn matrix-view-score
  [matrix]
  (let [h-vs (mapv view-score matrix)
        v-vs (->> matrix transpose (mapv view-score))
        col-count (count (first matrix))
        row-count (count (first v-vs))
        v-vs (->> v-vs transpose (mapv vec))]
    (->> (for [y (range row-count) x (range col-count)]
           (let [hv (nth (nth h-vs y) x)
                 vv (nth (nth v-vs y) x)]
             (->> (concat hv vv)
                  (remove zero?)
                  (apply *)))))))

(defn part-2
  []
  (->> (read-input "day/8/input.txt")
       matrix-view-score
       sort
       reverse
       first))
