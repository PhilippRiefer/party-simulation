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
    private int storePerceptionRangeX, storePerceptionRangeY = 0;


    public TomAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);
        storePerceptionRangeX = perceptionRange;
        storePerceptionRangeY = perceptionRange;
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

    public void getMyPosition(ArrayList<SpaceInfo> spacesInRange){
        Coordinate markerPosition = spacesInRange.get(0).getRelativeToAvatarCoordinate();
        SpaceType markerPositionType = spacesInRange.get(0).getType();
        System.out.println(storePerceptionRangeX);

        if(markerPositionType == SpaceType.OBSTACLE & storePerceptionRangeX > 1 )
            storePerceptionRangeX--;
        else if(markerPositionType == SpaceType.OBSTACLE & markerPosition.getY() > storePerceptionRangeY)
            storePerceptionRangeY--;


        Coordinate myPosition = new Coordinate(markerPosition.getX() + storePerceptionRangeX, markerPosition.getY() + storePerceptionRangeY);
        System.out.println("X" + myPosition.getX() + "Y" + myPosition.getY());
    }

    public Direction goToEmpty(ArrayList<SpaceInfo> spacesInRange){
        /* 
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
    }*/
    return Direction.LEFT;
}

    
    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        getMyPosition(spacesInRange);
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
