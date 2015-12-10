(ns advent-of-code-2015.day6
  (:require [clojure.java.io :as io])
  (:import java.util.BitSet))

(def grid-x 1000)
(def grid-y 1000)
(def grid-size (* grid-x grid-y))

(defn translate [x y]
  (+ (* x grid-x) y))

(defn run []
  (let [resource (slurp (io/resource "day6.txt"))
        ;; lines (str/split-lines resource)
        ops (re-seq #"(turn off|turn on|toggle) (\d+),(\d+) through (\d+),(\d+)" resource)
        lights (BitSet. grid-size)]

    ;; part 1
    (doseq [[_ op from-x from-y to-x to-y] ops]
      (let [op-fn (case op
                    "toggle" #(.flip lights ^Integer %)
                    "turn on" #(.set lights ^Integer % true)
                    "turn off" #(.set lights ^Integer % false))]
        (doseq [x (range (Integer/parseInt from-x) (+ 1 (Integer/parseInt to-x)))
                y (range (Integer/parseInt from-y) (+ 1 (Integer/parseInt to-y)))]
          (op-fn (translate x y)))))
    (.cardinality lights)

    ;; part 2
    (let [dimmers (int-array grid-size)]
      (doseq [[_ op from-x from-y to-x to-y] ops]
        (let [turn-knob (fn [by idx] (aset dimmers ^Integer idx ^Integer (max 0 (+ (aget dimmers idx) by))))
              op-fn (case op
                      "toggle" (partial turn-knob 2)
                      "turn on" (partial turn-knob 1)
                      "turn off" (partial turn-knob -1))]
          (doseq [x (range (Integer/parseInt from-x) (+ 1 (Integer/parseInt to-x)))
                  y (range (Integer/parseInt from-y) (+ 1 (Integer/parseInt to-y)))]
            (op-fn (translate x y)))))
      (reduce + dimmers))))
