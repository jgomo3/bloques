(ns bloque.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def initial-state
  {:color 0
   :bloques [[0 0] [1 0] [2 0] [1 1]]
   :origin [0 0]})

(defn update-state [state] state)

(def dir-to-vec {:right [1  0]
                 :up    [0 -1]
                 :left  [-1  0]
                 :down  [0  1]})

(defn sum-vec [& vecs]
  (apply map + vecs))

(defn mult-vec-by-scalar [scalar vec]
  (map (fn [val] (* scalar val)) vec))

(defn mov-vec [vec dir]
  (sum-vec vec (dir dir-to-vec)))

(defn on-key-pressed [old-state {key :key}]
  (update-in old-state [:origin] mov-vec key))

(def gui-bloque-size 50)

(defn gui-draw-bloque [x y]
  (let [x-pix (* gui-bloque-size x)
        y-pix (* gui-bloque-size y)]
    (q/rect x-pix y-pix gui-bloque-size gui-bloque-size)))

(defn gui-draw-state [state]
  (q/background 240)
  (q/fill (:color state) 255 255)
  (q/with-translation (mult-vec-by-scalar gui-bloque-size (:origin state))
    (doseq [bloque (:bloques state)]
      (apply gui-draw-bloque bloque))))

(defn gui-setup []
  (q/frame-rate 30)
  (q/color-mode :hsb)
  initial-state)

(q/defsketch bloque
  :host "host"
  :size [600 900]
  :setup gui-setup
  :update update-state
  :draw gui-draw-state
  :middleware [m/fun-mode]
  :key-pressed on-key-pressed)

(comment)