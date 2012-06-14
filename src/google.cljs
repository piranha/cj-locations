(ns ^{:doc "Google Maps interface"}
  locations.google
  (:use [locations.utils :only [log]]
        [events :only [on fire]])
  (:require [clojure.browser.dom :as dom])
  (:require-macros [enfocus.macros :as em]))

(def state
  {:el (atom nil)
   :map (atom nil)
   :coder (atom nil)
   })

(doseq [[key val] state]
  (add-watch val nil
             (fn [k r o n]
               (fire (keyword (str (name key) "-change")) n))))

(def url
  "http://maps.google.com/maps/api/js?sensor=false&callback=locations.google.init")

(defn start [el-id]
  (log "trying to initialize map")
  (reset! (state :el)
          (dom/get-element el-id))
  (.importScript_ js/goog url))

(defn ^:export init []
  (reset! (state :map)
          (google.maps.Map.
           @(state :el)
           (js-obj "mapTypeId" google.maps.MapTypeId.ROADMAP)))
  (reset! (state :coder)
          (google.maps.Geocoder.))
  (fire :ready "holy shit we are going in" "test")
  (log "map initialized"))
  
