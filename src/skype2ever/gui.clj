(ns skype2ever.gui ;;{{{
  (:use     [skype2ever.conf])
  (:require [clojure.java.io :as io]
            [skype2ever.skypedb :as skype])
  (:import (java.text SimpleDateFormat)
           (javafx.util Callback)
           (javafx.application Application)
           (javafx.beans.value ObservableValue)
           (javafx.beans.property SimpleBooleanProperty)
           (javafx.beans.property SimpleStringProperty)
           (javafx.collections ObservableList)
           (javafx.collections FXCollections)
           (javafx.event ActionEvent EventHandler)
           (javafx.fxml FXMLLoader)
           (javafx.scene.control.cell CheckBoxListCell)
           (javafx.scene.control ListCell)
           (javafx.scene Scene)
           (javafx.scene.control ListView)
           (javafx.scene.control CheckBox)
           (javafx.scene.control TableView)
           (javafx.scene.control TableCell)
           (javafx.scene.control TableColumn)
           (javafx.scene.control MenuBar)
           (javafx.scene.control Menu)
           (javafx.scene.layout AnchorPane)
           (javafx.scene.layout BorderPane)
           (javafx.stage Stage)
           (skype2ever Chat)
           (eu.schudt.javafx.controls.calendar DatePicker))
  (:gen-class
    :name "Skype2Ever"
    :main true
    :prefix "jfx-"
    :extends javafx.application.Application)) ;;}}}

;; recursive search feature would be nice
(defn find-node-by-id [^String id parent]
  (first (filter #(= id (.getId %)) (.getChildren parent))))

(defn jfx-start [this ^Stage stage]
  (let [pane (-> "../resources/layout.fxml" io/resource FXMLLoader/load)
        listView (find-node-by-id "chatsList" pane)]

    (doto listView
      ;; TODO: find chats dynamically when user input skype name
      (.setItems (FXCollections/observableArrayList (map #(Chat. (:name %)) (skype/find-chats))))
      (.setEditable true) ;; maybe useless
      (.setCellFactory (CheckBoxListCell/forListView
                         ;; return SimpleBooleanProperty (selection status of CheckBox) each time the row displayed on screen.
                         ;; By the way which code determined return type is SimpleBooleanProperty ?
                         (proxy [Callback] []
                           (call [chat] ;; items are observable list of Chat instances.
                             ; (println chat)
                             (.. chat getChecked)))))
      )

    (doto stage
      (.setTitle "Skype2Ever")
      (.setScene (Scene. pane))
      .show)))

(defn -main [& args]
  (Application/launch (Class/forName "Skype2Ever") (into-array String [])))

