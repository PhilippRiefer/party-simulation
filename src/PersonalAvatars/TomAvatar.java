package PersonalAvatars;

import Environment.*;
import java.util.ArrayList;


import AvatarInterface.*;
import java.awt.Color;


 //find my position and set to (0,0)
        //spacesInRange
        /* 0 |  3  | 5
         * 1 |  ME | 6
         * 2 |  4  | 7
        */


public class TomAvatar extends SuperAvatar {

    private static final int mapSizeX = 40;
    private static final int mapSizeY = 20;
    private SpaceType[][] mentalMap;


    public TomAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);
        this.mentalMap = new SpaceType[mapSizeX][mapSizeY];

    }

    public void createMentalMap(ArrayList<SpaceInfo> spacesInRange){
        for(int i = 0; i < spacesInRange.size(); i++){
            if(i < 3){
                mentalMap[spacesInRange.get(0).getRelativeToAvatarCoordinate().getX()]
                [spacesInRange.get(i).getRelativeToAvatarCoordinate().getY()] = spacesInRange.get(i).getType();
            }else if(i < 5){
                mentalMap[spacesInRange.get(3).getRelativeToAvatarCoordinate().getX()]
                [spacesInRange.get(i).getRelativeToAvatarCoordinate().getY()] = spacesInRange.get(i).getType();
            }else{
                mentalMap[spacesInRange.get(5).getRelativeToAvatarCoordinate().getX()]
                [spacesInRange.get(i).getRelativeToAvatarCoordinate().getY()] = spacesInRange.get(i).getType();
            }
        } 
    }

    public Direction goToEmpty(ArrayList<SpaceInfo> spacesInRange){
        for(int i = 0; i < spacesInRange.size(); i++){
            if(spacesInRange.get(i).getType() == SpaceType.EMPTY){        
                if(i == 1){
                        return Direction.LEFT;
                    }else if(i == 3){
                        return Direction.UP;
                    }else if(i == 4){
                        return Direction.DOWN;             
                    }else if(i == 6){
                        return Direction.RIGHT;
                        }
                    }
                }
        return Direction.DOWN;
    }
    
    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {

        createMentalMap(spacesInRange);
        return goToEmpty(spacesInRange);       
        
        
    }

    @Override
    public int getPerceptionRange() {
        return super.getPerceptionRange(); // Assuming SuperAvatar has a method to get the perception range
    }

    @Override
    public void setPerceptionRange(int perceptionRange) {
        super.setPerceptionRange(perceptionRange); // Set the perception range via the superclass method
    }
}
