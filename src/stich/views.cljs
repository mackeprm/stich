(ns stich.views
  (:require
   [re-frame.core :as re-frame]
   [stich.subs :as subs]
   [stich.events :as events]
   ))


(defn player-name-header [player-name]
  [:th {:key (str "th-" player-name)} player-name])

(defn stich-header []
  [:tr
   [:th "Runde"]
   [:th "Phase"]
   (map player-name-header @(re-frame/subscribe [::subs/player-names]))])

(defn num-cell [num]
  [:td (str num)])

;;TODO wie übergebe ich den krams?
(defn announced [row-data]
  (println row-data)
  [:tr
   [:td (str (get row-data :round))]
   [:td "gesagt"]
   (map num-cell (:announced row-data))])

(defn actual [row-data]
  [:tr
   [:td (str (get row-data :round))]
   [:td "gemacht"]
   (map num-cell (:actual row-data))])

;;TODO punkte berechnen
(defn points [row-data]
  [:tr
   [:td (str (get row-data :round))]
   [:td "punkte"]
   [:td "-1"]
   [:td "-1"]
   [:td "10"]])

;;TODO min max
;;TODO validieren das es nicht aufgeht
;;TODO save in db
(defn announce-input [round player-index]
  [:td {:key (str "tr-anounce-input" round "-" player-index)}
   [:input {:type "number" :id (str "anounce-input" round "-" player-index)}]])

(defn announce-editor [row-data]
  [:tr
   [:td (str (get row-data :round))]
   [:td "gesagt"]
   (map #(announce-input (str (get row-data :round)) %1)
        (range (get row-data :num-players)))
   [:td [:button {:type "submit"} "Speichern"]]])

(defn point-list []
  (let [game @(re-frame/subscribe [::subs/game])]
    [:form
     ;;TODO switch to cssgrid or flexbox
     [:table
      [:thead (stich-header)]
      ;;TODO round labels
      [:tbody
       (announced {:round 1 :announced (:announced (get (:rounds game) 0))})
       (actual {:round 1 :actual (:actual (get (:rounds game) 0))})
       (points {:round 1})
       (announce-editor {:round 2 :num-players 3})
      ;;TODO verbleibende runden
       ]]]))

;; ################# GAME SETTINGS #############
(defn player-name-edit-field [idx player-name]
  [:div {:key (str "player-input-" idx)}
   [:input {:type "text"
            :value (str player-name)
            :on-change #(re-frame/dispatch [::events/change-player-name (-> % .-target .-value) idx])}]])

(defn configure-game-view []
  (let [game-settings @(re-frame/subscribe [::subs/game-settings])]
    [:div
     [:h1 "Wilkommen"]
     [:div
      [:h5 "Neues Spiel"]
      [:div
       ;;TODO anzeigen wenn zu wenig karten da sind. max-cards*num-players < 32
       [:span "Bis wieviel Karten?"]
       [:input {:type "number" :min 1 :max 10
                :value (:max-cards game-settings)
                :on-change #(re-frame/dispatch [::events/change-max-cards (-> % .-target .-value)])}]]
      [:div
       [:span "Wie viel Spieler?"]
       [:input {:type "number" :min 2 :max 100
                :value (:num-players game-settings)
                :on-change #(re-frame/dispatch [::events/change-num-players (-> % .-target .-value)])}]]
      [:div
       (map-indexed player-name-edit-field (subvec (:player-names-tmp game-settings) 0 (:num-players game-settings)))]]
     [:div
      [:button {:type "submit"
              ;;TODO form-validation
                :on-click #((.preventDefault %1)
                            (re-frame/dispatch [::events/start-game]))} "Spiel starten"]]]))


;; ################# MAIN #############
;;TODO spiel abbrechen, spiel beenden
;;TODO spielstatus: initial, in-progress,finished
(defn main-panel []
  [:div
   [:h1 "Stich"]
   (let [game-state @(re-frame/subscribe [::subs/game-state])]
     (case game-state
       :initial [configure-game-view]
       :in-progress [point-list]
       [:span "error"]))])
