(ns locations.utils)

(defn- to-string [obj]
  (if (and (satisfies? cljs.core.ISeqable obj)
           (not (instance? js/String obj)))
    (pr-str obj)
    obj))

(defn log [& args]
  (.apply (.-log js/console) js/console
          (into-array (map to-string args))))

(defn oget [obj path]
  (reduce #(aget %1 %2) obj path))
