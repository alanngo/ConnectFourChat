# ConnectFourChat

#Prerequisites

Java version: openjdk 11.0.3 2019-04-16


In order to run the Java chat program:

1) open two terminals and go to Connect4Chat/src
2) run make clean && make all on terminal 1
3) run make server on terminal 1
4) run make client on terminal 2

5) IMPORTANT: after one side send a message, this side will have to wait for the response
    of the other side. After this side gets the response, it can send the next message.
    
5) In order to end the chat, type "end" on either the client or server side
6) Type "end" on server side, both sides will be terminated. But type "end"
    client side, client will be terminated and server can accept next client.
    
    
In order to play Connect Four game:

7) Type "start game" either side if client or server wants to play connect four game.
8) Type "c" immediately follow by a number from 1 to 7 to drop piece on the board.
    For example, client wants to drop piece on column 3, then client type "c3"
    
9) IMPORTANT: When you enter "c#" but currently is not your turn, this will be treated as 
    a normal text message!!!
    
10) While playing the game, client and server can still chat as long as they do not enter
    the format "c#" where # is a number.
11) When the game finishes, client and server can still chat. If they want to play again,
    either side can enter "start game" to start a new game.
12) When they are playing the game and the game has not finished yet, if either side enters
    "start game", this will NOT restart the game. It will be treated as a normal message.


#Authors
Code by:Alan Ngo, Yunhao Yang, and Lucinda Nguyen
