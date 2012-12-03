(defproject locations "0.0.1"
  :description "Some stuff to play with"
  :source-paths ["deps/clojurescript/src/clj"
                 "deps/clojurescript/src/cljs"]
  :plugins [[lein-cljsbuild "0.2.9" :hooks false]]
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [com.cemerick/piggieback "0.0.2"]
                 ;; [enfocus "0.9.1-SNAPSHOT"]
                 [domina "1.0.1"]
                 [solovyov/mesto "0.2.1"]
                 ]
  :injections [(require 'cemerick.piggieback)]
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
  ;; :hooks [leiningen.cljsbuild]
  :cljsbuild {:builds
              {
               :main {
                      :source-path "src"
                      :compiler {
                                 :output-to "build/locations.js"
                                 :optimizations :whitespace
                                 :pretty-print true
                                 }
                      }
               :mini {
                      :source-path "src"
                      :compiler {
                                 :output-to "build/mini.js"
                                 :optimizations :advanced
                                 :externs ["externs/google_maps_api_v3_9.js"]
                                 }
                      }
               }}
)
