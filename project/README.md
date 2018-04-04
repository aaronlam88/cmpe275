You will need to install protobuf and gradle to compile this project
You can import this project as gradle project to IntelliJ or Eclipse

To install dependencies and compile Java code, run
./gradlew installDist

To run server
./build/install/project/bin/project-server

To run client
./build/install/project/bin/project-client

## proto/
comm.proto is used for team2team communication

election.proto is used for leader election (node2node)

internal.proto is used for node2node communication

## resources/
db_config.json where to get config to access database

server_config. where to get config for server (node)
