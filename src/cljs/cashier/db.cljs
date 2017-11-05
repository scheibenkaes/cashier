(ns cashier.db)

(def initial-recording
  {:cashier?  true
   :customers []
   :cashier   0})

(def default-db
  {:name              "re-frame"
   :line-list         []
   :current-recording initial-recording})
