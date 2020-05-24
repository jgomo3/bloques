(ns bloque.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def bloque-size 50)

(defn draw-bloque [x y]
  (let [x-pix (* bloque-size x)
        y-pix (* bloque-size y)]
    (q/rect x-pix y-pix bloque-size bloque-size)))

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :hsb)
  {:color 0
   :bloques [[0 0] [1 0] [2 0] [1 1]]
   :origin [0 0]})

(defn update-state [state] state)

(defn draw-state [state]
  (q/background 240)
  (q/fill (:color state) 255 255)
  (q/with-translation (:origin state)
    (doseq [bloque (:bloques state)]
      (apply draw-bloque bloque))))

(def dir-to-vec {:right [  1  0 ]
                 :up    [  0 -1 ]
                 :left  [ -1  0 ]
                 :down  [  0  1 ]})

(defn sum-vec [& vecs]
  (apply map + vecs))

(defn mult-vec-by-scalar [scalar vec]
  (map (fn [val] (* scalar val)) vec))

(defn mov-vec [vec dir]
  (sum-vec vec (mult-vec-by-scalar bloque-size (dir dir-to-vec))))

(defn on-key-pressed [old-state {key :key}]
  (update-in old-state [:origin] mov-vec key))

(q/defsketch bloque
  :host "host"
  :size [600 900]
  :setup setup
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode]
  :key-pressed on-key-pressed)