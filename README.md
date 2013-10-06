Skype2Ever
===========================

Skype2Ever is a small Clojure script which enable you to save Skype chat messages into your EverNote.


How to use
===========================

## requirements

* [leiningen](https://github.com/technomancy/leiningen)
* Your EverNote developer token. (for now...)


## local repo

install JavaFX and [Datepicker](http://myjavafx.blogspot.ch/2012/01/javafx-calendar-control.html) to local repo.

    $ mvn install:install-file -DgroupId=local.oracle -DartifactId=jfxrt -Dversion=2.2.25 -Dpackaging=jar -DlocalRepositoryPath=m2repo -Dfile=$JAVA_HOME/jre/lib/jfxrt.jar
    $ mvn install:install-file -DgroupId=local.schudt -DartifactId=datepicker -Dversion=0.0.2 -Dpackaging=jar -DlocalRepositoryPath=m2repo -Dfile=/path/to/download/datepicker-0.0.2.jar


## How to use

```````````
$ git clone https://github.com/memerelics/skype2ever.git
$ cd skype2ever
$ lein deps
$ cat > skype2ever.properties
skypeUser = yourSkypeUserNameHere
evernoteToken = S=xx:U=xxx:E=xxxxxxxxxxx:C=xxxxxxxxxxx:P=xxx:A=xxxxxxxxxxx:V=x:H=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
evernoteHost = www.evernote.com

$ lein run 2013/05/05
```````````


TODO
===========================

* Use OAuth instead of developer token.
* sometimes friendlyname doesn't contain name of Group Chat
* tweak chat group-by unit
* find chats dynamically when user input skype name

