(defproject locations "0.0.1"
  :description "Some stuff to play with"
  :plugins [[lein-cljsbuild "0.2.1"]]
  :dependencies [[org.clojure/clojure "1.4.0"]
                 ;; [enfocus "0.9.1-SNAPSHOT"]
                 ;; [domina "1.0.0-beta4"]
                 ]
  :hooks [leiningen.cljsbuild]
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
               }})
