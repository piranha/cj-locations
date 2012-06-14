(ns ^{:doc "Locations entry point"}
  locations.core
  (:use [locations.utils :only [log]]
        [events :only [on]])
  (:require [locations.google :as google]))
            ;; [clojure.browser.repl :as repl]))

(on :el-change #(log "interesting"))

(defn ^:export start []
  (log "start!")
  (on :ready log)
  (google/start "map"))
