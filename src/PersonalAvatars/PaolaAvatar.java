package PersonalAvatars;

import AvatarInterface.SuperAvatar;
import Environment.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Paola's avatar
 */
public class PaolaAvatar extends SuperAvatar {

    // Constants for energy levels
    final private int ENERGY_MAX = 2400;
    final private int ENERGY_LOW_WARNING = 50;
    final private int ENERGY_MIN = 0;
    final private int ENERGY_LOSS_PER_TURN = 1;
    final private int ENERGY_INCREMENT_PER_TURN_IN_BAR = 500;

    // Constants for bladder levels
    final private int FULL_BLADDER = 2000;
    final private int BLADDER_HIGH_WARNING = 900;
    final private int EMPTY_BLADDER = 0;
    final private int BLADDER_FILL_PER_TURN = 10;
    final private int BLADDER_EMPTYING_PER_TURN_IN_TOILET = 200;

    // Constants for social interaction levels
    final private int SOCIAL_MAX = 1000;
    final private int SOCIAL_MIN = 0;
    final private int SOCIAL_INCREASE_PER_TURN = 10;

    // Constants for fun levels
    final private int FUN_MAX = 1000;
    final private int FUN_MIN = 0;
    final private int FUN_DECREMENT_PER_TURN = 10;
    final private int FUN_INCREMENT_PER_TURN_IN_DANCEFLOOR_OR_DJBOOTH = 5;

    // Grid dimensions
    final private int ROWS = 20;
    final private int COLUMNS = 40;

    private SpaceType[][] internalMap = new SpaceType[ROWS][COLUMNS];
    private boolean internalMapIsComplete = false;
    private boolean hasGoalInMind = false;

    Coordinate currentAvatarLocation = null;
    private int energy;
    private int bladder;
    private int social;
    private int fun;

    private Plan plan;
    private boolean internalMapHasAllElements = false;

    /**
     * Inner class representing the plan of the avatar
     */
    private class Plan {
        private SpaceType target;
        private Queue<Coordinate> queue = new LinkedList<>();
        private Boolean reachedEnd = false;
        private Boolean[][] explored = new Boolean[ROWS][COLUMNS];
        private int[] directionEastWest = new int[]{-1, 1, 0, 0};
        private int[] directionNorthSouth = new int[]{0, 0, 1, -1};

        boolean asumptionHasBeenMade = false;
        Coordinate targetCoordinate;

        private Coordinate[][] prev = new Coordinate[ROWS][COLUMNS];
        private LinkedList<Coordinate> route = new LinkedList<>();

