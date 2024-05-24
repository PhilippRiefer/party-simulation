package PersonalAvatars;

import java.util.ArrayList;

import AvatarInterface.SuperAvatar;
import Environment.Direction;
import Environment.SpaceInfo;
import Environment.SpaceType;
import java.awt.Color;
import java.awt.List;

public class MaximSpockAvatar extends SuperAvatar {
    // internal stats of Mr. Spock
    final private int MAXSTAT = 200;
    final private int MINSTAT = 0;
    final private int THRESHOLD = 30;
    int energy = MAXSTAT;
    int hydration = MAXSTAT;
    int bowelMovement = MINSTAT;
    int urination = MINSTAT;
    int countedTurns = MINSTAT;
    // internal memory of Mr. Spock
    SpaceType currentObjective = SpaceType.EMPTY;       // current Objective to move to for the Avatar
    SpaceType[][] clubMemory= new SpaceType[40][20];    // Mr. Spocks internal map of the Environment  
    // internal variables for movement
    boolean scouting = true;    // scouting for first *100 turns
    boolean hasPreComputed = false;
    boolean hasWaitedOneTurn = false;
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
        saveSpacesInRange(spacesInRange);                   // 1. step is to save Environment in memory
        if(countedTurns > 99){
            scouting = false;
        } 

        currentObjective = decideNextMovement();
        if(currentObjective == SpaceType.EMPTY){            // choosing next Objective to move to
            scouting = true;
        }

        if(scouting){                                       // moving to nearest empty space
            updateStats(); 
            return moveTo(clubMemory, null);
        }
        else{                                               // go after needs
            updateStats();                                  // stats are updated each turn (getting thirstier for e.g.)
            return moveTo(clubMemory, currentObjective);    // returning next chosen direction to SimulationControl
        }
        
    }

    public Direction moveTo(SpaceType[][] clubMemory, SpaceType currentObjective){
        // 1. check for unchecked spaces in memory
        // 2. compute 
        int i = 0;

        if(!hasPreComputed) {
            listOfDirections = preComputeDirection(clubMemory, currentObjective);
            hasPreComputed = true;
            return listOfDirections.get(i++);
        }   
        else {
            return listOfDirections.get(i++);
        }
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
