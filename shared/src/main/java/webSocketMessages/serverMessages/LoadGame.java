package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGame extends ServerMessage{
    private ChessGame game;
    public LoadGame(ChessGame game){
        this.game = game;
        this.serverMessageType = ServerMessageType.LOAD_GAME;
    }

    public ChessGame load(){
        return game;
    }
}
