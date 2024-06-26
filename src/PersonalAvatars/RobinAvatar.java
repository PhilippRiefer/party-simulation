package PersonalAvatars;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import AvatarInterface.SuperAvatar;
import Environment.Coordinate;
import Environment.Direction;
import Environment.SpaceInfo;
import Environment.SpaceType;
import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;

public class RobinAvatar extends SuperAvatar {

    enum State {
        FIND_WALL,
        FOLLOW_WALL,
        FIND_EMPTY,
        MOVE_TO_EMPTY,
        FULLY_EXPLORED,
        MOVE_TO_NEEDED,
        FILL_NEED
    }

    enum PersonalFieldType {
        EMPTY(true, false),
        JUST_SEEN(true, false),
        WALKED(true, false),
        WALL(false, false),
        UNKNOWN(false, true),
        REACHABLE(false, true),
        AVATAR(false,false);

        private final boolean walkable;
        private final boolean unknown;

        PersonalFieldType(boolean walkable, boolean unknown) {
            this.walkable = walkable;
            this.unknown = unknown;
        }

        public boolean isWalkable() {
            return walkable;
        }

        public boolean isUnknown() {
            return unknown;
        }

    }

    private final int environmentWidth = 40;
    private final int environmentHeight = 20;
    private int[][][] environment;
    private Coordinate position;
    private State state;
    private Direction lastDirection;
    private Direction lastWall;
    private Coordinate destination;
    private boolean destValid;
    private Direction startDirection;
    private Coordinate extPosition;
    private boolean extPositionValid;
    private PersonalFieldType[] PFTValues = PersonalFieldType.values();
    private SpaceType[] STValues = SpaceType.values();
    //private int uncheckedSpaces;
    private Vector<Direction> path;
    private int cycle;
    private int[][] needs;
    private Random rng;
    private int currentNeed;
    private boolean newPath;
    private int stuckCycles;
    private Vector<int[]> avatarSpaces;

