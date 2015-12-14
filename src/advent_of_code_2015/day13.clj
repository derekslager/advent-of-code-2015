(ns advent-of-code-2015.day13
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.math.combinatorics :refer [permutations]]))

(defn score-arrangement [stats arrangement]
  (let [diners (count arrangement)]
    (->> (for [[n person] (map-indexed vector arrangement)]
           (+ (or (get stats [person (get arrangement (mod (- n 1) diners))]) 0)
              (or (get stats [person (get arrangement (mod (+ n 1) diners))]) 0)))
         (reduce +))))

(defn parse-input [resource]
  (let [matches (re-seq #"(\w+) would (gain|lose) (\d+) happiness units by sitting next to (\w+)\." resource)]
    (into {} (for [[_ person direction amount next-to] matches]
               [[person next-to] (* (Integer/parseInt amount) (if (= "lose" direction) -1 1))]))))

(defn run []
  (let [resource (slurp (io/resource "day13.txt"))
        stats (parse-input resource)
        diners (set (map first (keys stats)))]

    ;; part 1
    (apply max
           (for [arrangement (permutations diners)]
             (score-arrangement stats (vec arrangement))))

    ;; part 2
    (let [new-diners (conj diners "Derek")]
      (apply max
             (for [arrangement (permutations new-diners)]
               (score-arrangement stats (vec arrangement)))))))
