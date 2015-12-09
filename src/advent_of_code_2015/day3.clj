(ns advent-of-code-2015.day3
  (:require [clojure.java.io :as io]))

(defn move [[x y] c]
  (case c
    \^ [x (+ y 1)]
    \v [x (- y 1)]
    \> [(+ x 1) y]
    \< [(- x 1) y]
    [x y]))

(defn run []
  (let [resource (slurp (io/resource "day3.txt"))]

    ;; part 1
    (->> resource
         (reductions move [0 0])
         (into #{})
         count)

    ;; part 2
    (let [santa (take-nth 2 resource)
          robo (take-nth 2 (drop 1 resource))
          points (into #{} (concat
                            (reductions move [0 0] santa)
                            (reductions move [0 0] robo)))]
      (count points))))
