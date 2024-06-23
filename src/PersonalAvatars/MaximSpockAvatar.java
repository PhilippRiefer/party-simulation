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
    boolean isScoutingForObject = false;
    boolean notMovingToObjective = true;
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

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        int sizeOfArrayList = spacesInRange.size();
        Coordinate personalCoordinates = spacesInRange.get(sizeOfArrayList / 2).getRelativeToAvatarCoordinate(); // 2n^2 + 2n
        personalCoordinates.setY(personalCoordinates.getY() - 1);

        System.out.println("\t\t\t---------Round " + x + "---------");
        x++;
        System.out.println("Current Objective:  " + currentObjective);
        
        saveSpacesInRange(spacesInRange, personalCoordinates); // 1. step is to save Environment in memory
        if (countedTurns > 99 && notMovingToObjective) { // 2. if counted Turns > 99 -> scouting not as default
            System.out.println("NOT SCOUTING AS DEFAULT ANYMORE");
            if(!allSpacesScouted){
                currentObjective = decideNextMovement();
            } 
            else{
                return Direction.STAY;
            }
        }
        printFloor();
        updateStats(); 
        return moveTo(clubMemory, currentObjective, personalCoordinates, spacesInRange); 
    }

    private void printFloor() {
        Map<SpaceType, Character> symbols = new HashMap<>();
        symbols.put(SpaceType.EMPTY, '.');
        symbols.put(SpaceType.OBSTACLE, '#');
        symbols.put(SpaceType.AVATAR, 'A');
        symbols.put(SpaceType.DANCEFLOOR, 'D');
        symbols.put(SpaceType.DJBOOTH, 'J');
        symbols.put(SpaceType.TOILET, 'T');
        symbols.put(SpaceType.BAR, 'B');
        symbols.put(SpaceType.SEATS, 'S');

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("clubMemory.txt"))) {
            for (int j = 0; j < YCOORDINATEMAX; j++) {
                for (int i = 0; i < XCOORDINATEMAX; i++) {
                    Character symbol = symbols.get(clubMemory[i][j]);
                    if (symbol == null) {
                        symbol = '?';
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
        System.out.println("hasPreComputed: " + hasPreComputed);
        if (!hasPreComputed || hasWaitedOneTurnForOtherAvatarToLeave) { // definitely preComputing path to Objective
            System.out.println("\t---PRECOMPUTING PATH!!!---");
            listOfDirections = preComputeDirection(clubMemory, currentObjective, personalCoordinates);
            if(listOfDirections == null){
                notMovingToObjective = true;
                hasPreComputed = false;
                return Direction.STAY;
            }
            hasPreComputed = true;
        }

        if (listOfDirections.size() == 0) { // Liste ist leer!
            // System.out.println("List Of Directions is Empty!!");
            listOfDirections = preComputeDirection(clubMemory, null, personalCoordinates);
            if (checkIfNextStepOkay(listOfDirections.get(stepOfTheGivenDirectionList), personalCoordinates)) {
                return listOfDirections.get(stepOfTheGivenDirectionList++);
            } else if (!hasWaitedOneTurnForOtherAvatarToLeave) { // wenn step nicht möglich, warten eine runde
                hasWaitedOneTurnForOtherAvatarToLeave = true;
                return Direction.STAY;
            } else {
                hasWaitedOneTurnForOtherAvatarToLeave = false;
                notMovingToObjective = true;
                hasPreComputed = false;
                return Direction.STAY;
            }
        }
        else if(stepOfTheGivenDirectionList >= listOfDirections.size()){    // ZIEL ERREICHT -> NEUES ZIEL SUCHEN
            if(currentObjective != null){
                rewardsForArrivingAtObjective(currentObjective);
            }
            notMovingToObjective = true;
            hasPreComputed = false;
            return Direction.STAY;
        }
        else{                                                               // Liste folgen!  // FEHLERBEHANDLUNG HIER/ WENN JEMAND IM WEG WARTEN SONST AUTOMATISCH WEGEN OBEN NEU BERECHNEN
            System.out.println("stepOfTheGivenDirectionList: " + (stepOfTheGivenDirectionList+1) + " of " + (listOfDirections.size()));
            
            if (checkIfNextStepOkay(listOfDirections.get(stepOfTheGivenDirectionList), personalCoordinates)) {
                return listOfDirections.get(stepOfTheGivenDirectionList++);
            } else if(!hasWaitedOneTurnForOtherAvatarToLeave) {    // wenn step nicht möglich, warten eine runde
                hasWaitedOneTurnForOtherAvatarToLeave = true;
                return Direction.STAY;
            } else{
                hasWaitedOneTurnForOtherAvatarToLeave = false;
                notMovingToObjective = true;
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

    public boolean checkIfNextStepOkay(Direction stepOfList, Coordinate personalCoordinates) {
        int x = personalCoordinates.getX();
        int y = personalCoordinates.getY();
        int nextX = x, nextY = y;

        switch (stepOfList) {
            case RIGHT:
                nextX = x + 1;
                break;
            case LEFT:
                nextX = x - 1;
                break;
            case UP:
                nextY = y - 1;
                break;
            case DOWN:
                nextY = y + 1;
                break;
        }

        if (isObstacleOrAvatar(nextX, nextY)) {
            System.out.println("ETWAS STEHT IM WEG -> NEUEN WEG BERECHNEN!");
            return false;
        }
        
        return true;
    }

    private boolean isObstacleOrAvatar(int x, int y) {
        if (x < 0 || y < 0 || x >= clubMemory.length || y >= clubMemory[0].length) {
            return false;
        }
        return clubMemory[x][y] == SpaceType.OBSTACLE || clubMemory[x][y] == SpaceType.AVATAR;
    }

    ArrayList<Direction> preComputeDirection(SpaceType[][] clubMemory, SpaceType currentObjective, Coordinate personalCoordinates) {
        ArrayList<Direction> listOfDirectionsToObjective = new ArrayList<Direction>();
        // find cell that is objective in Memory
        Coordinate objectiveCoordinates = findObjectiveInMemory(currentObjective, personalCoordinates);

        if (objectiveCoordinates == personalCoordinates) { // found nothing in Memory -> scout Empty Spaces
            System.out.println("\t---WHILE PRECOMPUTING FOR OBJECTIVE -> NOTHING FOUND -> DEFAULT: SCOUTING!---");
            objectiveCoordinates = findObjectiveInMemory(null, personalCoordinates);
            if (objectiveCoordinates == personalCoordinates) {  // not known
                if(currentObjective != null){
                    isScoutingForObject = true;
                }
                else{
                    System.out.println("every null has been cleared");
                    return listOfDirectionsToObjective;
                }
            }
        }

        listOfDirectionsToObjective = computeRoute(objectiveCoordinates, personalCoordinates);
        stepOfTheGivenDirectionList = 0;    // gerade neue Route berechnet -> bei erstem Schritt anfangen (Bei Liste = 0)
        isScoutingForObject = false;
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
        Boolean ObjectiveFound = false;
        System.out.println("\t---SEARCHING IN MEMORY FOR " + currentObjective + "---");
        
        // check vicinity 1/2/3... circle principle
        for (int surrounding = 1; !ObjectiveFound && surrounding < Math.floor(XCOORDINATEMAX / 2); surrounding++) {
            //System.out.println("Checking surroundings with distance: " + surrounding);
            objectiveCoordinates = checkVicinityForObjective(currentObjective, personalCoordinates, surrounding); 
            // if objective Coordiantes are showing the current objective
            if (objectiveCoordinates.getX() != personalCoordinates.getX() && objectiveCoordinates.getY() != personalCoordinates.getY()) { 
                System.out.println("OBJECTIVE FOUND IN MEMORY \t\t\t---- FOUND COORDINATES! --------");
                System.out.println("objective at found Coordinates: " + clubMemory[objectiveCoordinates.getX()][objectiveCoordinates.getY()]);
                System.out.println("objectiveCoordinateX in Memory: " + objectiveCoordinates.getX() + "\nobjectiveCoordinateY in Memory: " + objectiveCoordinates.getY());
                ObjectiveFound = true;
            }
            else{
                //System.out.println("OBJECTIVE NOT FOUND IN MEMORY");
                if(surrounding == Math.floor(XCOORDINATEMAX / 2) - 1){
                    System.out.println("OBJECTIVE DEFINETLY NOT IN MEMORY!");
                }
            }
        }

        System.out.println("findOBjectiveInMEmoryLäuftDurch"); 
        return objectiveCoordinates;
    }

    Coordinate checkVicinityForObjective(SpaceType currentObjective, Coordinate personalCoordinates, int surrounding) {
        Coordinate objectiveCoordinates = new Coordinate(personalCoordinates.getX(), personalCoordinates.getY());
        int personalMinusSurroundingX = personalCoordinates.getX() - surrounding;
        int personalMinusSurroundingY = personalCoordinates.getY() - surrounding;
        int xAddition = 0;
        int yAddition = 0;

        //System.out.println("\t---StartOf checkVicinity()--- ");
        //System.out.println("personalMinusSurroundingX + xAddition: " + (personalMinusSurroundingX + xAddition));
        //System.out.println("personalMinusSurroundingY + yAddition: " + (personalMinusSurroundingY + yAddition));

        //System.out.println("\t\t--start of for i++ -- ");
        for (xAddition = 0; xAddition < (surrounding * 2 + 1); xAddition++) {

            if ((personalMinusSurroundingX + xAddition) < XCOORDINATEMAX
                    && (personalMinusSurroundingX + xAddition) >= 0
                    && (personalMinusSurroundingY + yAddition) < YCOORDINATEMAX
                    && (personalMinusSurroundingY + yAddition) >= 0) {
                //printfCheckVicinity(xAddition, yAddition, personalMinusSurroundingX, personalMinusSurroundingY);
                if (clubMemory[personalMinusSurroundingX + xAddition][personalMinusSurroundingY
                        + yAddition] == currentObjective) {
                    System.out.println("Current Objective FOUND IN MEMORY!!!!!!!!!!!!!!!!!!!!!!!");
                    objectiveCoordinates.setX(personalMinusSurroundingX + xAddition);
                    objectiveCoordinates.setY(personalMinusSurroundingY + yAddition);
                    return objectiveCoordinates;
                }
            }
        }
        //System.out.println("\t\t--start of for j++ -- ");
        xAddition--;
        for (yAddition = 0; yAddition < (surrounding * 2 + 1); yAddition++) {
            if (personalMinusSurroundingX + xAddition < XCOORDINATEMAX
                    && personalMinusSurroundingX + xAddition >= 0
                    && personalMinusSurroundingY + yAddition < YCOORDINATEMAX
                    && personalMinusSurroundingY + yAddition >= 0) {
                //printfCheckVicinity(xAddition, yAddition, personalMinusSurroundingX, personalMinusSurroundingY);
                if (clubMemory[personalMinusSurroundingX + xAddition][personalMinusSurroundingY
                        + yAddition] == currentObjective) {
                    System.out.println("Current Objective FOUND IN MEMORY!!!!!!!!!!!!!!!!!!!!!!!");
                    objectiveCoordinates.setX(personalMinusSurroundingX + xAddition);
                    objectiveCoordinates.setY(personalMinusSurroundingY + yAddition);
                    return objectiveCoordinates;
                }
            }
        }
        yAddition--;
        //System.out.println("\t\t--start of for i-- -- ");
        for (xAddition = (surrounding * 2); xAddition >= 0; xAddition--) {
            if(personalMinusSurroundingX + xAddition < XCOORDINATEMAX 
                && personalMinusSurroundingX + xAddition >= 0
                && personalMinusSurroundingY + yAddition < YCOORDINATEMAX
                && personalMinusSurroundingY + yAddition >= 0){
                //printfCheckVicinity(xAddition, yAddition, personalMinusSurroundingX, personalMinusSurroundingY);
                if (clubMemory[personalMinusSurroundingX + xAddition][personalMinusSurroundingY + yAddition] == currentObjective) {
                    System.out.println("Current Objective FOUND IN MEMORY!!!!!!!!!!!!!!!!!!!!!!!");
                    objectiveCoordinates.setX(personalMinusSurroundingX + xAddition);
                    objectiveCoordinates.setY(personalMinusSurroundingY + yAddition);
                    return objectiveCoordinates;
                }
            }
        }
        xAddition++;
        //System.out.println("\t\t--start of for j-- -- ");
        for (yAddition = (surrounding * 2); yAddition > 0; yAddition--) {
            if(personalMinusSurroundingX + xAddition < XCOORDINATEMAX 
                && personalMinusSurroundingX + xAddition >= 0
                && personalMinusSurroundingY + yAddition < YCOORDINATEMAX
                && personalMinusSurroundingY + yAddition >= 0){
                //printfCheckVicinity(xAddition, yAddition, personalMinusSurroundingX, personalMinusSurroundingY);
                if (clubMemory[personalMinusSurroundingX + xAddition][personalMinusSurroundingY + yAddition] == currentObjective) {
                    System.out.println("Current Objective FOUND IN MEMORY!!!!!!!!!!!!!!!!!!!!!!!");
                    objectiveCoordinates.setX(personalMinusSurroundingX + xAddition);
                    objectiveCoordinates.setY(personalMinusSurroundingY + yAddition);
                    return objectiveCoordinates;
                }
            }
        }
        yAddition++;
        //System.out.println("Finished checking surroundings with distance "+surrounding+"---- FOUND NOTHING ");
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
        System.out.println("\t---Begin updating ClubMemory---");
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
        System.out.println("\t---ClubMemory updated---");
    }

    public SpaceType decideNextMovement() {
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
