(ns advent-of-code-2015.day19
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn match-indexes [pattern string]
  (let [matcher (re-matcher pattern string)]
    (loop [indexes [], match (re-find matcher)]
      (if (nil? match)
        indexes
        (recur (conj indexes [(.start matcher) (.end matcher)]) (re-find matcher))))))

(defn replace-string [[start end] string replacement]
  (str (subs string 0 start) replacement (subs string end)))

(defn shrink [replacements current goal steps]
  (if (= current goal) steps
      (let [[replace with first-index]
            (first (for [[_ replace with] replacements
                         :let [first-index (first (match-indexes (re-pattern with) current))]
                         :when first-index]
                     [replace with first-index]))]
        (if (nil? first-index)
          Integer/MAX_VALUE
          (shrink replacements (replace-string first-index current replace) goal (inc steps))))))

(defn run []
  (let [resource (slurp (io/resource "day19.txt"))
        replacements (re-seq #"(\w+) => (\w+)" resource)
        molecule (last (str/split-lines resource))]

    ;; part 1
    (->> (for [[_ replace with] replacements
               index (match-indexes (re-pattern replace) molecule)]
           (replace-string index molecule with))
         distinct
         count)

    ;; part 2
    (apply min (for [_ (range 20)]
                 (shrink (shuffle replacements) molecule "e" 0)))))
