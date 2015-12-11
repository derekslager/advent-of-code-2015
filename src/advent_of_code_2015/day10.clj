(ns advent-of-code-2015.day10)

(defn look-and-say [n]
  (->> (re-seq #"(\d)\1*" n)
       (map #(let [f (first %)] [(count f) (first f)]))
       flatten
       (apply str)))

(defn run []
  (let [input "1321131112"]
    (->> (iterate look-and-say input)
         (drop 50)
         first
         count)))
