(ns advent-of-code-2015.day17
  (:require [clojure.java.io :as io]
            [clojure.math.combinatorics :refer [subsets]]))

(defn run []
  (let [resource (slurp (io/resource "day17.txt"))
        containers (->> (re-seq #"\d+" resource) (map-indexed #(vector (Integer/parseInt %2) %1)))]
    (let [result (vec (->> (subsets containers)
                           (filter #(= 150 (reduce + (map first %))))))]
      ;; part 1
      (count result)
      ;; part 2
      (let [min-containers (->> result (map count) (apply min))]
        (->> result (map count) (filter #(= min-containers %)) count)))))
