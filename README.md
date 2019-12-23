# Simple Social

Final project of the Computer Networks course held at University of Pisa (June 6, 2016). The application features the usage of network-related Java classes and constructs (i.e. `Remote Method Invocation`, `Socket`, `InetAddress`, `I/O Streams`, `Serializable`, `Runnable`) to build a simple social network where users can interact with each other.

In particular, users can:

- Log in;
- Add friends;
- Search for users;
- Publish textual content;
- Follow users;
- Read contents published by followed users.

# Getting started

The application is ready to use and can be started by executing:

```
$java -jar SocialServer.jar
```

This command starts the social network's server, which can now accept registrations from new users. For each of them, open a new terminal and run:

```
$java -jar SocialClient.jar
```

Users can register, login and interact with each other. The lifecycle of the social network is bound to the one of the Social Server, and clients can log in and out whenever they want (without losing their data). When closing the application, information like network status, unread contents and friendship requests are persisted to text files inside the folder containing the two jars. 

# How to use

Instructions are prompted to the user by the `SocialClient` module and are written in Italian. The `SocialServer` displays a notification every time a new content is published. In addition, it shows the number of on-line users every ten seconds.

A written report is available in Italian in the [SimpleSocial.pdf](SimpleSocial.pdf) file, while the source code can be found under the [src/it/unipi/rcl/cariaggil](src/it/unipi/rcl/cariaggil).
