/*------------------------
Algorithm to check the HalfMap was developed based on the information from the following sources:
1. https://www.hackerearth.com/practice/algorithms/graphs/flood-fill-algorithm/tutorial/
2. MAZE EXPLORATION ALGORITHM FOR SMALL MOBILE PLATFORMS - DOI: 10.1515/ipc-2016-0013
3. https://en.wikipedia.org/wiki/Flood_fill
 ------------------------*/

package map;

import java.util.HashSet;
import java.util.Set;

import MessagesBase.ETerrain;
import MessagesBase.HalfMapNode;

public class CheckHalfMapForIsland {
    private Set<HalfMapNode> unvisited; //all nodes of the map
    private Set<HalfMapNode> unchecked; //nodes of neighbours to be checked
    private HalfMapNode currentStart;

    public CheckHalfMapForIsland(Set<HalfMapNode> halfMap){
        unchecked = new HashSet<>();
        unvisited = new HashSet<>();
        for(HalfMapNode node: halfMap){
            if(node.getTerrain() != ETerrain.Water){
                unvisited.add(node);
                if(node.isFortPresent()){
                    currentStart = node;
                }
            }
        }
    }

    public boolean floodFill(){
        do{
            removeFromUnvisited(currentStart);
            checkNeighbours(currentStart);
            for(HalfMapNode temp : unchecked){
                removeFromUnvisited(temp);
            }

            if(unchecked.size() != 0) {
                currentStart = unchecked.iterator().next();
            }
            unchecked.remove(currentStart);
        } while(unchecked.size() != 0);

        if(unvisited.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void checkNeighbours(HalfMapNode current){
        for(HalfMapNode neighbour : getUnvisited()){
        	if(neighbour.getTerrain() != ETerrain.Water) {
        		if(Math.abs(neighbour.getX() - current.getX()) == 1
                        	&& neighbour.getY() - current.getY() == 0) {
                    this.unchecked.add(neighbour);
                } else
                if(neighbour.getX() - current.getX() == 0
                        	&& Math.abs(neighbour.getY() - current.getY()) == 1) {
                	this.unchecked.add(neighbour);
                }
            }
        }
    }

    public void removeFromUnvisited(HalfMapNode node){
    	unvisited.remove(node);
    }

    public Set<HalfMapNode> getUnvisited(){
        return unvisited;
    }
}
