(ns advent-of-code-2015.day22
  (:require [clojure.test :refer [deftest is]]))

(def spell-book [{:cost 53 :damage 4}
                 {:cost 73 :damage 2 :heals 2}
                 {:cost 113 :effect {:turns 6 :armor 7}}
                 {:cost 173 :effect {:turns 6 :damage 3}}
                 {:cost 229 :effect {:turns 5 :mana 101}}])

(defn valid-spells [{:keys [mana effects]}]
  (filter
   (fn [{:keys [cost effect] :as spell}]
     (and (<= cost mana)
          (let [active-effect (first (filter #(= (dissoc % :turns) (dissoc effect :turns)) effects))]
            (or (not active-effect) (= (:turns active-effect) 1)))))
   spell-book))

(deftest test-valid-spells
  (is (= 0 (count (valid-spells {:mana 0 :effects []}))))
  (is (= 1 (count (valid-spells {:mana 53 :effects []}))))
  (is (= 5 (count (valid-spells {:mana 229 :effects []}))))
  (is (= 4 (count (valid-spells {:mana 500 :effects [{:turns 3 :damage 3}]}))))
  (is (= 5 (count (valid-spells {:mana 500 :effects [{:turns 1 :damage 3}]}))))
  (is (= 2 (count (valid-spells {:mana 500 :effects [{:turns 6 :armor 7} {:turns 6 :damage 3} {:turns 5 :mana 101}]})))))

(defn apply-effects [{:keys [effects] :as state}]
  (let [{:keys [damage armor mana] :or {damage 0 armor 0 mana 0}} (apply merge effects)]
    (-> state
        (update :boss-hp - damage)
        (update :mana + mana)
        (assoc :armor armor)
        (assoc :effects
               (->> effects
                    (map (fn [m] (update m :turns dec)))
                    (filter (fn [m] (> (:turns m) 0))))))))

(defn apply-hard-mode [{:keys [hard] :as state}]
  (if hard (update state :me-hp dec)
      state))

(defn turn [battle-state]
  (let [{:keys [attacker effects boss-damage] :as state} (-> battle-state
                                                             apply-effects
                                                             apply-hard-mode)
        all-valid-spells (valid-spells state)]
    (if (empty? all-valid-spells)
      (assoc state :no-more-spells true)
      (let [{:keys [cost effect] :as spell} (if (= attacker :me) (rand-nth all-valid-spells) {:cost 0})]
        (-> state
            (update :me-hp + (or (:heals spell) 0))
            (update :me-hp #(if (= attacker :boss) (- % (- boss-damage (:armor state))) %))
            (update :boss-hp #(if (= attacker :me) (- % (or (:damage spell) 0)) %))
            (update :spent + cost)
            (update :mana - cost)
            (assoc :effects (if effect (conj effects effect) effects))
            (assoc :attacker (if (= attacker :me) :boss :me)))))))

(defn game-over [{:keys [no-more-spells boss-hp me-hp] :as state}]
  (or no-more-spells (<= boss-hp 0) (<= me-hp 0)))

(defn run []
  (let [boss {:hit-points 58 :damage 9}]
    (loop [n 100000, best Integer/MAX_VALUE]
      (if (= n 0)
        best
        (let [result
              (-> (drop-while
                   #(not (game-over %))
                   (iterate
                    turn
                    {:attacker :me
                     :boss-hp (:hit-points boss)
                     :boss-damage (:damage boss)
                     :me-hp 50
                     :mana 500
                     :spent 0
                     :armor 0
                     :effects []
                     :hard true}))
                  first)
              i-won (<= (:boss-hp result) 0)
              spent (:spent result)]
          (when (and i-won (< spent best))
            (println "new best!" spent "beats" best result))
          (recur (dec n) (if i-won (min spent best) best)))))))

(run)
