package game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GameTest {

	@Test
	void createNewGame_gameIDisLengthFive() {
		Game testGame = new Game();
		assertEquals(5, testGame.getUniqueGameID().length());
	}

}
