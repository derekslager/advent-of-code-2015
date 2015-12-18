(ns advent-of-code-2015.day18
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn neighbors [point]
  (map #(map + %1 %2)
       (repeat point)
       [[-1 -1] [0 -1] [1 -1] [-1 0] [1 0] [-1 1] [0 1] [1 1]]))

(defn tick [grid]
  (vec (map-indexed
        (fn [x row]
          (vec (map-indexed
                (fn [y col]
                  (let [c (->> (neighbors [x y])
                               (map (partial get-in grid))
                               (filter (partial = \#))
                               count)]
                    (if (= col \#)
                      (if (#{2 3} c) \# \.)
                      (if (= 3 c) \# \.)))) row))) grid)))

(defn tick-2 [grid]
  (reduce #(assoc-in %1 %2 \#) (tick grid) [[0 0] [99 99] [0 99] [99 0]]))

(defn run []
  (let [resource (slurp (io/resource "day18.txt"))
        grid (vec (map #(vec %) (str/split-lines resource)))]
    (->> (iterate tick-2 grid)
         (drop 100)
         first
         flatten
         (filter (partial = \#))
         count)))
