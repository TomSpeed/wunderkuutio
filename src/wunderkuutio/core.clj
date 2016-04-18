(ns wunderkuutio.core)

(defn read-cube-file []
  (slurp (clojure.java.io/resource "cube.txt")))

(defn read-words-file []
  (slurp (clojure.java.io/resource "words.txt")))

(defn create-word-list []
  (clojure.string/split (read-words-file) #"\n"))

(defn split-cube-layers []
  (clojure.string/split (read-cube-file) #"\n\n"))

(defn split-string-at-line-change [s]
  (clojure.string/split s #"\n"))

(defn create-cube []
  (map split-string-at-line-change (split-cube-layers)))

(defn get-distinct-chars-from-cube []
  (seq (set (clojure.string/replace (read-cube-file) #"\n" ""))))

(def cube create-cube)

(def first-chars get-distinct-chars-from-cube)

(def words create-word-list)

(defn word-first-char-in-cube? [word]
  (not (= -1 (.indexOf (first-chars) (get (clojure.string/upper-case word) 0)))))

(defn remove-nonexistant-words []
  ())

(defn get-char-at-coord [z x y]
  (get (get (nth (cube) z) y) x))



