(ns ^{:doc "Common maps interface"}
    locations.map)

(defprotocol Map
  (init [this el-id callback])
  (locate [this address callback])
  (add-mark [this title position])
  (set-city [this address callback]))
