package PersonalAvatars;

import Environment.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Comparator;
import AvatarInterface.*;
import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class TomAvatar extends SuperAvatar {

    private ArrayList<SpaceInfo> mentalMapList = new ArrayList<>();
    private ArrayList<SpaceInfo> dancefloorList = new ArrayList<>();
    private ArrayList<SpaceInfo> toiletList = new ArrayList<>();
    private ArrayList<SpaceInfo> barList = new ArrayList<>();
    private ArrayList<SpaceInfo> seatsList = new ArrayList<>();
    private char [][] mapData = new char[20][40];
    private int storePerceptionRange = 0;
    private int phase = 0, stepCounter = 0, directionCounter = 0;
    private Direction myDirection = Direction.STAY;
    private Direction myDirectionStorage = Direction.STAY;
    private Direction myDirectionMoveBack = Direction.STAY;
    private static final Random random = new Random();
    private Coordinate myPosition = new Coordinate(0, 0);
    private Coordinate foundCoordinate = new Coordinate(0, 0);
    private int stayCounter = 0;
    private int decision = (int) (random.nextDouble() * 100);
    private int merker = 0;
    private int counterHitAvatar = 0;
    private int xDancefloor = 0, yDancefloor = 0;
    private int merkerDirection = 0;


    public TomAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);
        storePerceptionRange = perceptionRange;

    }

    public void createMentalMap(ArrayList<SpaceInfo> spacesInRange) {
        for (SpaceInfo infoForMentalMap : spacesInRange) {
            if (infoForMentalMap.getType() != SpaceType.EMPTY && infoForMentalMap.getType() != SpaceType.OBSTACLE && infoForMentalMap.getType() != SpaceType.AVATAR)
                mentalMapList.add(infoForMentalMap);
        }
    }

    private void removeDuplicateCoordinates() {
        Set<Coordinate> seenCoordinates = new HashSet<>();
        ArrayList<SpaceInfo> uniqueMentalMapList = new ArrayList<>();

        for (SpaceInfo spaceInfo : mentalMapList) {
            if (seenCoordinates.add(spaceInfo.getRelativeToAvatarCoordinate())) {
                uniqueMentalMapList.add(spaceInfo);
            }
        }

        mentalMapList = uniqueMentalMapList;
    }

    private void sortMentalMapListByType(ArrayList<SpaceInfo> spacesInRange) {
        for(SpaceInfo infos : mentalMapList){
            if(infos.getType() == SpaceType.DANCEFLOOR){
                dancefloorList.add(infos);
            }else if (infos.getType() == SpaceType.BAR){
                barList.add(infos);
            }else if(infos.getType() == SpaceType.TOILET){
                toiletList.add(infos);
            }else if(infos.getType() == SpaceType.SEATS){
                seatsList.add(infos);
            }
        }
    }

    public Direction goToStart(ArrayList<SpaceInfo> spacesInRange) {

        if (spacesInRange.get(1).getType() == SpaceType.OBSTACLE) {
            if (spacesInRange.get(3).getType() == SpaceType.OBSTACLE) {
                phase = 2;
                return Direction.STAY;
            }
            return Direction.UP;

        } else {
            return Direction.LEFT;
        }

    }

    public Direction startMapWalk(ArrayList<SpaceInfo> spacesInRange) {

        if (spacesInRange.get(4).getType() == SpaceType.OBSTACLE &&
                spacesInRange.get(6).getType() == SpaceType.OBSTACLE) {
            stepCounter = 0;
            phase = 3;
        }

        switch (directionCounter) {
            case 0:
                if (spacesInRange.get(6).getType() == SpaceType.OBSTACLE) {
                    directionCounter = 1;
                    return Direction.DOWN;
                }
                return Direction.RIGHT;
            case 1:
                stepCounter++;
                if (stepCounter == (storePerceptionRange * 2) + 1
                        || spacesInRange.get(4).getType() == SpaceType.OBSTACLE) {
                    stepCounter = 0;
                    directionCounter = 2;
                    return Direction.LEFT;
                }
                return Direction.DOWN;
            case 2:
                if (spacesInRange.get(1).getType() == SpaceType.OBSTACLE) {
                    directionCounter = 3;
                    return Direction.DOWN;
                }
                return Direction.LEFT;
            case 3:
                stepCounter++;
                if (stepCounter == (storePerceptionRange * 2) + 1
                        || spacesInRange.get(4).getType() == SpaceType.OBSTACLE) {
                    stepCounter = 0;
                    directionCounter = 0;
                    return Direction.RIGHT;
                }
                return Direction.DOWN;
        }

        return Direction.STAY;
    }

    public Direction hitAvatarMoveAway(ArrayList<SpaceInfo> spacesInRange) {

        if(merker == 0){
        if (myDirection == Direction.RIGHT && spacesInRange.get(6).getType() == SpaceType.AVATAR
                && spacesInRange.get(4).getType() != SpaceType.OBSTACLE) {
            merker = 1;
            return Direction.DOWN;

        } else if (myDirection == Direction.LEFT && spacesInRange.get(1).getType() == SpaceType.AVATAR
                && spacesInRange.get(3).getType() != SpaceType.OBSTACLE) {
            merker = 2;
            return Direction.UP;
        }
        
        else if (myDirection == Direction.UP && spacesInRange.get(3).getType() == SpaceType.AVATAR
                && spacesInRange.get(6).getType() != SpaceType.OBSTACLE) {
            merker = 3;
            return Direction.RIGHT;

        } else if (myDirection == Direction.DOWN && spacesInRange.get(4).getType() == SpaceType.AVATAR
                && spacesInRange.get(1).getType() != SpaceType.OBSTACLE) {
            merker = 4;
            return Direction.LEFT;
        }
    }
       
        if (merker == 1 && spacesInRange.get(3).getType() != SpaceType.AVATAR) {
            merker = 0;
            return Direction.UP;
        } else if (merker == 2 && spacesInRange.get(4).getType() != SpaceType.AVATAR) {
            merker = 0;
            return Direction.DOWN;
        } else if (merker == 3 && spacesInRange.get(6).getType() != SpaceType.AVATAR) {
            merker = 0;
            return Direction.LEFT;
        } else if (merker == 4 && spacesInRange.get(1).getType() != SpaceType.AVATAR) {
            merker = 0;
            return Direction.RIGHT;
        }
        
        return Direction.STAY;
    }

    public Direction hitAvatarOnWay(ArrayList<SpaceInfo> spacesInRange) {

        if(merker == 0){
            if(conflictDetected(spacesInRange, myDirection) == true){
                merker = 1;
                myDirectionStorage = goDancing();
                return myDirectionStorage;
            }
        }   

        if(merker == 1){
            merker = 0;
            if(myDirectionStorage == Direction.RIGHT){
                return Direction.LEFT;
            }else if(myDirectionStorage == Direction.LEFT){
                return Direction.RIGHT;
            }else if(myDirectionStorage == Direction.UP){
                return Direction.DOWN;
            }else if(myDirectionStorage == Direction.DOWN){
                return Direction.UP;
            }
        }
        return Direction.STAY;
    }

    
    public Direction takingDecision(ArrayList<SpaceInfo> spacesInRange) {
        
        if (decision <= 40) {
            myDirection = goToSomething2(spacesInRange, barList, SpaceType.BAR, 50);
        } else if (decision > 40 && decision <= 70) {
            myDirection = goToSomething2(spacesInRange, dancefloorList, SpaceType.DANCEFLOOR, 70);
        }else if(decision > 70 && decision <= 90){
            myDirection = goToSomething2(spacesInRange, seatsList, SpaceType.SEATS, 30);
        }else if(decision > 90 && decision <= 100){
            myDirection = goToSomething2(spacesInRange, toiletList, SpaceType.TOILET,20);
        }
        return myDirection;
    }
   

    public Direction goToSomething2(ArrayList<SpaceInfo> spacesInRange, ArrayList<SpaceInfo> aimType, SpaceType whereToGo, int timeToWait){
  
        if(stepCounter == 0){
            aimType.sort(Comparator.comparingInt(space -> getManhattanDistance(space.getRelativeToAvatarCoordinate(), getMyPosition(spacesInRange))));
            foundCoordinate = aimType.get(0).getRelativeToAvatarCoordinate();


            if(whereToGo == SpaceType.DANCEFLOOR){
                int xMin = 40, xMax = 0, yMin = 20, yMax = 0;
                for(int i = 0; i < aimType.size(); i++){
                    Coordinate minDiffCoordinate = aimType.get(i).getRelativeToAvatarCoordinate();
                    if(minDiffCoordinate.getX() < xMin){
                        xMin = minDiffCoordinate.getX();
                    }if(minDiffCoordinate.getX() >= xMax){
                        xMax = minDiffCoordinate.getX();
                    }if(minDiffCoordinate.getY() < yMin){
                        yMin = minDiffCoordinate.getY();
                    }if(minDiffCoordinate.getY() >= yMax){
                        yMax = minDiffCoordinate.getY();
                    }
                }
                xDancefloor = (xMax + xMin) / 2 + random.nextInt(7) - 3;
                yDancefloor = (yMax + yMin) / 2 + random.nextInt(7) - 3;
                          
                foundCoordinate.setX(xDancefloor);
                foundCoordinate.setY(yDancefloor);
            }
        }
        
        stepCounter++;
        if(getMyPosition(spacesInRange).equals(foundCoordinate)){
            stayCounter++;

            if (stayCounter == timeToWait) {
                xDancefloor = 0;
                yDancefloor = 0;
                stepCounter = 0;
                stayCounter = 0;
                decision = (int) (random.nextDouble() * 100);
                counterHitAvatar = 0;
            }
            if(whereToGo == SpaceType.DANCEFLOOR){
                return goDancing();
            }
            return Direction.STAY;
           }
        
        myDirection = goToFoundCoordinate(spacesInRange, foundCoordinate);
        foundCoordinate = solveHitAvatar(spacesInRange, aimType, myDirection, foundCoordinate); 
        return myDirection;
    }


    public Direction goDancing(){
        int dancingStep = random.nextInt(4);
        merkerDirection = 1;
        switch (dancingStep) {
            case 0:
                return Direction.RIGHT;
            case 1:
                return Direction.LEFT;
            case 2: 
                return Direction.UP;
            case 3:
                return Direction.DOWN;
        }
        return Direction.STAY;
    }

    public int getManhattanDistance(Coordinate seat, Coordinate b) {
        return Math.abs(seat.getX() - b.getX()) + Math.abs(seat.getY() - b.getY());
    }


    public Coordinate solveHitAvatar(ArrayList<SpaceInfo> spacesInRange, ArrayList<SpaceInfo> aimType, Direction myDirection, Coordinate foundCoordinate){
        if(conflictDetected(spacesInRange, myDirection) == true){

            myPosition = getMyPosition(spacesInRange);

        
            aimType.sort(Comparator.comparingInt(space -> getManhattanDistance(space.getRelativeToAvatarCoordinate(), myPosition)));
            foundCoordinate = aimType.get(++counterHitAvatar).getRelativeToAvatarCoordinate();
            if(counterHitAvatar == aimType.size() - 1){
                counterHitAvatar = 0;
            }
        }
        return foundCoordinate;
    }

    public Coordinate getMyPosition(ArrayList<SpaceInfo> spacesInRange){
        myPosition.setX(spacesInRange.get(3).getRelativeToAvatarCoordinate().getX());
        myPosition.setY(spacesInRange.get(3).getRelativeToAvatarCoordinate().getY() + 1);
        return myPosition;
    }

    //oben und unten Korrektur funktioniert
    public Direction goToFoundCoordinate(ArrayList<SpaceInfo> spacesInRange, Coordinate myCoordinate) {

        if(merkerDirection == 1){
            merkerDirection = 0;
        if(myDirection == Direction.UP || myDirection == Direction.DOWN){
            if(spacesInRange.get(3).getRelativeToAvatarCoordinate().getX() > myCoordinate.getX())
                return Direction.LEFT;
            else if (spacesInRange.get(3).getRelativeToAvatarCoordinate().getX() < myCoordinate.getX())
                return Direction.RIGHT;
        }else if(myDirection == Direction.RIGHT || myDirection == Direction.LEFT){
            if (spacesInRange.get(1).getRelativeToAvatarCoordinate().getY() > myCoordinate.getY())
                return Direction.UP;
            else if (spacesInRange.get(1).getRelativeToAvatarCoordinate().getY() < myCoordinate.getY())
                return Direction.DOWN;
        }
    }else{
        
        if (spacesInRange.get(3).getRelativeToAvatarCoordinate().getX() < myCoordinate.getX())
            return Direction.RIGHT;
        else if (spacesInRange.get(1).getRelativeToAvatarCoordinate().getY() > myCoordinate.getY())
            return Direction.UP;
        else if (spacesInRange.get(1).getRelativeToAvatarCoordinate().getY() < myCoordinate.getY())
            return Direction.DOWN;
        else if (spacesInRange.get(3).getRelativeToAvatarCoordinate().getX() > myCoordinate.getX())
            return Direction.LEFT;
    }
        return Direction.STAY;
    }

