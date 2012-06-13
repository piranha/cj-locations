(ns ^{:doc "Locations entry point"}
  locations.core
  (:use [locations.utils :only [log]])
  (:require [locations.google :as google]))

(defn ^:export start []
  (log "start!")
  (google/init))
