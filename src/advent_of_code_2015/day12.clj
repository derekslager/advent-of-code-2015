(ns advent-of-code-2015.day12
  (:require [clojure.java.io :as io]
            [clojure.walk :refer [postwalk]]
            [cognitect.transit :as transit]
            [com.rpl.specter :refer [select walker]]))

(defn non-scarlet? [data]
  (not (and (map? data)
            (get (clojure.set/map-invert data) "red"))))

(defn run []
  (let [input (io/input-stream (io/resource "day12.txt"))
        reader (transit/reader input :json)
        data (transit/read reader)]
    ;; part 1
    (reduce + (select (walker number?) data))
    ;; part 2
    (reduce + (select
               (walker number?)
               (postwalk #(when (non-scarlet? %) %) data)))))

(run)
