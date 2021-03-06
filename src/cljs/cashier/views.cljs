(ns cashier.views
  (:require [re-frame.core :as re-frame]
            [cashier.subs :as subs]
            [cashier.events :as events]
            [cashier.calc :as calc]
            [cashier.help :as help]))

;; help

(defn help-dialog
  []
  (let [show (re-frame/subscribe [::subs/show-help])]
    (fn []
      (when @show
        [:div.modal.is-active
         [:div.modal-background]
         [:div.modal-card
          [:header.modal-card-head
           [:button.delete {:on-click #(re-frame/dispatch [::events/show-help false])}]]
          [:section.modal-card-body help/help-text]
          [:footer.modal-card-foot]]]))))


;; record

(defn weight-button
  [num who]
  (fn [num who]
    [:button.button {:on-click
                              #(re-frame/dispatch
                                [::events/record-person [(js/parseInt num)
                                                         (if (= who "Cashier")
                                                           :cashier
                                                           :customer)]])} num]))
(defn record-weight
  [who]
  (let [recording (re-frame/subscribe [::subs/current-recording])]
    (fn [who]
      (let [no-input-yet? (zero? (calc/avg-of-line @recording))]
        [:div.record [:div.title.is-4 who (when-not (:cashier? @recording)
                                            (str " #" (-> @recording
                                                          :customers
                                                          count
                                                          inc)))]
         [:div.columns.is-mobile
          [:div.column
           [weight-button "1 (Teens)" who]]
          [:div.column
           [weight-button "2 (Twens)" who]]
          [:div.column
           [weight-button "3 (Thirties)" who]]]

         [:div.columns.is-mobile
          [:div.column
           [weight-button "5 (Fourties)" who]]
          [:div.column
           [weight-button "8 (Fifties)" who]]
          [:div.column
           [weight-button "13 (50+)" who]]]

         [:div.columns.is-mobile
          [:div.column.is-centered.buttons
           [:a.button {:href "#/"
                       :on-click #(re-frame/dispatch [::events/cancel-record])} [:span.icon>i.fa.fa-ban] [:span "Discard line"]]
           [:button.button {:on-click #(re-frame/dispatch [::events/show-help true])} [:span.icon>i.fa.fa-question-circle] [:span "Help"]]
           [:button.button {:disabled no-input-yet?
                            :on-click #(re-frame/dispatch [::events/store-recording-and-add-new])} [:span.icon>i.fa.fa-list] [:span "Save and add new"]]
           [:a.button {:on-click (fn [e]
                                   (if no-input-yet?
                                     (.preventDefault e)
                                     (re-frame/dispatch [::events/done-recording])))
                       :disabled no-input-yet?
                       :href     "#/"} [:span.icon>i.fa.fa-check-square-o] [:span "Done"]]
           ]]]))))


(defn record-stats
  [recording]
  (fn [recording]
    [:div.record-stats
     (when (pos? (:cashier recording))
       (str "Cashier " (:cashier recording)))
     [:ul (map-indexed (fn [idx customer]
                         [:li (str "Customer #" (inc idx) ": " customer)])
                       (:customers recording))]
     [:div (str "Sum: " (calc/sum-of-line recording) ", avg: " (calc/avg-of-line recording))]]))


(defn record-panel
  []
  (let [recording (re-frame/subscribe [::subs/current-recording])]
    (fn []
      [:section.section.is-medium
       [help-dialog]
       [:div.record-panel.container.is-fluid
        [record-weight (if (:cashier? @recording) "Cashier" "Customer")]
        [:hr]
        [record-stats @recording]]])))

(defn line-controls
  []
  [:div.columns.is-mobile
   [:div.column.is-one-third
    [:button.button {:on-click #(re-frame/dispatch [::events/reset-lines])} [:span.icon>i.fa.fa-eraser] [:span "Reset"]]]
   [:div.column.is-one-third
    [:button.button {:on-click #(re-frame/dispatch [::events/show-help true])}
     [:span.icon>i.fa.fa-question-circle] [:span "Help"]]]
   [:div.column.is-one-third
    [:a.button {:href "#/record"}
     [:span.icon>i.fa.fa-plus] [:span "Add line"]]]])

(defn format-num
  [num]
  (if (number? num)
    (.toFixed num 2)
    (str num)))


(defn line-list
  []
  (let [line-list (re-frame/subscribe [::subs/weighted-line-list])]
    (fn []
      [:div.line-list
       (if (not-empty @line-list)
         [:table.table
          [:thead>tr
           [:th "Factor"]
           [:th "Line"]]
          [:tbody.tbody
           (map-indexed (fn [idx list]
                          (let [name   (str "Line #" (:number list))
                                weight (format-num (:weight list))
                                first? (zero? idx)]
                            [:tr {:class (when first? "is-selected")} [:td (str weight)] [:td (str name)]])) @line-list)]]
         [:content "No lines yet. " [:a {:href "#/record"} "Click here to record your first one."]])])))


;; lines panel

(defn lines-panel
  []
  (fn []
    [:section.section.is-medium
     [help-dialog]
     [:div.container.is-fluid
      [:div.title.is-4 "Compare Lines"]
      [:div
       [line-controls]]
      [:hr]
      [:div.columns.is-mobile
       [:div.column.is-half.is-offset-one-quarter
        [line-list]]]]]))

;; main

(defn- panels [panel-name]
  (case panel-name
    :home-panel [lines-panel]
    :record-panel [record-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [show-panel @active-panel]))
