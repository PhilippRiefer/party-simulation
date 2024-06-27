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
public class PaolaAvatar extends SuperAvatar { // implements AvatarInterface

    final private int ENERGY_MAX = 2400; // enough to visit the 800 cells in the room 3 times, as each turn deducts one energy point
    final private int ENERGY_LOW_WARNING = 50; // for interrupt to go visit the bar for a drink
    final private int ENERGY_MIN = 0; // if 0, fall asleep and replenish

    final private int FULL_BLADDER = 1000; // TODO: Define behavior for when full bladder is reached
    final private int BLADDER_HIGH_WARNING = 900; // to prioritize visiting the toilet
    final private int EMPTY_BLADDER = 0; 

    final private int SOCIAL_MAX = 1000; // wants to find other avatars
    final private int SOCIAL_MIN = 0; // does not care for other avatars

    final private int FUN_MAX = 1000; // does not care about going to dancefloor
    final private int FUN_MIN = 0; // wants to have fun in the dancefloor or djbooth
    
    final private int ROWS = 20;
    final private int COLUMNS = 40;

    private SpaceType[][] internalMap = new SpaceType[ROWS][COLUMNS];
    private boolean internalMapIsComplete = false; // flag changes to "true" when map has been complete
                                                    // TODO: Design algorithm to detect that map has been complete 
                                                    // and change this flag.
    private boolean hasGoalInMind = false; // flag changes to "true" when a purpose has been selected (e.g. go to bar)
                                                    // TODO: Flag must change back to "false" when purpose has been completed  
                                                    
    Coordinate currentAvatarLocation = null;
    private int energy; // when low on energy, Paola will want to go to the bar and get a drink
    private int bladder; // when high, Paola will seek the bathroom
    private int social; // when social is high, Paola will be more likely to interact with other avatars
    private int fun; // when fun is low, Paola will want be more likely to go dancing

    private Plan plan;
    
    private class Plan {
        private SpaceType target;
        // private SpaceType[][] m = new SpaceType[ROWS][COLUMNS]; // Input character matrix TODO: NEEDED? Or is the internalMap enough?
        private Queue<Coordinate> queue = new LinkedList<>(); // Queue of coordinates to explore for the BFS algorithm
        private Boolean reachedEnd = false;
        private Boolean[][] explored = new Boolean[ROWS][COLUMNS];
        private int[] directionEastWest = new int[]{-1, 1, 0, 0};
        private int[] directionNorthSouth = new int[]{ 0, 0, 1, -1};

        boolean asumptionHasBeenMade = false; // for use when map is not complete. Avatar makes an assumption of where the target will be. 
        Coordinate targetCoordinate; // for use when map is not complete. This is the target coordinate where the avatar assumes the target will be.

        
        
        private Coordinate[][] prev = new Coordinate[ROWS][COLUMNS]; // Array prev to keep track of parent node for the BFS algorithm
        private LinkedList<Coordinate> route = new LinkedList<>(); // Queue of steps in route. Each step in the route has a coordinate

