(ns cashier.events
  (:require [re-frame.core :as re-frame]
            [cashier.db :as db]
            [cashier.calc :as calc]))

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))


(re-frame/reg-event-db
 ::reset-lines
 (fn [db [_ active-panel]]
   (assoc db :line-list [])))


(re-frame/reg-event-db
 ::show-help
 (fn [db [_ show]]
   (assoc db :show-help show)))


(re-frame/reg-event-db
 ::cancel-record
 (fn [db [_ _]]
   (assoc db
          :active-panel :home-panel
          :current-recording db/initial-recording)))

(re-frame/reg-event-fx
 ::store-recording-and-add-new
 (fn [{db :db} [_ _]]
   {:db (assoc db :current-recording db/initial-recording)
    :dispatch [::store-recording (:current-recording db)]}))

(re-frame/reg-event-db
 ::store-recording
 (fn [db [_ {:keys [customers cashier] :as recording}]]
   (let [num-recordings (count (:line-list db))]
     (update db :line-list conj
             {:customers customers
              :cashier cashier
              :weight (calc/avg-of-line recording)
              :number (inc num-recordings)}))))

(re-frame/reg-event-fx
 ::done-recording
 (fn [{db :db} [_ _]]
   {:db (assoc db
               :current-recording db/initial-recording)
    :dispatch-n [[::store-recording (:current-recording db)]
                 [::set-active-panel :home-panel]]}))

(re-frame/reg-event-db
 ::record-person
 (fn [db [_ [num who]]]
   (if (= who :cashier)
     (-> db
         (assoc-in [:current-recording :cashier] num)
         (assoc-in [:current-recording :cashier?] false))
     (update-in db [:current-recording :customers] conj num))))

(re-frame/reg-event-db
 ::set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))
