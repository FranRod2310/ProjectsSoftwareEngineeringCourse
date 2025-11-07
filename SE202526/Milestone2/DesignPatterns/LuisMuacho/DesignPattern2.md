## Chains of Responsibility Pattern

`mindustry/net/Administration.java`

### Code Snippet 1

```java
package mindustry.net;

import (...);

public class Administration{
    (...)
    public Seq<ChatFilter> chatFilters = new Seq<>();
    public Seq<ActionFilter> actionFilters = new Seq<>();
    (...)
    
    public Administration(){
        load();

        //anti-spam
        addChatFilter((player, message) -> {
            long resetTime = Config.messageRateLimit.num() * 1000L;
            if(Config.antiSpam.bool() && !player.isLocal() && !player.admin){
                //prevent people from spamming messages quickly
                if(resetTime > 0 && Time.timeSinceMillis(player.getInfo().lastMessageTime) < resetTime){
                    (...)
                    return null;
                }
                (...)
            }
            return message;
        });

        //block interaction rate limit
        addActionFilter(action -> {
            if(action.type != ActionType.breakBlock &&
                action.type != ActionType.placeBlock &&
                action.type != ActionType.commandUnits &&
                Config.antiSpam.bool() && !action.player.isLocal()){

                Ratekeeper rate = action.player.getInfo().rate;
                if(rate.allow(Config.interactRateWindow.num() * 1000L, Config.interactRateLimit.num())){
                    return true;
                }else{ (...)
                    return false;
                }
            }
            return true;
        });
    }

    (...)

    /** Adds a chat filter. This will transform the chat messages of every player.
     * This functionality can be used to implement things like swear filters and special commands.
     * Note that commands (starting with /) are not filtered.*/
    public void addChatFilter(ChatFilter filter){
        chatFilters.add(filter);
    }

    /** Filters out a chat message. */
    public @Nullable String filterMessage(Player player, String message){
        String current = message;
        for(ChatFilter f : chatFilters){
            current = f.filter(player, current);
            if(current == null) return null;
        }
        return current;
    }

    /** Add a filter to actions, preventing things such as breaking or configuring blocks. */
    public void addActionFilter(ActionFilter filter){
        actionFilters.add(filter);
    }

    /** @return whether this action is allowed by the action filters. */
    public boolean allowAction(Player player, ActionType type, Tile tile, Cons<PlayerAction> setter){
        return allowAction(player, type, action -> setter.get(action.set(player, type, tile)));
    }

    /** @return whether this action is allowed by the action filters. */
    public boolean allowAction(Player player, ActionType type, Cons<PlayerAction> setter){
        //some actions are done by the server (null player) and thus are always allowed
        if(player == null) return true;

        PlayerAction act = Pools.obtain(PlayerAction.class, PlayerAction::new);
        act.player = player;
        act.type = type;
        setter.get(act);
        for(ActionFilter filter : actionFilters){
            if(!filter.allow(act)){
                Pools.free(act);
                return false;
            }
        }
        Pools.free(act);
        return true;
    }
    
    (...)

    /** Handles chat messages from players and changes their contents. */
    public interface ChatFilter{
        /** @return the filtered message; a null string signals that the message should not be sent. */
        @Nullable String filter(Player player, String message);
    }

    /** Allows or disallows player actions. */
    public interface ActionFilter{
        /** @return whether this action should be permitted. if applicable, make sure to send this player a message specify why the action was prohibited. */
        boolean allow(PlayerAction action);
    }

    (...)
}
```

### Class Diagram

![COR - DesignPattern](DesignPattern2.png)

### Rationale

This class is a perfect example of the Chain of Responsibility pattern. The idea is to create an "chain" to process "requests" (chat messages or player actions).

 - **"Request"**: It's the String of the message in `fileterMessage`, or the object `PlayerAction` in the `allowAction`.
 - **Handler Interface**: These are the inner interfaces `ChatFilter` and `ActionFilter`. They define the contract that all concrete handlers must follow the method `filter()` or `allow()`.
 - **ConcreteHandlers**: These are implementations of those interfaces. In the snippet, they are the lambda functions that are passed to `addChatFilter` and `addActionFilter` in the constructor. Each one has specific logic to decide if it processes the request or skips it.
 - **Client** and **Chain Manage**: The class `Administration` acts as the client,
    1. Mount the chain: Use methods `addChatFilter` and `addActionFilter` to add handlers to the lists `Seq`.
    2. Init the Request: The methods `filterMessage` and `allowAction` initiate the process.
    3. BaseHandler Logic: The skip logic is implemented directly inside the methods `filterMessage` and `allowAction`, using `for` loops. If a handler returns null or false, it "treats" the request and the chain is interrupted. Otherwise, the loop continues to the next handler. 

**Benefits**:

- **Decoupling**: The sender (`Administration`) doesn't know who or how the request is handled.
- **Extensibility**: Easily add new filters without changing the `Administration` class.
- **Single Responsibility**: Each filter lambda/class does just one job.