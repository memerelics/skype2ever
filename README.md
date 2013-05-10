Skype2Ever
===========================

Skype2Ever is a small Clojure script which enable you to save Skype chat messages into your EverNote.


How to use
===========================

## requirements

* [leiningen](https://github.com/technomancy/leiningen)
* Your EverNote developer token. (for now...)

## How to use

```````````
$ git clone https://github.com/memerelics/skype2ever.git
$ cd skype2ever
$ lein deps
$ cat > skype2ever.properties
skypeUser = yourSkypeUserNameHere
evernoteToken = S=xx:U=xxx:E=xxxxxxxxxxx:C=xxxxxxxxxxx:P=xxx:A=xxxxxxxxxxx:V=x:H=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
evernoteHost = www.evernote.com

$ lein run 20130505
```````````


TODO
===========================

* Use OAuth instead of developer token.
* Automate daily store action (in what way?).

