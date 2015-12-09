(ns advent-of-code-2015.day2
  (:require [clojure.java.io :as io]
            [clojure.string :refer [split]])
  (:import java.lang.Integer))

(defn areas [[l w h]]
  [(* l w) (* w h) (* h l)])

(defn paper-required [areas]
  (let [slack (apply min areas)]
    (+ slack (reduce + (map #(* 2 %) areas)))))

(defn ribbon-required [dimensions]
  (+
   ;; ribbon
   (reduce + (map #(* 2 %) (take 2 (sort dimensions))))
   ;; bow
   (apply * dimensions)))

(defn run []
  (let [resource (slurp (io/resource "day2.txt"))
        sides (partition 3 (map #(Integer/parseInt %) (split resource #"x|\n")))]
    ;; part 1
    (reduce + (map paper-required (map areas sides)))
    ;; part 2
    (reduce + (map ribbon-required sides))))
