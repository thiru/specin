(ns specin.core
  "Facilitates defining function specs inline a function definition and automatic instrumentation.
  Based on the following library:
  https://github.com/Provisdom/defn-spec"
  (:require
    #_[clojure.pprint :as pp]
    [clojure.spec.alpha :as s]
    [clojure.spec.test.alpha :as st]))

(defn- qualify-symbol
  [sym-name]
  (symbol (str *ns*) (str sym-name)))

(defmacro s<
  "Expands into the common form taken for function arguments."
  [& args]
  (list 'quote `(s/cat ~@args)))

(defmacro s>
  "Expands into the form used for a function return spec."
  [s]
  (list 'quote s))

(defmacro apply-specs
  "Apply function specs and instrument arguments."
  []
  (->> (ns-interns *ns*)
       (filter #(fn? (var-get (val %))))
       (map #(assoc {}
                    :fn-sym (key %)
                    :args (-> % val meta :args)
                    :ret (-> % val meta :ret)))
       (filter #(or (:args %) (:ret %)))
       #_(map #(do (pp/pprint %) %)) ; DEBUG
       (mapv #(do
                `(let [fdef-ret# (s/fdef ~(:fn-sym %)
                                         :args ~(:args %)
                                         :ret ~(:ret %))]
                   ~@(when s/*compile-asserts*
                       [`(st/instrument '~(qualify-symbol (:fn-sym %)))])
                   fdef-ret#)))))
