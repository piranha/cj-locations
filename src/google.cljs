(ns ^{:doc "Google Maps interface"}
  locations.google
  (:use [locations.utils :only [log]]
        [events :only [on fire]]
        [locations.map :only [init locate set-city]])
  (:require [clojure.browser.dom :as dom]
            [locations.map :as lmap]
            [goog.net.Jsonp :as Jsonp]))

(defrecord Google [gmap coder info]
  lmap/Map

  (init [this el-id callback errback]
    (let [el (dom/get-element el-id)
          request (goog.net.Jsonp. "http://maps.google.com/maps/api/js" "callback")
          args (js-obj "sensor" false)]
      (.send request args
             (fn []
               (reset! gmap (google.maps.Map.
                             el
                             (js-obj "mapTypeId" google.maps.MapTypeId.ROADMAP)))
               (reset! coder (google.maps.Geocoder.))
               (reset! info (google.maps.InfoWindow.))
               (callback))
             errback)))

  (locate [this address callback errback]
    (.geocode @coder (js-obj "address" address)
              (fn [results status]
                (log "query done" status)
                (case status
                  google.maps.GeocoderStatus.OK (callback results)
                  google.maps.GeocoderStatus.OVER_QUERY_LIMIT (errback "WAIT")
                  (errback status)))))

  (set-city [this address]
    (locate this address
            (fn [results]
              (let [point (first results)]
                (.setCenter @gmap point.geometry.location)
                (.setZoom @gmap 12)
                (.fitBounds @gmap point.geometry.viewport)))
            log)))

(defn make []
  (->Google (atom nil) (atom nil) (atom nil)))
