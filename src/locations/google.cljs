(ns ^{:doc "Google Maps interface"}
  locations.google
  (:use-macros [locations.macros :only [doasync]])
  (:use [locations.utils :only [log]])
  (:require [clojure.browser.dom :as dom]
            [locations.map :as lmap]
            [goog.net.Jsonp :as Jsonp]))

(defrecord Google [gmap coder info]
  lmap/Map

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

  (set-city [this address]
    (doasync
     [results [lmap/locate this address]
      point (first results)
      _ (.setCenter @gmap point.geometry.location)
      _ (.setZoom @gmap 12)
      _ (.fitBounds @gmap point.geometry.viewport)])))

(defn make []
  (->Google (atom nil) (atom nil) (atom nil)))
