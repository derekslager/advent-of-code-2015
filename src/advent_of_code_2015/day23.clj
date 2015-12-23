(ns advent-of-code-2015.day23
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn run []
  (let [resource (slurp (io/resource "day23.txt"))
        instructions (vec (str/split-lines resource))]

    (loop [state {"a" (bigint 1) "b" (bigint 0)}, offset 0]
      (let [[state' offset']
            (condp re-matches (nth instructions offset)
              #"jmp ([+-]\d+)"
              :>> (fn [[_ jump]] [state (+ offset (Integer/parseInt jump))])
              #"jie (\w), ([+-]\d+)"
              :>> (fn [[_ r jump]] [state (if (even? (get state r))
                                              (+ offset (Integer/parseInt jump))
                                              (inc offset))])
              #"jio (\w), ([+-]\d+)"
              :>> (fn [[_ r jump]] [state (if (= 1 (get state r))
                                              (+ offset (Integer/parseInt jump))
                                              (inc offset))])
              #"hlf (\w)"
              :>> (fn [[_ r]] [(update state r / 2) (inc offset)])
              #"tpl (\w)"
              :>> (fn [[_ r]] [(update state r * 3) (inc offset)])
              #"inc (\w)"
              :>> (fn [[_ r]] [(update state r inc) (inc offset)])
              )]
        (if (< offset' (count instructions))
          (recur state' offset')
          state')))))

(run)
