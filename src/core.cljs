(ns ^{:doc "Locations entry point"}
  locations.core
  (:use [locations.utils :only [log]]
        [events :only [on once fire]]
        [locations.map :only [Map init locate set-city]])
  (:require [locations.google :as google]))

;;; Let the journey begin!

(def MAP (atom nil))


(defn ^:export start []
  (log "start!")
  (fire :test)
  (fire :test)
  (log (satisfies? Map (google/make)))
  (let [guy (google/make)]
    (init guy "map"
          (fn []
            (log "ready to go")
            (set-city guy "Kiev, Ukraine"))
          #(log "wat iz goin on"))))

;; (once :test (fn [] (log "test")))
;; (on :ready
;;     (fn []
;;       (log "ready to go")
;;       (google/set-city "Kiev, Ukraine")))