    public RobinAvatar(int id, int perceptionRange, Color color) {
        super(id, 1, Color.GREEN);
        environment = new int[2 * environmentWidth + 1][2 * environmentHeight + 1][3];
        position = new Coordinate(environmentWidth, environmentHeight);
        state = State.FIND_WALL;
        lastDirection = Direction.UP;
        destination = new Coordinate(0, 0);
        destValid = false;
        startDirection = Direction.UP;
        extPosition = new Coordinate(0, 0);
        extPositionValid = false;
        //uncheckedSpaces = 0;
        path = new Vector<Direction>();
        avatarSpaces = new Vector<int[]>();
        cycle = 0;
        currentNeed = -1;
        rng = new Random();
        newPath = false;
        stuckCycles = 0;
        initializeNeeds();
        initializeEnvironment();
    }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        //TODO: Vortrag
        createTxtFile();
        cycle++;
        updatePosition();
        updateNeeds();
        checkNeeds();
        if (!(getCouldMove() || newPath || lastDirection == Direction.STAY)){
            if(stuckCycles > 2){
                stuckCycles = 0;
                if(state == State.MOVE_TO_EMPTY || state == State.FIND_WALL || state == State.FOLLOW_WALL){
                    state = State.FIND_EMPTY;
                }
                else if(state == State.MOVE_TO_NEEDED){
                    newPath = true;
                    findPath(needs[currentNeed][0], 0);
                }
            }
            else{
                stuckCycles++;
                return lastDirection;
            }        
        }
        stuckCycles = 0;  
        newPath = false;
        updateEnvironment(spacesInRange);
        // printEnv(1);
        if(!(PFTValues[getFromEnvironment(Direction.UP, 1)].isWalkable() || PFTValues[getFromEnvironment(Direction.RIGHT, 1)].isWalkable() || PFTValues[getFromEnvironment(Direction.DOWN, 1)].isWalkable() || PFTValues[getFromEnvironment(Direction.LEFT, 1)].isWalkable())){
            lastDirection = Direction.STAY;
            return Direction.STAY;
        }
        switch (state) {
            case FIND_WALL:
                return findWall();
            case FOLLOW_WALL:
                return followWall();
            case FIND_EMPTY:
                return findEmpty();
            case MOVE_TO_EMPTY:
                return moveToEmpty();
            case MOVE_TO_NEEDED:
                return moveToNeeded();
            case FULLY_EXPLORED:
                lastDirection = Direction.STAY;
                return Direction.STAY;
            case FILL_NEED:
                if(needs[currentNeed][0] == SpaceType.DANCEFLOOR.ordinal()){
                    return dance();
                }
                lastDirection = Direction.STAY;
                return Direction.STAY;
            default:
                //System.out.println("done");
                printEnv(0);
                printEnv(1);
                //System.out.println(cycle);
                lastDirection = Direction.STAY;
                return Direction.STAY;
        }
    }

    private void initializeEnvironment() {
        for (int row = 0; row < environment[0].length; row++) {
            for (int col = 0; col < environment.length; col++) {
                environment[col][row][2] = 0;
                environment[col][row][1] = PersonalFieldType.UNKNOWN.ordinal();
                environment[col][row][0] = SpaceType.EMPTY.ordinal();
            }
        }
    }

    private void initializeNeeds() {
        needs = new int[4][2];
        needs[0][0] = SpaceType.BAR.ordinal();
        needs[1][0] = SpaceType.DANCEFLOOR.ordinal();
        needs[2][0] = SpaceType.SEATS.ordinal();
        needs[3][0] = SpaceType.TOILET.ordinal();
        for (int index = 0; index < needs.length; index++) {
            needs[index][1] = 100;
        }
    }

    private void updateNeeds() {
        for (int i = 0; i < needs.length; i++) {
            if (getFromEnvironment(position, 0) == needs[i][0] && needs[i][1] < 100) {
                if (true) {
                    needs[i][1]++;
                }
            } 
            else if(needs[i][0] == SpaceType.DANCEFLOOR.ordinal() && getFromEnvironment(position, 0) == SpaceType.BAR.ordinal())
            {
                if (rng.nextInt(100) < 40 && needs[i][1] > 0) {
                    needs[i][1]--;
                }
            }
            else if(needs[i][0] == SpaceType.BAR.ordinal() && getFromEnvironment(position, 0) == SpaceType.DANCEFLOOR.ordinal())
            {
                if (rng.nextInt(100) < 40 && needs[i][1] > 0) {
                    needs[i][1]--;
                }
            }
            else if(needs[i][0] == SpaceType.SEATS.ordinal() && getFromEnvironment(position, 0) == SpaceType.DANCEFLOOR.ordinal())
            {
                if (rng.nextInt(100) < 40 && needs[i][1] > 0) {
                    needs[i][1]--;
                }
            }
            else if(needs[i][0] == SpaceType.SEATS.ordinal() && getFromEnvironment(position, 0) == SpaceType.TOILET.ordinal())
            {
                if (rng.nextInt(100) < 40 && needs[i][1] < 100) {
                    needs[i][1]++;
                }
            }
            else if(needs[i][0] == SpaceType.TOILET.ordinal() && getFromEnvironment(position, 0) == SpaceType.BAR.ordinal())
            {
                if (rng.nextInt(100) < 40 && needs[i][1] > 0) {
                    needs[i][1]--;
                }
            }
            else
            {
                if (rng.nextInt(100) < 20 && needs[i][1] > 0) {
                    needs[i][1]--;
                }
            }
        }
    }

    private void checkNeeds() {
        int minNeed = 0;
        int minAmmount = 101;
        for (int i = 0; i < needs.length; i++) {
            if (needs[i][1] < minAmmount) {
                minAmmount = needs[i][1];
                minNeed = i;
            }
        }
        if (minAmmount < 20 || state == State.FULLY_EXPLORED) {
            if (minNeed != currentNeed) {
                if (currentNeed < 0) {
                    changeNeed(minNeed);
                } else {
                    if (!(((state == State.FILL_NEED) || (state == State.MOVE_TO_NEEDED)) && needs[currentNeed][1] < 50)) {
                        changeNeed(minNeed);
                    }
                }
            }
        }
        if (currentNeed >= 0) {
            if (needs[currentNeed][1] >= 100) {
                state = State.FIND_EMPTY;
            }
        }
    }

    private void changeNeed(int need) {
        currentNeed = need;
        //System.out.println(need);
        newPath = true;
        if (getFromEnvironment(position, 0) == needs[need][0]) {
            state = State.FILL_NEED;
        } else if (exists(needs[need][0])) {
            state = State.MOVE_TO_NEEDED;
            findPath(needs[need][0], 0);
        }
    }

    private boolean exists(int spaceType) {
        for (int row = 0; row < environment[0].length; row++) {
            for (int col = 0; col < environment.length; col++) {
                if (environment[col][row][0] == spaceType) {
                    return true;
                }
            }
        }
        return false;
    }

    private void printEnv(int entry) {
        System.out.println("Entry: " + entry);
        System.out.println("--------------------------------------------------------");
        for (int row = 0; row < environment[0].length; row++) {
            for (int col = 0; col < environment.length; col++) {
                System.out.print(environment[col][row][entry]);
            }
            System.out.print("\n");
        }
        System.out.println("--------------------------------------------------------");
    }

    private Direction coordinateToDirection(Coordinate coordinate) {
        if (coordinate.getY() < 0)
            return Direction.UP;
        if (coordinate.getY() > 0)
            return Direction.DOWN;
        if (coordinate.getX() < 0)
            return Direction.LEFT;
        if (coordinate.getX() > 0)
            return Direction.RIGHT;
        return Direction.STAY;
    }

    private Coordinate directionToCoordinate(Direction direction) {
        switch (direction) {
            case UP:
                return new Coordinate(0, -1);
            case RIGHT:
                return new Coordinate(1, 0);
            case DOWN:
                return new Coordinate(0, 1);
            case LEFT:
                return new Coordinate(-1, 0);
            default:
                return new Coordinate(0, 0);
        }
    }

    private Coordinate addCoordinates(Coordinate coordinate1, Coordinate coordinate2) {
        return new Coordinate(coordinate1.getX() + coordinate2.getX(), coordinate1.getY() + coordinate2.getY());
    }

    private Coordinate subCoordinates(Coordinate coordinate1, Coordinate coordinate2) {
        return new Coordinate(coordinate1.getX() - coordinate2.getX(), coordinate1.getY() - coordinate2.getY());
    }

    private int getFromEnvironment(Coordinate pos, int entry) {
        return environment[pos.getX()][pos.getY()][entry];
    }

    private int getFromEnvironment(Direction dir, int entry) {
        return getFromEnvironment(addCoordinates(position, directionToCoordinate(dir)), entry);
    }

    private void setInEnvironment(Coordinate pos, int entry, int value) {
        environment[pos.getX()][pos.getY()][entry] = value;
    }

    private void setInEnvironment(Direction dir, int entry, int value) {
        setInEnvironment(addCoordinates(position, directionToCoordinate(dir)), entry, value);
    }

    private void findExternalCoordinate(ArrayList<SpaceInfo> spacesInRange) {
        int minX = environmentWidth;
        int maxX = 0;
        int minY = environmentHeight;
        int maxY = 0;
        for (SpaceInfo spaceInfo : spacesInRange) {
            int x = spaceInfo.getRelativeToAvatarCoordinate().getX();
            int y = spaceInfo.getRelativeToAvatarCoordinate().getY();
            if (x < minX)
                minX = x;
            if (x > maxX)
                maxX = x;
            if (y < minY)
                minY = y;
            if (y > maxY)
                maxY = y;
        }
        extPosition = new Coordinate((maxX + minX) / 2, (maxY + minY) / 2);
        extPositionValid = true;
    }

    private Coordinate absToRelPos(Coordinate absPos) {
        return subCoordinates(absPos, extPosition);
    }

    private Direction rotate90Clkw(Direction direction, int times) {
        if (times == 0)
            return direction;
        switch (direction) {

            case UP:
                return rotate90Clkw(Direction.RIGHT, times - 1);
            case RIGHT:
                return rotate90Clkw(Direction.DOWN, times - 1);
            case DOWN:
                return rotate90Clkw(Direction.LEFT, times - 1);
            case LEFT:
                return rotate90Clkw(Direction.UP, times - 1);
            default:
                return Direction.STAY;
        }
    }

    private void updateReachable(Coordinate pos) {
        if (!(PFTValues[getFromEnvironment(pos, 1)].isWalkable()))
            return;
        for (Direction dir : Direction.values()) {
            Coordinate spaceAbsPos = addCoordinates(pos, directionToCoordinate(dir));
            if (PFTValues[getFromEnvironment(spaceAbsPos, 1)].isUnknown()) {
                setInEnvironment(spaceAbsPos, 1, PersonalFieldType.REACHABLE.ordinal());
            }
        }
    }

    private void updateEnvironment(ArrayList<SpaceInfo> spacesInRange) {
        if (!extPositionValid)
            findExternalCoordinate(spacesInRange);
        while (avatarSpaces.size() > 0) {
            environment[avatarSpaces.elementAt(0)[0]][avatarSpaces.elementAt(0)[1]][1] = avatarSpaces.elementAt(0)[2];
            avatarSpaces.removeElementAt(0);
        }
        for (int row = 0; row < environment[0].length; row++) {
            for (int col = 0; col < environment.length; col++) {
                if(environment[col][row][1] == PersonalFieldType.JUST_SEEN.ordinal()){
                    environment[col][row][1] = PersonalFieldType.WALKED.ordinal();
                }
                if(environment[col][row][1] == PersonalFieldType.EMPTY.ordinal()){
                    environment[col][row][1] = PersonalFieldType.JUST_SEEN.ordinal();
                }
                
            }
        }

        for (SpaceInfo spaceInfo : spacesInRange) {
            Coordinate spaceRelPos = absToRelPos(spaceInfo.getRelativeToAvatarCoordinate());
            Coordinate spaceAbsPos = addCoordinates(position, spaceRelPos);
            if (spaceInfo.getType() == SpaceType.AVATAR) {
                avatarSpaces.add(new int[]{spaceAbsPos.getX(), spaceAbsPos.getY(), getFromEnvironment(spaceAbsPos, 1)});
                setInEnvironment(spaceAbsPos, 1, PersonalFieldType.AVATAR.ordinal());
            }
            else if (PFTValues[getFromEnvironment(spaceAbsPos, 1)].isUnknown()) {
                    setInEnvironment(spaceAbsPos, 0, spaceInfo.getType().ordinal());
                    if (spaceInfo.getType() != SpaceType.OBSTACLE) {
                        setInEnvironment(spaceAbsPos, 1, PersonalFieldType.EMPTY.ordinal());
                    } else {
                        setInEnvironment(spaceAbsPos, 1, PersonalFieldType.WALL.ordinal());
                    }
                updateReachable(spaceAbsPos);
            }
        }
    }

    private void updatePosition() {
        if (getCouldMove()) {
            position = addCoordinates(position, directionToCoordinate(lastDirection));
            extPosition = addCoordinates(extPosition, directionToCoordinate(lastDirection));
        }
    }

    

    private Direction findWall() {
        setInEnvironment(position, 1, PersonalFieldType.WALKED.ordinal());
        if (getFromEnvironment(startDirection, 1) == PersonalFieldType.WALL.ordinal()) {
            lastWall = startDirection;
            state = State.FOLLOW_WALL;
            return followWall();
        } else {
            lastDirection = startDirection;
            return startDirection;
        }
    }


    //TODO Vortrag............................................................................................

    //Die nächste Richtung zurückgeben, um an der Wand entlang zu laufen
    private Direction followWall() {
        setInEnvironment(position, 1, PersonalFieldType.WALKED.ordinal()); //aktuelles Feld als betreten markieren
        for (int i = 0; i < 4; i++) { //alle vier Richtungen durchgehen
            int entry = getFromEnvironment(rotate90Clkw(lastWall, i), 1); //die angrenzenden Felder im Uhrzeigersinn angefangen
            //mit der letzten Wand durchgehen
            if (entry == PersonalFieldType.EMPTY.ordinal() || entry == PersonalFieldType.JUST_SEEN.ordinal()) { //In die erste freie Richtung bewegen
                lastDirection = rotate90Clkw(lastWall, i); //Die aktuell überprüfte Richtung ist die neue Richtung
                lastWall = rotate90Clkw(lastWall, 3 + i); //Die letzte Wand liegt links von der neuen Richtung
                return lastDirection; //Die neue Richtung zurückgeben
            }
        }
        // printEnv();
        state = State.FIND_EMPTY;
        return findEmpty();
    }

    private Direction findEmpty() {
        // printEnv(1);
        for (int row = 0; row < environment[0].length; row++) {
            for (int col = 0; col < environment.length; col++) {
                if (environment[col][row][1] == PersonalFieldType.REACHABLE.ordinal()) {
                    state = State.MOVE_TO_EMPTY;
                    newPath = true;
                    findPath(PersonalFieldType.REACHABLE.ordinal(), 1);
                    return moveToEmpty();
                }
            }
        }
        state = State.FULLY_EXPLORED;
        lastDirection = Direction.STAY;
        return Direction.STAY;
    }

    private Direction moveToEmpty() {
        // System.out.println("move to empty");
        setInEnvironment(position, 1, PersonalFieldType.WALKED.ordinal());
        if (path.size() == 0) {
            lastWall = rotate90Clkw(lastDirection, 2);
            state = State.FOLLOW_WALL;
            return followWall();
        }
        lastDirection = path.elementAt(0);
        path.removeElementAt(0);
        return lastDirection;
    }

    private Direction moveToNeeded() {
        // System.out.println("move to empty");
        setInEnvironment(position, 1, PersonalFieldType.WALKED.ordinal());
        if (path.size() == 0) {
            state = State.FILL_NEED;
            lastDirection = Direction.STAY;
            return Direction.STAY;
        }
        lastDirection = path.elementAt(0);
        path.removeElementAt(0);
        return lastDirection;
    }

    
