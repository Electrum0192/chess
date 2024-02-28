package dataAccess;

import model.UserData;

import java.util.Collection;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO{
    private static Collection<UserData> games;

    public MemoryGameDAO() {
        games = new HashSet<UserData>();
    }

    @Override
    public void clear() {
        games.clear();
    }

    public Collection<UserData> getGameCollection(){return games;}
}
