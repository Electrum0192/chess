package dataAccess;

import model.UserData;

import java.util.Collection;

public interface UserDAO {

    /**
     * Delete all user data from database. Used in testing.
     */
    public default void clear(){}

    /**
     * Create new user model with the given parameters
     * @param username
     * @param password
     * @param email
     */
    public default void createUser(String username, String password, String email){

    }

    /**
     * Get user data model for the given username
     * @param username
     * @return the UserData model for that user profile
     */
    public default UserData getUser(String username){
        return null;
    }

}
