(ns test.core
(:use protege.core)
(:import
  clojuretab.ClojureTab
  javax.swing.JFrame
  javax.swing.JLabel
  javax.swing.JTextField
  javax.swing.JButton
  java.awt.event.ActionListener
  java.awt.GridLayout
  java.awt.BorderLayout))

(def PERM1 (ref 0))

(def OPP (ref +))

(defn start-script []
  (println "1. Loading Clojure Programs..")
  (if-let [wps (fainst (cls-instances "WorkingPrograms") nil)]
    (do 
      (loop [i 1 pins (svs wps "cloPrograms")]
        (when (seq pins)
          (println (str " 1." i " " (sv (first pins) "title") " = " (ClojureTab/loadProgram (first pins))))
          (recur (inc i) (rest pins))))
      (println "2. Calculator..")
      (ClojureTab/invoke "test.core" "twocents"))
    (println "  Annotated instance of WorkingPrograms not found!")))

(defn action-listener [obj func]
  (.addActionListener obj
    (proxy [ActionListener] []
      (actionPerformed [evt] (func evt)))))

(defn add-button [obj symb func]
  (.add obj 
    (doto (JButton. symb) (action-listener func))))

(defn rset [r val]
  (dosync (ref-set r val)))

(defn  twocents []
  ;; Simple calaulator
  (let [temp-text (JTextField.)
        get-text #(.getText temp-text)
        get-number #(if (empty? (get-text))
                      0
                      (Double/parseDouble (get-text)))
        clear-text #(.setText temp-text "")
        set-text #(.setText temp-text %1)
        add-opp-button #(add-button %1 %2
                                    (fn [_]
                                      (rset PERM1 (get-number))
                                      (clear-text)
                                      (rset OPP %3)))
        add-number-button #(add-button %1 (str %2)
                                       (fn [_]
                                         (set-text (str (get-text) %2))))]
    (doto (JFrame. "2Cents Calculator")
      (.add temp-text)
      (#(doseq [i (range 1 10)] (add-number-button %1 i)))
      (add-number-button 0)
      (add-opp-button "+" +)
      (add-opp-button "-" -)
      (add-opp-button "*" *)
      (add-opp-button "/" /)
      (add-button "=" (fn [_]
                        (set-text (str (@OPP @PERM1 (get-number))))))
      (add-button "C" (fn [_]
                        (clear-text)
                        (rset PERM1 0)))
      (.setLayout (new GridLayout 5 2 3 3))
      (.setSize 250 300)
      (.setLocationRelativeTo (ClojureTab/main_panel))
      (.setVisible true))))

