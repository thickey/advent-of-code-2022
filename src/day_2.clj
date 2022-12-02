(ns day-2
  (:require
   [clojure.java.io :as io]
   [utils.data :as data]
   [utils.edn :as edn]))

(def play->shape '{A :rock
                   B :paper
                   C :scissors
                   X :rock
                   Y :paper
                   Z :scissors})

(def shape->points '{:rock     1
                     :paper    2
                     :scissors 3})

(def result->points {:win  6
                     :lose 0
                     :draw 3})

(def shape-beats {:rock     :scissors
                  :paper    :rock
                  :scissors :paper})

(defn beats [a b]
  (cond (= a b)               :draw
        (= (shape-beats a) b) :win
        :else                 :lose))

(defn plays->result
  [[opponent-play player-play]]
  (beats (play->shape player-play)
         (play->shape opponent-play)))

(defn score [[o p :as play]]
  (+ (-> p play->shape shape->points)
     (-> play plays->result result->points)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Part 1

(defn read-input [filename]
  (edn/read-all-edn-lines
   (-> filename io/resource io/reader)))

(defn part-1
  [filename]
  (->> filename
       read-input
       (map score)
       (reduce +)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Part 2

(def desired-outcome '{X :lose
                       Y :draw
                       Z :win})

(def shape-loses (zipmap (vals shape-beats) (keys shape-beats)))

(defn outcome->shape
  [opponent desired]
  (condp = desired
    :draw opponent
    :win (shape-loses opponent)
    :lose (shape-beats opponent)))

(defn outcome->score
  [[opponent-play desired :as d]]
  (let [outcome (desired-outcome desired)
        opponent (play->shape opponent-play)
        player (outcome->shape opponent outcome)]
    (+ (shape->points player)
       (result->points outcome))))

(defn part-2
  [filename]
  (->> filename
       read-input
       (map outcome->score)
       (reduce +)))

(comment
  (part-1 "day/2/test.txt")
  (part-1 "day/2/input.txt")
  
  (part-2 "day/2/test.txt")
  (part-2 "day/2/input.txt")
  )