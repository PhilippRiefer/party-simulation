package Environment;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

/**
 * authors: Paola, Ole, Soodeh
 * 
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
        this.model = new Room(40, 20);
        // System.out.println("Room constructed");
        this.view = new SimulationGUI();
        view.repaint();
        // System.out.println("View constructed");
        paintBar();
        paintDJBooth();
        paintWall();
        paintDancefloor();
        paintToilet();
        paintSeats();
        //paintRandomAvatar();
        model.createBlueprint();
    }

    public void setSpaceType(int x, int y, SpaceType spaceType) {
        // Set the space type in the room model
        try {
            model.setSpace(new Coordinate(x, y), spaceType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void paintSeats(){
        Random random = new Random();
        for(int i = 0; i < 4; ++i){
            int randomX = random.nextInt(1, 6);
            int randomY = random.nextInt(14, 19);
            view.paintComponent(randomX, randomY, new Color(255, 245, 157  ));
            setSpaceType(randomX, randomY, SpaceType.SEATS);

            randomX = random.nextInt(31, 38);
            randomY = random.nextInt(1, 6);
            view.paintComponent(randomX, randomY, new Color(255, 245, 157  ));
            
            setSpaceType(randomX, randomY, SpaceType.SEATS);
        }
    }

    public void paintRandomAvatar(){
        Random random = new Random();
        for(int i = 0; i < 3; ++i){
            int randomX = random.nextInt(3, 10);
            int randomY = random.nextInt(2, 17);
            view.paintComponent(randomX, randomY, new Color(174, 214, 241  ));
            setSpaceType(randomX, randomY, SpaceType.AVATAR);

            randomX = random.nextInt(23, 30);
            randomY = random.nextInt(2, 17);
            view.paintComponent(randomX, randomY, new Color(174, 214, 241  ));
            
            setSpaceType(randomX, randomY, SpaceType.AVATAR);
        }
    }

    public void paintBar(){
        for(int x = 1; x <= 2; ++x){
            for( int y = 7; y <= 12; ++y){
                view.paintComponent(x, y, new Color(161, 136, 127 ));
                setSpaceType(x, y, SpaceType.BAR);
            }
        }
        for(int x = 37; x <= 38; ++x){
            for( int y = 7; y <= 12; ++y){
                view.paintComponent(x, y, new Color(161, 136, 127  ));
                setSpaceType(x, y, SpaceType.BAR);
            }
        }
    }
    public void paintDancefloor(){
        for(int x = 13; x <= 21; ++x){
            for( int y = 4; y <= 12; ++y){
                view.paintComponent(x, y, new Color(209, 242, 235));
                setSpaceType(x, y, SpaceType.DANCEFLOOR);
            }
        }
    }
    public void paintDJBooth(){
        for(int x = 14; x <= 20; ++x){
            for( int y = 1; y <= 2; ++y){
                view.paintComponent(x, y, new Color(206, 147, 216 ));
                setSpaceType(x, y, SpaceType.DJBOOTH);
            }
        }
    }
    public void paintToilet(){
        for(int x = 35; x <= 38; ++x){
            for(int y = 17; y <= 18; ++y){
            view.paintComponent(x,y, new Color(79, 195, 247));
            setSpaceType(x, y, SpaceType.TOILET);

            }
            
        }
    }
    public void paintWall(){
        for(int x = 0; x < 40; ++x){
            view.paintComponent(x, 0, Color.GRAY);
            view.paintComponent(x, 19, Color.GRAY);
            setSpaceType(x, 0, SpaceType.OBSTACLE);
            setSpaceType(x, 19, SpaceType.OBSTACLE);
        }
        for(int x = 24; x < 30; ++x){
            view.paintComponent(x, 2, Color.GRAY);
            view.paintComponent(x, 3, Color.GRAY);
            view.paintComponent(x, 4, Color.GRAY);
            view.paintComponent(x, 5, Color.GRAY);
            view.paintComponent(x, 6, Color.GRAY);
            view.paintComponent(x, 7, Color.GRAY);
            view.paintComponent(x, 8, Color.GRAY);
            setSpaceType(x, 2, SpaceType.OBSTACLE);
            setSpaceType(x, 3, SpaceType.OBSTACLE);
            setSpaceType(x, 4, SpaceType.OBSTACLE);
            setSpaceType(x, 5, SpaceType.OBSTACLE);
            setSpaceType(x, 6, SpaceType.OBSTACLE);
            setSpaceType(x, 7, SpaceType.OBSTACLE);
            setSpaceType(x, 8, SpaceType.OBSTACLE);
        }
        for(int x = 0; x < 40; ++x){
            view.paintComponent(x, 10, Color.GRAY);
            setSpaceType(x, 10, SpaceType.OBSTACLE);
        }
        for(int x = 3; x < 12; ++x){
            view.paintComponent(x, 13, Color.GRAY);
            setSpaceType(x, 13, SpaceType.OBSTACLE);
        }
        for(int y = 1; y < 19; ++y){
            view.paintComponent(0, y, Color.GRAY);
            view.paintComponent(39, y, Color.GRAY);
            setSpaceType(0, y, SpaceType.OBSTACLE);
            setSpaceType(39, y, SpaceType.OBSTACLE);
        }
        for(int y = 2; y < 8; ++y){
            view.paintComponent(24, y, Color.GRAY);
            view.paintComponent(29, y, Color.GRAY);
            setSpaceType(24, y, SpaceType.OBSTACLE);
            setSpaceType(29, y, SpaceType.OBSTACLE);
        }
        for(int y = 13; y < 19; ++y){
            view.paintComponent(31, y, Color.GRAY);
            setSpaceType(31, y, SpaceType.OBSTACLE);
        }
        for(int y = 14; y < 19; ++y){
            view.paintComponent(11, y, Color.GRAY);
            setSpaceType(11, y, SpaceType.OBSTACLE);
        }
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
        // System.out.println("Placed Avatar with ID " + avatarID + " at X: " + avatarCoordinate.getX() + ", Y: " + avatarCoordinate.getY());
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
     * @param color    the color of the avatar
     * @return true if the avatar was successfully moved, false otherwise
     */
    public boolean moveAvatar(int avatarID, Direction dir, Color color) {
        Coordinate currentPos = model.getAvatarLocation(avatarID);
        if (currentPos == null) {
            throw new IllegalArgumentException("Avatar " + avatarID + " does not exist in the room.");
        }
    
        // Save the current position
        int oldX = currentPos.getX();
        int oldY = currentPos.getY();

        // Store original spacetype at current Avatar's position
        SpaceType oldSpaceType = model.getOriginalSpace(currentPos);
    
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
            try {
                model.setSpace(new Coordinate(oldX, oldY), oldSpaceType);
            } catch (Exception e) {
                // System.out.println("Failed to set space to oldSpaceType at " + oldX + ", " + oldY + ".");
            }

            // Erase the avatar from the old position
            try {
                model.setSpace(new Coordinate(oldX, oldY), oldSpaceType);
                if (oldSpaceType == SpaceType.EMPTY) {
                    model.setSpace(new Coordinate(oldX, oldY), SpaceType.EMPTY);
                }
                else{
                    model.setSpace(new Coordinate(oldX, oldY), oldSpaceType);
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            view.eraseAvatar(new Coordinate(oldX, oldY), oldSpaceType);
    
            // Paint the avatar at the new position
            view.paintAvatar(currentPos, color);
            
            return true;
        } else {
            // If placing failed, revert the position change
            currentPos.setX(oldX);
            currentPos.setY(oldY);
            return false;
        }
    }
    
}
