package PersonalAvatars;

import AvatarInterface.SuperAvatar;
import Environment.*;
import java.awt.Color;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class PaolaAvatar extends SuperAvatar {

    final private int ENERGY_MAX = 2400;
    final private int ENERGY_LOW_WARNING = 50;
    final private int ENERGY_MIN = 0;
    final private int ENERGY_LOSS_PER_TURN = 1;
    final private int ENERGY_INCREMENT_PER_TURN_IN_BAR = 500;

    final private int FULL_BLADDER = 2000;
    final private int BLADDER_HIGH_WARNING = 900;
    final private int EMPTY_BLADDER = 0;
    final private int BLADDER_FILL_PER_TURN = 10;
    final private int BLADDER_EMPTYING_PER_TURN_IN_TOILET = 200;

    final private int SOCIAL_MAX = 1000;
    final private int SOCIAL_MIN = 0;
    final private int SOCIAL_INCREASE_PER_TURN = 10;

    final private int FUN_MAX = 1000;
    final private int FUN_MIN = 0;
    final private int FUN_DECREMENT_PER_TURN = 10;
    final private int FUN_INCREMENT_PER_TURN_IN_DANCEFLOOR_OR_DJBOOTH = 5;

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

        private void reconstructRoute(Coordinate startCoordinate, Coordinate endCoordinate) {
            route.add(endCoordinate);
            Coordinate at = prev[endCoordinate.getY()][endCoordinate.getX()];
            while (at != null) {
                route.add(at);
                at = prev[at.getY()][at.getX()];
            }
            Collections.reverse(route);
        }

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

        private Coordinate makeAssumption() {
            if (asumptionHasBeenMade) {
                return targetCoordinate;
            }

            int ranX;
            int ranY;

            ranX = 1 + (int) (Math.random() * (COLUMNS - 2));
            ranY = 1 + (int) (Math.random() * (ROWS - 2));

            targetCoordinate = new Coordinate(ranX, ranY);
            asumptionHasBeenMade = true;
            return targetCoordinate;
        }

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

    public PaolaAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, Color.decode("#8b1a93"));
        Random random = new Random();
        energy = random.nextInt(ENERGY_LOW_WARNING, ENERGY_MAX);
        bladder = 0;
        social = random.nextInt(SOCIAL_MIN, SOCIAL_MAX);
        fun = random.nextInt(FUN_MIN, FUN_MAX);
    }

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

        System.out.println("Internal map");
        printMap(internalMap);
        System.out.println();
        return randomDirection();
    }

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

    private void updateColorBasedOnNeeds() {
        if (bladder >= BLADDER_HIGH_WARNING) {
            this.color = Color.decode("#89A203");
        } else if (energy <= ENERGY_LOW_WARNING) {
            this.color = Color.RED;
        } else {
            this.color = Color.decode("#8b1a93"); // Default color
        }
    }

    private void chooseAGoal() {
        int funWeight = FUN_MAX - fun;
        int seatWeight = FUN_MAX / 2;
        int barWeight = (ENERGY_MAX - energy) / 2;
        int toiletWeight = bladder;

        int totalWeight = funWeight + seatWeight + barWeight + toiletWeight;

        int randomWeight = (int) (Math.random() * totalWeight);

        if (randomWeight < funWeight) {
            plan = new Plan();
            if (Math.random() < 0.5) {
                plan.target = SpaceType.DANCEFLOOR;
            } else {
                plan.target = SpaceType.DJBOOTH;
            }
        } else if (randomWeight < funWeight + seatWeight) {
            plan = new Plan();
            plan.target = SpaceType.SEATS;
        } 
        // else if (randomWeight < funWeight + seatWeight + barWeight) {
        //     plan = new Plan();
        //     plan.target = SpaceType.BAR;
        // } else {
        //     plan = new Plan();
        //     plan.target = SpaceType.TOILET;
        // }
    }

    private Coordinate calculateCurrentLocation(SpaceInfo spaceInfo) {
        Coordinate leftToAvatar = spaceInfo.getRelativeToAvatarCoordinate();
        return new Coordinate(leftToAvatar.getX() + 1, leftToAvatar.getY());
    }

    private Direction calculateDirection(Coordinate nextMilestone, Coordinate currentAvatarCoordinate) {
        Coordinate directionCoordinate = currentAvatarCoordinate.subtract(nextMilestone);

        if (directionCoordinate.equals(new Coordinate(1, 0))) return Direction.LEFT;
        if (directionCoordinate.equals(new Coordinate(-1, 0))) return Direction.RIGHT;
        if (directionCoordinate.equals(new Coordinate(0, 1))) return Direction.UP;
        if (directionCoordinate.equals(new Coordinate(0, -1))) return Direction.DOWN;

        return Direction.STAY;
    }

    private void updateInternalMap(ArrayList<SpaceInfo> spacesInRange) {
        updateMap(spacesInRange);
    }

    private void updateMap(ArrayList<SpaceInfo> spacesInRange) {
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

    @Override
    public int getPerceptionRange() {
        return super.getPerceptionRange();
    }

    @Override
    public void setPerceptionRange(int perceptionRange) {
        super.setPerceptionRange(perceptionRange);
    }

    public <T> void printMap(T[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }
}
