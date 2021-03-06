(ns ^{:doc "Simple event system"}
  events)

(def handlers (atom {}))

(defn on
  "Attach a handler to an event"
  [event handler]
  (if (not (@handlers event))
    (swap! handlers assoc event #{}))
  (swap! handlers update-in [event] conj handler))

(defn off
  "Remove handler from event"
  [event handler]
  (if (@handlers event)
    (swap! handlers update-in [event] disj handler)))

(defn once
  "Perform a handler once on a next event firing"
  [event handler]
  (defn- inner-handler [& args]
    (apply handler args)
    (off event inner-handler))
  (on event inner-handler))

(defn fire
  "Fire an event with payload"
  [event & payload]
  (doseq [handler (@handlers event)]
    (apply handler payload)))
