(ns stich.events
  (:require
   [re-frame.core :as re-frame]
   [stich.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(defn calculate-round-labels [max-cards]
  (let [cardseq (range 1 (inc max-cards))]
    (into [] (flatten [cardseq (reverse cardseq)]))))

;;TODO back button navigation (spiel abbrechen)
;;TODO game arrays anlegen: announce, actual?
;; Set Round Labels
(re-frame/reg-event-db
 ::start-game
 (fn [db _]
   (-> db
       ;;TODO reset game to empty one
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

;; GAME EVENTS
;;TODO abfragen: wer gibt? (dealer-index)

;; Change predictions before actual game
;; Hier könnte man ein nicht X label einblenden
;; Vor einer runde: Vektor mit X * 0 erzeugen
;;TODO das sollte man im nachhinein anpassen können
(defn create-round-template [num-players]
  {
   :announced (into [] (take num-players(repeat 0))) 
   :actual (into [] (take num-players (repeat 0)))
  })

(re-frame/reg-event-db
 ::start-prediction-phase
 (fn [db _]
   (let [num-players (get-in db [:game-settings :num-players])
         rounds (get-in db [:game :rounds])
         next-round (create-round-template num-players)]
     (assoc-in db [:game :rounds] (conj rounds next-round)))))

(re-frame/reg-event-db
 ::change-prediction
 (fn [db [_ current-round current-player prediction-value]]))
;; Eintragen der tatsächlich gemachten stiche (muss das atomar erfolgen?)

