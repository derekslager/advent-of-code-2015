(ns advent-of-code-2015.day11)

(def A 97)
(def Z 122)
(def illegal #{105 111 108})

(defn has-straight [ccs]
  (-> (map (fn [[a b]] (- b a)) (partition 2 1 ccs))
      (java.util.Collections/indexOfSubList [1 1])
      (not= -1)))

(defn all-legal-chars [ccs]
  (every? not (map illegal ccs)))

(defn non-overlapping [ccs]
  (>= (->> (partition 2 1 ccs)
           (filter #(apply = %))
           set
           count)
      2))

(defn is-legit [ccs]
  (and (has-straight ccs)
       (all-legal-chars ccs)
       (non-overlapping ccs)))

(defn next-pw [ccs]
  (loop [pos (- (count ccs) 1), vals ccs]
    (let [c (get vals pos)]
      (if (= c Z)
        (recur (- pos 1) (assoc vals pos A))
        (assoc vals pos (+ c 1))))))

(defn run []
  (let [input "vzbxxyzz"
        ccs (vec (map int input))]
    (->> (iterate next-pw ccs)
         (drop 1)
         (filter is-legit)
         first
         (map char)
         char-array
         (String.))))
