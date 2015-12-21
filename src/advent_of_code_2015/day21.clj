(ns advent-of-code-2015.day21
  (:require [clojure.math.combinatorics :refer [subsets]]))

(def weapons
  [{:cost 8 :damage 4 :armor 0}
   {:cost 10 :damage 5 :armor 0}
   {:cost 25 :damage 6 :armor 0}
   {:cost 40 :damage 7 :armor 0}
   {:cost 74 :damage 8 :armor 0}])

(def armor
  [{:cost 13 :damage 0 :armor 1}
   {:cost 31 :damage 0 :armor 2}
   {:cost 53 :damage 0 :armor 3}
   {:cost 75 :damage 0 :armor 4}
   {:cost 102 :damage 0 :armor 5}])

(def rings
  [{:cost 25 :damage 1 :armor 0}
   {:cost 50 :damage 2 :armor 0}
   {:cost 100 :damage 3 :armor 0}
   {:cost 20 :damage 0 :armor 1}
   {:cost 40 :damage 0 :armor 2}
   {:cost 80 :damage 0 :armor 3}])

(defn battle [boss me]
  (let [my-divisor (- (:damage boss) (:armor me))
        turns-to-my-death (Math/ceil (/ 100 (if (<= my-divisor 0) 1 my-divisor)))
        boss-divisor (- (:damage me) (:armor boss))
        turns-to-boss-death (Math/ceil (/ (:hit-points boss) (if (<= boss-divisor 0) 1 boss-divisor)))]
    (>= turns-to-my-death turns-to-boss-death)))

(defn purchases []
  (for [weapon weapons
        armor (->> (subsets armor) (filter #(<= (count %) 1)))
        rings (->> (subsets rings) (filter #(<= (count %) 2)))]
    (merge-with + weapon (apply merge-with + armor) (apply merge-with + rings))))

(defn run []
  (let [boss {:hit-points 103 :damage 9 :armor 2}]
    (for [[truth final] [[true? min] [false? max]]]
      (->> (purchases)
           (map (juxt :cost (partial battle boss)))
           (filter (comp truth second))
           (map first)
           (apply final)))))
