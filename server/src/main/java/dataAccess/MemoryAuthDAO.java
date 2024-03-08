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
        String authToken = generateAuthToken();
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
    public void deleteAuth(String authtoken) {
        for(var i : auths){
            if(i.authToken().equals(authtoken)){
                auths.remove(i);
                return;
            }
        }
    }

    /**
     * Adds an existing AuthData object to the memory database. Used in startup sync with SQL database.
     * @param username
     * @param authToken
     */
    public void addAuth(String username, String authToken){
        auths.add(new AuthData(authToken, username));
    }

    public Collection<AuthData> getAuthCollection(){return auths;}

    private String generateAuthToken(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        return generatedString;
    }
}
