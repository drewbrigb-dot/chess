package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import org.junit.jupiter.api.function.Executable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDataAccess implements GameDataAccess{

    public SQLGameDataAccess() throws DataAccessException {
        createDatabase();
    }



    public void createDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatement) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex ) {
            throw new DataAccessException("Unable to read data");
        }
    }
    //make create tables function
    private final String[] createStatement = {
            """
            CREATE TABLE IF NOT EXISTS  GameData (
              `gameID` INT NOT NULL,
              `whiteUsername` VARCHAR(255),
              `blackUsername` VARCHAR(255),
              `gameName` VARCHAR(255) NOT NULL,
               `game` TEXT,
              PRIMARY KEY (`gameID`)
            );
            """



    };

    @Override
    public GameData getGame(int gameID) throws DataAccessException{
        var serializer = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM GameData WHERE gameID=?")) {
                preparedStatement.setString(1, String.valueOf(gameID));
                ResultSet result = preparedStatement.executeQuery();
                if (result.next()) {
                    int gameId = result.getInt("gameID");
                    String whiteUsername = result.getString("whiteUsername");
                    String blackUsername = result.getString("blackUsername");
                    String gameName = result.getString("gameName");
                    String chess = result.getString("game");
                    ChessGame chessGame = serializer.fromJson(chess, ChessGame.class);
                    GameData gameData = new GameData(gameId,whiteUsername,blackUsername,gameName,chessGame);
                    return gameData;
                    //do I need to check for password?
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("What the freak happened");
        }
        return null;
    }

    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        var serializer = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("Select Count(*) FROM GameData;")) {
                ResultSet result = preparedStatement.executeQuery();
                if (result.next()) {
                    int rows = result.getInt(1);
                    int gameID = rows+1;

                    try (var preparedAddStatement = conn.prepareStatement
                            ("INSERT INTO GameData (gameID,whiteUsername,blackUsername,gameName,game) VALUES (?,?,?,?,?)")) {
                        preparedAddStatement.setString(1, String.valueOf(gameID));
                        preparedAddStatement.setString(2, "");
                        preparedAddStatement.setString(3,"");
                        preparedAddStatement.setString(4,gameName);
                        ChessGame chessGame = new ChessGame();
                        String jsonChessGame = serializer.toJson(chessGame);
                        preparedAddStatement.setString(5,jsonChessGame);
                        preparedAddStatement.executeUpdate();
                        return gameID;
                    }


                    //do I need to check for password?
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("What the freak happened");
        }


        return 0;
    }

    @Override
    public ArrayList<GameData> listOfGames() throws DataAccessException {

        ArrayList<GameData> listGames = new ArrayList<>();

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("Select Count(*) FROM GameData;")) {
                ResultSet result = preparedStatement.executeQuery();
                result.next();
                int gameId = result.getInt(1);
                listOfGames(listGames,gameId);
            }
        }catch (SQLException e) {
            throw new DataAccessException("What the freak happened");
        }

        return listGames;
    }

    @Override
    public void clearGame() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE GameData")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("da freak");
        }
    }

    @Override
    public void joinGame(ChessGame.TeamColor playerColor, Integer gameID, String username) throws DataAccessException {
        String teamColor;
        String statement;
        if (playerColor == ChessGame.TeamColor.BLACK) {
            teamColor = "blackUsername";
            statement = "UPDATE GameData SET blackUsername=? WHERE gameID=?";
        }else {
            teamColor = "whiteUsername";
            statement = "UPDATE GameData SET whiteUsername=? WHERE gameID=?";
        }

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2,String.valueOf(gameID));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("da freak");
        }


    }

    private void listOfGames (ArrayList<GameData>list, int rows) throws DataAccessException {
        var serializer = new Gson();
        for (int i = 1; i <= rows; i++) {
            try (var conn = DatabaseManager.getConnection()) {
                try (var preparedStatement = conn.prepareStatement("SELECT * FROM GameData WHERE gameID = ?")) {
                    preparedStatement.setString(1, String.valueOf(i));
                    ResultSet result = preparedStatement.executeQuery();
                    result.next();
                    int gameId = result.getInt("gameID");
                    String whiteUsername = result.getString("whiteUsername");
                    String blackUsername = result.getString("blackUsername");
                    String gameName = result.getString("gameName");
                    String chessGame = result.getString("game");

                    ChessGame chessGameData = serializer.fromJson(chessGame, ChessGame.class);
                    GameData gameData = new GameData(gameId,whiteUsername,blackUsername,gameName,chessGameData);
                    list.add(gameData);
                    //int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game
                }
            }catch (SQLException e) {
                throw new DataAccessException("What the freak happened");
            }

        }



    }
}
