(ns lojic.core
  (:require [lojic.parser :refer [parser]]
            [lojic.repl   :refer [repl]])
  (:gen-class))

(defn- test-evaluator
  ([input]
     (case input
       "hello" "Hi there!"
       "quit" (do (println "Goodbye!") (System/exit 0))
       "Error!")))

(defn -main
  "I don't do a whole lot ... yet."
  ([& args]
     (repl parser)))
