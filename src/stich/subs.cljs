(ns stich.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::game-settings
 (fn [db]
   (get db :game-settings)))

(re-frame/reg-sub
 ::player-names
 (fn [db]
   (get-in db [:game-settings :player-names])))

(re-frame/reg-sub
 ::game-state
 (fn [db]
   (get db :game-state)))

(re-frame/reg-sub
 ::game
 (fn [db]
   (get db :game)))