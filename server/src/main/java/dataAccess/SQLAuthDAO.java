package dataAccess;

import model.AuthData;

import java.util.Collection;
import java.util.Random;

public class SQLAuthDAO implements AuthDAO{
    @Override
    public void clear() {
        try(var conn = DatabaseManager.getConnection()){
            var preparedStatement = conn.prepareStatement("TRUNCATE TABLE auths");
            preparedStatement.executeUpdate();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public AuthData createAuth(String username) {
        try(var conn = DatabaseManager.getConnection()){
            var preparedStatement = conn.prepareStatement("INSERT INTO auths (username, authToken) VALUES(?, ?)");
            preparedStatement.setString(1, username);

            String authToken = generateAuthToken();
            preparedStatement.setString(2,authToken);

            preparedStatement.executeUpdate();
            return new AuthData(authToken,username);
        }catch (Exception e){
            System.out.println("SQLAuthDao.createAuth: "+e.getMessage());
        }
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) {
        try(var conn = DatabaseManager.getConnection()){
            var preparedStatement = conn.prepareStatement("SELECT authToken, username FROM auths WHERE authToken=?");
            preparedStatement.setString(1, authToken);
            try(var rs = preparedStatement.executeQuery()){
                while(rs.next()) {
                    var username = rs.getString("username");
                    return new AuthData(authToken, username);
                }
            }catch (Exception e){
                System.out.println("SQLAuthDao.getAuth.executeQuery: "+e.getMessage());
            }
        }catch (Exception e){
            System.out.println("SQLAuthDao.getAuth: "+e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {
        try(var conn = DatabaseManager.getConnection()){
            var preparedStatement = conn.prepareStatement("DELETE FROM auths WHERE authToken=?");
            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

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

    public void printAll(){
        try(var conn = DatabaseManager.getConnection()){
            var preparedStatement = conn.prepareStatement("SELECT authToken, username FROM auths");
            try(var rs = preparedStatement.executeQuery()){
                while(rs.next()) {
                    System.out.print(rs.getString("authToken"));
                    System.out.print(":");
                    System.out.println(rs.getString("username"));
                }
            }catch (Exception e){
                System.out.println("SQLAuthDao.printAll.executeQuery: "+e.getMessage());
            }
        }catch (Exception e){
            System.out.println("SQLAuthDao.printAll: "+e.getMessage());
        }
    }
}
