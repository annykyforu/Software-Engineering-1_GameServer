package player;

import org.junit.Test;

import MessagesGameState.EPlayerGameState;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    @Test
    public void createNewPlayer_correctInfo_assignUniquePlayerID(){
        Player testPlayer = new Player("Max", "Mustermann", "01234567", EPlayerGameState.ShouldWait);
        System.out.println("uniquePlayerID = " + testPlayer.getUniquePlayerID());
        assertFalse(testPlayer.getUniquePlayerID().isEmpty(), "UniquePlayerID must not be empty.");
    }

    @Test
    public void createNewPlayer_emptyOrWrongInfo_ShouldThrowException(){
    	Exception exception = assertThrows(IllegalArgumentException.class,
    									  () -> new Player("", "Mustermann", "01234567", EPlayerGameState.ShouldWait));
    	assertEquals("Error! Player information can't be empty.", exception.getMessage());
    }
}