//TODO Vortrag............................................................................................

//Den kürzesten Weg zum nächsten Feld finden, bei dem Eintrag goalType den Wert goal hat
    private boolean findPath(int goal, int goalType) {
        boolean retVal = true; //Standardmäßig true zurückgeben, außer es konnte kein Weg gefunden werden
        destValid = false; //Die destination wird erst valid gesetzt wenn das nächste Feld gefunden wurde

        //pathfinding array auf 0 setzen
        for (int row = 0; row < environment[0].length; row++) {
            for (int col = 0; col < environment.length; col++) {
                environment[col][row][2] = 0;
            }
        }
        // printEnv(2);
        setInEnvironment(position, 2, 1); //Beim aktuellen Feld 1 eintragen
        if (fillAround(position, 2, goal, goalType)) { //angrenzend 2 eintragen
            int i = 2;
            for (i = 2; i <= environmentHeight * environmentWidth && !destValid; i++) { //Abbruchbedingung wenn ein Ziel gfunden wurde oder
                //so viele Zahöen eingetragen wurden, dass schon alles voll sein müsste
                boolean keepGoing = false; //muss in den inneren Schleifen auf true gesetzt werden, damit es weiter geht
                
                //Alle Felder durchlaufen
                for (int row = 0; row < environment[0].length && !destValid; row++) {
                    for (int col = 0; col < environment.length && !destValid; col++) {
                        if (environment[col][row][2] == i) //Wenn die aktuelle Zahl gefunden wurde
                            if (fillAround(new Coordinate(col, row), i + 1, goal, goalType)) //die angrenzenden Felder mit der nächsten Zahl füllen
                                keepGoing = true; //wenn irgendetwas eingetragen wurde geht es weiter, sonst ist alles voll ohne dass ein Weg gefunden wurde
                    }
                }
                if (!keepGoing) {
                    destination = position; // No path found
                    destValid = true;
                    retVal = false;
                }
            }
            // printEnv(2);
            if (!destValid) {
                destination = position; // No path found
                destValid = true;
                retVal = false;
            }
            // System.out.println("Save Path"); //save Path
            path.clear(); //Neuen Weg berechnen, erstmal den alten leeren
            for (i--; i > 0; i--) { //von der letzen eingetragenen Zahl rückwärts gehen
                for (int j = 0; j < 4; j++) { //Alle angrenzenden Felder durchghen
                    Coordinate space = addCoordinates(destination, directionToCoordinate(rotate90Clkw(Direction.UP, j))); //aktulles Feld zwischenspeichern
                    if (getFromEnvironment(space, 2) == i) {//auf aktuell geprüfte Zahl prüfen
                        path.add(0, rotate90Clkw(Direction.UP, j + 2)); //Die entsprechende Richtung dem Path hinzufügen
                        destination = space; //Das Feld ist das neu Ziel zu dem der Weg berechnet werden muss
                        break;
                    }
                }
            }
            // System.out.println(path);
            return retVal;
        } else {
            path.clear();
            return false;
        }

    }

    private boolean fillAround(Coordinate center, int value, int goal, int goalType) {
        boolean retVal = false;
        for (int i = 0; i < 4; i++) {
            Coordinate space = addCoordinates(center, directionToCoordinate(rotate90Clkw(Direction.UP, i)));
            if (getFromEnvironment(space, goalType) == goal && (goalType != 0 || PFTValues[getFromEnvironment(space, 1)].walkable)) {
                if (!destValid) {
                    destination = space;
                    destValid = true;
                }
                // printEnv(2);
                return true;
            }
            if (getFromEnvironment(space, 2) == 0 && PFTValues[getFromEnvironment(space, 1)].walkable) {
                setInEnvironment(space, 2, value);
                retVal = true;
            }
        }
        // printEnv(2);
        return retVal;
    }

    private Direction dance(){
        if(true){
            int randNum = rng.nextInt(4);
            for (int i = randNum; i < randNum + 4; i++) {
                Direction randDir = rotate90Clkw(Direction.UP, i);
                if(getFromEnvironment(randDir, 0) == SpaceType.DANCEFLOOR.ordinal() && PFTValues[getFromEnvironment(randDir, 1)].walkable){
                    lastDirection = randDir;
                    return randDir;
                }
            }
        }
        lastDirection = Direction.STAY;
        return Direction.STAY;
    }

    @Override
    public int getPerceptionRange() {
        return super.getPerceptionRange(); // Assuming SuperAvatar has a method to get the perception range
    }

    @Override
    public void setPerceptionRange(int perceptionRange) {
        super.setPerceptionRange(perceptionRange); // Set the perception range via the superclass method
    }

    private void createTxtFile() {
        try (FileWriter writer = new FileWriter("RobinBrain.txt")) {
            writer.write("Cycles: " + cycle + "\n");
            writer.write("State: " + state + "\n");
            if(lastDirection == Direction.STAY){
                writer.write("Didn't try to move.\n");
            }
            else{
                writer.write("Tried to move " + lastDirection + "\n");
            }
            if (path.isEmpty()) {
                writer.write("Path unknown.\n");
            } else {
                writer.write("Path: " + path + "\n");
            }
            writer.write("Needs:\n");
            for (int[] need : needs) {
                writer.write("\t" + STValues[need[0]] + ":\t" + need[1] + "\n");
            }
            writer.write("Focussing on ");
            if(state == State.FIND_WALL || state == State.FOLLOW_WALL || state == State.FIND_EMPTY || state == State.MOVE_TO_EMPTY){
                writer.write("exploring\n");
            }
            else{
                if(currentNeed == -1){
                    writer.write("no paticular need\n");
                }
                else{
                    writer.write(STValues[needs[currentNeed][0]].toString() + "\n");
                }
            }
            if(stuckCycles == 0){
                writer.write("Moved as planed.\n");
            } else {
                writer.write("Movement blocked for " + stuckCycles + " cycles.\n");
            }

            writer.write("Space Types:\n");
            for (int row = 0; row < environment[0].length; row++) {
                for (int col = 0; col < environment.length; col++) {
                    writer.write(spaceTypeToChar(STValues[environment[col][row][0]]));
                }
                writer.write("\n");
            }

            writer.write("Personal Field Types:\n");
            for (int row = 0; row < environment[0].length; row++) {
                for (int col = 0; col < environment.length; col++) {
                    writer.write(personalFieldTypeToChar(PFTValues[environment[col][row][1]]));
                }
                writer.write("\n");
            }

            writer.write("Spaces blocked by Avatars: ");
            for (int[] avatarSpace : avatarSpaces) {
                writer.write("(" + avatarSpace[0] + "," + avatarSpace[1] + "): " + personalFieldTypeToChar(PFTValues[avatarSpace[2]]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private char spaceTypeToChar(SpaceType type) {
        switch (type) {
            case EMPTY:
                return (' ');
            case OBSTACLE:
                return ('O');
            case AVATAR:
                return ('A');
            case DANCEFLOOR:
                return ('D');
            case DJBOOTH:
                return ('J');
            case TOILET:
                return ('T');
            case BAR:
                return ('B');
            case SEATS:
                return ('S');
            default:
                return ('?');
        }
    }

    private char personalFieldTypeToChar(PersonalFieldType type) {
        switch (type) {
            case EMPTY:
                return ('E');
            case JUST_SEEN:
                return ('J');
            case WALKED:
                return ('W');
            case WALL:
                return ('O');
            case UNKNOWN:
                return (' ');
            case REACHABLE:
                return ('R');
            case AVATAR:
                return ('A');
            default:
                return ('?');
        }
    }

}