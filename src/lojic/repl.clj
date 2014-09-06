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
     (loop []
       (prompt prompt-str)
       (let [input  (read-line)
             output (evaluator input)]
         (println output)
         (recur)))))
