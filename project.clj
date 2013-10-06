(defproject skype2ever "0.0.1-SNAPSHOT"
  :description "It enable you to save Skype chat messages into your EverNote."
  :url "https://github.com/memerelics/skype2ever"
  ;; Add maven repository
  :repositories {"sonatype" "https://oss.sonatype.org/content/groups/public/",
                  "local" {:url ~(str (.toURI (java.io.File. "m2repo")))}}
  ;; Override location of the local maven repository. Relative to project root.
  :local-repo "m2repo"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [matchure "0.10.1"]
                 [org.clojure/java.jdbc "0.3.0-alpha1"]
                 [org.xerial/sqlite-jdbc "3.7.2"]
                 [com.evernote/evernote-api "1.23"]
                 [local.oracle/jfxrt "2.2.25"]
                 [local.schudt/datepicker "0.0.2"]]
  :java-source-paths ["src/java"]
  :aot [skype2ever.gui]
  :main skype2ever.gui)
