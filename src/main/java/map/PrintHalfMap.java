package map;

import java.util.Iterator;
import java.util.Set;

import MessagesBase.HalfMapNode;

public class PrintHalfMap {
	public static void printHalfMap(Set<HalfMapNode> nodes) {
		int row = 0;
        int column = 0;
        for(int i = 0; i < 8; ++i){ System.out.print("----"); }
        System.out.println("-");
        while(row < 4) {
            while(column < 8) {
                for (Iterator<HalfMapNode> it = nodes.iterator(); it.hasNext();) {
                    HalfMapNode f = it.next();
                    if(f.getY() == row && f. getX() == column) {
                        String sign = "";
                        System.out.print("|");
                        switch(f.getTerrain()) {
                            case Grass: sign = "..."; break;
                            case Water: sign = "~~~"; break;
                            case Mountain: sign = "^^^"; break;
                        }
                        if(f.isFortPresent()) {
                            System.out.print("[ ]");
                        } else {
                            System.out.print(sign);
                        }
                    }
                }
                ++column;
            }
            System.out.print("|\n");
            for(int i = 0; i < 8; ++i){ System.out.print("----"); }
            System.out.println("-");
            column = 0;
            ++row;
        }
	}
}
