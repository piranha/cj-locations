(ns ^{:doc "Google Maps interface"}
  locations.google
  (:use-macros [locations.macros :only [doasync]])
  (:use [locations.utils :only [log]]
        [locations.map :only [Map locate]])
  (:require [clojure.browser.dom :as dom]
            [goog.net.Jsonp :as Jsonp]))

(defrecord Google [gmap coder info]
  Map

  (init [this el-id callback]
    (doasync
     [el (dom/get-element el-id)
      request (goog.net.Jsonp. "http://maps.google.com/maps/api/js" "callback")
      args (js-obj "sensor" false)
      _ [.send request args]
      -gmap (google.maps.Map.
             el (js-obj "mapTypeId" google.maps.MapTypeId.ROADMAP))

      ;; set properties
      _ (reset! gmap -gmap)
      _ (reset! coder (google.maps.Geocoder.))
      _ (reset! info (google.maps.InfoWindow.))

      _ (callback)]))

  (locate [this address callback]
    (doasync
     [args (js-obj "address" address)
      [results status] [.geocode @coder args]
      _ (log address "query done:" status)
      _ (case status
          google.maps.GeocoderStatus.OK (callback results)
          google.maps.GeocoderStatus.OVER_QUERY_LIMIT (throw "WAIT")
          (throw status))]))

  (add-mark [this title position]
    (google.maps.Marker.
     (js-obj "map" @gmap
             "position" position
             "title" title
             "flat" true)))
  
  (set-city [this address callback]
    (doasync
     [results [locate this address]
      point (first results)
      geometry (.-geometry point)
      _ (.setCenter @gmap (.-location geometry))
      _ (.setZoom @gmap 12)
      _ (.fitBounds @gmap (.-viewport geometry))
      _ (callback)])))

(defn make []
  (->Google (atom nil) (atom nil) (atom nil)))
