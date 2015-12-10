(ns advent-of-code-2015.day7
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def ops
  {:and bit-and :or bit-or
   :lshift bit-shift-left :rshift bit-shift-right
   :not bit-not})

(defn make-op [op & args]
  {:op op
   :args args})

(defn make-unary-op [val]
  (let [op (if (re-matches #"\d+" val) :constant :var)]
    (make-op
     op
     (if (= op :constant) (Integer/parseInt val) val))))

(def eval-op
  (memoize (fn [network {:keys [op args] :as opn}]
             (condp = op
               :constant (first args)
               :var (eval-op network (get network (first args)))
               (apply (get ops op) (map (partial eval-op network) args))))))

(defn run []
  (let [resource (slurp (io/resource "day7.txt"))
        instructions (str/split-lines resource)
        state (atom {})]

    (doseq [instruction instructions]
      (let [[_ expr target] (re-matches #"(.*) -> ([a-z]+)" instruction)]
        (swap! state assoc target
               (condp re-matches expr
                 #"(\w+)" :>> (fn [[_ val]] (make-unary-op val))
                 #"([a-z]+) (RSHIFT|LSHIFT) (\d+)"
                 :>> (fn [[_ sym-shift op shift-by]]
                       (make-op (if (= op "RSHIFT") :rshift :lshift) (make-op :var sym-shift) (make-unary-op shift-by)))
                 #"(\w+) (AND|OR) (\w+)"
                 :>> (fn [[_ lhs op rhs]]
                       (make-op (if (= op "AND") :and :or) (make-unary-op lhs) (make-unary-op rhs)))
                 #"NOT ([a-z]+)" :>> (fn [[_ sym]] (make-op :not (make-unary-op sym)))))))

    ;; part 1
    (eval-op @state {:op :var :args ["a"]})

    ;; part 2
    (swap! state assoc "b" (make-unary-op "46065"))
    (eval-op @state {:op :var :args ["a"]})))
