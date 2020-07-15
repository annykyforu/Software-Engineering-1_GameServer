package game;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesGameState.GameState;
import exceptions.GameIDException;
import exceptions.NewGameException;

public class MultiGame {
	private static Logger logger = LoggerFactory.getLogger(MultiGame.class);

	private final int MAX_ENTRIES = 999;
	private Map<String, Game> allGames;
	
	public MultiGame(){
		allGames = new LinkedHashMap<String, Game>() {
	        @Override
	        protected boolean removeEldestEntry(final Map.Entry eldest) {
	        	return size() > MAX_ENTRIES;
	        }
	    };
	}
	
	public void addNewGame(Game newGame) {
		if(!allGames.containsKey(newGame.getUniqueGameID())) {
			allGames.put(newGame.getUniqueGameID(), newGame);
		} else {
			throw new NewGameException("Message: Game with same ID already exists.");
		}
	}
		
	public Game getGame(String gameID) {
		if (allGames.containsKey(gameID)){
			return allGames.get(gameID);
		}
		else {
            throw new GameIDException("Message: There is no such game. Check GameID.");
		}
	}
	
	public GameState getGameState(String gameID, String playerID) {
		Game thisGame = getGame(gameID);
		GameState currentGameState = thisGame.getGameState(playerID);
		return currentGameState;
	}
	
	// check whether such GameID exists
	public void checkID(String _gameID) {
        if(allGames.containsKey(_gameID)){
        	logger.debug("Game with ID [" + _gameID + "] is present.");
        } else {
			logger.error("Error! Requested GameID doesn't exist [" + _gameID + "]");
            throw new GameIDException("Message: There is no such game. Check GameID.");
        }
	}

	// check whether such GameID exists and player is registered in this game
	public void checkID(String _gameID, String _playerID) {
        if(allGames.containsKey(_gameID)){
        	logger.debug("Game with ID [" + _gameID + "] is present.");
        	allGames.get(_gameID).checkPlayerID(_playerID);
        } else {
			logger.error("Error! Requested GameID doesn't exist [" + _gameID + "]");
            throw new GameIDException("Message: There is no such game. Check GameID.");
        }
	}
}
