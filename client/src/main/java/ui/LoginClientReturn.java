package ui;

import chess.ChessGame;

public record LoginClientReturn(boolean gameJoined, Integer gameID, ChessGame.TeamColor color, String authToken) {
}
