package PersonalAvatars;

import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import AvatarInterface.SuperAvatar;
import Environment.Coordinate;
import Environment.Direction;
import Environment.SpaceInfo;
import Environment.SpaceType;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MaximSpockAvatar extends SuperAvatar {
    // wohin auslagern?
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
    SpaceType currentObjective = null; // current Objective to move to unknown cell
    SpaceType[][] clubMemory = new SpaceType[XCOORDINATEMAX][YCOORDINATEMAX]; // Mr. Spocks internal map of the
                                                                              // Environment
    // internal variables for movement
    boolean movingToObjective = false;
    boolean hasPreComputed = false;
    boolean hasWaitedOneTurnForOtherAvatarToLeave = false;
    
    boolean allSpacesScouted = false;

    int stepOfTheGivenDirectionList = 0;
    ArrayList<Direction> listOfDirections = new ArrayList<Direction>();

    // perception Range should be higher than of a human!
    public MaximSpockAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);
    }

    int x = 1;

    /**
     * Determines the direction for the avatar's next turn based on the spaces
     * within its perception range.
     * 
     * @return the direction for the avatar's next turn
     */
    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        int sizeOfArrayList = spacesInRange.size();
        Coordinate personalCoordinates = spacesInRange.get(sizeOfArrayList / 2).getRelativeToAvatarCoordinate(); // 2n^2 + 2n
        personalCoordinates.setY(personalCoordinates.getY() - 1);

        // For Debugging
        System.out.println("\t\t\t---------Round " + x + "---------");
        x++;

        saveSpacesInRange(spacesInRange, personalCoordinates); // 1. step is to save Environment in memory
        printFloor(); 

        if (countedTurns > 99 && !movingToObjective) { // 2. if counted Turns > 99 -> scouting not as default
            currentObjective = decideNextMovement();
            System.out.println("Current Objective:  " + currentObjective);
        }
        updateStats(); // stats are updated each turn (getting thirstier for e.g.)
        return moveTo(clubMemory, currentObjective, personalCoordinates, spacesInRange); // returning next chosen direction to SimulationControl
    }

    private void printFloor() {
        // Zuordnung von Symbolen zu jedem SpaceType
        Map<SpaceType, Character> symbols = new HashMap<>();
        symbols.put(SpaceType.EMPTY, '.');
        symbols.put(SpaceType.OBSTACLE, '#');
        symbols.put(SpaceType.AVATAR, 'A');
        symbols.put(SpaceType.DANCEFLOOR, 'D');
        symbols.put(SpaceType.DJBOOTH, 'J');
        symbols.put(SpaceType.TOILET, 'T');
        symbols.put(SpaceType.BAR, 'B');
        symbols.put(SpaceType.SEATS, 'S');

        // Schreiben der clubMemory-Variable in eine Textdatei
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("clubMemory.txt"))) {
            for (int j = 0; j < YCOORDINATEMAX; j++) { 
                for (int i = 0; i < XCOORDINATEMAX; i++) {  
                    Character symbol = symbols.get(clubMemory[i][j]);
                    if (symbol == null) {
                        symbol = '?';  // Verwenden von ? für unbekannte Spaces
                    }
                    writer.write(symbol);
                    writer.write(' '); 
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Direction moveTo(SpaceType[][] clubMemory, SpaceType currentObjective, Coordinate personalCoordinates, ArrayList<SpaceInfo> spacesInRange) {
        //System.out.println("\t---moveTo Opened!!!---");
        if (!hasPreComputed || hasWaitedOneTurnForOtherAvatarToLeave) { // definitely preComputing path to Objective
            listOfDirections = preComputeDirection(clubMemory, currentObjective, personalCoordinates);
            if(listOfDirections.size() == 0){
                System.out.println("List Of Directions is Empty!!");
                movingToObjective = false;
                hasPreComputed = false;
            }
            else{
                System.out.println("List Of Directions is NOT Empty!!");
                movingToObjective = true;
                hasPreComputed = true;
            }
        }

        if (listOfDirections.size() == 0) { // Liste ist leer! -> nach ungescouteten Feldern suchen
            if (!allSpacesScouted) {        // -> wenn nicht alle schon gescouted
                System.out.println("NOT ALL SPACES SCOUTED -> SCOUT!!!!");
                listOfDirections = preComputeDirection(clubMemory, null, personalCoordinates);
                if (checkIfNextStepOkay(listOfDirections.get(stepOfTheGivenDirectionList), personalCoordinates)) {
                    movingToObjective = true;
                    return listOfDirections.get(stepOfTheGivenDirectionList++);
                } else if (!hasWaitedOneTurnForOtherAvatarToLeave) { // wenn step nicht möglich, warten eine runde
                    movingToObjective = true;
                    hasWaitedOneTurnForOtherAvatarToLeave = true;
                    return Direction.STAY;
                } else {
                    hasWaitedOneTurnForOtherAvatarToLeave = false;
                    movingToObjective = false;
                    hasPreComputed = false;
                    return Direction.STAY;
                }
            }
            else{                       // KEINE UNBEKANNTEN FELDER!!! -> wenn doch, warten!
                return Direction.STAY;
            }
            
        }
        else if(stepOfTheGivenDirectionList == listOfDirections.size()){    // (Bedingung Liste nicht null) ZIEL ERREICHT -> NEUES ZIEL SUCHEN
            System.out.println("List Of Directions at End -> arrived at destination");
            if(currentObjective != null){
                rewardsForArrivingAtObjective(currentObjective);
            }
            movingToObjective = false;
            hasPreComputed = false;
            return Direction.STAY;
        }
        else{   // Liste folgen!  // FEHLERBEHANDLUNG HIER/ WENN JEMAND IM WEG WARTEN SONST AUTOMATISCH WEGEN OBEN NEU BERECHNEN
            System.out.println("stepOfTheGivenDirectionList: " + (stepOfTheGivenDirectionList+1) + " of " + (listOfDirections.size()));
            
            if (checkIfNextStepOkay(listOfDirections.get(stepOfTheGivenDirectionList), personalCoordinates)) {
                return listOfDirections.get(stepOfTheGivenDirectionList++);
            } else if(!hasWaitedOneTurnForOtherAvatarToLeave) {    // wenn step nicht möglich, warten eine runde
                System.out.println("Avatar in way");
                movingToObjective = true;
                hasWaitedOneTurnForOtherAvatarToLeave = true;
                return Direction.STAY;
            } else{
                System.out.println("Already waited for Avatar to leave");
                hasWaitedOneTurnForOtherAvatarToLeave = false;
                movingToObjective = false;
                hasPreComputed = false;
                return Direction.STAY;
            }
        }
    }

    void rewardsForArrivingAtObjective(SpaceType currentObjective){
        switch (currentObjective) {
            case TOILET:
                bowelMovement = MINSTAT;
                urination = MINSTAT;
                break;
           
            case BAR:
                hydration = MAXSTAT;
                break;
        
            case SEATS:
                energy = MAXSTAT;
                break;

            default:
                break;
        }  
    } 

    Boolean checkIfNextStepOkay(Direction stepOfList, Coordinate personalCoordinates) {
        if (stepOfList == Direction.RIGHT) {
            if (clubMemory[personalCoordinates.getX() + 1][personalCoordinates.getY()] == (SpaceType.OBSTACLE)
                && clubMemory[personalCoordinates.getX() + 1][personalCoordinates.getY()] == (SpaceType.AVATAR)) {
                System.out.println("ETWAS STEHT IM WEG -> NEUEN WEG BERECHNEN!");
                return false;
            }
        }
        if (stepOfList == Direction.LEFT) {
            if (clubMemory[personalCoordinates.getX() - 1][personalCoordinates.getY()] == (SpaceType.OBSTACLE)
                && clubMemory[personalCoordinates.getX() + 1][personalCoordinates.getY()] == (SpaceType.AVATAR)) {
                System.out.println("ETWAS STEHT IM WEG -> NEUEN WEG BERECHNEN!");
                return false;
            }
        }
        if (stepOfList == Direction.UP) {
            if (clubMemory[personalCoordinates.getX()][personalCoordinates.getY() - 1] == (SpaceType.OBSTACLE)
                && clubMemory[personalCoordinates.getX() + 1][personalCoordinates.getY()] == (SpaceType.AVATAR)) {
                System.out.println("ETWAS STEHT IM WEG -> NEUEN WEG BERECHNEN!");
                return false;
            }
        }
        if (stepOfList == Direction.DOWN) {
            if (clubMemory[personalCoordinates.getX()][personalCoordinates.getY() + 1] == (SpaceType.OBSTACLE)
                && clubMemory[personalCoordinates.getX() + 1][personalCoordinates.getY()] == (SpaceType.AVATAR)) {
                System.out.println("ETWAS STEHT IM WEG -> NEUEN WEG BERECHNEN!");
                return false;
            }
        }
       
        // wenn alles in Ordnung!
        return true;
    }

    ArrayList<Direction> preComputeDirection(SpaceType[][] clubMemory, SpaceType currentObjective, Coordinate personalCoordinates) {
        ArrayList<Direction> listOfDirectionsToObjective = new ArrayList<Direction>();
        // find cell that is objective in Memory
        Coordinate objectiveCoordinates = findObjectiveInMemory(currentObjective, personalCoordinates);

        if(objectiveCoordinates.getX() != personalCoordinates.getX() || objectiveCoordinates.getY() != personalCoordinates.getY()) {
            listOfDirectionsToObjective = computeRoute(objectiveCoordinates, personalCoordinates);
        }
        System.out.println("---- WORKS TILL preComputeDirection ----");
        stepOfTheGivenDirectionList = 0;    // gerade neue Route berechnet -> bei erstem Schritt anfangen (Bei Liste = 0)
        return listOfDirectionsToObjective;
    }

    ArrayList<Direction> computeRoute(Coordinate objectiveCoordinates, Coordinate personalCoordinates){
        System.out.println("---- computeRoute() ----");
        ArrayList<Direction> listOfDirectionsToObjective = new ArrayList<Direction>();
        int moveX = 0;
        int moveY = 0;
        boolean cantMoveHorizontal = false;
        boolean cantMoveVertical = false;

        while(!cantMoveHorizontal){
            if (objectiveCoordinates.getX() < personalCoordinates.getX() + moveX
                    && clubMemory[personalCoordinates.getX() + moveX - 1][personalCoordinates.getY()] != SpaceType.OBSTACLE) { // objective links, im vgl zu avatar -> avatar nach links gehen
                listOfDirectionsToObjective.add(Direction.LEFT);
                moveX--;
            }
            else if(clubMemory[personalCoordinates.getX() + moveX + 1][personalCoordinates.getY()] != SpaceType.OBSTACLE){                                                           // objective rechts, im vgl zu avatar -> avatar nach rechts gehen
                listOfDirectionsToObjective.add(Direction.RIGHT);
                moveX++;
            }
            else{
                cantMoveHorizontal = true;
                break;
            }
            if(objectiveCoordinates.getX() != personalCoordinates.getX() + moveX){
                cantMoveHorizontal = true;
            }
        }
        while(!cantMoveVertical){
            if(objectiveCoordinates.getY() < personalCoordinates.getY() + moveY
                    && clubMemory[personalCoordinates.getX() + moveX][personalCoordinates.getY() + moveY - 1] != SpaceType.OBSTACLE) { // objective oben, im vgl zu avatar -> avatar nach oben gehen
                listOfDirectionsToObjective.add(Direction.UP);
                moveY--;
            }
            else if(clubMemory[personalCoordinates.getX() + moveX][personalCoordinates.getY() + moveY + 1] != SpaceType.OBSTACLE) {                                                           // objective unten, im vgl zu avatar -> avatar nach unten gehen
                listOfDirectionsToObjective.add(Direction.DOWN);
                moveY++;
            }
            else{
                cantMoveVertical = true;
                break;
            }
            if(objectiveCoordinates.getY() != personalCoordinates.getY() + moveY){
                cantMoveVertical = true;
            }
        }

        return listOfDirectionsToObjective;
    }

    Coordinate findObjectiveInMemory(SpaceType currentObjective, Coordinate personalCoordinates) {
        Coordinate objectiveCoordinates = new Coordinate(personalCoordinates.getX(), personalCoordinates.getY());
        Boolean objectiveFound = false;
        System.out.println("\t---SEARCHING IN MEMORY FOR " + currentObjective + "---");
    
        // Check vicinity 1/2/3... circle principle
        for (int surrounding = 1; !objectiveFound && surrounding < XCOORDINATEMAX; surrounding++) {
            objectiveCoordinates = checkVicinityForObjective(currentObjective, personalCoordinates, surrounding);
    
            // If objective coordinates are not the same as personal coordinates
            if (objectiveCoordinates.getX() != personalCoordinates.getX() || objectiveCoordinates.getY() != personalCoordinates.getY()) {
                System.out.println("OBJECTIVE FOUND IN MEMORY \t\t---- FOUND COORDINATES! --------");
                System.out.println("Objective at found coordinates: " + clubMemory[objectiveCoordinates.getX()][objectiveCoordinates.getY()]);
                System.out.println("ObjectiveCoordinateX in memory: " + objectiveCoordinates.getX() + "\nObjectiveCoordinateY in memory: " + objectiveCoordinates.getY());
                objectiveFound = true;
                return objectiveCoordinates; // Return found coordinates
            } else {
                if (surrounding == XCOORDINATEMAX - 1) {
                    System.out.println("OBJECTIVE DEFINITELY NOT IN MEMORY!");
                    if (currentObjective == null) allSpacesScouted = true;
                }
            }
        }
    
        System.out.println("Objective not found in memory. Setting currentObjective to null.");
        currentObjective = null; // Set currentObjective to null if not found
        return objectiveCoordinates;
    }

    Coordinate checkVicinityForObjective(SpaceType currentObjective, Coordinate personalCoordinates, int surrounding) {
        Coordinate objectiveCoordinates = new Coordinate(personalCoordinates.getX(), personalCoordinates.getY());
    
        // Loop through the entire environment
        for (int x = 0; x < XCOORDINATEMAX; x++) {
            for (int y = 0; y < YCOORDINATEMAX; y++) {
                if (clubMemory[x][y] == currentObjective) {
                    System.out.println("Objective found at: (" + x + ", " + y + ")");
                    objectiveCoordinates.setX(x);
                    objectiveCoordinates.setY(y);
                    return objectiveCoordinates;
                }
            }
        }
        return objectiveCoordinates;
    }

    void printfCheckVicinity(int xAddition, int yAddition, int personalMinusSurroundingX, int personalMinusSurroundingY){
        System.out.println("What is in x == " +xAddition+" and y == " +yAddition+ ": " +  clubMemory[personalMinusSurroundingX + xAddition][personalMinusSurroundingY + yAddition]);
        System.out.println("CoordinateX: " + (personalMinusSurroundingX + xAddition));
        System.out.println("CoordinateY: " + (personalMinusSurroundingY + yAddition));
    }

    // saves latest Information on Environment
    public void saveSpacesInRange(ArrayList<SpaceInfo> spacesInRange, Coordinate persCoordinate) {
        int i = 0;
        //System.out.println("\t---Begin updating ClubMemory---");
        while (i < spacesInRange.size()) {
            int clubMemoryCoordinateX = spacesInRange.get(i).getRelativeToAvatarCoordinate().getX();
            int clubMemoryCoordinateY = spacesInRange.get(i).getRelativeToAvatarCoordinate().getY();
            if(i == 2*Math.pow(getPerceptionRange(), 2) + 2 * getPerceptionRange()){
                clubMemoryCoordinateY++;
            }
            // TODO TRY CATCH
            if(clubMemoryCoordinateX > 0 || clubMemoryCoordinateX < XCOORDINATEMAX || clubMemoryCoordinateY > 0 || clubMemoryCoordinateY > YCOORDINATEMAX){
                clubMemory[clubMemoryCoordinateX][clubMemoryCoordinateY] = spacesInRange.get(i).getType();
            }
            i++;
            //System.out.println("clubMemoryX: " + clubMemoryCoordinateX);
            //System.out.println("clubMemoryY: " + clubMemoryCoordinateY);
            //System.out.println("clubMemory[" + i + "]: " + clubMemory[clubMemoryCoordinateX][clubMemoryCoordinateY] );
        }
        //System.out.println("\t---ClubMemory updated---");
    }

    public SpaceType decideNextMovement() {
        hasPreComputed = false;
        if (bowelMovement > (200 - THRESHOLD) || urination > (200 - THRESHOLD)) {
            return currentObjective = SpaceType.TOILET;
        } else if (hydration < THRESHOLD) {
            return currentObjective = SpaceType.BAR;
        } else if (energy < THRESHOLD) {
            return currentObjective = SpaceType.SEATS;
        } else { // else Scouting
            return currentObjective = null;
        }
    }

    public void updateStats() {
        energy--;
        hydration--;
        bowelMovement++;
        urination++;
        countedTurns++;
    }

    /**
     * Generates a random direction for the avatar to move.
     *
     * @return a random direction
     */
    // private Direction randomDirection() {
    // int directionNumber = (int) (Math.random() * 4);

    // switch (directionNumber) {
    // case 0:
    // return Direction.LEFT;
    // case 1:
    // return Direction.RIGHT;
    // case 2:
    // return Direction.UP;
    // case 3:
    // return Direction.DOWN;
    // default:
    // return Direction.STAY; // Safety net, though unnecessary as directionNumber
    // is bound by 0-3
    // }
    // }

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
