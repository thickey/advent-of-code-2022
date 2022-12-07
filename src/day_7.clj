(ns day-7
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn categorize
  [s]
  (let [[a name] (str/split s #"\ ")]
    {:name name
     :type (if (= a "dir") :dir :file)
     :size (when (not= a "dir") (Integer/parseInt a))}))

(defn commands
  [[f & r]]
  (letfn [(is-command? [s] (str/starts-with? s "$ "))]
    (loop [acc []
           action f
           others r]
      (if action
        (let [out (take-while (complement is-command?) others)
              res (drop-while (complement is-command?) others)
              [_ cmd & params] (str/split action #"\ ")]
          (recur (conj acc {:cmd [cmd params] :out (map categorize out)})
                 (first res)
                 (rest res)))
        acc))))

(defn tree
  [s]
  (reduce (fn [{:keys [pwd tree dirs] :as db}
               {:keys [cmd out]}]
            (let [[c [p]] cmd
                  db (assoc db :current-command cmd)]
              (cond (and (= c "cd")
                         (= p ".."))
                    (-> db (assoc :pwd (pop pwd)))

                    (= c "cd")
                    (assoc db :pwd (conj pwd p))

                    (= c "ls")
                    (-> db
                        (assoc-in (concat [:tree] (interpose :children pwd) [:children])
                                  (zipmap (map :name out) out))))))
          {:pwd []
           :tree {"/" {:type :dir}}}
          s))

(declare size-tree)
(defn size-tree
  [tree]
  (reduce-kv (fn [m k {:keys [children] :as v}]
               (if children
                 (let [kids (size-tree children)]
                   (assoc m k
                          (merge v {:children kids
                                    :size (reduce + (map :size (vals kids)))})))
                 (assoc m k v)))
             {}
             tree))

(declare dirs-by-size)
(defn dirs-by-size
  ([tree] (dirs-by-size tree [] {}))
  ([tree path all-dirs]
   (reduce (fn [acc [k v]]
             (assoc (dirs-by-size (:children v) (conj path k) acc)
                    (conj path k) (:size v)))
           all-dirs
           (filter (fn [[k v]]
                     (= :dir (:type v)))
                   tree))))

(defn read-input [filename]
  (with-open [rdr (io/reader (io/resource filename))]
    (doall (line-seq rdr))))

(defn part-1
  [filename]
  (let [input (read-input filename)
        {:keys [tree]} (->> input commands tree)]
    (->> tree
         size-tree
         dirs-by-size
         vals
         (filter #(> 100000 %))
         (reduce +))))

(defn part-2
  [filename]
  (let [input (read-input filename)
        {:keys [tree]} (->> input commands tree)
        sizes (->> tree
                   size-tree
                   dirs-by-size
                   vals
                   sort)
        fs-total 70000000
        needed 30000000
        current-unused (- fs-total (->> sizes reverse first))
        remaining (- needed current-unused)]
    (->> sizes 
         (filter #(< remaining %))
         first)))

(comment
  (part-1 "day/7/test.txt")
  (part-1 "day/7/input.txt")
  
  
  (part-2 "day/7/test.txt")
  (part-2 "day/7/input.txt")
  )