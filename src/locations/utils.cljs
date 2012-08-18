(ns locations.utils)

(defn log [& args]
  (.apply (.-log js/console) js/console
          (into-array (map #(if (satisfies? cljs.core.ISeqable %)
                              (pr-str %)
                              %)
                           args))))
