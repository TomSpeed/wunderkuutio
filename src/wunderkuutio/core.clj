(ns wunderkuutio.core)

(defonce words (atom '()))
(defonce found-words (atom '()))
(defonce illegal-paths (atom '()))

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
  (distinct (clojure.string/replace (read-cube-file) #"\n" "")))

(def cube (create-cube))

(defn get-max-dimensions []
  (let [z (count cube)
        x (count (get (nth cube 0) 0))
        y (count (nth cube 0))]
    {:z z :x x :y y}))

(defn get-all-coords [max-dimensions]
  (for [z (range (:z max-dimensions))
       y (range (:y max-dimensions))
       x (range (:x max-dimensions))]
      (vector z x y)))

(defn get-char-at-coord [coord]
  (let [z (get coord 0)
        x (get coord 1)
        y (get coord 2)]
    (get (get (nth cube z) y) x)))

(def cube-chars (get-distinct-chars-from-cube))

(defn char-in-cube? [c]
  (not (= -1 (.indexOf cube-chars (get (clojure.string/upper-case c) 0)))))

(defn word-chars-in-cube? [word]
  (every? #(char-in-cube? %) word))

(defn get-word-with-existing-chars [words-from-file]
  (filter #(word-chars-in-cube? %) words-from-file))

(defn vector-contains-values-out-of-bounds? [v]
 (let [max-dimensions (get-max-dimensions)
       max-dim-vector (vector (:z max-dimensions) (:x max-dimensions) (:y max-dimensions))
       ofb-results (map #(not(< % 0)) (map - v max-dim-vector))]
  (some true? ofb-results)))

(defn vector-contains-negative-values? [v]
  (let [neg-results (map neg? v)]
    (some true? neg-results)))

(defn get-adjacent-coords[coord]
  (let [coords (for [z (range -1 2)
        x (range -1 2)
        y (range -1 2)]
      (into [] (map + coord (vector z x y))))]
    (remove #(or (= coord %)
                 (vector-contains-negative-values? %)
                 (vector-contains-values-out-of-bounds? %)) coords)))

(defn get-adjacent-letters[coord]
  (let [adj-coords (get-adjacent-coords coord)]
  (map get-char-at-coord adj-coords)))

(defn get-words-starting-with-str [s]
  (filter #(= 0 (.indexOf (clojure.string/upper-case %) (clojure.string/upper-case s))) @words))

(defn str-exists-in-words? [s]
  (> (count (get-words-starting-with-str s)) 0))

(defn get-words-in-cube []
  (let [all-coords (get-all-coords (get-max-dimensions))]
    ;(println (map (fn[x] (map (fn[y](str (get-char-at-coord x) y))(get-adjacent-letters x))) all-coords))
    ))

(defn initiate []
  (reset! words '())
  (swap! words concat (get-word-with-existing-chars(create-word-list)))
  (get-words-in-cube))

(defn -main[& args]
  (initiate))
