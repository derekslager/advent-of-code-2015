(ns advent-of-code-2015.day15
  (:require [clojure.java.io :as io]
            [clojure.math.combinatorics :refer [selections]]))

(defn parse-input [resource]
  (into [] (for [[_ ingredient props] (re-seq #"(\w+): (.*)" resource)]
             [ingredient (into {} (for [[_ k v] (re-seq #"(\w+) (-?\d+)" props)]
                                    [(keyword k) (Integer/parseInt v)]))])))

(defn score-key [stats recipe key]
  (max 0 (reduce + (map * recipe (for [[_ stat] stats] (key stat))))))

(defn score [stats recipe]
  (apply *
         (for [key [:capacity :durability :flavor :texture]
               :let [score (score-key stats recipe key)]
               :while (not= 0 score)]
           score)))

(defn run []
  (let [resource (slurp (io/resource "day15.txt"))
        stats (parse-input resource)
        ingredients (set (map first stats))]
    ;; part 1
    (apply max
           (for [recipe (selections (range 1 97) (count ingredients))
                 :when (= 100 (reduce + recipe))
                 ;; part 2
                 :when (= 500 (score-key stats recipe :calories))]
             (score stats recipe)))))
