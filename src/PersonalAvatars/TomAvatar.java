package PersonalAvatars;

import Environment.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.ModuleElement.DirectiveKind;

import java.util.Collections;
import java.util.Comparator;
import AvatarInterface.*;
import java.awt.Color;
import java.util.Random;

//find my position and set to (0,0)
//spacesInRange
/* 0 |  3  | 5
 * 1 |  ME | 6
 * 2 |  4  | 7
*/

public class TomAvatar extends SuperAvatar {

    private ArrayList<SpaceInfo> mentalMapList = new ArrayList<>();
    private int storePerceptionRange = 0;
    private int phase = 0, stepCounter = 0, directionCounter = 0;
    private Direction myDirection = Direction.STAY;
    private Direction myDirectionMoveBack = Direction.STAY;
    private static final Random random = new Random();
    private Coordinate myCoordinate = new Coordinate(0, 0);
    private Coordinate foundCoordinate = new Coordinate(0, 0);
    private int minDiff = Integer.MAX_VALUE, minX = 0, minY = 0, minDiffY = 20;
    private int stayCounter = 0;
    private int decision = (int) (random.nextDouble() * 100);
    private int howLongStaying = random.nextInt(100);
    private int waitOnPosition = 0;
    private int merker = 0;
    private int someOneAround = 0;
    private int merkerStay = 0;
    private SpaceInfo infoOfSpace;
    private int iterateThrowLoop = 0;

