package serviceTests;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTests{

    private static UserService service;
    private static MemoryUserDAO memoryUserDAO;
    private static MemoryAuthDAO memoryAuthDAO;
    private static UserData newUser;

    @BeforeAll
    public static void init(){
        service = new UserService();
        memoryUserDAO = MemoryUserDAO.getInstance();
        memoryAuthDAO = MemoryAuthDAO.getInstance();
        newUser = new UserData("newuser","newpassword","newemail");
    }

    @BeforeEach
    public void setup(){
        service.clearUsers();
    }

    @Test
    @Order(1)
    @DisplayName("Clear")
    public void clear() throws Exception{
        service.register(newUser);
        service.clearUsers();
        Assertions.assertTrue(memoryUserDAO.getUserCollection().isEmpty());
    }

    @Test
    @Order(2)
    @DisplayName("Register")
    public void register() throws Exception{
        AuthData auth = service.register(newUser);
        Assertions.assertTrue(memoryUserDAO.getUserCollection().contains(newUser),"User is successfully created");
        Assertions.assertTrue(memoryAuthDAO.getAuth(auth.authToken()).equals(auth), "AuthData matches memory");

        Exception exception = assertThrows(Exception.class, () -> {service.register(newUser);});
        Assertions.assertTrue(exception.getMessage().contains("Error: Already Taken"));

    }

    @Test
    @Order(3)
    @DisplayName("Login")
    public void login() throws Exception{
        service.register(newUser);
        AuthData auth = service.login(newUser);
        Assertions.assertEquals(memoryAuthDAO.getAuth(auth.authToken()), auth, "User is successfully logged in");

        UserData badPassword = new UserData(newUser.username(), "badpassword",newUser.email());
        Exception exception = assertThrows(Exception.class, () -> service.login(badPassword));
        Assertions.assertTrue(exception.getMessage().contains("Error: unauthorized"));
    }

    @Test
    @Order(4)
    @DisplayName("Logout")
    public void logout() throws Exception{
        service.register(newUser);
        AuthData auth = service.login(newUser);
        Assertions.assertEquals(memoryAuthDAO.getAuth(auth.authToken()), auth, "User is successfully logged in");

        service.logout(auth);
        Assertions.assertNull(memoryAuthDAO.getAuth(auth.authToken()), "User is successfully logged out");

        Exception exception = assertThrows(Exception.class, () -> service.logout(auth));
        Assertions.assertTrue(exception.getMessage().contains("Error: unauthorized"));
    }
}
