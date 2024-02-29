package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;

public class UserService {
    /**
     * Delete all user data from database. Used in testing.
     */
    public void clearUsers(){
        MemoryUserDAO access = MemoryUserDAO.getInstance();
        access.clear();
    }
    /**
     * Register a new user and log them in
     * @param user the UserData of the new user
     * @return an AuthData object for the new user after login
     */
    public AuthData register(UserData user) throws Exception {
        MemoryUserDAO access = MemoryUserDAO.getInstance();
        //Check if user already exists
        if(access.getUser(user.username()) != null){
            throw new Exception("Error: already taken");
        }
        //Check if UserData contains a password
        if(user.password() == null){
            throw new Exception("Error: bad request");
        }
        //Create User
        access.createUser(user.username(), user.password(), user.email());
        //Get new AuthData for user
        MemoryAuthDAO authAccess = MemoryAuthDAO.getInstance();
        return authAccess.createAuth(user.username());
    }

    /**
     * Login a user
     * @param user the Userdata of the user
     * @return an AuthData object for the user after login
     */
    public AuthData login(UserData user) throws Exception {
        MemoryUserDAO access = MemoryUserDAO.getInstance();
        //Check if user exists and password is correct
        if(access.getUser(user.username()) == null){
            throw new Exception("Error: unauthorized");
        }
        if(!access.getUser(user.username()).password().equals(user.password())){
            throw new Exception("Error: unauthorized");
        }
        //Get new AuthData for user
        MemoryAuthDAO authAccess = MemoryAuthDAO.getInstance();
        return authAccess.createAuth(user.username());
    }

    /**
     * Logout the user
     * @param auth the AuthData of the User
     */
    public void logout(String auth) throws Exception {
        MemoryAuthDAO authAccess = MemoryAuthDAO.getInstance();
        //Is auth in database? AKA is user authorized
        if(authAccess.getAuth(auth) == null){
            throw new Exception("Error: unauthorized");
        }
        //Logout the user
        authAccess.deleteAuth(auth);
    }
}
