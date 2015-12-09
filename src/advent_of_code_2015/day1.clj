(ns advent-of-code-2015.day1
  (:require [clojure.java.io :as io]))

(defn move [c]
  (case c \( 1 \) -1 0))

(defn run []
  ;; part 1
  (let [resource (map move (slurp (io/resource "day1.txt")))]
    (reduce + resource)
    ;; part 2
    (+ 1 (.indexOf (reductions + resource) -1))))
