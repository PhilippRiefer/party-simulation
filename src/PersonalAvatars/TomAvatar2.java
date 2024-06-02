package PersonalAvatars;

import Environment.*;
import java.util.ArrayList;

import org.reflections.vfs.Vfs.Dir;

import AvatarInterface.*;
import java.awt.Color;

//find my position and set to (0,0)
//spacesInRange
/* 0 |  3  | 5
 * 1 |  ME | 6
 * 2 |  4  | 7
*/

public class TomAvatar2 extends SuperAvatar {

    private static final int mapSizeX = 40;
    private static final int mapSizeY = 20;
    private SpaceType[][] mentalMap;
    private int storePerceptionRange = 0, a = 0, b = 0;
    private int phase = 0, stepCounter = 0, directionCounter = 0, circleCounter = 0;
    private Coordinate myCoordinate;
    boolean returnToStart = false;


    public TomAvatar2(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);
        storePerceptionRange = perceptionRange;
        this.mentalMap = new SpaceType[mapSizeX][mapSizeY];
        this.myCoordinate = new Coordinate(0, 0);

    }

    public void createMentalMap(ArrayList<SpaceInfo> spacesInRange) {
        /*for (int i = 0; i < spacesInRange.size(); i++) {
            if (i < 3) {
                mentalMap[spacesInRange.get(0).getRelativeToAvatarCoordinate().getX()][spacesInRange.get(i)
                        .getRelativeToAvatarCoordinate().getY()] = spacesInRange.get(i).getType();
            } else if (i < 5) {
                mentalMap[spacesInRange.get(3).getRelativeToAvatarCoordinate().getX()][spacesInRange.get(i)
                        .getRelativeToAvatarCoordinate().getY()] = spacesInRange.get(i).getType();
            } else {
                mentalMap[spacesInRange.get(5).getRelativeToAvatarCoordinate().getX()][spacesInRange.get(i)
                        .getRelativeToAvatarCoordinate().getY()] = spacesInRange.get(i).getType();
            }
        }*/

        for(int i = 0; i < spacesInRange.size(); i++){
            int x = spacesInRange.get(i).getRelativeToAvatarCoordinate().getX();
            int y = spacesInRange.get(i). getRelativeToAvatarCoordinate().getY();
            mentalMap[x][y] = spacesInRange.get(i).getType();
        }
    }

    public void getMyPosition(ArrayList<SpaceInfo> spacesInRange) {

        for (int i = 0; i < spacesInRange.size(); i++) {
            if ((spacesInRange.get(i).getRelativeToAvatarCoordinate().getX() == spacesInRange.get(i + 1)
                    .getRelativeToAvatarCoordinate().getX()) &
                    (spacesInRange.get(i).getRelativeToAvatarCoordinate().getY() -
                            spacesInRange.get(i + 1).getRelativeToAvatarCoordinate().getY()) == -2) {

                myCoordinate.setX(spacesInRange.get(i).getRelativeToAvatarCoordinate().getX());
                myCoordinate.setY(spacesInRange.get(i).getRelativeToAvatarCoordinate().getY() + 1);
                System.out.println("X" + myCoordinate.getX() + "Y" + myCoordinate.getY());
                break;
            }
        }
    }

    public Direction goToStart() {

        if (myCoordinate.getX() > 1)
            return Direction.LEFT;
        else if (myCoordinate.getY() > storePerceptionRange)
            return Direction.UP;
        else if (myCoordinate.getY() < storePerceptionRange)
            return Direction.DOWN;
        else
            return Direction.STAY;

    }

    public Direction goToStartTest() {

        if (myCoordinate.getX() < 1)
            return Direction.RIGHT;
        else if (myCoordinate.getY() > 9)
            return Direction.UP;      
        else if (myCoordinate.getY() < 9)
            return Direction.DOWN;
        else if(myCoordinate.getX() > 1)
            return Direction.LEFT;
        else
            return Direction.STAY;

    }

    
    public Direction startMapWalk(ArrayList<SpaceInfo> spacesInRange){

        if(returnToStart == true){
            return goToStart();
        }
        /* 
        if(returnToStart == false){
            createMentalMap(spacesInRange);
        }*/
        
        switch(directionCounter){
            case 0:
                if(18 - myCoordinate.getY() <= storePerceptionRange && myCoordinate.getX() == 39 - storePerceptionRange){
                    returnToStart = true;
                    break;
                }else if(myCoordinate.getX() == 39 - storePerceptionRange){
                    directionCounter = 1;
                    return Direction.DOWN;
                }
                return Direction.RIGHT;
            case 1: 
                stepCounter++;
                if(stepCounter == (storePerceptionRange*2)+1 || myCoordinate.getY() == 18){
                    stepCounter = 0;
                    directionCounter = 2;
                    return Direction.LEFT;
                }
                return Direction.DOWN;
            case 2:
                
                if(18 - myCoordinate.getY() <= storePerceptionRange && myCoordinate.getX() == storePerceptionRange){
                    returnToStart = true;
                    break;

                }else if(myCoordinate.getX() == storePerceptionRange){
                    directionCounter = 3;
                    return Direction.DOWN;
                }

                return Direction.LEFT;
            case 3:
                stepCounter++;
                if(stepCounter == (storePerceptionRange*2)+1 || myCoordinate.getY() == 18){
                    stepCounter = 0;
                    directionCounter = 0;
                    return Direction.RIGHT;
                    }
                return Direction.DOWN;

            default:
                return Direction.STAY;               
        }
      return Direction.STAY;
    }


    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        
        // createMentalMap(spacesInRange);
        Direction dir = Direction.STAY;
        getMyPosition(spacesInRange);
        /* 
        if(myCoordinate.getX() == 1 && myCoordinate.getY() == storePerceptionRange && returnToStart == false){
            phase = 1;
        }else if(myCoordinate.getX() == 1 && myCoordinate.getY() == storePerceptionRange && returnToStart == true){
            phase = 2;
        }*/

        
        if(myCoordinate.getX() == 1 && myCoordinate.getY() == 9 && returnToStart == false){
            phase = 1;
        }else if(myCoordinate.getX() == 1 && myCoordinate.getY() == storePerceptionRange && returnToStart == true){
            phase = 2;
        }
        createMentalMap(spacesInRange);

        switch (phase) {
            case 0:
                //dir = goToStart();
                dir = goToStartTest();
            break;
            case 1: 
                dir = startMapWalk(spacesInRange);
            break;
            case 2:
                

                //Gebe Mehrdimensionales Array mentalMap in Konsole aus
                for (a = 0; a < mentalMap.length; a++) {
                    for (int b = 0; b < mentalMap[a].length; b++) {
                            System.out.print(mentalMap[a][b]);
                    }
                    System.out.println();
                }
                return Direction.STAY;
            default:
                break;
        }
        return dir;

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