// Methode zum Bewegen in eine Richtung
public Coordinate moveInDirection(Coordinate coord, Direction direction) {
    switch (direction) {
        case UP: return new Coordinate(coord.getX(), coord.getY() - 1);
        case DOWN: return new Coordinate(coord.getX(), coord.getY() + 1);
        case LEFT: return new Coordinate(coord.getX() - 1, coord.getY());
        case RIGHT: return new Coordinate(coord.getX() + 1, coord.getY());
        default: return coord;
    }
}

public boolean conflictDetected(ArrayList<SpaceInfo> spacesInRange, Direction direction) {
    Coordinate nextPosition = moveInDirection(getMyPosition(spacesInRange), direction);
    for (SpaceInfo space : spacesInRange) {
        if (space.getRelativeToAvatarCoordinate().equals(nextPosition) && space.getType() == SpaceType.AVATAR) {
            return true;
        }
    }
    return false;
}

public char getFirstLetter(SpaceType locationType) {
        char firstLetter = 'U';  // Standardwert für undefinierte Typen
    
        // Prüfen, ob der übergebene Typ "OBSTACLE" ist

        switch (locationType) {
            case OBSTACLE:
                firstLetter = 'O';
            break;
            case DANCEFLOOR:
                firstLetter = 'D';
            break;
            case BAR:
                firstLetter = 'B';
            break;
            case SEATS:
                firstLetter = 'S';
            break;
            case DJBOOTH:
                firstLetter = 'J';
            break;
            case TOILET:
                firstLetter = 'T';
            break;
            case EMPTY:
                firstLetter = ' ';
            break;
            case AVATAR:
                firstLetter = 'A';
            break;       
        }
        return firstLetter;
    }

	
 public void initializeMap() {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 40; j++) {
                mapData[i][j] = '.';  // Füllt die Map mit 'U' für undefined
            }
        }
    }


    public void createMapFile() {
        try (FileWriter writer = new FileWriter("mentalMap.txt")) {
            for (int i = 0; i < mapData.length; i++) {
                for (int j = 0; j < mapData[i].length; j++) {
                    writer.write(mapData[i][j] + " ");
                }
                writer.write("\n");
            }
            //System.out.println("Map file created successfully.");
        } catch (IOException e) {
            System.err.println("Error while creating the map file: " + e.getMessage());
        }
    }

    public void updateMapData(Coordinate sourroundCoordinate, char locationType) {
        if (sourroundCoordinate.getX() >= 0 && sourroundCoordinate.getX() < 40 && sourroundCoordinate.getY() >= 0 && sourroundCoordinate.getY() < 20) {
            mapData[sourroundCoordinate.getY()][sourroundCoordinate.getX()] = locationType;
        }
        //createMapFile(); // Aktualisiert die map.txt Datei nach jeder Änderung
    }


    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {


        switch (phase) {
            case 0:
                initializeMap();
                createMapFile();
                phase = 1;
            break;
            case 1:
                myDirection = goToStart(spacesInRange);
                break;
            case 2:
                createMentalMap(spacesInRange);
                myDirection = startMapWalk(spacesInRange);       
                myDirectionMoveBack = hitAvatarMoveAway(spacesInRange);

                if(myDirectionMoveBack != Direction.STAY)
                    return myDirectionMoveBack;
                break;
            case 3:
                removeDuplicateCoordinates();
                sortMentalMapListByType(spacesInRange);
                phase = 4;
                break;
            case 4:
                myDirection = takingDecision(spacesInRange);
                if(conflictDetected(spacesInRange, myDirection) == true){         
                    if(merkerDirection == 1){
                        goToFoundCoordinate(spacesInRange, foundCoordinate);
                    }
                    myDirection = goDancing();    
                }
                break;
        }

        for(int i = 0; i < spacesInRange.size(); i++){
            updateMapData(spacesInRange.get(i).getRelativeToAvatarCoordinate(), getFirstLetter(spacesInRange.get(i).getType()));
        }
        createMapFile();

        return myDirection;
    }

    @Override
    public int getPerceptionRange() {
        return super.getPerceptionRange(); // Assuming SuperAvatar has a method to get the perception range
    }

    @Override
    public void setPerceptionRange(int perceptionRange) {
        super.setPerceptionRange(perceptionRange); // Set the perception range via the superclass method
    }
}
