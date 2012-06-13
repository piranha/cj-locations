(ns ^{:doc "Google Maps interface"}
  locations.google
  (:use [locations.utils :only [log]]
        [domina.css :only [sel]]
        [domina :only [single-node set-attr! append!]])
  (:require-macros [enfocus.macros :as em]))

(def url "http://maps.google.com/maps/api/js?sensor=false&callback=locations.google.callback")

(defn ^:export callback []
  (log "map initialized"))

(defn init []
  (log "trying to initialize map")
  (append!
   (sel "head")
   (set-attr! (single-node "<script></script>") "src" url)))
  
