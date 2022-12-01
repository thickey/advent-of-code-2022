(ns utils.edn
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.string :as string])
  (:import [java.io Reader PushbackReader StringReader]))

(defonce ^:private eof (reify Object (toString [_] "edn EOF")))

(defn eof? [x] (identical? eof x))

(defn pushback-reader
  "Return x coerced to a PushbackReader. Understands anything
  clojure.java.io/reader understands plus PushbackReader itself (which is
  returned unchanged)."
  ^PushbackReader [x]
  (cond (instance? PushbackReader x) x
        (instance? Reader x) (PushbackReader. x)
        :else (PushbackReader. (io/reader x :encoding "UTF-8"))))

(defn pushback-reader-string ^PushbackReader [s]
  "Return a PushbackReader wrapping the contents of string s."
  (PushbackReader. (StringReader. s)))

(defn read-all-edn
  "Given a PushbackReader of something that contains edn forms, return a lazy
   sequence of the data read.
   
   `opts` is a map to supply options to `clojure.edn/read` such as `:readers`
   and `:default` for tagged readers.

   Note: this function always closes the underlying reader automatically when
   the edn is exhausted. If you need more control, use `clojure.edn/read` and
   `eof?` directly."
  ([pbr] (read-all-edn pbr {}))
  ([pbr opts]
   (let [opts (merge opts {:eof eof})
         erdr #(clojure.edn/read opts pbr)]
     (take-while #(if (eof? %)
                    (do (.close pbr) false)
                    true)
                 (repeatedly erdr)))))

(defn read-all-edn-string
  "Like read-all-edn except first argument must be a string containing edn.

  Accepts the same edn reader opts read-all-edn."
  ([s] (read-all-edn-string s {}))
  ([s opts] (read-all-edn (pushback-reader-string s) opts)))

(defn read-all-edn-resource
  [filename]
  (let [pbr (PushbackReader. (io/reader (io/resource filename)))]
    (read-all-edn pbr)))

(defn read-all-edn-lines
  "Read all forms line-by-line, returns a sequence off all forms per line"
  [reader]
  (with-open [rdr reader]
    (doall (map read-all-edn-string
                (line-seq rdr)))))

(defn process-each-edn-line
  "Run a process-fn per line, passing it the outpupt of read-all-edn-string
   for the line."
  [readable process-fn]
  (with-open [rdr (io/reader readable)]
    (doall (->> (line-seq rdr)
                (map read-all-edn-string)
                (map process-fn)))))

(defn process-all-edn-lines
  "Pass a lazy-seq of lines that have been read with read-all-edn-string.
   The provided process-fn must fully consume the sequence or the reader may be
   closed prior to processing"
  [readable process-fn]
  (with-open [rdr (io/reader readable)]
    (->> (line-seq rdr)
         (map read-all-edn-string)
         process-fn)))
