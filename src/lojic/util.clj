(ns lojic.util)

(defn comma-list
  ([list]
     (comma-list list ""))
  ([list op]
     (case (count list)
       0 ""
       1 (first list)
       (clojure.string/join " " [(clojure.string/join ", " (butlast list))
                                 op (last list)]))))

(defn and-list
  ([list]
     (comma-list list "and")))

(defn or-list
  ([list]
     (comma-list list "or")))
