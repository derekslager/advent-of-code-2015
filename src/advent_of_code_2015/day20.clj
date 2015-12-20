(ns advent-of-code-2015.day20)

(defn visit-houses [n value-per-house limit]
  (let [houses (int-array n)]
    (doseq [elf (range 1 n)
            visits (range elf (if limit (min n (* limit elf)) n) elf)]
      (aset-int houses visits (+ (* value-per-house elf) (aget houses visits))))
    houses))

(defn run []
  (let [goal 34000000
        search-bound 1000000
        houses (visit-houses search-bound 10 nil)
        houses-2 (visit-houses search-bound 11 50)]
    (->> houses-2
         (map-indexed vector)
         (filter (fn [[n count]] (>= count goal)))
         first)))
