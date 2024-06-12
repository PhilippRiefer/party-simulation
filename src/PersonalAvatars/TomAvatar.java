package PersonalAvatars;

import Environment.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.lang.model.element.ModuleElement.DirectiveKind;

import org.reflections.vfs.Vfs.Dir;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

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
    private ArrayList<SpaceInfo> dancefloorList = new ArrayList<>();
    private ArrayList<SpaceInfo> toiletList = new ArrayList<>();
    private ArrayList<SpaceInfo> barList = new ArrayList<>();
    private ArrayList<SpaceInfo> seatsList = new ArrayList<>();
    private int storePerceptionRange = 0;
    private int phase = 0, stepCounter = 0, directionCounter = 0;
    private Direction myDirection = Direction.STAY;
    private Direction myDirectionStorage = Direction.STAY;
    private Direction myDirectionMoveBack = Direction.STAY;
    private static final Random random = new Random();
    private Coordinate myPosition = new Coordinate(0, 0);
    private Coordinate foundCoordinate = new Coordinate(0, 0);
    private int minDiff = Integer.MAX_VALUE, secondMinDiff = Integer.MAX_VALUE;
    private int stayCounter = 0;
    private int decision = (int) (random.nextDouble() * 100);
    private int howLongStaying = random.nextInt(100);
    private int waitOnPosition = 0;
    private int merker = 0;
    private int someOneAround = 0;
    private int merkerStay = 0;
    private SpaceInfo infoOfSpace;
    private int iterateThrowLoop = 0;
    private boolean conflictMerker = false;
    private int counterHitAvatarSeats = 0, counterHitAvatarBar = 0, counterHitAvatarDancefloor = 0, counterHitAvatarToilet = 0;
    private int xDancefloor = 0, yDancefloor = 0;
    private int storageMerker = 0;
    private int merkerDirection = 0;


    public TomAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);
        storePerceptionRange = perceptionRange;

    }

    public void createMentalMap(ArrayList<SpaceInfo> spacesInRange) {
        for (SpaceInfo infoForMentalMap : spacesInRange) {
            if (infoForMentalMap.getType() != SpaceType.EMPTY && infoForMentalMap.getType() != SpaceType.OBSTACLE && infoForMentalMap.getType() != SpaceType.AVATAR)
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

    private void sortMentalMapListByType(ArrayList<SpaceInfo> spacesInRange) {
        Collections.sort(mentalMapList, new Comparator<SpaceInfo>() {
            @Override
            public int compare(SpaceInfo o1, SpaceInfo o2) {
                return o1.getType().name().compareTo(o2.getType().name());
            }
        });

        for(SpaceInfo infos : mentalMapList){
            if(infos.getType() == SpaceType.DANCEFLOOR){
                dancefloorList.add(infos);
            }else if (infos.getType() == SpaceType.BAR){
                barList.add(infos);
            }else if(infos.getType() == SpaceType.TOILET){
                toiletList.add(infos);
            }else if(infos.getType() == SpaceType.SEATS){
                seatsList.add(infos);
            }
        }
    }

    public Direction goToStart(ArrayList<SpaceInfo> spacesInRange) {

        if (spacesInRange.get(1).getType() == SpaceType.OBSTACLE) {
            if (spacesInRange.get(3).getType() == SpaceType.OBSTACLE) {
                phase = 1;
                return Direction.STAY;
            }
            return Direction.UP;

        } else {
            return Direction.LEFT;
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

    public Direction hitAvatarOnWay(ArrayList<SpaceInfo> spacesInRange) {

        if(merker == 0){
            if(conflictDetected(spacesInRange, myDirection) == true){
                merker = 1;
                myDirectionStorage = goDancing();
                return myDirectionStorage;
            }
        }   

        if(merker == 1){
            merker = 0;
            if(myDirectionStorage == Direction.RIGHT){
                return Direction.LEFT;
            }else if(myDirectionStorage == Direction.LEFT){
                return Direction.RIGHT;
            }else if(myDirectionStorage == Direction.UP){
                return Direction.DOWN;
            }else if(myDirectionStorage == Direction.DOWN){
                return Direction.UP;
            }
        }
        return Direction.STAY;
    }

    public Direction takingDecision(ArrayList<SpaceInfo> spacesInRange) {
        
        if (decision <= 25) {
            myDirection = goToSomething2(spacesInRange, SpaceType.BAR);
        } else if (decision > 25 && decision <= 50) {
            myDirection = goToSomething2(spacesInRange, SpaceType.DANCEFLOOR);
        }else if(decision > 50 && decision <= 75){
            myDirection = goToSomething2(spacesInRange, SpaceType.SEATS);
        }else if(decision > 75 && decision <= 100){
            myDirection = goToSomething2(spacesInRange, SpaceType.TOILET);
        }
        return myDirection;
    }

    public Direction goToSomething2(ArrayList<SpaceInfo> spacesInRange, SpaceType whereToGo){
        int diff = 0,  xMin = 40, xMax = 0, yMin = 20, yMax = 0;
        if(stepCounter == 0){
            for (iterateThrowLoop = 0; iterateThrowLoop < mentalMapList.size(); iterateThrowLoop++) {
                if (mentalMapList.get(iterateThrowLoop).getType() == whereToGo) {
                    
                    Coordinate minDiffCoordinate = mentalMapList.get(iterateThrowLoop).getRelativeToAvatarCoordinate();

                    diff = Math.abs(minDiffCoordinate.getX() - spacesInRange.get(3).getRelativeToAvatarCoordinate().getX())
                        + Math.abs(minDiffCoordinate.getY() - (spacesInRange.get(3).getRelativeToAvatarCoordinate().getY() + 1));

                    if(whereToGo == SpaceType.DANCEFLOOR){
                        if(minDiffCoordinate.getX() < xMin){
                            xMin = minDiffCoordinate.getX();
                        }else if(minDiffCoordinate.getX() > xMax){
                            xMax = minDiffCoordinate.getX();
                        }else if(minDiffCoordinate.getY() < yMin){
                            yMin = minDiffCoordinate.getY();
                        }else if(minDiffCoordinate.getY() > yMax){
                            yMax = minDiffCoordinate.getY();
                        }

                    }if(diff < minDiff) {
                        minDiff = diff;
                        storageMerker = iterateThrowLoop;
                        foundCoordinate = minDiffCoordinate;
                    }
                }       
            }
            if(whereToGo == SpaceType.DANCEFLOOR){
                
                xDancefloor = (xMax + xMin) / 2 + random.nextInt(7) - 3;
                yDancefloor = (yMax + yMin) / 2 + random.nextInt(7) - 3;
                          
                foundCoordinate.setX(xDancefloor);
                foundCoordinate.setY(yDancefloor);
            }
        }
        stepCounter++;
        if(spacesInRange.get(3).getRelativeToAvatarCoordinate().getY() + 1 == foundCoordinate.getY() &&
           spacesInRange.get(3).getRelativeToAvatarCoordinate().getX() == foundCoordinate.getX()){
            stayCounter++;

            if (stayCounter == 50) {
                xDancefloor = 0;
                yDancefloor = 0;
                stepCounter = 0;
                stayCounter = 0;
                iterateThrowLoop = 0;
                minDiff = Integer.MAX_VALUE;
                decision = (int) (random.nextDouble() * 100);
                counterHitAvatarSeats = 0;
                counterHitAvatarBar = 0;
                counterHitAvatarDancefloor = 0;
                counterHitAvatarToilet = 0;
            }
            if(whereToGo == SpaceType.DANCEFLOOR){
                return goDancing();
            }
            return Direction.STAY;
           }
        
        myDirection = goToFoundCoordinate(spacesInRange, foundCoordinate);
        foundCoordinate = solveHitAvatar(spacesInRange, myDirection, foundCoordinate, whereToGo); 
        return myDirection;
    }

    
    public Direction goDancing(){
        int dancingStep = random.nextInt(4);
        merkerDirection = 1;
        switch (dancingStep) {
            case 0:
                return Direction.RIGHT;
            case 1:
                return Direction.LEFT;
            case 2: 
                return Direction.UP;
            case 3:
                return Direction.DOWN;
        }
        return Direction.STAY;
    }

    public int getManhattanDistance(Coordinate seat, Coordinate b) {
        return Math.abs(seat.getX() - b.getX()) + Math.abs(seat.getY() - b.getY());
    }

    public Coordinate solveHitAvatar(ArrayList<SpaceInfo> spacesInRange, Direction myDirection, Coordinate foundCoordinate, SpaceType whereToGo){
        if(myDirection == Direction.UP && spacesInRange.get(3).getRelativeToAvatarCoordinate().getX() == foundCoordinate.getX() &&
        spacesInRange.get(3).getRelativeToAvatarCoordinate().getY() == foundCoordinate.getY() && spacesInRange.get(3).getType() != whereToGo ||
        myDirection == Direction.DOWN && spacesInRange.get(4).getRelativeToAvatarCoordinate().getX() == foundCoordinate.getX() &&
        spacesInRange.get(4).getRelativeToAvatarCoordinate().getY() == foundCoordinate.getY() && spacesInRange.get(4).getType() != whereToGo ||
        myDirection == Direction.RIGHT && spacesInRange.get(6).getRelativeToAvatarCoordinate().getX() == foundCoordinate.getX() &&
        spacesInRange.get(6).getRelativeToAvatarCoordinate().getY() == foundCoordinate.getY() && spacesInRange.get(6).getType() != whereToGo||
        myDirection == Direction.LEFT && spacesInRange.get(1).getRelativeToAvatarCoordinate().getX() == foundCoordinate.getX() &&
        spacesInRange.get(1).getRelativeToAvatarCoordinate().getY() == foundCoordinate.getY() && spacesInRange.get(1).getType() != whereToGo){

            myPosition = getMyPosition(spacesInRange);

            if(whereToGo == SpaceType.TOILET){
                toiletList.sort(Comparator.comparingInt(toilet -> getManhattanDistance(toilet.getRelativeToAvatarCoordinate(), myPosition)));
                foundCoordinate = toiletList.get(++counterHitAvatarToilet).getRelativeToAvatarCoordinate();
                if(counterHitAvatarToilet == toiletList.size() - 1){
                    counterHitAvatarToilet = 0;
                }

            }else if(whereToGo == SpaceType.SEATS){                    
                seatsList.sort(Comparator.comparingInt(seat -> getManhattanDistance(seat.getRelativeToAvatarCoordinate(), myPosition)));
                foundCoordinate = seatsList.get(++counterHitAvatarSeats).getRelativeToAvatarCoordinate();
                if(counterHitAvatarSeats == seatsList.size() - 1){
                    counterHitAvatarSeats = 0;
                }

            }else if(whereToGo == SpaceType.BAR){
                barList.sort(Comparator.comparingInt(bar -> getManhattanDistance(bar.getRelativeToAvatarCoordinate(), myPosition)));
                foundCoordinate = barList.get(++counterHitAvatarSeats).getRelativeToAvatarCoordinate();
                if(counterHitAvatarBar == barList.size() - 1){
                    counterHitAvatarBar = 0;
                }

            }else if(whereToGo == SpaceType.DANCEFLOOR){
                dancefloorList.sort(Comparator.comparingInt(dancefloor -> getManhattanDistance(dancefloor.getRelativeToAvatarCoordinate(), myPosition)));
                foundCoordinate = dancefloorList.get(++counterHitAvatarDancefloor).getRelativeToAvatarCoordinate();
                if(counterHitAvatarDancefloor == dancefloorList.size() - 1){
                    counterHitAvatarDancefloor = 0;
                }
            }
        }
            return foundCoordinate;
    }

    public Coordinate getMyPosition(ArrayList<SpaceInfo> spacesInRange){
        myPosition.setX(spacesInRange.get(3).getRelativeToAvatarCoordinate().getX());
        myPosition.setY(spacesInRange.get(3).getRelativeToAvatarCoordinate().getY() + 1);
        return myPosition;
    }

    //oben und unten Korrektur funktioniert
    public Direction goToFoundCoordinate(ArrayList<SpaceInfo> spacesInRange, Coordinate myCoordinate) {

        if(merkerDirection == 1){
            merkerDirection = 0;
        if(myDirection == Direction.UP || myDirection == Direction.DOWN){
            if(spacesInRange.get(3).getRelativeToAvatarCoordinate().getX() > myCoordinate.getX())
                return Direction.LEFT;
            else if (spacesInRange.get(3).getRelativeToAvatarCoordinate().getX() < myCoordinate.getX())
                return Direction.RIGHT;
        }else if(myDirection == Direction.RIGHT || myDirection == Direction.LEFT){
            if (spacesInRange.get(1).getRelativeToAvatarCoordinate().getY() > myCoordinate.getY())
                return Direction.UP;
            else if (spacesInRange.get(1).getRelativeToAvatarCoordinate().getY() < myCoordinate.getY())
                return Direction.DOWN;
        }
    }else{
        
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
        return Direction.STAY;
    }

// Methode zum Bewegen in eine Richtung
public Coordinate moveInDirection(Coordinate coord, Direction direction) {
    switch (direction) {
        case UP: return new Coordinate(coord.getX(), coord.getY() - 1);
        case DOWN: return new Coordinate(coord.getX(), coord.getY() + 1);
        case LEFT: return new Coordinate(coord.getX() - 1, coord.getY());
        case RIGHT: return new Coordinate(coord.getX() + 1, coord.getY());
        default: return coord;
    }
}

public boolean conflictDetected(ArrayList<SpaceInfo> spacesInRange, Direction direction) {
    Coordinate nextPosition = moveInDirection(getMyPosition(spacesInRange), direction);
    for (SpaceInfo space : spacesInRange) {
        if (space.getRelativeToAvatarCoordinate().equals(nextPosition) && space.getType() == SpaceType.AVATAR) {
            return true;
        }
    }
    return false;
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

                
                myDirectionMoveBack = hitAvatarMoveAway(spacesInRange);

                if(myDirectionMoveBack != Direction.STAY)
                    return myDirectionMoveBack;
                

                break;
            case 2:
                removeDuplicateCoordinates();
                sortMentalMapListByType(spacesInRange);
                phase = 3;
                break;
            case 3:
                myDirection = takingDecision(spacesInRange);
                if(conflictDetected(spacesInRange, myDirection) == true){
                    
                    if(merkerDirection == 1){
                        goToFoundCoordinate(spacesInRange, foundCoordinate);
                    }
                    myDirection = goDancing();
                    
                }
                break;

            default:
                break;
        }
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
