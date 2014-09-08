(ns lojic.parser
  (:require [lojic.util :as util]))

(declare parser-identifier
         parser-assignment
         parser-query
         parser-expression
         parser-and
         parser-or
         parser-xor
         parser-not-paren-id-lit
         parser-literal)

(defn identifier?
  ([token]
     (apply <= (map int [\a token \z]))))

(defn literal?
  ([token]
     (apply <= (map int [\0 token \1]))))

(def variables
  (int-array (apply - (map int [\z \a]))))

(defn- variable-index
  ([var-name]
     (apply - (map int [var-name \a]))))

(defn- get-variable
  ([var-name]
     (aget variables (variable-index var-name))))

(defn- set-variable
  ([var-name value]
     (aset variables (variable-index var-name) value)))

(defn tokenize
  ([input]
     (filter #(not (Character/isWhitespace %)) input)))

(defn parse-error
  ([expected got]
     (ex-info (str "Expected " (util/or-list expected)
                   ", but got " got)
              {:cause :parse-error})))

(defn parser
  ([input]
     (let [tokens (tokenize input)
           tok    (first tokens)]
       (try
         (cond
          ; this is making it crash :\
          (identifier? tok) (parser-identifier tok (rest tokens))
          ; replace this with an exception.
          ; make a special ex-info wrapper function which provides it with
          ; the "Expected ___ got ___" messages
          :default (clojure.string/join " " ["Expected a-z, but got" tok]))
         (catch clojure.lang.ExceptionInfo e
           (if (= :parse-error (-> e ex-data :cause))
             (.getMessage e)
             "Segmentation fault"))))))

(defn- parser-identifier
  ([identifier tokens]
     (let [operator (first tokens)]
       (case operator
         \= (parser-assignment identifier (rest tokens))
         \? (parser-query      identifier (rest tokens))
         (throw (parse-error ["=", "?"] identifier))))))

(defn- parser-assignment
  ([identifier tokens]
     (set-variable identifier (parser-expression tokens))
     ; successful assignment is silent, so return nil
     nil))


     ;; (if-let [tok (first tokens)]
       
     ;;   (if (-> tokens rest seq)
     ;;     (throw (parse-error ["\\n"] (second tokens)))
     ;;     (cond
     ;;      (identifier? tok) (set-variable identifier
     ;;                                      (get-variable tok))
     ;;      (boolean? tok) (set-variable identifier (parser-boolean tok))
     ;;      :default (throw (parse-error ["[a-z]", "[0-1]"] tok))))
     ;;   (throw (parse-error ["[a-z]", "[0-1]"] "\\n")))
     ;; ; assignment returns nothing
     ;; nil))

(defn- parser-query
  ([identifier tokens]
     (if (empty? tokens)
       (get-variable identifier)
       (throw (parse-error ["\\n"] (first tokens))))))

(defn- parser-expression
  ([tokens]
     (let [[r tokens] (parser-or tokens)]
       (if (empty? tokens)
         r
         (throw (parse-error ["\\n"] (first tokens)))))))



(defn- parser-or
  ([tokens]
     (let [[r tokens] (parser-xor tokens)]
       (if (= (first tokens) \|)
         (let [[-r tokens] (parser-or (rest tokens))]
           [(bit-or r -r) tokens])
         [r tokens]))))

(defn- parser-xor
  ([tokens]
     (let [[r tokens] (parser-and tokens)]
       (if (= (first tokens) \^)
         (let [[-r tokens] (parser-xor (rest tokens))]
           [(bit-xor r -r) tokens])
         [r tokens]))))

(defn- parser-and
  ([tokens]
     (let [[r tokens] (parser-not-paren-id-lit tokens)]
       (if (= (first tokens) \&)
         (let [[-r tokens] (parser-and (rest tokens))]
           [(bit-and r -r) tokens])
         [r tokens]))))

(defn- parser-not-paren-id-lit
  ([tokens]
     (let [tok (first tokens)]
       (cond
        ; not
        (= tok \~) (let [[r tokens] (parser-or (rest tokens))]
                     [(bit-xor r 1) tokens])
        ; parenthesis
        (= tok \() (let [[r tokens] (parser-or (rest tokens))
                         tok (first tokens)]
                     (if (= tok \))
                       [r (rest tokens)]
                       (throw (parse-error [")"] tok))))
        ; identifier
        (identifier? tok) [(get-variable tok) (rest tokens)]
        ; literal
        (literal? tok) [(parser-literal tok) (rest tokens)]
        :default (throw (parse-error ["~" "(" "[a-z]" "[0-1]"] tok))))))

(defn- parser-literal
  ([token]
     (apply - (map int [token \0]))))
