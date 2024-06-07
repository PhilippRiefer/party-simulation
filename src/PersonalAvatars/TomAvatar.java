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

public class TomAvatar extends SuperAvatar {

    private static final int mapSizeX = 40;
    private static final int mapSizeY = 20;
    private SpaceType[][] mentalMap;
    private int storePerceptionRange = 0;
    private int phase = 0, stepCounter = 0, directionCounter = 0, circleCounter = 0;
    private Coordinate myCoordinate;
    boolean nextDirection = false;

    public TomAvatar(int id, int perceptionRange, Color color) {
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

        if (myCoordinate.getX() > 19)
            return Direction.LEFT;
        else if (myCoordinate.getX() < 19)
            return Direction.RIGHT;
        else if (myCoordinate.getY() > 9)
            return Direction.UP;
        else if (myCoordinate.getY() < 9)
            return Direction.DOWN;
        else
            return Direction.STAY;

    }

    
    public Direction startSpiral(ArrayList<SpaceInfo> spacesInRange){
        
        

        while(myCoordinate.getX() > 1){
            stepCounter++;


            if(directionCounter == 0 & stepCounter <= (2*storePerceptionRange + 1)*(2*circleCounter + 1)){
                if((stepCounter == (2*storePerceptionRange + 1)*(2*circleCounter + 1)) || (myCoordinate.getX() == 38)){
                    stepCounter = 0;
                    directionCounter = 1;
                    return Direction.UP;
                }        
                return Direction.RIGHT;
            } else if(directionCounter == 1 & stepCounter <= (2*storePerceptionRange + 1)*(2*circleCounter + 1)){
                System.out.println("Y" + myCoordinate.getY());
                if((stepCounter == (2*storePerceptionRange + 1)*(2*circleCounter + 1)) || (myCoordinate.getY() == 1)){
                    stepCounter = 0;
                    directionCounter = 2;
                    return Direction.LEFT;
                }
                return Direction.UP;
            } else if(directionCounter == 2 & stepCounter <= ((2*storePerceptionRange + 1)*2) * (circleCounter+1)){
                if((stepCounter == ((2*storePerceptionRange + 1)*2) * (circleCounter + 1)) || (myCoordinate.getX() == 1)){
                    stepCounter = 0;
                    directionCounter = 3;
                    return Direction.DOWN;
                }
                return Direction.LEFT;
            }else if(directionCounter == 3 & stepCounter <= ((2*storePerceptionRange + 1)*2) * (circleCounter + 1)){
                if((stepCounter == ((2*storePerceptionRange + 1)*2) * (circleCounter + 1)) || (myCoordinate.getY() == 18)){
                    stepCounter = 0;
                    directionCounter = 0;
                    circleCounter++;
                    return Direction.RIGHT;
                }
                return Direction.DOWN;
            }
        }
        
        return Direction.STAY;
    }


    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        
        // createMentalMap(spacesInRange);
        Direction dir = Direction.STAY;

        if(myCoordinate.getX() == 19 & myCoordinate.getY() == 9){
            phase = 1;
        }


        switch (phase) {
            case 0:
            getMyPosition(spacesInRange);
            dir = goToStart();
            break;
            case 1: 
            getMyPosition(spacesInRange);
            dir = startSpiral(spacesInRange);
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
