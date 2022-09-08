(ns stich.events
  (:require
   [re-frame.core :as re-frame]
   [stich.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))
