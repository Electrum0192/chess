package dataAccess;

import model.UserData;

import java.util.Collection;
import java.util.HashSet;

public class MemoryUserDAO implements UserDAO{
    private static Collection<UserData> users;

    public MemoryUserDAO() {
        users = new HashSet<UserData>();
    }

    @Override
    public void clear() {
        users.clear();
    }

    public Collection<UserData> getUserCollection(){return users;}
}
