(ns ^{:doc "Storage/notification system"}
  storage
  (:refer-clojure :exclude [update-in assoc-in get-in find])
  (:use [locations.utils :only [log]])
  (:require [cljs.core :as cj]))

"Надо обдумать, каким образом можно сетить и нотифицировать об обновлениях
сущностей в списках. Если есть список из нескольких похожих сущностей, не будет
же каждая из них хранить свой порядковый номер, чтобы как-то обновиться.

Как тогда? Придумать какую-то систему фильтров? Типа

  (assoc-in [:contacts {:id 5} :name] \"New Name\")

Тогда придëтся написать свою проходилку таких путей и в них проверять такие
фильтры. Более того, может тогда просто функции там ложить? Дело в том, что
нужно либо такие словари преобразовывать, либо хелпер написать, потому что
просто `#(= (% :name) 5)` не подходит - оно только трушность возвращает, а не
нужный элемент из списка. Можно типа фильтра `#(find {:id 5} %)`. Опять же,
вложенные словари можно на это проверять. Вряд ли кто-то будет использовать
словарь, как ключ?
"

(def world (atom cljs.core.PersistentHashMap/EMPTY))

(defn- notify
  [path value]
  (loop [mypath path
         dict (@world :handlers)]
    (when-not (empty? dict)
      (doseq [f (dict :_handlers [])]
        (f path value))
      (if-not (empty? mypath)
        (recur
         (rest mypath)
         ((first mypath) dict))))))

(defn on
  "Set a handler for when value in path changes"
  [path handler]
  (let [full-path (cons :handlers path)
        handler-path (cons :handlers (conj path :_handlers))]
    (if (not (get-in full-path))
      (swap! world cj/assoc-in handler-path #{}))
    (swap! world cj/update-in handler-path conj handler)))

(defn assoc-in
  [path value]
  (swap! world cj/assoc-in path value)
  (notify path value))

(defn update-in
  [path f & args]
  (apply swap! world cj/update-in path f args)
  (let [value (get-in path)]
    (notify path value)))

(defn get-in
  [path]
  (cj/get-in @world path))

(defn find
  [condition list]
  (first (filter #(= condition (select-keys % (keys condition))) list)))
