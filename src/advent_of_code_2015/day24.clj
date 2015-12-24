(ns advent-of-code-2015.day24
  (:require [clojure.java.io :as io]
            [clojure.math.combinatorics :as c]
            [clojure.set :as set]))

(defn quantum-entanglement [packages]
  (reduce * packages))

(defn find-group [target packages]
  (loop [n 1]
    (let [g1s (->> n
                   (c/combinations packages)
                   (filter #(and (= target (reduce + %))
                                 ;; optional?
                                 #_(->> (c/partitions (vec (set/difference (set packages) %)) :min 2 :max 2)
                                      (some (fn [ps] (apply = (map (fn [p] (reduce + p)) ps))))))))]
      (if (empty? g1s)
        (recur (inc n))
        (apply min (map quantum-entanglement g1s))))))

(defn run []
  (let [resource (slurp (io/resource "day24.txt"))
        packages (mapv #(Long/parseLong %) (re-seq #"\d+" resource))
        splits 4
        target (/ (reduce + packages) splits)]

    (find-group target packages)))
