package PersonalAvatars;

import java.util.ArrayList;

import AvatarInterface.SuperAvatar;
import Environment.Direction;
import Environment.Environment;
import Environment.SpaceInfo;
import Environment.SpaceType;
import java.awt.Color;

public class MaximSpockAvatar extends SuperAvatar {
    int energy = 100;
    int hydration = 100;
    int bowelMovement = 0;
    int urination = 0;
    SpaceType currentObjective = SpaceType.EMPTY; // current Objective to move to for the Avatar
    SpaceType[][] clubMemory= new SpaceType[40][20];

    // perception Range should be higher than of a human!
    public MaximSpockAvatar(int id, int perceptionRange) {
        super(id, perceptionRange);
    }

    /**
     * Determines the direction for the avatar's next turn based on the spaces
     * within its perception range.
     *
     * @return the direction for the avatar's next turn
     */
    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        saveSpacesInRange(spacesInRange);           // 1. step is to save Environment in memory
        if(currentObjective == SpaceType.EMPTY){    // deciding for next Object to move to
            decideNextMovement();      // next move is decided based on internal stats
        }
        updateStats();                              // stats are updated each turn (getting thirstier for e.g.)

        return moveTo(spacesInRange, currentObjective); // returning next chosen direction to SimulationControl
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

    public Direction moveTo(ArrayList<SpaceInfo> spacesInRange, SpaceType currentObjective){
        return Direction.STAY;
    }

    public SpaceType decideNextMovement(){
        if (bowelMovement > 25 || urination > 25) {
            return currentObjective = SpaceType.TOILET;
        } else if (hydration < 25) {
            return currentObjective = SpaceType.BAR;
        } else if (energy < 25) {
            return currentObjective = SpaceType.SEATS;
        }
        else{
            return currentObjective = SpaceType.EMPTY;
        }
    }

    public void updateStats() {
        energy = energy - 1;
        hydration = hydration - 1;
        bowelMovement = bowelMovement + 1;
        urination = urination + 1;

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
