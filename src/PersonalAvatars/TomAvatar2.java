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
    private int storePerceptionRange = 0;
    private int phase = 0, stepCounter = 0, directionCounter = 0, circleCounter = 0;
    private Coordinate myCoordinate;
    boolean nextDirection = false;

    public TomAvatar2(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);
        storePerceptionRange = perceptionRange;
        this.mentalMap = new SpaceType[mapSizeX][mapSizeY];
        this.myCoordinate = new Coordinate(0, 0);

    }

    public void createMentalMap(ArrayList<SpaceInfo> spacesInRange) {
        for (int i = 0; i < spacesInRange.size(); i++) {
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
        else if (myCoordinate.getY() > 1)
            return Direction.UP;
        else
            return Direction.STAY;

    }

    
    public Direction startMapWalk(ArrayList<SpaceInfo> spacesInRange){

        if(myCoordinate.getY() == 18 & myCoordinate.getX() == 38){
            nextDirection = true;
        }

        if(nextDirection == true){
            return goToStart();
        }
        
        switch(directionCounter){
            case 0:
                if(myCoordinate.getX() == 38){
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
                if(myCoordinate.getX() == 1){
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
      
    }


    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        
        // createMentalMap(spacesInRange);
        Direction dir = Direction.STAY;

        if(myCoordinate.getX() == 1 && myCoordinate.getY() == 1){
            phase = 1;
            nextDirection = false;
        }


        switch (phase) {
            case 0:
            getMyPosition(spacesInRange);
            dir = goToStart();
            break;
            case 1: 
            getMyPosition(spacesInRange);
            dir = startMapWalk(spacesInRange);
            break;
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
