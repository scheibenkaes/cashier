(ns cashier.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::line-list
 (fn [db]
   (:line-list db)))

(re-frame/reg-sub
 ::show-help
 (fn [db]
   (:show-help db)))

(re-frame/reg-sub
 ::weighted-line-list
 (fn [db] (re-frame/subscribe [::line-list]))
 (fn [list]
   (sort-by :weight list)))

(re-frame/reg-sub
 ::current-recording
 (fn [db]
   (:current-recording db)))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))
