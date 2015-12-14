(ns advent-of-code-2015.day14
    (:require [clojure.java.io :as io]
            [clojure.string :as str]
            ))

(defn parse-input [resource]
  (let [matches (re-seq #"(\w+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds\." resource)]
    (for [[_ reindeer & vals] matches]
      (into [reindeer] (map #(Integer/parseInt %) vals)))))

(defn run-run-rudolph [speed endurance rest-time]
  (cycle (concat (repeat endurance speed) (repeat rest-time 0))))

(defn score-race [results]
  (let [best (apply max (map second results))]
    (into {}
          (for [[horse score] results :when (= score best)]
            [horse 1]))))

(defn run []
  (let [resource (slurp (io/resource "day14.txt"))
        stats (parse-input resource)]
    ;; part 1
    (for [[reindeer & performance] stats]
      [reindeer (reduce + (take 2503 (apply run-run-rudolph performance)))])
    ;; part 2
    (->> (for [n (range 1 2504)]
           (score-race (for [[reindeer & performance] stats]
                         [reindeer (reduce + (take n (apply run-run-rudolph performance)))])))
         (apply merge-with +))))
