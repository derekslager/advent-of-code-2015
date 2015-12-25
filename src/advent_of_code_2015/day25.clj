(ns advent-of-code-2015.day25)

(defn sequence [[row column] value]
  (let [[next-row next-column] [(if (= row 1) (inc column) (dec row))
                                (if (= row 1) 1 (inc column))]
        next-value (mod (* value 252533) 33554393)
        output [[next-row next-column] next-value]]
    (lazy-seq (cons output (apply sequence output)))))

(defn run []
  (let [row 3010, column 3019, start (long 20151125)]
    (some (fn [[[r c] v]] (when (and (= r row) (= c column)) v)) (sequence [1 1] start))))
