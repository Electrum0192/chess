package dataAccess;

import model.UserData;

import java.util.Collection;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO{
    private static Collection<UserData> games;

    private static final MemoryGameDAO instance = new MemoryGameDAO();

    private MemoryGameDAO() {
        games = new HashSet<UserData>();
    }
    public static MemoryGameDAO getInstance(){return instance;}

    @Override
    public void clear() {
        games.clear();
    }

    public Collection<UserData> getGameCollection(){return games;}
}