        Plan() {
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLUMNS; j++) {
                    explored[i][j] = false;
                }
            }
        }

        /**
         * Calculates the route to the target using BFS
         */
        void calculateRoute() {
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLUMNS; j++) {
                    explored[i][j] = false;
                    prev[i][j] = null;
                }
            }

            queue.clear();
            queue.add(currentAvatarLocation);
            explored[currentAvatarLocation.getY()][currentAvatarLocation.getX()] = true;

            Coordinate endCoordinate = null;
            while (queue.size() > 0) {
                Coordinate coordinateToExplore = queue.remove();
                if (internalMap[coordinateToExplore.getY()][coordinateToExplore.getX()] == target) {
                    endCoordinate = coordinateToExplore;
                    break;
                }
                exploreNeighbors(coordinateToExplore);
            }

            if (endCoordinate != null) {
                reconstructRoute(currentAvatarLocation, endCoordinate);
            }
        }

        /**
         * Calculates the route to the given target coordinate using BFS
         * @param targetCoordinate the target coordinate to reach
         */
        private void calculateRoute(Coordinate targetCoordinate) {
            reachedEnd = false;
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLUMNS; j++) {
                    explored[i][j] = false;
                    prev[i][j] = null;
                }
            }
            queue.clear();
            queue.add(currentAvatarLocation);
            explored[currentAvatarLocation.getY()][currentAvatarLocation.getX()] = true;

            while (queue.size() > 0) {
                Coordinate coordinateToExplore = queue.remove();
                if (coordinateToExplore.equals(targetCoordinate)) {
                    reachedEnd = true;
                    break;
                }
                exploreNeighbors(coordinateToExplore);
            }
            if (reachedEnd) {
                reconstructRoute(currentAvatarLocation, targetCoordinate);
            }
        }

        /**
         * Reconstructs the route from the start coordinate to the end coordinate
         * @param startCoordinate the start coordinate
         * @param endCoordinate the end coordinate
         */
        private void reconstructRoute(Coordinate startCoordinate, Coordinate endCoordinate) {
            route.add(endCoordinate);
            Coordinate at = prev[endCoordinate.getY()][endCoordinate.getX()];
            while (at != null) {
                route.add(at);
                at = prev[at.getY()][at.getX()];
            }
            Collections.reverse(route);
        }

        /**
         * Explores the neighboring coordinates of the given coordinate
         * @param coordinate the coordinate to explore neighbors of
         */
        private void exploreNeighbors(Coordinate coordinate) {
            for (int i = 0; i < 4; i++) {
                int neighboringRow = coordinate.getY() + directionEastWest[i];
                int neighboringColumn = coordinate.getX() + directionNorthSouth[i];

                if (neighboringRow < 0 || neighboringColumn < 0) continue;
                if (neighboringRow >= ROWS || neighboringColumn >= COLUMNS) continue;

                // skip invalid coordinates
                if (explored[neighboringRow][neighboringColumn]) continue;
                if (internalMap[neighboringRow][neighboringColumn] == SpaceType.OBSTACLE) continue;
                Coordinate coordinateToExploreLater = new Coordinate(neighboringColumn, neighboringRow);
                if (internalMap[neighboringRow][neighboringColumn] == SpaceType.AVATAR 
                    && coordinateIsAdjacentToCurrentLocation(coordinateToExploreLater)) continue;

                queue.add(coordinateToExploreLater);
                explored[neighboringRow][neighboringColumn] = true;

                prev[neighboringRow][neighboringColumn] = coordinate;
            }
        }

        /**
         * Checks if the given coordinate is adjacent to the current location
         * @param coordinateToExploreLater the coordinate to check
         * @return true if the coordinate is adjacent, false otherwise
         */
        private boolean coordinateIsAdjacentToCurrentLocation(Coordinate coordinateToExploreLater) {
            for (int i = 0; i < 4; i++) {
                int neighboringRow = coordinateToExploreLater.getY() + directionEastWest[i];
                int neighboringColumn = coordinateToExploreLater.getX() + directionNorthSouth[i];
                Coordinate neighboringCoordinate = new Coordinate(neighboringColumn, neighboringRow);
                if (neighboringCoordinate.equals(currentAvatarLocation)){
                    return true;
                }
            }
            return false;
        }

        /**
         * Makes an assumption about the target's location if the map is incomplete
         * @return the assumed target coordinate
         */
        private Coordinate makeAssumption() {
            if (asumptionHasBeenMade) {
                return targetCoordinate;
            }
            
            int ranX;
            int ranY;
            
            if (target == SpaceType.TOILET) {
                int startX = (int) (COLUMNS * 0.75); // Start at 75% of the grid width
                int startY = (int) (ROWS * 0.75); // Start at 75% of the grid height
                ranX = startX + (int) (Math.random() * (COLUMNS - startX - 2)); // between startX and COLUMNS - 1
                ranY = startY + (int) (Math.random() * (ROWS - startY - 2)); // between startY and ROWS - 1
            } else {
                ranX = 1 + (int) (Math.random() * (COLUMNS - 2)); // between 1 and COLUMNS - 1
                ranY = 1 + (int) (Math.random() * (ROWS - 2)); // between 1 and ROWS - 1
            }
            
            targetCoordinate = new Coordinate(ranX, ranY);
            asumptionHasBeenMade = true;
            return targetCoordinate;
        }

        /**
         * Gets the next milestone on the route to the target
         * @return the next milestone coordinate
         */
        Coordinate nextMilestone() {
            route.clear();
            if (!internalMapHasAllElements) {
                targetCoordinate = makeAssumption();
                calculateRoute(targetCoordinate);
            } else if (internalMapHasAllElements) {
                calculateRoute();
            }

            Coordinate next;

            if (route.isEmpty()) {
                int randX = (int) (Math.random() * 2);
                int randY = (int) (Math.random() * 2);
                return currentAvatarLocation.add(new Coordinate(randX, randY)); // Go to a random place if no route is found
            }

            do {
                next = route.remove();
            } while (currentAvatarLocation.equals(next));

            return next;
        }
    }

    /**
     * Constructs a PaolaAvatar object with the specified ID and perception range.
     *
     * @param id              the ID of the avatar
     * @param perceptionRange the perception range of the avatar
     * @param color           the color of the avatar
     */
    public PaolaAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, Color.decode("#8b1a93"));
        Random random = new Random();
        energy = random.nextInt(ENERGY_LOW_WARNING, ENERGY_MAX);
        bladder = 0;
        social = random.nextInt(SOCIAL_MIN, SOCIAL_MAX);
        fun = random.nextInt(FUN_MIN, FUN_MAX);
    }

    /**
     * Determines the direction for the avatar's next turn based on the spaces within its perception range.
     * This method can be overridden to implement a more sophisticated strategy.
     *
     * @param spacesInRange the list of spaces within the avatar's perception range
     * @return the direction for the avatar's next turn
     */
    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        currentAvatarLocation = calculateCurrentLocation(spacesInRange.get(1));

        updateInternalMap(spacesInRange);
        updateNeeds();
        updateColorBasedOnNeeds();

        if (plan != null && (internalMap[currentAvatarLocation.getY()][currentAvatarLocation.getX()] == plan.target || currentAvatarLocation.equals(plan.targetCoordinate))) {
            plan = null;
        }

        if (plan != null) {
            if (energy > ENERGY_LOW_WARNING) {
                if (bladder < BLADDER_HIGH_WARNING) {
                    return calculateDirection(plan.nextMilestone(), currentAvatarLocation);
                } else {
                    plan.target = SpaceType.TOILET;
                    return calculateDirection(plan.nextMilestone(), currentAvatarLocation);
                }
            } else {
                plan.target = SpaceType.BAR;
                plan.calculateRoute();
                return calculateDirection(currentAvatarLocation, plan.nextMilestone());
            }
        } else {
            chooseAGoal();
        }

        // System.out.println("Internal map");
        // printMap(internalMap);
        // System.out.println();
        return randomDirection();
    }

    /**
     * Updates the avatar's needs based on its current location
     */
    private void updateNeeds() {
        if (internalMap[currentAvatarLocation.getY()][currentAvatarLocation.getX()] != null) {
            switch (internalMap[currentAvatarLocation.getY()][currentAvatarLocation.getX()]) {
                case TOILET:
                    bladder = EMPTY_BLADDER;
                    break;
                case DJBOOTH:
                case DANCEFLOOR:
                    fun = FUN_MAX;
                    break;
                case BAR:
                case SEATS:
                    energy = ENERGY_MAX;
                    break;
                default:
                    energy = Math.max(ENERGY_MIN, energy - ENERGY_LOSS_PER_TURN);
                    bladder = Math.min(FULL_BLADDER, bladder + BLADDER_FILL_PER_TURN);
                    fun = Math.max(FUN_MIN, fun - FUN_DECREMENT_PER_TURN);
                    break;
            }
        } else {
            energy = Math.max(ENERGY_MIN, energy - ENERGY_LOSS_PER_TURN);
            bladder = Math.min(FULL_BLADDER, bladder + BLADDER_FILL_PER_TURN);
            fun = Math.max(FUN_MIN, fun - FUN_DECREMENT_PER_TURN);
        }
    }

    /**
     * Updates the avatar's color based on its current needs
     */
    private void updateColorBasedOnNeeds() {
        if (bladder >= BLADDER_HIGH_WARNING && energy <= ENERGY_LOW_WARNING) {
            this.color = Color.decode("#7a5901"); // ugly brown
        } else if (energy <= ENERGY_LOW_WARNING) {
            this.color = Color.RED;
        } else if (bladder >= BLADDER_HIGH_WARNING) {
            this.color = Color.decode("#89A203"); // ugly green
        } else {
            this.color = Color.decode("#8b1a93"); // Default color
        }
    }

    /**
     * Chooses a goal based on the avatar's current needs
     */
    private void chooseAGoal() {
        int funWeight = FUN_MAX - fun;
        int barWeight = (ENERGY_MAX - energy) / 8;
    
        int totalWeight = funWeight + barWeight;
    
        int randomWeight = (int) (Math.random() * totalWeight);
    
        if (randomWeight < funWeight) {
            plan = new Plan();
            if (Math.random() < 0.5) {
                plan.target = SpaceType.DANCEFLOOR;
            } else {
                plan.target = SpaceType.DJBOOTH;
            }
        } else {
            plan = new Plan();
            if (Math.random() < 0.5) {
                plan.target = SpaceType.SEATS;
            } else {
                plan.target = SpaceType.BAR;
            }
            
        }
    }

    /**
     * Calculates the current location of the avatar based on the given space info
     * @param spaceInfo the space info relative to the avatar
     * @return the current location of the avatar
     */
    private Coordinate calculateCurrentLocation(SpaceInfo spaceInfo) {
        Coordinate leftToAvatar = spaceInfo.getRelativeToAvatarCoordinate();
        return new Coordinate(leftToAvatar.getX() + 1, leftToAvatar.getY());
    }

    /**
     * Calculates the direction to move from the current location to the next milestone
     * @param nextMilestone the next milestone coordinate
     * @param currentAvatarCoordinate the current avatar coordinate
     * @return the direction to move
     */
    private Direction calculateDirection(Coordinate nextMilestone, Coordinate currentAvatarCoordinate) {
        Coordinate directionCoordinate = currentAvatarCoordinate.subtract(nextMilestone);

        if (directionCoordinate.equals(new Coordinate(1, 0))) return Direction.LEFT;
        if (directionCoordinate.equals(new Coordinate(-1, 0))) return Direction.RIGHT;
        if (directionCoordinate.equals(new Coordinate(0, 1))) return Direction.UP;
        if (directionCoordinate.equals(new Coordinate(0, -1))) return Direction.DOWN;

        return randomDirection();
    }

    /**
     * Updates the internal map with the spaces in range
     * @param spacesInRange the list of spaces within the avatar's perception range
     */
    private void updateInternalMap(ArrayList<SpaceInfo> spacesInRange) {
        for (SpaceInfo space : spacesInRange) {
            SpaceType spaceType = space.getType();
            Coordinate spaceCoordinate = space.getRelativeToAvatarCoordinate();
            internalMap[spaceCoordinate.getY()][spaceCoordinate.getX()] = spaceType;
        }
        checkIfMapComplete();
        if (!internalMapHasAllElements) {
            checkIfMapHasAllElements();
        }
    }

    /**
     * Checks if the internal map is complete
     */
    private void checkIfMapComplete() {
        for (int i = 0; i < internalMap.length; i++) {
            for (int j = 0; j < internalMap[i].length; j++) {
                if (internalMap[i][j] == null) {
                    internalMapIsComplete = false;
                    return;
                }
            }
        }
        internalMapIsComplete = true;
    }

    /**
     * Checks if the internal map has all required elements (TOILET, DANCEFLOOR, DJBOOTH, BAR, SEATS)
     */
    private void checkIfMapHasAllElements() {
        boolean hasToilet = false;
        boolean hasDancefloor = false;
        boolean hasDjBooth = false;
        boolean hasBar = false;
        boolean hasSeats = false;

        for (int i = 0; i < internalMap.length; i++) {
            for (int j = 0; j < internalMap[i].length; j++) {
                if (internalMap[i][j] == SpaceType.TOILET) {
                    hasToilet = true;
                }
                if (internalMap[i][j] == SpaceType.DANCEFLOOR) {
                    hasDancefloor = true;
                }
                if (internalMap[i][j] == SpaceType.DJBOOTH) {
                    hasDjBooth = true;
                }
                if (internalMap[i][j] == SpaceType.BAR) {
                    hasBar = true;
                }
                if (internalMap[i][j] == SpaceType.SEATS) {
                    hasSeats = true;
                }
                if (hasToilet && hasDancefloor && hasDjBooth && hasBar && hasSeats) {
                    internalMapHasAllElements = true;
                    return;
                }
            }
        }
    }

    /**
     * Generates a random direction for the avatar to move
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
                return Direction.STAY;
        }
    }

    /**
     * Returns the perception range of the avatar
     * @return the perception range
     */
    @Override
    public int getPerceptionRange() {
        return super.getPerceptionRange();
    }

    /**
     * Sets the perception range of the avatar
     * @param perceptionRange the perception range to set
     */
    @Override
    public void setPerceptionRange(int perceptionRange) {
        super.setPerceptionRange(perceptionRange);
    }

    /**
     * Prints the internal map
     * @param <T> the type of the map
     * @param map the map to print
     */
    public <T> void printMap(T[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }   
    
}


