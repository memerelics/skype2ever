(ns skype2ever.skypedb
  (:use [skype2ever.conf]
        [matchure :only (cond-match)])
  (:require [clojure.java.jdbc :as j])
  (:import [java.text SimpleDateFormat]))


(defn- skype-db [os home user]
  {:classname "org.sqlite.JDBC" :subprotocol "sqlite"
   :subname (cond-match os
              #"Linux"   (str home "/.Skype/" user "/main.db")
              #"Mac"     (str home "/Library/Application Support/Skype/" user "/main.db")
              #"Windows" (str home "\\AppData\\Roaming\\Skype\\" user "\\main.db")
              ? nil)})

(defn- select-from [table query]
  (j/query (skype-db (System/getProperty "os.name")
                     (System/getProperty "user.home")
                     (get conf "skypeUser"))
           [(str "SELECT * FROM " table " " query ";")]))

;; (select-from "messages" "limit 1")

;; TODO: sometimes friendlyname doesn't contain name of Group Chat
;; (map #(friendly-chatname (:chatname %)) (find-messages "2013/09/27"))
(defn friendly-chatname [^String raw-chatname]
  (:friendlyname
    (first (select-from "chats"
                        (str "WHERE name='" raw-chatname "'")))))


(defn- date-to-timestamp [^String date]
  (/ (.. (SimpleDateFormat. "yyyy/MM/dd") (parse date) getTime) 1000))

(defn- find-messages-between [^String from ^String to]
  (select-from "messages"
               (str "WHERE timestamp BETWEEN "
                    (date-to-timestamp from)
                    " AND "
                    (- (+ (date-to-timestamp to) (* 24 60 60)) 1)
                    " ORDER BY timestamp ASC")))

(defn find-messages
  ([^String date] (find-messages date date))
  ([^String from ^String to] (find-messages-between from to)))

;; (= (count (find-messages "2013/09/26" "2013/09/28"))
;;    (+ (count (find-messages "2013/09/26"))
;;       (count (find-messages "2013/09/27"))
;;       (count (find-messages "2013/09/28"))))
;;
;; (= (count (find-messages "2013/09/26" "2013/09/26"))
;;    (count (find-messages "2013/09/26")))

(defn find-chats [] (select-from "chats" ""))
