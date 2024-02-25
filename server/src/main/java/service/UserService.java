package service;

import model.AuthData;
import model.UserData;

public class UserService {
    /**
     * Delete all user data from database. Used in testing.
     */
    public void clearUsers(){}
    /**
     * Register a new user and log them in
     * @param user the UserData of the new user
     * @return an AuthData object for the new user after login
     */
    public AuthData register(UserData user) {
        return null;
    }

    /**
     * Login a user
     * @param user the Userdata of the user
     * @return an AuthData object for the user after login
     */
    public AuthData login(UserData user) {
        return null;
    }

    /**
     * Logout the user
     * @param user the UserData of the user
     */
    public void logout(UserData user) {}
}
