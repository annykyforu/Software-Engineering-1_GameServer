package game;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.FullMap;
import MessagesGameState.FullMapNode;
import MessagesGameState.GameState;
import MessagesGameState.PlayerState;
import exceptions.GameIDException;
import exceptions.GenericEndpointException;
import exceptions.HalfMapFortException;
import exceptions.HalfMapGrassException;
import exceptions.HalfMapIslandException;
import exceptions.HalfMapMountainException;
import exceptions.HalfMapPresentException;
import exceptions.HalfMapWaterException;
import exceptions.HalfMapWaterOnSideException;
import exceptions.NoPlayerException;
import exceptions.PlayerMoveException;
import exceptions.TwoPlayersException;
import exceptions.WaitFor2PlayerException;
import exceptions.WrongPlayerIDException;
import map.FullGameMap;
import map.PlayerMap;
import map.PrintHalfMap;
import player.Player;

public class Game {
	private static Logger logger = LoggerFactory.getLogger(Game.class);

    private UniqueGameIdentifier gameIdentifier;
    private String gameStateID;
    private Player player1, player2;
    private FullGameMap mapPlayer1, mapPlayer2;
    private boolean mapIsSquare, firstMap;
    private int turns;
    
	public Game() {
		String uniqueGameID = RandomStringUtils.randomAlphanumeric(5);
		gameIdentifier = new UniqueGameIdentifier(uniqueGameID);
		gameStateID = new GameState(uniqueGameID).getGameStateId();
		player1 = null;
		player2 = null;
	    mapIsSquare = new Random().nextBoolean();
	    firstMap = new Random().nextBoolean();
		mapPlayer1 = new FullGameMap(mapIsSquare, firstMap);		
		mapPlayer2 = new FullGameMap(mapIsSquare, !firstMap);
		turns = 0;
	}

	public UniqueGameIdentifier getGameIdentifier() {
		return gameIdentifier;
	}
	
	public String getUniqueGameID() {
		return gameIdentifier.getUniqueGameID();
	}
	
	public String getGameStateID() {
		return gameStateID;
	}

	public void setNewGameStateID() {
		gameStateID = UUID.randomUUID().toString();
	}
    	
	public Player getPlayer1() {
		return player1;
	}
	
	public Player getPlayer2() {
		return player2;
	}
	
    // create GameState
    public GameState getGameState(String playerID) {
    	GameState currentGameState;
    	Set<PlayerState> players = createPlayersState(playerID);
    	currentGameState = new GameState(Optional.of(createCurrentMap(playerID)), players, gameStateID);	    	    				
    	return currentGameState;
    }
    
	// add Player to the Game
    public UniquePlayerIdentifier addPlayer(PlayerRegistration playerRegistration){
        if(player1 == null){
        	player1 = new Player(playerRegistration.getStudentFirstName(),
                    				playerRegistration.getStudentLastName(),
                    				playerRegistration.getStudentID(),
                    				EPlayerGameState.ShouldWait);
        	
        	setNewGameStateID();
            return player1.getPlayerIdentifier();
        } else if(player2 == null) {
        	player2 = new Player(playerRegistration.getStudentFirstName(),
                    playerRegistration.getStudentLastName(),
                    playerRegistration.getStudentID(),
    				EPlayerGameState.ShouldWait);
        	setNewGameStateID();
        	randomizePlayers();
            return player2.getPlayerIdentifier();
        } else {
        	logger.error("2 Players are already registered for the game.");
            throw new TwoPlayersException("Message: Already 2 Players are registered for the game.");
        }
    }
    
    // generate Set<PlayerState>
    private Set<PlayerState> createPlayersState(String playerID) {
        Set<PlayerState> players = new HashSet<>();

        if(playerID.equals(player1.getUniquePlayerID())){
        	players.add(player1.convertPlayerState());
            if(player2 != null){
            	players.add(player2.convertPlayerStateHiddenID());	//hide ID for player2
            }
        } else
        	if(this.player2 != null && playerID.equals(player2.getUniquePlayerID())){
        		players.add(player1.convertPlayerStateHiddenID());	//hide ID for player1
        		players.add(player2.convertPlayerState());
        }
        return players;
    }
    
    // generate current FullMap of the game
    private FullMap createCurrentMap(String _playerID) {
    	Set<FullMapNode> allNodes = new HashSet<FullMapNode>();
    	boolean fakePos = false;
    	if(turns < 10) { fakePos = true; }
    	
    	if(_playerID.equals(player1.getUniquePlayerID())) {
    		if(!mapPlayer1.getFullMap().isEmpty()) { allNodes.addAll(mapPlayer1.getFullMap()); }
    		if(!mapPlayer2.getFullMap().isEmpty()) { allNodes.addAll(mapPlayer2.createMapForEnemy(fakePos)); }
    	} else 
    	if(_playerID.equals(player2.getUniquePlayerID())) {
    		if(!mapPlayer2.getFullMap().isEmpty()) { allNodes.addAll(mapPlayer2.getFullMap()); }
    		if(!mapPlayer1.getFullMap().isEmpty()) { allNodes.addAll(mapPlayer1.createMapForEnemy(fakePos)); }
    	}

    	return allNodes.isEmpty() ? new FullMap() : new FullMap(allNodes);
    }
    
