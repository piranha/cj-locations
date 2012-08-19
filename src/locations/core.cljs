(ns ^{:doc "Locations entry point"}
  locations.core
  (:use-macros [locations.macros :only [doasync]])
  (:use [locations.utils :only [log]]
        [locations.map :only [init locate set-city]])
  (:require [locations.google :as google]))

;;; Let the journey begin!

(defn ^:export start []
  (log "start!")
  (doasync
   [guy (google/make)
    _ [init guy "map"]
    _ (log "map initialized")
    _ (set-city guy "Kiev, Ukraine")]))
