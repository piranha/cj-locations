(ns ^{:doc "Locations entry point"}
  locations.core
  (:use [locations.utils :only [log]]
        [events :only [on fire]])
  (:require [locations.google :as google]))

;;; Let the journey begin!

(defn ^:export start []
  (log "start!")
  (google/init "map"))

(on :ready
    (fn []
      (log "ready to go")
      (google/set-city "Kiev, Ukraine")))
