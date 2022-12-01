(ns utils.data)

(defn pred-seq
  [pred coll]
  "Return a sequence of successive matches of `pred` against the elements of 
   `coll`. I.e., `pred` is for items to be kept.
   
   E.g., (pred-seq pos? [1 2 -1 3 4 -5 -2 9]) => ((1 2) (3 4) (9))"
  (lazy-seq
   (when-let [s (seq coll)]
     (let [xs (take-while pred s)
           ys (drop-while pred s)]
       (if (seq xs)
         (cons xs (pred-seq pred (drop-while (complement pred) ys)))
         (if (seq ys)
           (pred-seq pred (drop-while (complement pred) ys))
           '()))))))
