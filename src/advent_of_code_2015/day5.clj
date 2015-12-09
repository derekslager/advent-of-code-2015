(ns advent-of-code-2015.day5
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn not-bad [s]
  (not (re-find #"(ab|cd|pq|xy)" s)))

(defn doubles [s]
  (re-find #"([a-z])\1" s))

(defn vowels [s]
  (>= (count (re-seq #"(?i)[aeiou]" s)) 3))

(defn nice [s]
  (and (not-bad s)
       (doubles s)
       (vowels s)))

(defn pairs-no-overlap [s]
  (re-find #"([a-z][a-z]).*\1" s))

(defn one-letter-between [s]
  (re-find #"([a-z])[a-z]\1" s))

(defn nice2 [s]
  (and (pairs-no-overlap s)
       (one-letter-between s)))

(defn run []
  (let [resource (slurp (io/resource "day5.txt"))
        strings (str/split-lines resource)]
    [(->> strings (filter nice) count)
     (->> strings (filter nice2) count)]))
