(ns advent-of-code-2015.day4
  (:import java.math.BigInteger
           java.security.MessageDigest))

(defn candidates [key]
  (->> (interleave (repeat key) (range))
       (partition 2)
       (map #(apply str %))))

(defn md5 [s]
  (let [digest (MessageDigest/getInstance "MD5")
        hash (.digest digest (.getBytes s))]
    hash))

(defn hex [b]
  (apply str (map #(format "%02x" %) b)))

(defn run []
  (let [key "yzbqklnj"]
    (first
     (for [candidate (candidates key)
           :when (.startsWith (hex (md5 candidate)) "000000")]
       candidate))))
