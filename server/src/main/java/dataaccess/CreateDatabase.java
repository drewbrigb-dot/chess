package dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateDatabase {
    private String[]  createStatement;
    public CreateDatabase (String[] createStatement)  {
        this.createStatement = createStatement;
    }

    public void executeDatabase () throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatement) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException | DataAccessException ex ) {
            throw new DataAccessException("Unable to read data");
        }
    }

}
