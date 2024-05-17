package Environment;

import java.awt.Color;
import java.util.ArrayList;

/**
 * The Environment class represents the environment in which the party
 * simulation takes place.
 * It manages the room, the simulation GUI, and the interactions between them.
 */
public class Environment {
    private Room model;
    private SimulationGUI view;

    /**
     * Constructs a new Environment object.
     * Initializes the room and the simulation GUI.
     */
    public Environment() {
        this.model = new Room(20, 20);
   
        System.out.println("Room constructed");
        this.view = new SimulationGUI();
        view.repaint();
        System.out.println("View constructed");
    }

    /**
     * Places an avatar in the room.
     * 
     * @param avatarID the ID of the avatar to be placed
     */
    public void placeAvatar(int avatarID) {
        Coordinate avatarCoordinate = model.findPlaceForAvatar(avatarID);
        if (avatarCoordinate != null) {
            view.paintAvatar(avatarCoordinate, Color.BLUE);
        }
        System.out.println("Placed Avatar with ID " + avatarID + " at X: " + avatarCoordinate.getX() + ", Y: " + avatarCoordinate.getY());
    }

    /**
     * Gets the adjacent spaces to an avatar within a given perception range.
     * 
     * @param avatarId        the ID of the avatar
     * @param perceptionRange the perception range of the avatar
     * @return an ArrayList of SpaceInfo objects representing the adjacent spaces
     */
    public ArrayList<SpaceInfo> getAdjacentToAvatar(int avatarId, int perceptionRange) {
        return model.getAdjacentToAvatar(avatarId, perceptionRange);
    }

    /**
     * Moves an avatar in the specified direction.
     * 
     * @param avatarID the ID of the avatar to be moved
     * @param dir      the direction in which to move the avatar
     * @return true if the avatar was successfully moved, false otherwise
     */
    // public boolean moveAvatar(int avatarID, Direction dir) {
    //     Coordinate currentPos = model.getAvatarLocation(avatarID);
    //     if (currentPos == null) {
    //         throw new IllegalArgumentException("Avatar " + avatarID + " does not exist in the room.");
    //     }
        

    //     switch (dir){
    //         case UP -> currentPos.setY(currentPos.getY() - 1);
    //         case DOWN -> currentPos.setY(currentPos.getY() + 1);
    //         case LEFT -> currentPos.setX(currentPos.getX() - 1);
    //         case RIGHT -> currentPos.setX(currentPos.getX() + 1);
    //         default -> {
    //         }

    //     }

    //     if (model.tryToPlaceAvatar(avatarID, currentPos)) {
    //         view.paintAvatar(currentPos, Color.BLUE);
    //         return true;
    //     }
    //     return false;
    // }
    public boolean moveAvatar(int avatarID, Direction dir) {
        Coordinate currentPos = model.getAvatarLocation(avatarID);
        if (currentPos == null) {
            throw new IllegalArgumentException("Avatar " + avatarID + " does not exist in the room.");
        }
    
        // Save the current position
        int oldX = currentPos.getX();
        int oldY = currentPos.getY();
    
        // Update the coordinates based on the direction
        switch (dir) {
            case UP -> currentPos.setY(currentPos.getY() - 1);
            case DOWN -> currentPos.setY(currentPos.getY() + 1);
            case LEFT -> currentPos.setX(currentPos.getX() - 1);
            case RIGHT -> currentPos.setX(currentPos.getX() + 1);
            default -> {
                return false;
            }
        }
    
        // Check if the new position is valid and try to place the avatar there
        if (model.tryToPlaceAvatar(avatarID, currentPos)) {
            // Erase the avatar from the old position
            view.eraseAvatar(new Coordinate(oldX, oldY));
    
            // Paint the avatar at the new position
            view.paintAvatar(currentPos, Color.GREEN);
            
            return true;
        } else {
            // If placing failed, revert the position change
            currentPos.setX(oldX);
            currentPos.setY(oldY);
            return false;
        }
    }
    
}
