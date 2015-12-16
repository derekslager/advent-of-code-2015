(ns advent-of-code-2015.day16
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as set]))

(defn parse-input [resource]
  (for [line (str/split-lines resource)]
    (into {} (for [[_ k v] (re-seq #"([a-z]+): (\d+)" line)] [(keyword k) (Integer/parseInt v)]))))

(def gift
  {:children 3 :cats 7 :samoyeds 2 :pomeranians 3 :akitas 0
   :vizslas 0 :goldfish 5 :trees 3 :cars 2 :perfumes 1})

(defn match-auntie [n auntie]
  [n (set/superset? (set gift) (set auntie))])

(defn match-auntie-2 [n auntie]
  [n (let [exact (dissoc auntie :cats :trees :pomeranians :goldfish)]
       (and (set/superset? (set gift) (set exact))
            (> (or (:cats auntie) 8) 7) (> (or (:trees auntie) 4) 3)
            (< (or (:pomeranians auntie) 0) 3) (< (or (:goldfish auntie) 0) 5)))])

(defn run []
  (let [resource (slurp (io/resource "day16.txt"))
        aunties (parse-input resource)]
    ;; part 1
    (->> aunties (map-indexed match-auntie) (filter second) first first inc)
    ;; part 2
    (->> aunties (map-indexed match-auntie-2) (filter second) first first inc)))
