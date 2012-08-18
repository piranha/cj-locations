(ns ^{:doc "Locations macros"}
  locations.macros)

(defmacro except
  "Simplify handling of exceptions in ClojureScript.

  NOTE: not working right now. :(

  Wraps all but last expressions in a try block, using last as function in a
  catch block. This function is passed instance of an exception as a sole
  argument.

  Example:

  (except
    (some-expression)
    (some-expression)
    #(log \"oh my god an error occured!\" %))
"
  [& clauses]
  `(try (do ~@(butlast clauses))
        (catch js/Error e
          (~(last clauses) e))))

;; https://github.com/netguy204/MOVE/blob/master/src/move/macros.clj#L27
(defmacro doasync
  "Converts a sequence of binding forms into synchronous and
asynchronous calls.

  Bindings look mostly like the bindings in a let form (including
  destructuring capability) but the RHS of each binding takes on
  special meaning if it happens to be a vector.

  If the RHS of a binding is a vector then its treating like an
  asynchronous invocation. Async invocations look like normal
  invocations (other than the shape of their parentheses) but the
  function must accept a callback as its final argument. The LHS of
  the binding will only be established once the callback fires and it
  will take on the value that was given to the callback.

  The doasync form does not return a meaningful value because of its
  asynchronous execution behavior.

  For example:

  (doasync
    [name [get-json \"/name.json\"]
     profile-target (str name \".html\")
     [user profile] [get-json profile-target]
     _ (set-profile-data profile)
     _ (log \"profile data set for user\" user)])

  Here the get-json call is executed asynchronously. It's real
  signature looks something like:

  (defn get-json [url callback] ...)

  When get-json calls its callback with the data, that data is boiund
  to name and the next binding in doasync is allowed to execute. Since
  the RHS of this form is not a vector, it executes immediately to
  create the binding on the RHS. Next, another get-json request fires
  asynchronously with the computed url. Finally, we synchronously tell
  our view to update with the data that we got back from the call.
"
  [bindings]

  (reduce
   (fn [next [bind-var expr]]
     (if (vector? expr)
       ;; asynchronous expression
       (if (vector? bind-var)
         `(~@expr (fn [~@bind-var] ~next))
         `(~@expr (fn [~bind-var] ~next)))

       ;; synchronous expression
       `(let [~bind-var ~expr]
          ~next)))
   'identity
   (reverse (partition 2 bindings))))
