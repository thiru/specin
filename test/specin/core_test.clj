(ns specin.core-test
  (:require
    [specin.core :refer [apply-specs s< s>] :as sn]))

(defn int-add
  "Add two integers."
  {:args (s< :a int? :b int?)
   :ret (s> int?)}
  [a b]
  (+ a b))

(apply-specs)