    public TomAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);
        storePerceptionRange = perceptionRange;

    }

    public void createMentalMap(ArrayList<SpaceInfo> spacesInRange) {
        for (SpaceInfo infoForMentalMap : spacesInRange) {
            if (infoForMentalMap.getType() != SpaceType.EMPTY && infoForMentalMap.getType() != SpaceType.OBSTACLE)
                mentalMapList.add(infoForMentalMap);
        }
    }

    private void removeDuplicateCoordinates() {
        Set<Coordinate> seenCoordinates = new HashSet<>();
        ArrayList<SpaceInfo> uniqueMentalMapList = new ArrayList<>();

        for (SpaceInfo spaceInfo : mentalMapList) {
            if (seenCoordinates.add(spaceInfo.getRelativeToAvatarCoordinate())) {
                uniqueMentalMapList.add(spaceInfo);
            }
        }

        mentalMapList = uniqueMentalMapList;
    }

    private void sortMentalMapListByType() {
        Collections.sort(mentalMapList, new Comparator<SpaceInfo>() {
            @Override
            public int compare(SpaceInfo o1, SpaceInfo o2) {
                return o1.getType().name().compareTo(o2.getType().name());
            }
        });
    }

    public Direction goToStart(ArrayList<SpaceInfo> spacesInRange) {

        if (spacesInRange.get(3).getType() == SpaceType.OBSTACLE) {
            if (spacesInRange.get(1).getType() == SpaceType.OBSTACLE) {
                phase = 1;
                return Direction.STAY;
            }
            return Direction.LEFT;

        } else {
            return Direction.UP;
        }

    }

    public Direction startMapWalk(ArrayList<SpaceInfo> spacesInRange) {

        if (spacesInRange.get(4).getType() == SpaceType.OBSTACLE &&
                spacesInRange.get(6).getType() == SpaceType.OBSTACLE) {
            stepCounter = 0;
            phase = 2;
        }

        switch (directionCounter) {
            case 0:
                if (spacesInRange.get(6).getType() == SpaceType.OBSTACLE) {
                    directionCounter = 1;
                    return Direction.DOWN;
                }
                return Direction.RIGHT;
            case 1:
                stepCounter++;
                if (stepCounter == (storePerceptionRange * 2) + 1
                        || spacesInRange.get(4).getType() == SpaceType.OBSTACLE) {
                    stepCounter = 0;
                    directionCounter = 2;
                    return Direction.LEFT;
                }
                return Direction.DOWN;
            case 2:
                if (spacesInRange.get(1).getType() == SpaceType.OBSTACLE) {
                    directionCounter = 3;
                    return Direction.DOWN;
                }
                return Direction.LEFT;
            case 3:
                stepCounter++;
                if (stepCounter == (storePerceptionRange * 2) + 1
                        || spacesInRange.get(4).getType() == SpaceType.OBSTACLE) {
                    stepCounter = 0;
                    directionCounter = 0;
                    return Direction.RIGHT;
                }
                return Direction.DOWN;
        }

        return Direction.STAY;
    }

    public Direction hitAvatarMoveAway(ArrayList<SpaceInfo> spacesInRange) {
        
        if(merker == 0){
        if (myDirection == Direction.RIGHT && spacesInRange.get(6).getType() == SpaceType.AVATAR
                && spacesInRange.get(4).getType() != SpaceType.OBSTACLE) {
            merker = 1;
            return Direction.DOWN;

        } else if (myDirection == Direction.LEFT && spacesInRange.get(1).getType() == SpaceType.AVATAR
                && spacesInRange.get(3).getType() != SpaceType.OBSTACLE) {
            merker = 2;
            return Direction.UP;
        }
        
        else if (myDirection == Direction.UP && spacesInRange.get(3).getType() == SpaceType.AVATAR
                && spacesInRange.get(6).getType() != SpaceType.OBSTACLE) {
            merker = 3;
            return Direction.RIGHT;

        } else if (myDirection == Direction.DOWN && spacesInRange.get(4).getType() == SpaceType.AVATAR
                && spacesInRange.get(1).getType() != SpaceType.OBSTACLE) {
            merker = 4;
            return Direction.LEFT;
        }
    }

      
        if (merker == 1 && spacesInRange.get(3).getType() != SpaceType.AVATAR) {
            merker = 0;
            return Direction.UP;
        } else if (merker == 2 && spacesInRange.get(4).getType() != SpaceType.AVATAR) {
            merker = 0;
            return Direction.DOWN;
        } else if (merker == 3 && spacesInRange.get(6).getType() != SpaceType.AVATAR) {
            merker = 0;
            return Direction.LEFT;
        } else if (merker == 4 && spacesInRange.get(1).getType() != SpaceType.AVATAR) {
            merker = 0;
            return Direction.RIGHT;
        }
        
        return Direction.STAY;
    }

    public Direction takingDecision(ArrayList<SpaceInfo> spacesInRange) {
        if (decision <= 25) {
            myDirection = goToSomething2(spacesInRange, SpaceType.BAR);
        } else if (decision > 25 && decision <= 50) {
            myDirection = goToSomething2(spacesInRange, SpaceType.SEATS);
        }else if(decision > 50 && decision <= 75){
            myDirection = goToSomething2(spacesInRange, SpaceType.TOILET);
        }else if(decision > 75 && decision <= 100){
            myDirection = goToSomething2(spacesInRange, SpaceType.DANCEFLOOR);
        }
        return myDirection;
    }


    public Direction goToSomething(ArrayList<SpaceInfo> spacesInRange, SpaceType whereToGo){
        if (iterateThrowLoop == 0) {
            for (iterateThrowLoop = 0; iterateThrowLoop < mentalMapList.size(); iterateThrowLoop++) {
                if (mentalMapList.get(iterateThrowLoop).getType() == whereToGo) {
                    infoOfSpace = mentalMapList.get(iterateThrowLoop);
                    break;
                    
                }
            }
        }

        if(spacesInRange.get(3).getRelativeToAvatarCoordinate().getY() + 1 == infoOfSpace.getRelativeToAvatarCoordinate().getY()){
            stayCounter++;

            if (stayCounter == 20) {
                stayCounter = 0;
                iterateThrowLoop = 0;
                decision = (int) (random.nextDouble() * 100);
                return Direction.STAY;
            }
        }

        if (myDirection == Direction.UP && spacesInRange.get(3).getRelativeToAvatarCoordinate().getX() == infoOfSpace.getRelativeToAvatarCoordinate().getX() &&
                spacesInRange.get(3).getRelativeToAvatarCoordinate().getY() == infoOfSpace.getRelativeToAvatarCoordinate().getY()
                && spacesInRange.get(3).getType() != infoOfSpace.getType()) {
                    iterateThrowLoop++;
        } else if (myDirection == Direction.RIGHT && spacesInRange.get(6).getRelativeToAvatarCoordinate().getX() == infoOfSpace.getRelativeToAvatarCoordinate().getX() &&
                spacesInRange.get(6).getRelativeToAvatarCoordinate().getY() == infoOfSpace.getRelativeToAvatarCoordinate().getY()
                && spacesInRange.get(6).getType() != infoOfSpace.getType()) {
                    iterateThrowLoop++;
        } else if (myDirection == Direction.LEFT && spacesInRange.get(1).getRelativeToAvatarCoordinate().getX() == infoOfSpace.getRelativeToAvatarCoordinate().getX() &&
                spacesInRange.get(1).getRelativeToAvatarCoordinate().getY() == infoOfSpace.getRelativeToAvatarCoordinate().getY()
                && spacesInRange.get(1).getType() != infoOfSpace.getType()) {
                    iterateThrowLoop++;
        } else if (myDirection == Direction.DOWN && spacesInRange.get(4).getRelativeToAvatarCoordinate().getX() == infoOfSpace.getRelativeToAvatarCoordinate().getX() &&
            spacesInRange.get(4).getRelativeToAvatarCoordinate().getY() == infoOfSpace.getRelativeToAvatarCoordinate().getY()
            && spacesInRange.get(4).getType() != infoOfSpace.getType()) {
                iterateThrowLoop++;
        }

        infoOfSpace = mentalMapList.get(iterateThrowLoop);
        myCoordinate = infoOfSpace.getRelativeToAvatarCoordinate();
        return goToMyCoordinate2(spacesInRange, myCoordinate);
    }

    public Direction goToSomething2(ArrayList<SpaceInfo> spacesInRange, SpaceType whereToGo){
        int diff = 0;
        if (stepCounter == 0) {
            for (iterateThrowLoop = 0; iterateThrowLoop < mentalMapList.size(); iterateThrowLoop++) {
                if (mentalMapList.get(iterateThrowLoop).getType() == whereToGo) {
                    
                    Coordinate minDiffCoordinate = mentalMapList.get(iterateThrowLoop).getRelativeToAvatarCoordinate();

                    diff = Math.abs(minDiffCoordinate.getX() - spacesInRange.get(3).getRelativeToAvatarCoordinate().getX())
                        + Math.abs(minDiffCoordinate.getY() - (spacesInRange.get(3).getRelativeToAvatarCoordinate().getY() + 1));

                    if (diff < minDiff) {
                        System.out.println("Diff unten: " + diff);
                        minDiff = diff;
                        foundCoordinate = minDiffCoordinate;
                    }                  
                }
            }
        }
        stepCounter++;

        if(spacesInRange.get(3).getRelativeToAvatarCoordinate().getY() + 1 == foundCoordinate.getY()){
            stayCounter++;

            if (stayCounter == 20) {
                stepCounter = 0;
                stayCounter = 0;
                iterateThrowLoop = 0;
                minDiff = Integer.MAX_VALUE;
                decision = (int) (random.nextDouble() * 100);
            }
        }
        /* 
        if (myDirection == Direction.UP && spacesInRange.get(3).getRelativeToAvatarCoordinate().getX() == infoOfSpace.getRelativeToAvatarCoordinate().getX() &&
                spacesInRange.get(3).getRelativeToAvatarCoordinate().getY() == infoOfSpace.getRelativeToAvatarCoordinate().getY()
                && spacesInRange.get(3).getType() != infoOfSpace.getType()) {
                    iterateThrowLoop++;
        } else if (myDirection == Direction.RIGHT && spacesInRange.get(6).getRelativeToAvatarCoordinate().getX() == infoOfSpace.getRelativeToAvatarCoordinate().getX() &&
                spacesInRange.get(6).getRelativeToAvatarCoordinate().getY() == infoOfSpace.getRelativeToAvatarCoordinate().getY()
                && spacesInRange.get(6).getType() != infoOfSpace.getType()) {
                    iterateThrowLoop++;
        } else if (myDirection == Direction.LEFT && spacesInRange.get(1).getRelativeToAvatarCoordinate().getX() == infoOfSpace.getRelativeToAvatarCoordinate().getX() &&
                spacesInRange.get(1).getRelativeToAvatarCoordinate().getY() == infoOfSpace.getRelativeToAvatarCoordinate().getY()
                && spacesInRange.get(1).getType() != infoOfSpace.getType()) {
                    iterateThrowLoop++;
        } else if (myDirection == Direction.DOWN && spacesInRange.get(4).getRelativeToAvatarCoordinate().getX() == infoOfSpace.getRelativeToAvatarCoordinate().getX() &&
            spacesInRange.get(4).getRelativeToAvatarCoordinate().getY() == infoOfSpace.getRelativeToAvatarCoordinate().getY()
            && spacesInRange.get(4).getType() != infoOfSpace.getType()) {
                iterateThrowLoop++;
        }*/

        return goToMyCoordinate2(spacesInRange, foundCoordinate);
    }


   

    public Direction goToMyCoordinate2(ArrayList<SpaceInfo> spacesInRange, Coordinate myCoordinate) {

        if (spacesInRange.get(3).getRelativeToAvatarCoordinate().getX() < myCoordinate.getX())
            return Direction.RIGHT;
        else if (spacesInRange.get(1).getRelativeToAvatarCoordinate().getY() > myCoordinate.getY())
            return Direction.UP;
        else if (spacesInRange.get(1).getRelativeToAvatarCoordinate().getY() < myCoordinate.getY())
            return Direction.DOWN;
        else if (spacesInRange.get(3).getRelativeToAvatarCoordinate().getX() > myCoordinate.getX())
            return Direction.LEFT;
        else {
            return Direction.STAY;
        }

    }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {

        switch (phase) {
            case 0:
                myDirection = goToStart(spacesInRange);
                break;
            case 1:
                createMentalMap(spacesInRange);
                myDirection = startMapWalk(spacesInRange);
                break;
            case 2:
                removeDuplicateCoordinates();
                sortMentalMapListByType();
                phase = 3;
                break;
            case 3:
                myDirection = takingDecision(spacesInRange);
                break;
            case 5:

            default:
                break;
        }

        myDirectionMoveBack = hitAvatarMoveAway(spacesInRange);

        if(myDirectionMoveBack != Direction.STAY)
            return myDirectionMoveBack;

        return myDirection;

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
