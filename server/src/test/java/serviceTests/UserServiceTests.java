package serviceTests;

import dataAccess.MemoryUserDAO;
import dataAccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.*;
import service.UserService;

public class UserServiceTests{

    private static UserService service;
    private static MemoryUserDAO memoryUserDAO;
    private static UserData newUser;

    @BeforeAll
    public static void init(){
        service = new UserService();
        memoryUserDAO = new MemoryUserDAO();
        newUser = new UserData("newuser","newpassword","newemail");
    }

    @Test
    @Order(1)
    @DisplayName("Clear")
    public void clear() throws Exception{
        service.register(newUser);
        service.clearUsers();
        Assertions.assertTrue(memoryUserDAO.getUserCollection().isEmpty());
    }
}
