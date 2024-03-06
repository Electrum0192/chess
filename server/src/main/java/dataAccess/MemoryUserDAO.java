package dataAccess;

import model.UserData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO{
    private static Collection<UserData> users;

    private static final MemoryUserDAO instance = new MemoryUserDAO();

    private MemoryUserDAO() {
        users = new HashSet<>();
    }

    public static MemoryUserDAO getInstance(){return instance;}

    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public void createUser(String username, String password, String email) {
        UserData newUser = new UserData(username,password,email);
        users.add(newUser);
    }

    @Override
    public UserData getUser(String username) {
        for(var i : users){
            if(Objects.equals(i.username(), username)){
                return i;
            }
        }
        return null;
    }

    public Collection<UserData> getUserCollection(){return users;}
}
