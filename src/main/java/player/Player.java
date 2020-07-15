package player;

import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.PlayerState;

public class Player {
    private UniquePlayerIdentifier uniqueIdentifier;
    private EPlayerGameState playerState;
    private boolean collectedTreasure;
    private boolean sentHalfMap;
    private String firstName;
    private String lastName;
    private String studentID;

    public Player(String _firstName, String _lastName, String _studentID, EPlayerGameState _playerState) {
    	if(validateStudentData(_firstName, _lastName, _studentID)) {
        	firstName = _firstName;
        	lastName = _lastName;
        	studentID = _studentID;
            uniqueIdentifier = new UniquePlayerIdentifier().random();
            playerState = _playerState;
            collectedTreasure = false;
            sentHalfMap = false;
    	} else {
    		throw new IllegalArgumentException("Error! Player information can't be empty.");
    	}
    }
    
    private boolean validateStudentData(String _firstName, String _lastName, String _studentID) {
    	if(_firstName.isBlank() || _lastName.isBlank() || _studentID.isBlank()) {
    		return false;
    	} else {
    		return true;
    	}
    }

    public String getFirstName() {
    	return firstName;
    }
    
    public String getLastName() {
    	return lastName;
    }
    
    public String getStudentID() {
    	return studentID;
    }
    
    public UniquePlayerIdentifier getPlayerIdentifier() {
    	return uniqueIdentifier;
    }
    
    public String getUniquePlayerID() {
        return uniqueIdentifier.getUniquePlayerID();
    }
    
    public EPlayerGameState getPlayerState() {
    	return playerState;
    }
    public void setPlayerState(EPlayerGameState newPlayerState) {
    	playerState = newPlayerState;
    }
    
    public boolean getTreasureState() {
    	return collectedTreasure;
    }
    
    public void setTreasureState(boolean found) {
    	collectedTreasure = found;
    }
    
    public boolean receivedHalfMap() {
    	return sentHalfMap;
    }
    
    public void setReceivedHalfMap(boolean received) {
    	sentHalfMap = received;
    }

    public boolean checkPlayerID(String uniquePlayerID){
        return uniqueIdentifier.getUniquePlayerID().equals(uniquePlayerID);
    }

    public PlayerState convertPlayerState(){
        return new PlayerState(firstName, lastName, studentID, playerState, uniqueIdentifier, collectedTreasure);
    }

    public PlayerState convertPlayerStateHiddenID(){
        new UniquePlayerIdentifier();
        return new PlayerState(firstName, lastName, studentID, playerState, UniquePlayerIdentifier.random(), collectedTreasure);
    }

}
