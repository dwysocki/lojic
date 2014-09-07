(ns lojic.parser)

(declare parser-identifier
         parser-assignment
         parser-query
         parser-boolean)

(defn boolean?
  ([token]
     (apply <= (map int [\0 token \1]))))

(defn identifier?
  ([token]
     (apply <= (map int [\a token \z]))))

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
     (ex-info (clojure.string/join "" ["Expected " expected
                                       ", but got " got])
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
         (throw (parse-error "= or ?" identifier))))))

(defn- parser-assignment
  ([identifier tokens]
     (let [tok (first tokens)]
       (if (-> tokens rest seq)
         (throw (parse-error "\\n" (second tokens)))
         (cond
          (identifier? tok) (set-variable identifier
                                          (get-variable tok))
          (boolean? tok) (set-variable identifier (parser-boolean tok))
          :default (throw (parse-error "[a-z], or [0-1]" tok)))))
     ; assignment should return nothing, so display the empty string
     "\b\b"))

(defn- parser-query
  ([identifier tokens]
     (if (empty? tokens)
       (get-variable identifier)
       (throw (parse-error "\\n" (first tokens))))))

(defn- parser-boolean
  ([token]
     (apply - (map int [token \0]))))
