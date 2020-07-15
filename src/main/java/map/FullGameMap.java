package map;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import MessagesBase.HalfMapNode;
import MessagesGameState.EFortState;
import MessagesGameState.EPlayerPositionState;
import MessagesGameState.ETreasureState;
import MessagesGameState.FullMapNode;

public class FullGameMap {
	private Set<FullMapNode> fullMap;
	int shiftX = 0;
	int shiftY = 0;
	
	public FullGameMap(boolean mapIsSquare, boolean _firstMap) {
		fullMap = new HashSet<FullMapNode>();
		if(mapIsSquare) {
			if(!_firstMap) {
				shiftY = 4;
			}
		} else {
			if(!_firstMap) {
				shiftX = 8;
			}
		}
	}
	
	public void createFullMap(Set<HalfMapNode> tiles) {
		for(HalfMapNode tempNode : tiles) {
			if(tempNode.isFortPresent()) {
				fullMap.add(new FullMapNode(tempNode.getTerrain(),
											EPlayerPositionState.MyPosition,
											ETreasureState.NoOrUnknownTreasureState,
											EFortState.MyFortPresent,
											tempNode.getX()+shiftX, tempNode.getY()+shiftY));
			}
			fullMap.add(new FullMapNode(tempNode.getTerrain(),
                  EPlayerPositionState.NoPlayerPresent,
                  ETreasureState.NoOrUnknownTreasureState,
                  EFortState.NoOrUnknownFortState,
                  tempNode.getX()+shiftX, tempNode.getY()+shiftY));
		}
	}
	
	public Set<FullMapNode> createMapForEnemy(boolean fakePos){
		Set<FullMapNode> enemyMap = new HashSet<>();
		int randomPos = new Random().nextInt(fullMap.size());
		int currentPos = 0;
		for(FullMapNode temp : fullMap) {
			if(currentPos == randomPos) {
				enemyMap.add(new FullMapNode(temp.getTerrain(),
						EPlayerPositionState.EnemyPlayerPosition,
						ETreasureState.NoOrUnknownTreasureState,
						EFortState.NoOrUnknownFortState,
						temp.getX(), temp.getY()));
				++currentPos;
				continue;
			}
			
			if(temp.getFortState() == EFortState.MyFortPresent) {
				enemyMap.add(new FullMapNode(temp.getTerrain(),
						EPlayerPositionState.NoPlayerPresent,
						ETreasureState.NoOrUnknownTreasureState,
						EFortState.NoOrUnknownFortState,
						temp.getX(), temp.getY()));
				++currentPos;
				continue;
			}
			enemyMap.add(temp);
			++currentPos;
		}
		return enemyMap;
	}

    public Set<FullMapNode> getFullMap(){
        return fullMap;
    }
}
