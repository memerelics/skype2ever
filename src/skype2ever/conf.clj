(ns skype2ever.conf
  (:use     [clojure.java.io :only (reader)])
  (:import (java.util Properties)))

(def conf
  (let [conf (Properties.)]
    (.load conf (reader "skype2ever.properties"))
    conf))

