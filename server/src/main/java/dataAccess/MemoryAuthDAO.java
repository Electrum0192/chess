package dataAccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

public class MemoryAuthDAO implements AuthDAO{
    private static Collection<AuthData> auths;

    private static final MemoryAuthDAO instance = new MemoryAuthDAO();

    private MemoryAuthDAO() {
        auths = new HashSet<AuthData>();
    }

    public static MemoryAuthDAO getInstance(){return instance;}

    @Override
    public void clear() {
        auths.clear();
    }

    @Override
    public AuthData createAuth(String username) {
        Random rand = new Random();
        String authToken = rand.nextInt(1000)+"";
        AuthData newAuth = new AuthData(authToken,username);
        auths.add(newAuth);
        return newAuth;
    }

    @Override
    public AuthData getAuth(String authToken) {
        for(var i : auths){
            if(Objects.equals(i.authToken(), authToken)){
                return i;
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData) {
        auths.remove(authData);
    }

    public Collection<AuthData> getAuthCollection(){return auths;}
}
