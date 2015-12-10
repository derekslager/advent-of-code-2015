(ns advent-of-code-2015.day8
  (:require [clojure.java.io :as io]
            [clojure.string :as str])
  (:import java.util.regex.Matcher))

(defn unescape-string [string]
  (-> string
      (str/replace #"\\\\" (Matcher/quoteReplacement "\""))
      (str/replace #"\\\"" (Matcher/quoteReplacement "\\"))
      (str/replace #"\\x[0-9,a-f]{2}" #(str (char (Integer/parseInt (.substring %1 2) 16))))))

(defn escape-string [string]
  (str "\""
       (-> string
           (str/replace "\\" "\\\\")
           (str/replace "\"" "\\\""))
       "\""))

(defn count-string [[l r] string]
  [(+ l (count string))
   (+ r (- (count (unescape-string string)) 2))])

(defn count-string-2 [[l r] string]
  [(+ l (count (escape-string string)))
   (+ r (count string))])

(defn run []
  (let [resource (slurp (io/resource "day8.txt"))
        strings (str/split-lines resource)
        counts (reduce count-string [0 0] strings)
        counts-2 (reduce count-string-2 [0 0] strings)]
    ;; part 1
    (apply - counts)
    ;; part 2
    (apply - counts-2)))
