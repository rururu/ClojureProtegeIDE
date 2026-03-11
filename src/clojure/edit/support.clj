(ns edit.support
(:use protege.core)
(:import
  clojuretab.ProgramGenerator))

(defn edit-functions [hm inst]
  (let [mp (into {} hm)
      funcs (selection mp "cloFunctions")]
  (doseq [funci funcs]
    (ProgramGenerator/editFunction funci))))

(defn close-all-editors [hm inst]
  (ProgramGenerator/closeAllEditors))

