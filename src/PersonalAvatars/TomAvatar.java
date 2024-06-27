package PersonalAvatars;

import Environment.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Comparator;
import AvatarInterface.*;
import java.awt.Color;
import java.util.Random;

public class TomAvatar extends SuperAvatar {

    //Declaration of the lists for saving the field contents.
    private ArrayList<SpaceInfo> allDataList = new ArrayList<>();
    private ArrayList<SpaceInfo> dancefloorList = new ArrayList<>();
    private ArrayList<SpaceInfo> toiletList = new ArrayList<>();
    private ArrayList<SpaceInfo> barList = new ArrayList<>();
    private ArrayList<SpaceInfo> seatsList = new ArrayList<>();

    //To save the selected direction.
    private Direction myDirection = Direction.STAY;
    private Direction myDirectionMoveBack = Direction.STAY;

    //To save the current position in myPosition and the target coordinate in foundCoordinate.
    private Coordinate myPosition = new Coordinate(0, 0);
    private Coordinate foundCoordinate = new Coordinate(0, 0);

    //state to save the status of the program.
    private int state = 0;

    //Variables to move the snack-like pattern
    private int storePerceptionRange = 0, stepUpOrDownCounter = 0, directionCounter = 0;
    
    //Initialize the random number generator
    private static final Random random = new Random();

    //Generate random number to take a decision at beginning
    private int decision = (int) (random.nextDouble() * 100);

    //Some also needed Integer
    private int stayCounter = 0;
    private int merkerDidFirstMove = 0;
    private int counterHitAvatar = 1;
    private int xDancefloor = 0, yDancefloor = 0;
    private int didRandomStep = 0;
    private int foundCoordinateCounter = 0;

