# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
### Server Design

For a more complete look at how the server handles requests, check out the full sequence diagram here:
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDAEooDmSAzmFMARDQVqhFHXyFiwUgBkInIZSqjxDKaQDiwALYphykGNwTG05gBF+wAIIhDXLpgAmVgEbAuKGE9eZM7KAgAV2wYAGI0YCoAT1YObl5+JEFwgHcACyQwMURUUgBaAD5yJUoALhgAbQAFAHkyABUAXRgAeiDPKAAdNABvDspI3QAaGFwHVOgnUZRtYCQEAF9MfUoYIrjOHj4BRRFDCqh4nkoACgGoIZRR8a5JqGmYWfmEAEpMNi3E3f1RdeLLGAbHYUA4KuwUGAAKqdc6dK7vQHA+xcf7eVwVMgAURkWLgDRgFyuMAAZoFtITOpgkbYUesNp8EjtknsDCgKmggggEB9jt8Wb9DGiaSCwTAQEd+CgYWciTprmMPHcpoirLTQaiNj4KgBJABy2JYBLlI0VEymMzmCxg+oatUptEZ2ySgkFXg2IpRFQlKCl1iCYHScMG8tVQPVDjR2ptBqxRodl3lo2AAfSDQgAGt0DG7TAU4HqWrRZqGXzma6lKIKvm05n0LyvuXWX98htVlBq6n01m0CtSrBW8VcugwBUAEwABgnPV6Ne76GW6CcfgCwVCYWg0ghMDkCjSmWymGHBWK7YqNXqzTangcLJnJoVt3uTmW7bRu6QzYOMAQ8k-wcTU0nxVTAPy-d0ASLL0YAhaFYQfMNkQ1KMMXIHE8WNeF5VJckE0LcNi3pYowLdCoLlAv9wOFKCNXBSFqiVZ9TmAh5EIjTVimjbFcXxM1lQeHCIApcjPWQwcd0o0i+OfCiFDdaiCOgn0-VTACEXwpDIy1VD9UNTCQ1NOc6zQHN7RrDT2KIiS5Mrb8jJ7WTP3k8SzzzLtjL7ag1nE49RxgSdpz6eyF0wJcV0CEJwgCFBs13YJmDCDIshyZARzRVzKnMdCGixFpWhvLg70idyHLfDYSNs9kf3keLTmCtB3gq-YIJgUSxW4AA1YBkCcec0Dqkr0DYwiii49DeIfZNBpMskhLcgs2pLQomrZMisN0RyqKKRaKicFAEEhFB-UDAbAz64a6VG1CWCxABZWoOqxebax7QSKXMxaiJWqsYAaFgoSxTbnOWyTKoqX92FqhCgcqojXL+gHPJoAcil88cp1CtBl38CL1yOJwdwSGAtF0VFEoPFK8mYNt+3PDQsSvVp2HlHp6qR7zihJvQwZgyEua4U53l-CBQk5bR+cwLngZ23mwC505md0HVzAusTCjGniCUVrxZopbWLJGoopZ5-WwuN5q4dp+AIG5FBwBZAAebXCnZlHCjR-ypxnfmKl6bXldGJLshlQCFVcBBQAzEOrlGbW9XlRZFyx8K13CbAgigbADvgSVsmJ7DyeSo9UpPEovI7Ko6kaPLtdZ6aZzjhPXf+c3VvFXOUHl+rY-lePdHeVuW0KGXOu6pBeuM06XqGg3Ls41DuIwhMY+evq3ue2exKN+UpIfSWd9h7aaLFNAUFSeXG-7zetPVnTY3jbXTJg+Vr6Wwfv398x990Zyilcz-m6Dg9gFBu8plZJ2xquSKYRoj7V-KkGAAApCAn5866HCOHSOxcqbpStjUKEjNa7FTOsZGcvk4A22gD3JW5hXz9hbgfZqFQABWKC0AXzAeYUYIBKFQCmiQnsA9GFsn+CPLgXUep9SnudV+KFMTjX0qHfh08Zq4Q+sfN+wifp73fhBGWsEOE0NVjfDWS9H662fhtT6g5dHghfrohhP8ebACcE4aoEdYFQDUkmcUvChFOItoOf+L92xAJLn5AKmNIG4yinwWIiBfSwGANgLOhAXQmULoeXyuDy7niyriHKeUfCAMKLY8UB08DWBSfzQWmBhaiyCOLeUjgHFH0UrRbw+1DqGJQMrYxHFow3Xuo9SxOtcL62sdvAJbcEaAzNlooUQSrazJKd9b8IAKlQCqdgEOAshYQBFjAMWuyYaBOHho3aXTg7wXWigfp-xBl3Qek9B868RIaK+qDJhv1-pzOTvUo5jS+qODWXoi5nSDrZCkfcq6FQhnPNXsZde6j2laSKKCioKywqgstrkn5iMyru3CejCcUTMBAA
