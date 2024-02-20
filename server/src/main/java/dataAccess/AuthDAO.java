package dataAccess;

import model.AuthData;

public interface AuthDAO {
    /**
     * Delete all authentication data from database. Used in testing.
     */
    public default void clear(){

    }

    /**
     * Create a new Authentication for a specific user
     * @param username username to associate with the login
     * @return the data object for the authentication
     */
    public default AuthData createAuth(String username){
        return null;
    }

    /**
     * Get the data model for a specified authToken. Used to check if user is valid.
     * @param authToken
     * @return AuthData model for that authToken
     */
    public default AuthData getAuth(String authToken){
        return null;
    }

    /**
     * Remove an AuthData model from the database
     * @param authData
     */
    public default void deleteAuth(AuthData authData){

    }

}
