Updated client-server program now with implemented AES encryption

You must run the server file first before running the client file.
Only allows one client to connect to server at a time, and server file ends once client disconnects from server

Client only connects to server when username is in subscriptions.txt with vaild secretKey.

How to run on command line:
javac Server.java Client.java
java Server
java Client

***NOTE***
Although you may compile javac Server.java Client.java on the same command prompt window, you must run java Server and java Client on different command prompt windows for entire client-server program may work correctly.
