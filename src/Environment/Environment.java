package Environment;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Environment class represents the environment in which the party
 * simulation takes place.
 * It manages the room, the simulation GUI, and the interactions between them.
 */
public class Environment {
    private Room model;
    private SimulationGUI view;
    private ArrayList<Coordinate> wallCoordinates;
    private int currentWallIndex = 0;
    private Color wallAnimationColor = Color.YELLOW; // The color of the moving light

    /**
     * Constructs a new Environment object.
     * Initializes the room and the simulation GUI.
     */
    public Environment() {
        this.model = new Room(40, 20);
        System.out.println("Room constructed");
        this.view = new SimulationGUI();
        view.repaint();
        System.out.println("View constructed");
        paintBar();
        paintDJBooth();
        paintWall();
        paintDancefloor();
        paintToilet();
        paintSeats();
        model.createBlueprint();
        startWallAnimation();
    }

    public void setSpaceType(int x, int y, SpaceType spaceType) {
        // Set the space type in the room model
        try {
            model.setSpace(new Coordinate(x, y), spaceType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void paintSeats() {
        for (int x = 32; x <= 37; ++x) {
            for (int y = 2; y <= 4; ++y) {
                view.paintComponent(x, y, Color.CYAN);
                setSpaceType(x, y, SpaceType.SEATS);
            }
        }
        for (int x = 2; x <= 5; ++x) {
            for (int y = 15; y <= 17; ++y) {
                view.paintComponent(x, y, Color.CYAN);
                setSpaceType(x, y, SpaceType.SEATS);
            }
        }
    }

    public void paintBar() {
        for (int x = 2; x <= 3; ++x) {
            for (int y = 7; y <= 12; ++y) {
                view.paintComponent(x, y, Color.BLACK);
                setSpaceType(x, y, SpaceType.BAR);
            }
        }
        for (int x = 36; x <= 37; ++x) {
            for (int y = 7; y <= 12; ++y) {
                view.paintComponent(x, y, Color.BLACK);
                setSpaceType(x, y, SpaceType.BAR);
            }
        }
    }

    public void paintDancefloor() {
        for (int x = 13; x <= 21; ++x) {
            for (int y = 6; y <= 14; ++y) {
                view.paintComponent(x, y, Color.YELLOW);
                setSpaceType(x, y, SpaceType.DANCEFLOOR);
            }
        }
    }

    public void paintDJBooth() {
        for (int x = 14; x <= 20; ++x) {
            for (int y = 2; y <= 3; ++y) {
                view.paintComponent(x, y, Color.PINK);
                setSpaceType(x, y, SpaceType.DJBOOTH);
            }
        }
    }

    public void paintToilet() {
        for (int x = 34; x <= 37; ++x) {
            view.paintComponent(x, 18, Color.GREEN);
            setSpaceType(x, 18, SpaceType.TOILET);
        }
    }
    /* 
     * wallCoordinates: an array to store the coordinates of the wall sections.
     */
    public void paintWall() {
        wallCoordinates = new ArrayList<>();
        for (int x = 0; x < 40; ++x) {
            view.paintComponent(x, 0, Color.LIGHT_GRAY);
            view.paintComponent(x, 19, Color.LIGHT_GRAY);
            setSpaceType(x, 0, SpaceType.OBSTACLE);
            setSpaceType(x, 19, SpaceType.OBSTACLE);
            wallCoordinates.add(new Coordinate(x, 0));
            wallCoordinates.add(new Coordinate(x, 19));
        }
        for (int y = 1; y < 19; ++y) {
            view.paintComponent(0, y, Color.LIGHT_GRAY);
            view.paintComponent(39, y, Color.LIGHT_GRAY);
            setSpaceType(0, y, SpaceType.OBSTACLE);
            setSpaceType(39, y, SpaceType.OBSTACLE);
            wallCoordinates.add(new Coordinate(0, y));
            wallCoordinates.add(new Coordinate(39, y));
        }
    }

    /**
     * Starts the animation for the wall.
     * initializes a Timer that calls the animateWall method every 100 milliseconds
     * @param timer  to periodically update the colors of the wall sections.
     */
    public void startWallAnimation() {
        Timer timer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animateWall();
            }
        });
        timer.start();
    }

    /**
     * changes the color of wall sections in a sequence to create the effect of a moving light
     * @param wallCoordinates  a list that stores the coordinates of all wall sections to be animated.
     */
    public void animateWall() {
        // Reset the previous wall section to gray
        if (currentWallIndex > 0) {
            Coordinate prevCoordinate = wallCoordinates.get((currentWallIndex - 1) % wallCoordinates.size());
            view.paintComponent(prevCoordinate.getX(), prevCoordinate.getY(), Color.LIGHT_GRAY);
        }

        // Set the current wall section to the animation color
        Coordinate currentCoordinate = wallCoordinates.get(currentWallIndex % wallCoordinates.size());
        view.paintComponent(currentCoordinate.getX(), currentCoordinate.getY(), wallAnimationColor);

        // Move to the next wall section
        currentWallIndex++;
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
     * @throws Exception
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
                System.out.println("Failed to set space to oldSpaceType at " + oldX + ", " + oldY + ".");
            }

            // Erase the avatar from the old position
            try {
                model.setSpace(new Coordinate(oldX, oldY), SpaceType.EMPTY);
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
