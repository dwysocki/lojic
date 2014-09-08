(ns lojic.core
  (:require [lojic.parser :refer [parser]]
            [lojic.repl   :refer [repl]])
  (:gen-class))

(defn- line-numbering-pushback-reader
  ([filename]
     (clojure.lang.LineNumberingPushbackReader.
      (java.io.FileReader. filename))))

(defn -main
  "I don't do a whole lot ... yet."
  ([& args]
     (try
       (case (count args)
         0 (repl parser)
         1 (binding [*in* (line-numbering-pushback-reader (first args))]
             (repl parser ""))
         (println "Usage: lojic [filename]"))
       (catch java.io.FileNotFoundException e
           (println "File" (first args) "not found.")))))
