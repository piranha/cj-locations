(ns ^{:doc "Storage/notification system"}
  storage
  (:refer-clojure :exclude [update-in assoc-in])
  (:use [locations.utils :only [log]])
  (:require [cljs.core :as cj]))

(def world (atom {:handlers {}}))

(defn on
  "Set a handler for when value in path changes"
  [path handler]
  (let [full-path (cons :handlers path)
        handler-path (cons :handlers (conj path :_handlers))]
    (if (not (get-in @world full-path))
      (swap! world cj/assoc-in handler-path #{}))
    (swap! world cj/update-in handler-path conj handler)))

(defn- notify
  [path value]
  (reduce (fn [dict path-element]
            (let [inner (dict path-element)]
              (doseq [f (inner :_handlers [])]
                (f path value))
              inner))
          @world
          (cons :handlers path)))

(defn assoc-in
  [path value]
  (swap! world cj/assoc-in path value)
  (notify path value))

(defn update-in
  [path f & args]
  (apply swap! world cj/update-in path f args)
  (let [value (get-in @world path)]
    (notify path value)))
