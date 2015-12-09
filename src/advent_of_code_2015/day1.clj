(ns advent-of-code-2015.day1
  (:require [clojure.java.io :as io]))

(defn areas [sides]
  (case c \( 1 \) -1 0))

(defn run []
  (let [resource (map move (slurp (io/resource "day1.txt")))]
    ;; part 1
    (reduce + resource)
    ;; part 2
    (+ 1 (.indexOf (reductions + resource) -1))))
