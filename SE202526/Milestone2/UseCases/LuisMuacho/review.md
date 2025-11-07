# Review Use Cases

## **Reviewer**: Filipe Nobre 67850

### Host Game:
This use case is clear about starting or loading a game as the host and opening it to other players. It correctly identifies the Host Player as the main actor, but the description could be written more fluently (“loads a saved game as the host”). Adding what happens when the host closes or saves the game would make it more complete.

### Join Game:
Good explanation of how a client finds or joins a server. It connects well to related cases like chat and building. However, it could include exceptions (e.g., wrong IP, server full, connection failed) to better reflect real gameplay scenarios.

### Use In-Game Chat:
Simple and clear use case that describes communication between players. It effectively shows interaction with the Chat System. To improve, it could mention limits (e.g., message length or chat filtering).

### Co-operatively Build:
A strong use case showing teamwork and shared resources. It correctly links to the World and Block Systems, which reflects how the game world updates for all players. It could also note synchronization or conflicts (two players editing the same area).

### Manage Players:
Well-designed for administrative actions by the host. It shows proper authority control (kick, ban, team change). To improve, it could mention what feedback the client gets when an action affects them.


### Diagram Review
The diagram is visually clear and correctly uses UML notation. The main system (“Multiplayer”) is represented as a subsystem boundary, and all use cases are contained inside it. The actors (Host, Client, Player, Server System, Chat System, World System, Block System) are properly placed outside, with associations pointing to the relevant use cases.