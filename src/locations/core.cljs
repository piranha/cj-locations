(ns ^{:doc "Locations entry point"}
  locations.core
  (:use-macros [locations.macros :only [doasync]])
  (:require [domina.css :refer [sel]]
            [domine.events :refer [listen! prevent-default]]
            [domina :refer [value]]
            [solovyov/mesto :as me]
            [clojure.browser.repl :as repl]
            [locations.utils :refer [log oget]]
            [locations.google :as google]
            [locations.map :as m]))

(comment
  (require 'cljs.repl.browser)

  (cemerick.piggieback/cljs-repl
   :repl-env (doto (cljs.repl.browser/repl-env :port 9000)
               cljs.repl/-setup)))


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
        position (oget (first results) [:geometry :location])
        _ (m/add-mark guy place position)]))))

(defn ^:export start []
  ;; (repl/connect "http://localhost:9000/repl")
  (log "start!")
  (me/assoc-in [:test] {:id 1 :name "yo"})
  (me/assoc-in [:test :name] "yo2")

  (log (pr-str (me/find {:id 1} [{:id 1 :name "q"}])))
  (doasync
   [guy (google/make)
    _ [m/init guy "map"]
    _ (log "map initialized")
    _ [m/set-city guy "Kiev, Ukraine"]
    _ (listen! (sel "#search") :click (listener guy))]))

(s/on [:test] (fn [path value]
                (log (pr-str path) ";;;" (pr-str value))))
