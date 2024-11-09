(ns user
  "Initial namespace loaded when using a REPL (e.g. using `clj`)."
  {:clj-kondo/config '{:linters {:unused-namespace {:level :off}
                                 :unused-referred-var {:level :off}}}}
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [clojure.reflect :as reflect]
    [clojure.repl :refer [doc]]
    [clojure.spec.alpha :as s]
    [clojure.spec.test.alpha :as st]
    [clojure.string :as str]
    [puget.printer :as puget]
    [rebel-readline.main :as rebel]
    [specin.core :refer [apply-specs s< s>] :as sn]
    [utils.nrepl :as nrepl]
    [utils.printing :as printing :refer [PP]]))


(defonce initialised? (atom false))

(when (not @initialised?)
  (reset! initialised? true)
  (printing/install-expound-printer)
  (nrepl/start-server)
  ;; Blocking call:
  (rebel/-main)
  (nrepl/stop-server)
  ;; HACK: rebel-readline causes process to hang and not quit without this:
  (System/exit 0))

