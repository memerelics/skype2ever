(ns skype2ever.main
  (:use     [clojure.java.io :only (reader)])
  (:require [clojure.java.jdbc :as j]
            [clojure.java.jdbc.sql :as s])
  (:import [java.util Properties]
           [com.evernote.thrift.protocol TBinaryProtocol]
           [com.evernote.thrift.transport THttpClient TTransportException]
           [com.evernote.edam.userstore UserStore$Client]
           [com.evernote.edam.notestore NoteStore$Client NoteFilter NoteList]
           [com.evernote.edam.type Note Notebook]
           [com.evernote.edam.error EDAMUserException]))

(def conf
  (let [conf (Properties.)]
    (.load conf (reader "skype2ever.properties"))
    conf))

(def skypeDBPath
  (let [os (. System getProperty "os.name")]
    (cond (>= (.indexOf os "Linux")   0) (System/exit 1) ;; TODO
          (>= (.indexOf os "Mac")     0) (str (. System getProperty "user.home") "/Library/Application Support/Skype/" (get conf "skypeUser") "/main.db")
          (>= (.indexOf os "Windows") 0) (System/exit 1) ;; TODO
          :else (System/exit 1))))

(def authToken (get conf "evernoteToken"))

(def userStore
  (let [userStoreUrl (str "https://" (get conf "evernoteHost") "/edam/user"),
        userStoreProt (TBinaryProtocol. (THttpClient. userStoreUrl))]
    (UserStore$Client. userStoreProt userStoreProt)))

(def noteStore
  (let [noteStoreProt (TBinaryProtocol.
                        (THttpClient.
                          (. userStore getNoteStoreUrl authToken)))]
    (NoteStore$Client. noteStoreProt noteStoreProt)))


(defn msgFormatter [message]
  (str "<tr>"
       "<td>" (. (java.text.SimpleDateFormat. "HH:mm:ss") format (java.util.Date. (* 1000 (:timestamp message)))) "</td>"
       "<td>" (:from_dispname message) "</td>"
       "<td>" (:body_xml message) "</td>"
       "</tr>"))

(defn saveMessages [title messages]
  (let [note (Note.),
        tableStyle (str "color:#535353;"
                        "background-color:#ECECEC;"
                        "border:1px solid rgba(0, 0, 0, 0.2);"
                        "width:100%;"
                        "font-size: 11px;"
                        "margin: 0 0 0 3px;"),
        thStyle    (str "padding:3px;"
                        "text-align:center;"
                        "border:1px solid rgba(0, 0, 0, 0.2);"
                        "color:#ECECEC;"
                        "background-color:#636363;")]

    (. note setTitle title)
    (. note addToTagNames "skype")
    (. note setContent
       (str "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">"
            "<en-note>"
            "<table style='" tableStyle "'>"
            "<tr>"
            "<th style='" thStyle "'>time</th>"
            "<th style='" thStyle "'>from</th>"
            "<th style='" thStyle "'>text</th>"
            "</tr>"
            (clojure.string/join (map msgFormatter messages))
            "</table>"
            "</en-note>"))

    (. noteStore createNote authToken note)))


(defn -main [& args]
  (if (nil? (first args)) (System/exit 1))

  (let [db {:classname "org.sqlite.JDBC" :subprotocol "sqlite"
            :subname skypeDBPath}
        targetDate (. (java.text.SimpleDateFormat. "yyyyMMdd") parse (first args)),
        targetTimestamp (/ (. targetDate getTime) 1000), ;; milliseconds to seconds
        messages (j/query db [(str "SELECT *"
                                   " FROM messages"
                                   " WHERE timestamp BETWEEN "
                                   targetTimestamp
                                   " AND "
                                   (+ targetTimestamp (* 24 60 60))
                                   " ORDER BY timestamp ASC")])]

    (defn friendlyChatname [chatname]
      (:friendlyname (first (j/query db [(str "SELECT * FROM chats WHERE name='" chatname "'")]))))

    (map (fn [groupedMsg]
           (saveMessages
             (str "Skype["
                  (. (java.text.SimpleDateFormat. "yyyy/MM/dd") format targetDate)
                  "] "
                  (friendlyChatname (first groupedMsg)))
             (last groupedMsg)))
         (group-by :chatname messages))))
