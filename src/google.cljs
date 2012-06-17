(ns ^{:doc "Google Maps interface"}
  locations.google
  (:use [locations.utils :only [log]]
        [events :only [on fire]])
  (:require [clojure.browser.dom :as dom]
            [goog.net.Jsonp :as Jsonp]))

(def gmap (atom nil))
(def coder (atom nil))
(def info (atom nil))

(defn gotApi [el]
  (fn []
    (reset! gmap
            (google.maps.Map.
             el
             (js-obj "mapTypeId" google.maps.MapTypeId.ROADMAP)))
    (reset! coder (google.maps.Geocoder.))
    (fire :ready "hehe")))

(defn init [el-id]
  (log "trying to initialize map")
  (let [el (dom/get-element el-id)
        request (goog.net.Jsonp. "http://maps.google.com/maps/api/js" "callback")
        args (js-obj "sensor" false)
        callback (gotApi el)
        errback #(log "error loading map api")]
    (.send request args callback errback)))
  