        Plan(){
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLUMNS; j++) {
                    explored[i][j] = false;
                }
            }
        }

        void calculateRoute(){
            // Reset explored and prev arrays
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLUMNS; j++) {
                    explored[i][j] = false;
                    prev[i][j] = null;
                }
            }
        
            queue.clear(); // Clear the queue
            queue.add(currentAvatarLocation);
            explored[currentAvatarLocation.getY()][currentAvatarLocation.getX()] = true;
        
            Coordinate endCoordinate = null;
            while (queue.size() > 0) {
                Coordinate coordinateToExplore = queue.remove();
                if (internalMap[coordinateToExplore.getY()][coordinateToExplore.getX()] == target) {
                    endCoordinate = coordinateToExplore;
                    reachedEnd = true;
                    break;
                }
                exploreNeighbors(coordinateToExplore);
            }
        
            if (endCoordinate != null) {
                reconstructRoute(currentAvatarLocation, endCoordinate);
            }
        }
        

        private void calculateRoute(Coordinate targetCoordinate) {
            // Reset explored and prev arrays
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLUMNS; j++) {
                    explored[i][j] = false;
                    prev[i][j] = null;
                }
            }
            queue.clear(); // Clear the queue
            queue.add(currentAvatarLocation);
            explored[currentAvatarLocation.getY()][currentAvatarLocation.getX()] = true;
            
            while (queue.size() > 0){
                Coordinate coordinateToExplore = queue.remove();
                System.out.println("queue: " + queue);
                System.out.println("target coordinete: "+ targetCoordinate);
                if (coordinateToExplore.equals(targetCoordinate)){
                    reachedEnd = true;
                    break;
                }
                exploreNeighbors(coordinateToExplore);
            }
            if (reachedEnd){
                reconstructRoute(currentAvatarLocation, targetCoordinate);
            }
        }

        private void reconstructRoute(Coordinate startCoordinate, Coordinate endCoordinate) {
            // reconstruct route going backwards from startCoordinate
            route.add(endCoordinate);
            // check in prev what coordinate is stored in endCoordinate
            Coordinate at = prev[endCoordinate.getY()][endCoordinate.getX()];
            while(at!=null){
                // add that coordinate to route
                route.add(at);
                System.out.println("unreversed route: " +route);
                // repeat with the coordinate that was just added to route
                at = prev[at.getY()][at.getX()];
            }
            
            Collections.reverse(route);  
        }

        private void exploreNeighbors(Coordinate coordinate) {
            for(int i = 0; i < 4; i++){
                int neighboringRow = coordinate.getY() + directionEastWest[i];
                int neighboringColumn = coordinate.getX() + directionNorthSouth[i];

                // Skip out of bounds locations
                if (neighboringRow < 0 || neighboringColumn < 0) continue;
                if (neighboringRow >= ROWS || neighboringColumn >= COLUMNS) continue;

                // Skip visited locations or blocked cells
                if (explored[neighboringRow][neighboringColumn]) continue;
                if (internalMap[neighboringRow][neighboringColumn] == SpaceType.OBSTACLE) continue;

                queue.add(new Coordinate(neighboringColumn, neighboringRow));
                // if(internalMapIsComplete){
                    explored[neighboringRow][neighboringColumn] = true;
                // }
                 
                // Add coordinate being currently explored into the cells corresponding to the neighbors. This is to reconstruct path later
                prev[neighboringRow][neighboringColumn] = coordinate;               
            }
        }

        private Coordinate makeAssumption() {
            // if assumption has been made, no need to make assumption
            if (asumptionHasBeenMade){
                // return already calculated assumed coordinate
                return targetCoordinate;
            }
            // if assumption has not been made:
            //calculate a random X and Y
            int ranX = 1 + (int) (Math.random() * (COLUMNS - 2)); // between 1 and COLUMNS - 1
            int ranY = 1 + (int) (Math.random() * (ROWS - 2)); // between 1 and ROWS - 1
            // update global `targetCoordinate` with the random assumption
            targetCoordinate = new Coordinate(ranX, ranY);
            // update assumptionHasBeenMade
            asumptionHasBeenMade = true;
            // return assumed coordinate
            return targetCoordinate;
        }

        Coordinate nextMilestone(){
            route.clear();
            // if the internal map is not complete, make an assumption of where the target location will be, and calculate route to get there
            if (!internalMapIsComplete){
                targetCoordinate = makeAssumption();                
                calculateRoute(targetCoordinate);
            }
            // if the internal map is complete, calculate route to the target location
            else if (internalMapIsComplete) {
                calculateRoute(); // calculates the route every time. Needed because other avatars might move in my original way
            }
            Coordinate next;
            do {
                next = route.remove();
            } while (currentAvatarLocation.equals(next));

            
            System.out.println("--");
            System.out.println("route: " + route);
            System.out.println("next: " + next);
            System.out.println("--");
            return next;
            
            
            
            // // if the internal map is not complete, the route cannot be calculated, therefore, start with a new queue
            // if(!internalMapIsComplete){
            //     queue.clear();
            // }
            // calculateRoute(); // calculates the route every time. Needed because other avatars might move in my original way
            // if(internalMapIsComplete){   
            //     return route.remove();
            // }
            // System.out.println();
            // // System.out.println("Explored:");
            // // printMap(explored);
            // System.out.println("Queue:");
            // queue.forEach(element -> System.out.print(element));
            // System.out.println();
            // System.out.println("prev:");
            // // printMap(prev);

            // Coordinate next = queue.remove();
            // Coordinate parentOfCurrentCoordinate = prev[currentAvatarLocation.getY()][currentAvatarLocation.getX()];

            // System.out.println("Next: "+ next);
            // System.out.println("parent of current coordinate: " + parentOfCurrentCoordinate);
            // System.out.println("next is equal to parent of current coordinate:"+ next.equals(parentOfCurrentCoordinate));
            // System.out.println("");
            // // if next in queue is the same as parent of current coordinate, remove it from queue
            // if (next.equals(parentOfCurrentCoordinate)){
            //     next = queue.remove();
            //     System.out.println("New next: "+ next);
            // }
   
            // return next; // or is it peek?
        }

        
        
    }

    /**
     * Constructs a TemplateAvatar object with the specified ID and perception range.
     *
     * @param id              the ID of the avatar
     * @param perceptionRange the perception range of the avatar
     */
    public PaolaAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, Color.decode("#8b1a93"));
        Random random = new Random();
        energy = random.nextInt(ENERGY_LOW_WARNING,ENERGY_MAX); // initialize with half of the energy to full energy
        bladder = 0; // empty bladder
        social = random.nextInt(SOCIAL_MIN, SOCIAL_MAX); // initialize with half of the desire to be social to full desire
        fun = random.nextInt(FUN_MIN, FUN_MAX); // low on fun means wants to dance
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
        /* Spaces in range 
        * 0-3-5
        * 1-A-6
        * 2-4-7
        */
        currentAvatarLocation = calculateCurrentLocation(spacesInRange.get(1)); // calculate current location based on the coordinate of the item to the left
        // depending on where I am standing fulfill a need

        updateInternalMap(spacesInRange); // update internal map, only if not complete 

        // when avatar has reached its current target, get rid of the current plan
        if (plan != null && (internalMap[currentAvatarLocation.getY()][currentAvatarLocation.getX()] == plan.target || currentAvatarLocation.equals(plan.targetCoordinate)))
        {
            plan = null;
        }
        
        
        
        // Commented out for the time being, as logic to manage needs status has not been implemented
        // if(hasGoalInMind){ // TODO: maybe I need to make 'hasGoalInMind' a trivalue, yes, no, urgent. If urgent, skip checking for energy and toilet needs
        //     if (energy > ENERGY_LOW_WARNING){ // has enough energy
        //         if (bladder < BLADDER_HIGH_WARNING){ // does not need to go to toilet
        //             return calculateDirection(currentAvatarLocation, goal.nextMilestone());
        //         } else { // needs to go to toilet
        //             goal.target = SpaceType.TOILET;
        //             goal.calculateRoute();
        //             return calculateDirection(currentAvatarLocation, goal.nextMilestone());
        //         }
        //     } else { // needs an energy drink
        //         goal.target = SpaceType.BAR;
        //         goal.calculateRoute(); // TODO: There are two bars, take care of this when calculating route to bar
        //         return calculateDirection(currentAvatarLocation, goal.nextMilestone());
        //     }
        // } else {
        //     chooseAGoal();
        // }
        
        // if already has a plan in mind and has not reached it
        if(plan!=null){ // TODO: maybe I need to make 'hasGoalInMind' a trivalue, yes, no, urgent. If urgent, skip checking for energy and toilet needs
            Coordinate nextMilestone = plan.nextMilestone();
            Direction direction = calculateDirection(nextMilestone, currentAvatarLocation);
            return direction;
        }
        chooseAGoal();
    
        
        // select purpose based on current need
        // set a purpose flag, so that it does not interfere with other needs and needs aren't 
            // changing with every call to yourTurn
        // if purpose flag is raised:
            // retrieve location of desired target
            // calculate direction to go to target
            // return that direction
        // if purpose flag is not raised, choose a purpose based on needs

        
        
        
        System.out.println("Internal map");
        printMap(internalMap);
        System.out.println();
        // For now, let's continue to move randomly as a placeholder
        return randomDirection();
    }

    private void chooseAGoal() {
        // choose goal randomly for the moment TODO: Implement logic to choose goal according to needs
        plan = new Plan();
        int directionNumber = (int) (Math.random() * 5);

        switch (directionNumber) {
            case 0:
                plan.target = SpaceType.BAR;
                break;
            case 1:
                plan.target = SpaceType.DANCEFLOOR;
                break;
            case 2:
                plan.target = SpaceType.DJBOOTH;
                break;
            case 3:
                plan.target = SpaceType.SEATS;
                break;
            case 4:
                plan.target = SpaceType.TOILET;
                break;
            default:
                plan.target = SpaceType.TOILET;
                break;
        }
    }

    private Coordinate calculateCurrentLocation(SpaceInfo spaceInfo) {
        Coordinate leftToAvatar = spaceInfo.getRelativeToAvatarCoordinate();
        Coordinate currentLocation = new Coordinate(leftToAvatar.getX()+1, leftToAvatar.getY());
        return currentLocation;
    }

    private Direction calculateDirection(Coordinate nextMilestone, Coordinate currentAvatarCoordinate) {

        Coordinate directionCoordinate = currentAvatarCoordinate.subtract(nextMilestone);
        
        if (directionCoordinate.equals(new Coordinate(1,0))) return Direction.LEFT;

        if (directionCoordinate.equals(new Coordinate(-1,0))) return Direction.RIGHT;

        if (directionCoordinate.equals(new Coordinate(0,1))) return Direction.UP;

        if (directionCoordinate.equals(new Coordinate(0,-1))) return Direction.DOWN;

        return Direction.STAY;
}

    private void updateInternalMap(ArrayList<SpaceInfo> spacesInRange) {
        if (!internalMapIsComplete){
            updateMap(spacesInRange);
        }
        
    }

    private void updateMap(ArrayList<SpaceInfo> spacesInRange) {
        for(SpaceInfo space:spacesInRange){
            SpaceType spaceType = space.getType();
            if(spaceType != SpaceType.AVATAR){
                Coordinate spaceCoordinate = space.getRelativeToAvatarCoordinate();
                internalMap[spaceCoordinate.getY()][spaceCoordinate.getX()] = spaceType;
            }
            
        }
        checkIfMapComplete();
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

    public <T> void printMap(T[][] map){
        for (int i=0; i<map.length; i++){
            for (int j=0; j<map[i].length; j++){
                System.out.print(map[i][j]+" ");
            }
            System.out.println();
        }
        
    }
}