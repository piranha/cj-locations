(ns locations.utils)

(defn log [& args]
  (.log js/console (pr-str args)))
