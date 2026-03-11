(ns news.paper
(:use protege.core)
(:import clojuretab.ClojureTab))

(defn start-script []
  (println "1. Loading Clojure Programs..")
  (if-let [wps (fainst (cls-instances "WorkingPrograms") nil)]
    (do 
      (loop [i 1 pins (svs wps "cloPrograms")]
        (when (seq pins)
          (println (str " 1." i " " (sv (first pins) "title") " = " (ClojureTab/loadProgram (first pins))))
          (recur (inc i) (rest pins))))
      (println "2. .."))
      ;;(ClojureTab/invoke "test.core" "twocents"))
    (println "  Annotated instance of WorkingPrograms not found!")))

(defn persons-number [hm inst]
  (println (javax.swing.JOptionPane/showMessageDialog nil 
              (str "Persons: " (count (cls-instances "Person"))))))

