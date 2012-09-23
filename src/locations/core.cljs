(ns ^{:doc "Locations entry point"}
  locations.core
  (:use-macros [locations.macros :only [doasync]])

  (:use [locations.utils :only [log]]
        [domina.css :only [sel]]
        [domina.events :only [listen! prevent-default]]
        [domina :only [value]])

  (:require [storage :as s]
            [locations.google :as google]
            [locations.map :as m]
            [clojure.browser.repl :as repl]))

;;; Let the journey begin!

(defn get-places []
  (-> (sel "#locations")
                 (value)
                 (.split "\n")))

(defn listener [guy]
  (fn [evt]
    (prevent-default evt)
    (doseq [place (get-places)]
      (doasync
       [results [m/locate guy place]
        position (-> (first results) (.-geometry) (.-location))
        _ (m/add-mark guy place position)]))))

(defn ^:export start []
  ;; (repl/connect "http://localhost:9000/repl")
  (log "start!")
  (s/assoc-in [:test] {:id 1 :name "yo"})
  (s/assoc-in [:test :name] "yo2")
  (doasync
   [guy (google/make)
    _ [m/init guy "map"]
    _ (log "map initialized")
    _ [m/set-city guy "Kiev, Ukraine"]
    _ (listen! (sel "#search") :click (listener guy))]))

(s/on [:test] (fn [path value]
                (log (pr-str path) (pr-str value))))
