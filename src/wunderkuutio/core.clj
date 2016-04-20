(ns wunderkuutio.core)

(defonce words (atom '()))

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

(defn get-char-at-coord [z x y]
  (get (get (nth (cube) z) y) x))

(def first-chars get-distinct-chars-from-cube)

(defn word-first-char-in-cube? [word]
  (not (= -1 (.indexOf (first-chars) (get (clojure.string/upper-case word) 0)))))

(defn get-words-with-existing-first-char [words-from-file]
  (filter (fn [x] (word-first-char-in-cube? x)) words-from-file))

(defn initiate []
  (reset! words '())
  (swap! words concat (get-words-with-existing-first-char(create-word-list))))

(defn -main[& args]
  (initiate))
