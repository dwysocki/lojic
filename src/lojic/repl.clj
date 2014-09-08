(ns lojic.repl)

(defn- prompt
  "Displays prompt and flushes to stdout without printing a newline."
  ([prompt]
     (print prompt)
     (flush)))

(defn repl
  "Prompts the user for input with the `prompt-str` (or \"# \" by default),
  reads a line of input, evalutates the input through the `evaluator` function,
  prints the result of evaluation, and repeats forever."
  ([evaluator]
     (repl evaluator "# "))
  ([evaluator prompt-str]
     (repl evaluator prompt-str ""))
  ([evaluator prompt-str exit-msg]
     (loop []
       (prompt prompt-str)
       (if-let [input  (read-line)]
         (let [output (evaluator input)]
           (when output
             (println output))
           (recur))
         (println exit-msg)))))
