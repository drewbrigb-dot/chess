package ui;

import chess.ChessGame;

public record LoginClientReturn(boolean gameJoined, ChessGame.TeamColor color) {
}
