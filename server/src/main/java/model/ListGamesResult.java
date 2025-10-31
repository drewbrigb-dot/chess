package model;

import dataaccess.GameInfo;

import java.util.ArrayList;

public record ListGamesResult(ArrayList<GameInfo> games) {
}
