package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserService {
    /**
     * Delete all user data from database. Used in testing.
     */
    public void clearUsers(){
        MemoryUserDAO.getInstance().clear();
        new SQLUserDAO().clear();
        MemoryAuthDAO.getInstance().clear();
        new SQLAuthDAO().clear();
    }
    /**
     * Register a new user and log them in
     * @param user the UserData of the new user
     * @return an AuthData object for the new user after login
     */
    public AuthData register(UserData user) throws Exception {
        MemoryUserDAO access = MemoryUserDAO.getInstance();
        SQLUserDAO dataAccess = new SQLUserDAO();
        //Check if user already exists
        if(dataAccess.getUser(user.username()) != null){
            throw new Exception("Error: already taken");
        }
        //Check if UserData contains a password
        if(user.password() == null || user.password().equals("null")){
            throw new Exception("Error: bad request");
        }
        //Encrypt Password
        BCryptPasswordEncoder coder = new BCryptPasswordEncoder();
        String cryptedPassword = coder.encode(user.password());
        //Create User
        access.createUser(user.username(), user.password(), user.email());
        new SQLUserDAO().createUser(user.username(), cryptedPassword, user.email());
        //Get new AuthData for user
        return new SQLAuthDAO().createAuth(user.username());
    }

    /**
     * Login a user
     * @param user the Userdata of the user
     * @return an AuthData object for the user after login
     */
    public AuthData login(UserData user) throws Exception {
        MemoryUserDAO access = MemoryUserDAO.getInstance();
        SQLUserDAO dataAccess = new SQLUserDAO();
        //Check if user exists and password is correct
        if(dataAccess.getUser(user.username()) == null){
            throw new Exception("Error: unauthorized");
        }
        BCryptPasswordEncoder coder = new BCryptPasswordEncoder();
        if(!coder.matches(user.password(), dataAccess.getUser(user.username()).password())){
            throw new Exception("Error: unauthorized");
        }
        //Get new AuthData for user
        return new SQLAuthDAO().createAuth(user.username());
    }

    /**
     * Logout the user
     * @param auth the AuthData of the User
     */
    public void logout(String auth) throws Exception {
        MemoryAuthDAO authAccess = MemoryAuthDAO.getInstance();
        SQLAuthDAO dataAccess = new SQLAuthDAO();
        //Is auth in database? AKA is user authorized
        if(dataAccess.getAuth(auth) == null){
            throw new Exception("Error: unauthorized");
        }
        //Logout the user
        authAccess.deleteAuth(auth);
        dataAccess.deleteAuth(auth);
    }
}
