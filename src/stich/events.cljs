(ns stich.events
  (:require
   [re-frame.core :as re-frame]
   [stich.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(defn calculate-round-labels [max-cards]
  (let [cardseq (range 1 (inc max-cards))]
    (into [] (map str (flatten [cardseq (reverse cardseq)])))))

;;TODO back button navigation (spiel abbrechen)
;;TODO game arrays anlegen: announce, actual?
;; Set Round Labels
(re-frame/reg-event-db
 ::start-game
 (fn [db _]
   (-> db
       (assoc-in [:game :round-labels] (calculate-round-labels (get-in db [:game-settings :max-cards])))
       (assoc-in [:game-settings :player-names] (subvec (get-in db [:game-settings :player-names-tmp])
                                                        0
                                                        (get-in db [:game-settings :num-players])))
       (assoc :game-state :in-progress))))


(defn fit [v new-size]
  (if (> new-size (count v))
    (into v (repeat (- new-size (count v)) ""))
    v))

(re-frame/reg-event-db
 ::change-num-players
 (fn [db [_ new-num-players]]
   (-> db
       (assoc-in [:game-settings :num-players] new-num-players)
       (assoc-in [:game-settings :player-names-tmp] (fit (get-in db [:game-settings :player-names-tmp]) new-num-players)))))

(re-frame/reg-event-db
 ::change-player-name
 (fn [db [_ player-name player-index]]
   (assoc-in db [:game-settings :player-names-tmp player-index] player-name)))

(re-frame/reg-event-db
 ::change-max-cards
 (fn [db [_ new-max-cards]]
   (assoc-in db [:game-settings :max-cards] new-max-cards)))
