(ns ^{:doc "Locations entry point"}
  locations.core
  (:use-macros [locations.macros :only [except]])
  (:use [locations.utils :only [log]]
        [events :only [on once fire]]
        [locations.map :only [init locate set-city]])
  (:require [locations.google :as google]))

;;; Let the journey begin!

(def MAP (atom nil))


(defn ^:export start []
  (log "start!")
  ;; (except
  ;;  (throw "test exceptions")
  ;;  log)
  (try
    (throw "test exceptions")
    (catch js/Object e
      (log "Error!" e)))
  (fire :test)
  (fire :test)
  (log (satisfies? lmap/Map (google/make)))
  (let [guy (google/make)]
    (init guy "map"
          (fn []
            (log "ready to go")
            (set-city guy "Kiev, Ukraine")))))
