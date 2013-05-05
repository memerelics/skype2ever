(defproject skype2ever "0.0.1-SNAPSHOT"
  :description "It enable you to save Skype chat messages into your EverNote."
  :url "https://github.com/memerelics/skype2ever"
  ;; Add maven repository
  :repositories {"sonatype" "https://oss.sonatype.org/content/groups/public/"}
  ;; Override location of the local maven repository. Relative to project root.
  :local-repo "lib"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/java.jdbc "0.3.0-alpha1"]
                 [org.xerial/sqlite-jdbc "3.7.2"]
                 [com.evernote/evernote-api "1.23"]]
  :main skype2ever.main)