    public TomAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);
        storePerceptionRange = perceptionRange;

    }

    //storeDataInList adds the SpaceInfos to a List
    public void storeDataInList(ArrayList<SpaceInfo> spacesInRange) {
        for (SpaceInfo addInfosInList : spacesInRange) {
            if (addInfosInList.getType() != SpaceType.EMPTY && addInfosInList.getType() != SpaceType.OBSTACLE && addInfosInList.getType() != SpaceType.AVATAR)
                allDataList.add(addInfosInList);
        }
    }

    //All Data stored multiple times are deleted from allDataList
    private void removeDuplicateCoordinates() {
        Set<Coordinate> seenCoordinates = new HashSet<>();
        ArrayList<SpaceInfo> uniqueMentalMapList = new ArrayList<>();

        for (SpaceInfo spaceInfo : allDataList) {
            if (seenCoordinates.add(spaceInfo.getRelativeToAvatarCoordinate())) {
                uniqueMentalMapList.add(spaceInfo);
            }
        }

        allDataList = uniqueMentalMapList;
    }

    //Based on the data in allDataList, all data is added to individual SpaceType lists 
    private void createSpaceTypesLists(ArrayList<SpaceInfo> spacesInRange) {
        for(SpaceInfo infos : allDataList){
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

    //All avatars on the map move to the starting point (1,1)
    public Direction goToStart(ArrayList<SpaceInfo> spacesInRange) {

        if (spacesInRange.get(1).getType() == SpaceType.OBSTACLE) {
            if (spacesInRange.get(3).getType() == SpaceType.OBSTACLE) {
                state = 1;
                return Direction.STAY;
            }
            return Direction.UP;

        } else {
            return Direction.LEFT;
        }

    }

    /*
     * The avatars move in a snake-like pattern to the target point (38,38). 
     * Meanwhile, the list is filled with data.
     */
    public Direction startMapWalkAndStore(ArrayList<SpaceInfo> spacesInRange) {

        if (spacesInRange.get(4).getType() == SpaceType.OBSTACLE &&
                spacesInRange.get(6).getType() == SpaceType.OBSTACLE) {
            stepUpOrDownCounter = 0;
            state = 2;
        }

        switch (directionCounter) {
            case 0:
                if (spacesInRange.get(6).getType() == SpaceType.OBSTACLE) {
                    directionCounter = 1;
                    return Direction.DOWN;
                }
                return Direction.RIGHT;
            case 1:
                stepUpOrDownCounter++;
                if (stepUpOrDownCounter == (storePerceptionRange * 2) + 1
                        || spacesInRange.get(4).getType() == SpaceType.OBSTACLE) {
                    stepUpOrDownCounter = 0;
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
                stepUpOrDownCounter++;
                if (stepUpOrDownCounter == (storePerceptionRange * 2) + 1
                        || spacesInRange.get(4).getType() == SpaceType.OBSTACLE) {
                    stepUpOrDownCounter = 0;
                    directionCounter = 0;
                    return Direction.RIGHT;
                }
                return Direction.DOWN;
        }

        return Direction.STAY;
    }

    /*
     * If avatars meet on their way during the saving process, they move away from each other in 
     * specific directions. The avatar then waits until the other avatar has disappeared from the original field.
     * The avatars then move back to their original field.
     */
    public Direction hitAvatarAndMoveAway(ArrayList<SpaceInfo> spacesInRange) {

        if(merkerDidFirstMove == 0){
            if (myDirection == Direction.RIGHT && spacesInRange.get(6).getType() == SpaceType.AVATAR
                && spacesInRange.get(4).getType() != SpaceType.OBSTACLE) {
                merkerDidFirstMove = 1;
                return Direction.DOWN;

            } else if (myDirection == Direction.LEFT && spacesInRange.get(1).getType() == SpaceType.AVATAR
                && spacesInRange.get(3).getType() != SpaceType.OBSTACLE) {
                merkerDidFirstMove = 2;
                return Direction.UP;

            } else if (myDirection == Direction.UP && spacesInRange.get(3).getType() == SpaceType.AVATAR
                && spacesInRange.get(6).getType() != SpaceType.OBSTACLE) {
                merkerDidFirstMove = 3;
                return Direction.RIGHT;

            } else if (myDirection == Direction.DOWN && spacesInRange.get(4).getType() == SpaceType.AVATAR
                && spacesInRange.get(1).getType() != SpaceType.OBSTACLE) {
                merkerDidFirstMove = 4;
                return Direction.LEFT;
        }
    }
       
        if (merkerDidFirstMove == 1 && spacesInRange.get(3).getType() != SpaceType.AVATAR) {
            merkerDidFirstMove = 0;
            return Direction.UP;
        } else if (merkerDidFirstMove == 2 && spacesInRange.get(4).getType() != SpaceType.AVATAR) {
            merkerDidFirstMove = 0;
            return Direction.DOWN;
        } else if (merkerDidFirstMove == 3 && spacesInRange.get(6).getType() != SpaceType.AVATAR) {
            merkerDidFirstMove = 0;
            return Direction.LEFT;
        } else if (merkerDidFirstMove == 4 && spacesInRange.get(1).getType() != SpaceType.AVATAR) {
            merkerDidFirstMove = 0;
            return Direction.RIGHT;
        }
        return Direction.STAY;
    }
    
    /*
     * The probabilities for the respective SpaceType are defined.
     * The avatar makes a decision in (38, 38) and in each target point.
     * It goes to the respective SpaceType at 25%. 
     */
    public Direction takingDecision(ArrayList<SpaceInfo> spacesInRange) {
        
        if (decision <= 24) {
            myDirection = goToSpaceType(spacesInRange, barList, SpaceType.BAR, 30);
        } else if (decision > 24 && decision <= 49) {
            myDirection = goToSpaceType(spacesInRange, dancefloorList, SpaceType.DANCEFLOOR, 30);
        }else if(decision > 49 && decision <= 74){
            myDirection = goToSpaceType(spacesInRange, seatsList, SpaceType.SEATS, 30);
        }else if(decision > 74 && decision <= 99){
            myDirection = goToSpaceType(spacesInRange, toiletList, SpaceType.TOILET,30);
        }
        return myDirection;
    }
   
    /*
     * The Avatar has found its SpaceType. The shortest route to the SpaceType is calculated 
     * based on the avatar's position. If an avatar wants to go to the dancefloor, 
     * random coordinates on the dancefloor are calculated. Once the avatar has found its destination,
     * it waits for a certain amount of time and makes a new decision. 
     * The avatar goes to the nearest SpaceType if a space is already occupied.
     */
    public Direction goToSpaceType(ArrayList<SpaceInfo> spacesInRange, ArrayList<SpaceInfo> aimType, SpaceType whereToGo, int timeToWait){
  
        if(foundCoordinateCounter == 0){
            //List is sorted by the shortest distance from the SpaceType to the avatar
            aimType.sort(Comparator.comparingInt(space -> getMinimumDistance(space.getRelativeToAvatarCoordinate(), getMyPosition(spacesInRange))));
            //The closest SpaceType in at Position 0. Now foundCoordinate is found
            foundCoordinate = aimType.get(0).getRelativeToAvatarCoordinate();


            //If the dancefloor is selected, a random coordinate within the dancefloor is calculated
            if(whereToGo == SpaceType.DANCEFLOOR){
                int xMin = 40, xMax = 0, yMin = 20, yMax = 0;
                //The minimum x and y values are searched for in listDanceFloor
                for(int i = 0; i < aimType.size(); i++){
                    Coordinate minDiffCoordinate = aimType.get(i).getRelativeToAvatarCoordinate();
                    if(minDiffCoordinate.getX() < xMin){
                        xMin = minDiffCoordinate.getX();
                    }if(minDiffCoordinate.getX() >= xMax){
                        xMax = minDiffCoordinate.getX();
                    }if(minDiffCoordinate.getY() < yMin){
                        yMin = minDiffCoordinate.getY();
                    }if(minDiffCoordinate.getY() >= yMax){
                        yMax = minDiffCoordinate.getY();
                    }
                }
                //A random number is generated and added to the center of the dancefloor
                xDancefloor = (xMax + xMin) / 2 + random.nextInt(7) - 3;
                yDancefloor = (yMax + yMin) / 2 + random.nextInt(7) - 3;
                          
                //The found coordinate is assigned to foundCoordinate
                foundCoordinate.setX(xDancefloor);
                foundCoordinate.setY(yDancefloor);
            }
        }
        
        foundCoordinateCounter++;
        //Once the avatar has arrived at foundCoordinate, it waits and searches for a new destination
        if(getMyPosition(spacesInRange).equals(foundCoordinate)){
            stayCounter++;
            //Reset variables and generate new random number
            if (stayCounter == timeToWait) {
                xDancefloor = 0;
                yDancefloor = 0;
                foundCoordinateCounter = 0;
                stayCounter = 0;
                //generate random number between 0 and 99
                decision = (int) (random.nextDouble() * 100);
                counterHitAvatar = 0;
            }
            //A random sequence of steps is danced around foundCoordinate
            if(whereToGo == SpaceType.DANCEFLOOR){
                return doRandomStep();
            }
            return Direction.STAY;
           }
        //Now the avatar goes to the SpaceType
        myDirection = goToFoundCoordinate(spacesInRange);
        //In the direction taken, it must be checked whether an avatar is hit
        foundCoordinate = hitAvatarOnFoundCoordinate(spacesInRange, aimType, myDirection, foundCoordinate); 
        return myDirection;
    }

    //Just do a random step
    public Direction doRandomStep(){
        int dancingStep = random.nextInt(4);
        didRandomStep = 1;
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

    //calculate the shortest distance between two coordinates
    public int getMinimumDistance(Coordinate a, Coordinate b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    /*
     * Is called up as soon as an avatar is already at foundCoordinate. 
     * The nearest Coordinate is then selected as the new foundCoordinate.
     * If an Avatar is hit again, it is moved to the next closest Avatar.
     */
    public Coordinate hitAvatarOnFoundCoordinate(ArrayList<SpaceInfo> spacesInRange, ArrayList<SpaceInfo> aimType, Direction myDirection, Coordinate foundCoordinate){
        if(conflictDetected(spacesInRange, myDirection) == true){

            myPosition = getMyPosition(spacesInRange);
            //Sort list by the closest foundCoordinate of the respective SpaceType
            aimType.sort(Comparator.comparingInt(space -> getMinimumDistance(space.getRelativeToAvatarCoordinate(), myPosition)));

            //If an avatar does not find a place, a new decision is made
            if(counterHitAvatar >= aimType.size() - 1){
                counterHitAvatar = 1;
                decision = (int) (random.nextDouble() * 100);
            }
            //foundCoordinate is assigned to the nearest coordinate, then to the next nearest...
            if(counterHitAvatar <= aimType.size() - 1)
                foundCoordinate = aimType.get(counterHitAvatar).getRelativeToAvatarCoordinate();
       
            counterHitAvatar++;
        }
        return foundCoordinate;
    }

    //Returns the current position of the avatar
    public Coordinate getMyPosition(ArrayList<SpaceInfo> spacesInRange){
        myPosition.setX(spacesInRange.get(3).getRelativeToAvatarCoordinate().getX());
        myPosition.setY(spacesInRange.get(3).getRelativeToAvatarCoordinate().getY() + 1);
        return myPosition;
    }

    /*
     * Go to the coordinate foundCoordinate.
     * When a random step is made, try to walk past the avatar in Direction of foundCoordinate.
     */
    public Direction goToFoundCoordinate(ArrayList<SpaceInfo> spacesInRange) {

        //Try to walk past Avatar in Dircetion of foundCoordinate
        if(didRandomStep == 1){
            didRandomStep = 0;
            if(myDirection == Direction.UP || myDirection == Direction.DOWN){
                if(getMyPosition(spacesInRange).getX() > foundCoordinate.getX())
                    return Direction.LEFT;
                else if (getMyPosition(spacesInRange).getX() < foundCoordinate.getX())
                    return Direction.RIGHT;
            }else if(myDirection == Direction.RIGHT || myDirection == Direction.LEFT){
                if (getMyPosition(spacesInRange).getY() > foundCoordinate.getY())
                    return Direction.UP;
                else if (getMyPosition(spacesInRange).getY() < foundCoordinate.getY())
                    return Direction.DOWN;
            }
            //Just go to foundCoordinate
        }else{       
            if (getMyPosition(spacesInRange).getX() < foundCoordinate.getX())
                return Direction.RIGHT;
            else if (getMyPosition(spacesInRange).getY() > foundCoordinate.getY())
                return Direction.UP;
            else if (getMyPosition(spacesInRange).getY() < foundCoordinate.getY())
                return Direction.DOWN;
            else if (getMyPosition(spacesInRange).getX() > foundCoordinate.getX())
                return Direction.LEFT;
    }
        return Direction.STAY;
    }

    //Returns the coordinate of the direction of movement
    public Coordinate moveInDirection(Coordinate coord, Direction direction) {
        switch (direction) {
            case UP: return new Coordinate(coord.getX(), coord.getY() - 1);
            case DOWN: return new Coordinate(coord.getX(), coord.getY() + 1);
            case LEFT: return new Coordinate(coord.getX() - 1, coord.getY());
            case RIGHT: return new Coordinate(coord.getX() + 1, coord.getY());
            default: return coord;
        }
    }

    //If one avatar hits another in their direction of movement, true must be returned.
    public boolean conflictDetected(ArrayList<SpaceInfo> spacesInRange, Direction direction) {
        Coordinate nextPosition = moveInDirection(getMyPosition(spacesInRange), direction);
        for (SpaceInfo space : spacesInRange) {
            if (space.getRelativeToAvatarCoordinate().equals(nextPosition) && space.getType() == SpaceType.AVATAR) {
            return true;
            }
        }
        return false;
    }

    /*
     * The yourTurn function consists of states for organizing the program sequence.
     */
    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {

        switch (state) {
            //All avatars move to the starting point (0,0) to start the saving process
            case 0:
                myDirection = goToStart(spacesInRange);
                
                //If another avatar is hit, a random step is executed
                if(conflictDetected(spacesInRange, myDirection) == true){
                    myDirection = doRandomStep();
                }
                break;
            //The avatars move up to (38,38). In the meantime, they store Data in allDataList
            case 1:
                storeDataInList(spacesInRange);
                myDirection = startMapWalkAndStore(spacesInRange);     
                myDirectionMoveBack = hitAvatarAndMoveAway(spacesInRange); 

                //If another avatar is hit, the avatar moves away and goes back again.
                if(myDirectionMoveBack != Direction.STAY)
                    return myDirectionMoveBack;
                break;
            
            //Duplicates are removed from the list and the data is written to SpaceType lists
            case 2:
                removeDuplicateCoordinates();
                createSpaceTypesLists(spacesInRange);
                state = 3;
                break;
            
            //The decision is made and the avatar goes to foundCoordinate
            case 3:
                myDirection = takingDecision(spacesInRange);
                //A hit avatar at foundCoordinate moves randomly and then continues to the target point.
                if(conflictDetected(spacesInRange, myDirection) == true){         
                    if(didRandomStep == 1){
                        goToFoundCoordinate(spacesInRange);
                    }
                    myDirection = doRandomStep();    
                }
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

/*
 * What worked:
 * - The avatars are moving the snake-like pattern and store all Data
 * - The avatars are able to move away in a Direction and then move back to original place
 * - They take a decision and moving to all SpaceTypes
 * - They take the shortest way to SpaceType
 * - If a SpaceType is already taken they choose the next close place of SpaceType
 * - They choose another SpaceType if seats are taken
 * - They dance on dancfloor
 * 
 * What not worked:
 * - If too many avatars go to (0.0), they can block each other despite the random step
 * - While the data is being saved, an avatar does not react to other avatars that stop. Then mine also stops
 * - It is better to store permanently than only in a one state. It is difficult to go back 
 *   to the old way in every test case.
 * - The decision system is boring
 * 
 * What is to improve:
 * - A pathfinding algorithm to safely reach the target coordinate
 * - All test cases must be secured
 * - store permanently for better design options
 */
