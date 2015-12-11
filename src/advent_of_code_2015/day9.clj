(ns advent-of-code-2015.day9
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.math.combinatorics :refer [permutations]]))

(defn compute-distance [distance-map locations]
  (let [distances
        (for [pair (partition 2 1 locations)]
          (get distance-map (set pair)))]
    (reduce + distances)))

(defn run []
  (let [resource (slurp (io/resource "day9.txt"))
        matches (re-seq #"(\w+) to (\w+) = (\d+)" resource)
        distances (into {} (for [[_ from to distance] matches]
                             [#{from to} (Integer/parseInt distance)]))
        locations (set (mapcat (juxt first second) (keys distances)))]

    (->> (permutations locations)
         (map (partial compute-distance distances))
         (apply max))))
