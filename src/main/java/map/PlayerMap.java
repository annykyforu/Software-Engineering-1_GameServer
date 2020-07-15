package map;

import java.util.Set;

import MessagesBase.HalfMapNode;
import exceptions.FaultyHalfMapException;
import exceptions.HalfMapFortException;
import exceptions.HalfMapGrassException;
import exceptions.HalfMapIslandException;
import exceptions.HalfMapMountainException;
import exceptions.HalfMapWaterException;
import exceptions.HalfMapWaterOnSideException;

public class PlayerMap {
		// check if player sending the correct HalfMap
		public static boolean checkHalfMap(Set<HalfMapNode> nodes){
	        if(nodes.size() != 32) {
	            throw new FaultyHalfMapException("Message: Your HalfMap must have exactly 32 Tiles. Try again.");
	        }
	        int gras = 0;
	        int water = 0;
	        int mountain = 0;
	        int waterLeftSide = 0;
	        int waterRightSide = 0;
	        int waterTopSide = 0;
	        int waterBottomSide = 0;
	        
	        boolean fort = false;

	        for(HalfMapNode tile : nodes){
	        	switch(tile.getTerrain()) {
	        	case Grass:
	        		if(tile.isFortPresent()) { fort = true; }
	        		++gras;
	        		break;
	        	case Water:
	        		if(tile.getX() == 0) { ++waterLeftSide; }
	        		if(tile.getX() == 7) { ++waterRightSide; }
	        		if(tile.getY() == 0) { ++waterTopSide; }
	        		if(tile.getY() == 3) { ++waterBottomSide; }
	        		++water;
	        		break;
	        	case Mountain:
	        		++mountain;
	        		break;
	        	}
	        }
	        	        
	        if(!fort) {
	        	throw new HalfMapFortException("Message: Fort has not been found.");
	        }
	        if(gras < 15) {
	        	throw new HalfMapGrassException("Message: Not enough Grass tiles. Minimum is 15.");
	        }
	        if(water < 4) {
	        	throw new HalfMapWaterException("Message: Not enough Water tiles. Minimum is 4.");
	        }        
	        if(mountain < 3) {
	        	throw new HalfMapMountainException("Message: Not enough Mountain tiles. Minimum is 3.");
	        }
	        if(waterLeftSide > 1 || waterRightSide > 1) {
	        	throw new HalfMapWaterOnSideException("Message: Too much Water tiles on the map short boundary of the map.");
	        }
	        if(waterTopSide > 3 || waterBottomSide > 3) {
	        	throw new HalfMapWaterOnSideException("Message: Too much Water tiles on the map long boundary of the map.");
	        }
	        
	        // TODO
//	        //check for island - returns true if no island is found
//	        CheckHalfMapForIsland islandCheck = new CheckHalfMapForIsland(nodes);
//	        if(!islandCheck.floodFill()) {
//	        	throw new HalfMapIslandException("Message: An island has been found. Check the your map.");
//	        }
	        
	        return true;
	    }
}
