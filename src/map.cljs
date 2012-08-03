(ns
    locations.map)

(defprotocol Map
  (init [this el-id callback errback])
  (locate [this address callback errback])
  (set-city [this address]))
