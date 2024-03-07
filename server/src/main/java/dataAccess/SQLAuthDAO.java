package dataAccess;

import model.AuthData;

import java.util.Collection;

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

            AuthData authData = null;
            Collection<AuthData> all = MemoryAuthDAO.getInstance().getAuthCollection();
            for(var i : all){
                if(i.username().equals(username)){
                    preparedStatement.setString(2,i.authToken());
                    authData = new AuthData(i.authToken(),username);
                    break;
                }
            }

            preparedStatement.executeUpdate();
            return authData;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) {
        try(var conn = DatabaseManager.getConnection()){
            var preparedStatement = conn.prepareStatement("SELECT username FROM auths WHERE authToken=?");
            preparedStatement.setString(1, authToken);
            try(var rs = preparedStatement.executeQuery()){
                var username = rs.getString("username");
                return new AuthData(authToken,username);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
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
}
