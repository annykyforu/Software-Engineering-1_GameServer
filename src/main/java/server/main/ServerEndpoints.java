package server.main;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.GameState;
import exceptions.GameIDException;
import exceptions.GenericEndpointException;
import exceptions.NoPlayerException;
import exceptions.PlayerMoveException;
import exceptions.TwoPlayersException;
import exceptions.WaitFor2PlayerException;
import exceptions.WrongPlayerIDException;
import game.Game;
import game.MultiGame;

@Controller
@RequestMapping(value = "/games")
public class ServerEndpoints {
	private static Logger logger = LoggerFactory.getLogger(ServerEndpoints.class);
	MultiGame games = new MultiGame();

	// request GameState
	@RequestMapping(value = "/{gameID}/states/{playerID}",
					method = RequestMethod.GET,
					produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<GameState> gameState(
			@PathVariable String gameID,
			@PathVariable String playerID) {		
		try {
			// base check GameID, PlayerID
			games.checkID(gameID, playerID);
			return new ResponseEnvelope<>(games.getGameState(gameID, playerID));			
		} catch (GameIDException e){
			logger.error("Error! GameID doesn't exist - " + gameID);
			throw new GenericEndpointException(e.getErrorName(), e.getMessage());
		} catch (WrongPlayerIDException e) {
			logger.error("Error! PlayerID is not UUID.");
			throw new GenericEndpointException(e.getErrorName(), e.getMessage());
		} catch (NoPlayerException e){
			logger.error("Error! This player is not in the game - " + playerID);
			throw new GenericEndpointException(e.getErrorName(), e.getMessage());			
		}
	}
	
	// create New Game and get UniqueGameID
	@RequestMapping(value = "",
					method = RequestMethod.GET,
					produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody UniqueGameIdentifier newGame() {
		Game newGame = new Game();
		games.addNewGame(newGame);
		logger.info("A new game is started: [uniqueGameID=" + newGame.getGameIdentifier().getUniqueGameID() + "]");
		return newGame.getGameIdentifier();
	}
	
	// register Player for a Game and receive Unique PlayerID
	@RequestMapping(value = "/{gameID}/players",
					method = RequestMethod.POST,
					consumes = MediaType.APPLICATION_XML_VALUE,
					produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<UniquePlayerIdentifier> registerPlayer(
			@PathVariable String gameID,
			@Validated @RequestBody PlayerRegistration playerRegistration) {
		try {
			// base check GameID
			games.checkID(gameID);
			UniquePlayerIdentifier newPlayerID = games.getGame(gameID).addPlayer(playerRegistration);
			ResponseEnvelope<UniquePlayerIdentifier> playerIDMessage = new ResponseEnvelope<>(newPlayerID);
			logger.info("Game [" + gameID + "] New player registered: [playerID=" + newPlayerID.getUniquePlayerID() + "]");
			return playerIDMessage;
		} catch (GameIDException e){
			throw new GenericEndpointException(e.getErrorName(), e.getMessage());
		}
		catch (TwoPlayersException e){
			logger.error("Error! You can't join the game - two players are already registered.");
			throw new GenericEndpointException(e.getErrorName(), e.getMessage());
		}	
	}

	// receive HalfMap from Player
	@RequestMapping(value = "/{gameID}/halfmaps",
					method = RequestMethod.POST,
					consumes = MediaType.APPLICATION_XML_VALUE,
					produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<?> halfMap(
			@PathVariable String gameID,
			@Validated @RequestBody HalfMap playerMap) {
		try {
			// base check GameID, PlayerID
			games.checkID(gameID, playerMap.getUniquePlayerID());

			// check whether both players are registered for the game
			games.getGame(gameID).check2players();
			games.getGame(gameID).checkPlayerState(playerMap.getUniquePlayerID());
			
			games.getGame(gameID).receiveHalfMap(playerMap);
			logger.info("Game [" + gameID + "] => Player " + playerMap.getUniquePlayerID() + "has transfered his HalfMap.");
			return new ResponseEnvelope<>();				
		} catch (GameIDException e){
			throw new GenericEndpointException(e.getErrorName(), e.getMessage());
		} catch (WrongPlayerIDException e) {
			logger.error("Error! Game: [" + gameID + "] => PlayerID is faulty. " + e.getMessage());
			throw new GenericEndpointException(e.getErrorName(), e.getMessage());
		} catch (NoPlayerException e){
			logger.error("Error! Game: [" + gameID + "] => This player is not in the game - " + playerMap.getUniquePlayerID());
			throw new GenericEndpointException(e.getErrorName(), e.getMessage());			
		} catch (WaitFor2PlayerException e) {
			logger.error("Error! Game: [" + gameID + "] => Not all players are registered.");
			throw new GenericEndpointException(e.getErrorName(), e.getMessage());			
		} catch (PlayerMoveException e) {
			logger.error("Error! Game: [" + gameID + "] => " + e.getMessage());
			throw new GenericEndpointException(e.getErrorName(), e.getMessage());						
		}		
	}
		
	// TODO
	// receive Move from Player
	@RequestMapping(value = "/{gameID}/moves",
					method = RequestMethod.POST,
					consumes = MediaType.APPLICATION_XML_VALUE,
					produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<?> playerMove(
			@PathVariable String gameID,
			@Validated @RequestBody HalfMap playerMove){
		try {
			// base checkGameID, PlayerID
			games.checkID(gameID, playerMove.getUniquePlayerID());

			// check whether both players are registered for the game
			games.getGame(gameID).check2players();
			games.getGame(gameID).checkPlayerState(playerMove.getUniquePlayerID());
			
			logger.info("Game [" + gameID + "] => Player " + playerMove.getUniquePlayerID() + " sent his Move: " + playerMove);
			return new ResponseEnvelope<>();				
		} catch (PlayerMoveException e) {
			logger.error("Error!" + e.getMessage());
			throw new GenericEndpointException(e.getErrorName(), e.getMessage());						
		}
	}
	
	
	/*Note, this is only the most basic way of handling exceptions in spring (but sufficient for our task)
	it would for example struggle if you use multiple controllers. Add the exception types to the @ExceptionHandler
	which your exception handling should support. For larger projects one would most likely want to use the
	HandlerExceptionResolver, see here https://www.baeldung.com/exception-handling-for-rest-with-spring

	Ask yourself: Why is handling the exceptions in a different method than the endpoint methods a good solution? */
	@ExceptionHandler({ GenericEndpointException.class })
	public @ResponseBody ResponseEnvelope<?> handleException(GenericEndpointException ex, HttpServletResponse response) {
		ResponseEnvelope<?> result = new ResponseEnvelope<>(ex.getErrorName(), ex.getMessage( ));
		// https://kodejava.org/how-do-i-send-a-response-status-in-servlet/
		// SC_OK Status code (200) indicating the request succeeded normally
		// reply with 200 OK as defined in the network documentation
		// Side note: We only do this here for simplicity reasons. For future projects, you should check out HTTP status codes and 
		// what they can be used for. Note, the WebClient used on the client can react to them using the .onStatus(...) method.
		response.setStatus(HttpServletResponse.SC_OK); 
	  return result;
	}
}