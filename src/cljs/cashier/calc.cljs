(ns cashier.calc)


(defn sum-of-line
  [{:keys [cashier customers]}]
  (apply + cashier customers))

(defn avg-of-line
  [{:keys [cashier customers] :as recording}]
  (/ (sum-of-line recording)
     (inc (count customers))))
