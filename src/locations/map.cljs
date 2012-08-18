(ns
    locations.map)

(defprotocol Map
  (init [this el-id callback])
  (locate [this address callback])
  (set-city [this address]))
