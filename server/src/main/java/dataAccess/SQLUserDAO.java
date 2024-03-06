package dataAccess;

import model.UserData;

public class SQLUserDAO implements UserDAO{
    @Override
    public void clear() {
        try(var conn = DatabaseManager.getConnection()){
            var preparedStatement = conn.prepareStatement("TRUNCATE TABLE users");
            preparedStatement.executeUpdate();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void createUser(String username, String password, String email) {
        try(var conn = DatabaseManager.getConnection()){
            var preparedStatement = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES(?, ?, ?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) {
        try(var conn = DatabaseManager.getConnection()){
            var preparedStatement = conn.prepareStatement("SELECT password, email FROM users WHERE username=?");
            preparedStatement.setString(1,username);
            try(var rs = preparedStatement.executeQuery()){
                var password = rs.getString("password");
                var email = rs.getString("email");

                return new UserData(username,password,email);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
