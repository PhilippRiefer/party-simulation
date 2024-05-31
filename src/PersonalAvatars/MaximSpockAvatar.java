package PersonalAvatars;

import java.lang.Math;
import java.util.ArrayList;
import AvatarInterface.SuperAvatar;
import Environment.Coordinate;
import Environment.Direction;
import Environment.SpaceInfo;
import Environment.SpaceType;
import java.awt.Color;
import java.awt.List;

public class MaximSpockAvatar extends SuperAvatar {
    final private int MAXSTAT = 200;
    final private int MINSTAT = 0;
    final private int THRESHOLD = 30;
    final private int XCOORDINATEMAX = 40;
    final private int YCOORDINATEMAX = 20;
    // internal stats of Mr. Spock
    int energy = MAXSTAT;
    int hydration = MAXSTAT;
    int bowelMovement = MINSTAT;
    int urination = MINSTAT;
    int countedTurns = MINSTAT;
    // internal memory of Mr. Spock
    SpaceType currentObjective = SpaceType.EMPTY;       // current Objective to move to for the Avatar
    SpaceType[][] clubMemory= new SpaceType[XCOORDINATEMAX][YCOORDINATEMAX];    // Mr. Spocks internal map of the Environment  
    // internal variables for movement
    boolean isScouting = true;
    boolean notMovingToObjective = true;
    boolean hasPreComputed = false;
    boolean hasWaitedOneTurnForOtherAvatarToLeave = false;
    int stepOfTheGivenDirectionList = 0;
    ArrayList<Direction> listOfDirections = new ArrayList<Direction>();

    // perception Range should be higher than of a human!
    public MaximSpockAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);
    }

    /**
     * Determines the direction for the avatar's next turn based on the spaces
     * within its perception range.
     *
     * @return the direction for the avatar's next turn
     */
    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        Coordinate personalCoordinates = spacesInRange.get(1 + (int)Math.pow(2, this.getPerceptionRange())).getRelativeToAvatarCoordinate();

        saveSpacesInRange(spacesInRange);                   // 1. step is to save Environment in memory
        if(countedTurns > 99 && notMovingToObjective){                              // 2. if counted Turns > 99 -> scouting not as default
            currentObjective = decideNextMovement();
        } 
        updateStats();                                      // stats are updated each turn (getting thirstier for e.g.)
        return moveTo(clubMemory, currentObjective, personalCoordinates);    // returning next chosen direction to SimulationControl
        
    }

    public Direction moveTo(SpaceType[][] clubMemory, SpaceType currentObjective, Coordinate personalCoordinates){
        if(!hasPreComputed || hasWaitedOneTurnForOtherAvatarToLeave == true) { // definitely preComputing path to Objective
            listOfDirections = preComputeDirection(clubMemory, currentObjective, personalCoordinates);
            hasPreComputed = true;
        }   
        return listOfDirections.get(stepOfTheGivenDirectionList++);
    }

    ArrayList<Direction> preComputeDirection(SpaceType[][] clubMemory, SpaceType currentObjective, Coordinate personalCoordinates) {
        ArrayList<Direction> listOfDirectionsToObjective = new ArrayList<Direction>();
        Coordinate objectiveCoordinates;

        // find cell that is objective in Memory
        objectiveCoordinates = findObjectiveInMemory(clubMemory, currentObjective, personalCoordinates);

        if (objectiveCoordinates == personalCoordinates) { // found nothing in Memory -> scout Empty Spaces
            objectiveCoordinates = findObjectiveInMemory(clubMemory, SpaceType.EMPTY , personalCoordinates);
        }
        // compute Route

        return listOfDirectionsToObjective;
    }

    Coordinate findObjectiveInMemory(SpaceType[][] clubMemory, SpaceType currentObjective, Coordinate personalCoordinates) {
         // 1. while(SpaceType[x][y] != currentObjective) -> keep looking
        // 2. start with avatar as center
        // 3. start checking for OBjective right from avatar
        // order of searching == right, down, left, up, right,....
        // mathematicaly one to right, one down, two left, two up, three right...
        // so amount of steps is +1 for each third turn right(1+2*i)

        Coordinate objectiveCoordinates = new Coordinate(personalCoordinates.getX(), personalCoordinates.getY());
        int x = 0;
        int y = 1;
        while (clubMemory[personalCoordinates.getX() + x][personalCoordinates.getY() + y] != currentObjective) {
            if(x != XCOORDINATEMAX && ()){
                
            }
            if(y != YCOORDINATEMAX){

            }
        }
        return objectiveCoordinates;
    }

    // saves latest Information on Environment
    public void saveSpacesInRange(ArrayList<SpaceInfo> spacesInRange){
        int i = 0;
        while(i < spacesInRange.size()){
            int clubMemoryCoordinateX = 0;
            int clubMemoryCoordinateY = 0;
            clubMemoryCoordinateX = spacesInRange.get(i).getRelativeToAvatarCoordinate().getX();
            clubMemoryCoordinateY = spacesInRange.get(i).getRelativeToAvatarCoordinate().getY();
            clubMemory[clubMemoryCoordinateX][clubMemoryCoordinateY] = spacesInRange.get(i).getType();  
        }
        return;
    }

    public SpaceType decideNextMovement(){
        if (bowelMovement > (200 - THRESHOLD) || urination > (200 - THRESHOLD)) {
            return currentObjective = SpaceType.TOILET;
        } else if (hydration < THRESHOLD) {
            return currentObjective = SpaceType.BAR;
        } else if (energy < THRESHOLD) {
            return currentObjective = SpaceType.SEATS;
        }
        else{
            isScouting = true;
            return currentObjective = SpaceType.EMPTY;
        }
    }

    public void updateStats() {
        energy--;
        hydration--;
        bowelMovement++;
        urination++;
        countedTurns++;

        return;
    }

    /**
     * Generates a random direction for the avatar to move.
     *
     * @return a random direction
     */
    private Direction randomDirection() {
        int directionNumber = (int) (Math.random() * 4);

        switch (directionNumber) {
            case 0:
                return Direction.LEFT;
            case 1:
                return Direction.RIGHT;
            case 2:
                return Direction.UP;
            case 3:
                return Direction.DOWN;
            default:
                return Direction.STAY; // Safety net, though unnecessary as directionNumber is bound by 0-3
        }
    }

    /**
     * Returns the perception range of the avatar.
     *
     * @return the perception range
     */
    @Override
    public int getPerceptionRange() {
        return super.getPerceptionRange(); // Assuming SuperAvatar has a method to get the perception range
    }

    /**
     * Sets the perception range of the avatar.
     *
     * @param perceptionRange the perception range to set
     */
    @Override
    public void setPerceptionRange(int perceptionRange) {
        super.setPerceptionRange(perceptionRange); // Set the perception range via the superclass method
    }

}
