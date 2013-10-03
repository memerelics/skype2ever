(ns skype2ever.main
  (:use     [skype2ever.conf]
            [skype2ever.skypedb])
  (:require [clojure.java.jdbc :as j]
            [clojure.java.jdbc.sql :as s])
  (:import [java.util Properties]
           [com.evernote.thrift.protocol TBinaryProtocol]
           [com.evernote.thrift.transport THttpClient TTransportException]
           [com.evernote.edam.userstore UserStore$Client]
           [com.evernote.edam.notestore NoteStore$Client NoteFilter NoteList]
           [com.evernote.edam.type Note Notebook]
           [com.evernote.edam.error EDAMUserException]))

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


(def tableStyle (str "color:#535353;"
                     "background-color:#ECECEC;"
                     "border:1px solid rgba(0, 0, 0, 0.2);"
                     "width:100%;"
                     "font-size: 11px;"
                     "margin: 0 0 0 3px;"))

(def thStyle    (str "padding:3px;"
                     "text-align:center;"
                     "border:1px solid rgba(0, 0, 0, 0.2);"
                     "color:#ECECEC;"
                     "background-color:#636363;"))

(defn- extract-time [message]
  (.. (java.text.SimpleDateFormat. "HH:mm:ss")
      (format (java.util.Date. (* 1000 (:timestamp message))))))

(defn msgFormatter [message]
  (str "<tr><td>" (extract-time message) "</td>"
           "<td>" (:from_dispname message) "</td>"
           "<td>" (:body_xml message) "</td></tr>"))

;; (msgFormatter (first messages))

(defn saveMessages [title messages]
  (println title)
  (let [note (doto (Note.)
               (.setTitle title)
               (.addToTagNames "skype"))]

    (.. note (setContent
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
                    "</en-note>")))

    (.. noteStore (createNote authToken note))))

;; (def messages (find-messages "2013/09/26"))
;; (def groupedMsg (group-by :chatname messages))
;; (saveMessages "test-title" (val (second groupedMsg)))

(defn gen-note-title [date groupedMsg]
  (str "Skype[" date "] "
       (friendly-chatname (key groupedMsg))))

(defn -main [& args]
  (let [date (first args)]
    (doseq [groupedMsg (group-by :chatname (find-messages date))]
      (saveMessages (gen-note-title date groupedMsg) (val groupedMsg)))))