    // receive HalfMap
    public void receiveHalfMap(HalfMap _playerMap) {
    	String playerID = _playerMap.getUniquePlayerID();
    	Set<HalfMapNode> tempNodes = (Set<HalfMapNode>) _playerMap.getNodes();
    	
    	try {
    		PlayerMap.checkHalfMap(tempNodes);
    	} catch(HalfMapFortException e) {
    		setPlayerLost(playerID);
			throw new GenericEndpointException(e.getErrorName(), e.getMessage());
		} catch(HalfMapGrassException e) {
    		setPlayerLost(playerID);
			throw new GenericEndpointException(e.getErrorName(), e.getMessage());
		} catch(HalfMapWaterException e) {
    		setPlayerLost(playerID);
			throw new GenericEndpointException(e.getErrorName(), e.getMessage());
		} catch(HalfMapMountainException e) {
    		setPlayerLost(playerID);
			throw new GenericEndpointException(e.getErrorName(), e.getMessage());
		} catch (HalfMapWaterOnSideException e) {
    		setPlayerLost(playerID);
			throw new GenericEndpointException(e.getErrorName(), e.getMessage());
		} catch (HalfMapIslandException e) {
    		setPlayerLost(playerID);
    		logger.error("Player [" + playerID + "] sent a map with an island.");
    		PrintHalfMap.printHalfMap(tempNodes);
			throw new GenericEndpointException(e.getErrorName(), e.getMessage());
		}
    	
    	if(playerID.equals(player1.getUniquePlayerID()) && player1.getPlayerState() == EPlayerGameState.ShouldActNext) {
    		if(player1.receivedHalfMap()) {
    			setPlayerLost(playerID);
                throw new HalfMapPresentException("Message: You have already sent a HalfMap.");
    		}
    		
    		mapPlayer1.createFullMap(tempNodes);
    		player1.setReceivedHalfMap(true);
            player1.setPlayerState(EPlayerGameState.ShouldWait);
            player2.setPlayerState(EPlayerGameState.ShouldActNext);
    	} 
    	
    	if(playerID.equals(player2.getUniquePlayerID()) && player2.getPlayerState() == EPlayerGameState.ShouldActNext) {
    		if(player2.receivedHalfMap()) {
    			setPlayerLost(playerID);
                throw new HalfMapPresentException("Message: You have already sent a HalfMap.");
    		}
    		
    		mapPlayer2.createFullMap(tempNodes);
    		player2.setReceivedHalfMap(true);
            player2.setPlayerState(EPlayerGameState.ShouldWait);
            player1.setPlayerState(EPlayerGameState.ShouldActNext);
    	}
    	setNewGameStateID();
    }
    
    // check playerID and if player is in the Game
    public void checkPlayerID(String playerID) {
    	if(playerID.length() != 36) {
    		throw new WrongPlayerIDException("Message: UUID check failed.");
    	}
    	if(!player1.checkPlayerID(playerID) && !player2.checkPlayerID(playerID)) {
    		throw new NoPlayerException("Message: This player is not registered for the game.");
    	} 	
    }
    
    // check that 2 players are in the game
    public boolean check2players() {
    	if(player1 != null && player2 != null) {
    		return true;
    	} else {
            throw new WaitFor2PlayerException("Message: You must wait for second player to register.");
    	}
    }
    
    // check if player canActNext
    public boolean checkPlayerState(String _playerID) {
    	if(_playerID.equals(player1.getUniquePlayerID()) && player1.getPlayerState() == EPlayerGameState.ShouldActNext) {
    		return true;
    	} else
    	if(player2 != null && _playerID.equals(player2.getUniquePlayerID()) && player2.getPlayerState() == EPlayerGameState.ShouldActNext) {
        	return true;
        } else {
        	if(_playerID.equals(player1.getUniquePlayerID())) {
				player1.setPlayerState(EPlayerGameState.Lost);
          		player2.setPlayerState(EPlayerGameState.Won);
          		setNewGameStateID();
        		throw new PlayerMoveException("Message: Your current State = " + player1.getPlayerState());
        	}
        	if(_playerID.equals(player2.getUniquePlayerID())) {
          		player1.setPlayerState(EPlayerGameState.Won);
          		player2.setPlayerState(EPlayerGameState.Lost);
          		setNewGameStateID();
        		throw new PlayerMoveException("Message: Your current State = " + player2.getPlayerState());
        	}
        	return false;
        }
    }
    
    // set random NextMove for players
    public void randomizePlayers() {    	
    	if(new Random().nextBoolean()) {
    		player1.setPlayerState(EPlayerGameState.ShouldActNext);
    	} else {
    		player2.setPlayerState(EPlayerGameState.ShouldActNext);
    	}
    }
    
    // set this PlayerState as Lost
    public void setPlayerLost(String playerID) {
    	if(playerID.equals(player1.getUniquePlayerID())) {
			player1.setPlayerState(EPlayerGameState.Lost);
      		player2.setPlayerState(EPlayerGameState.Won);
		} else {
			player1.setPlayerState(EPlayerGameState.Won);
      		player2.setPlayerState(EPlayerGameState.Lost);
		}
		setNewGameStateID();
    }
}