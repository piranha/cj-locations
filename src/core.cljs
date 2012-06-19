(ns ^{:doc "Locations entry point"}
  locations.core
  (:use [locations.utils :only [log]]
        [events :only [on once fire]])
  (:require [locations.google :as google]))

;;; Let the journey begin!

(defn ^:export start []
  (log "start!")
  (fire :test)
  (fire :test)
  (google/init "map"))

(once :test (fn [] (log "test")))
(on :ready
    (fn []
      (log "ready to go")
      (google/set-city "Kiev, Ukraine")))
