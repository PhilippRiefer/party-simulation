package PersonalAvatars;

import Environment.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;
import AvatarInterface.*;
import java.awt.Color;
import java.util.Random;

//find my position and set to (0,0)
//spacesInRange
/* 0 |  3  | 5
 * 1 |  ME | 6
 * 2 |  4  | 7
*/

public class TomAvatar extends SuperAvatar {

  
    private ArrayList<SpaceInfo> mentalMapList = new ArrayList<>();
    private int storePerceptionRange = 0;
    private int phase = 0, stepCounter = 0, directionCounter = 0;
    private Direction myDirection = Direction.STAY;
    private static final Random random = new Random();


    public TomAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);
        storePerceptionRange = perceptionRange;

    }

    public void createMentalMap(ArrayList<SpaceInfo> spacesInRange) {
        for (SpaceInfo infoForMentalMap : spacesInRange) {
            if (infoForMentalMap.getType() != SpaceType.EMPTY && infoForMentalMap.getType() != SpaceType.OBSTACLE)
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

    private void sortMentalMapListByType() {
        Collections.sort(mentalMapList, new Comparator<SpaceInfo>() {
            @Override
            public int compare(SpaceInfo o1, SpaceInfo o2) {
                return o1.getType().name().compareTo(o2.getType().name());
            }
        });
    }

    public Direction goToStart(ArrayList<SpaceInfo> spacesInRange) {

        if (spacesInRange.get(3).getType() == SpaceType.OBSTACLE) {
            if (spacesInRange.get(1).getType() == SpaceType.OBSTACLE) {
                phase = 1;
                return Direction.STAY;
            }
            return Direction.LEFT;

        } else {
            return Direction.UP;
        }

    }

    public Direction startMapWalk(ArrayList<SpaceInfo> spacesInRange) {

        if (spacesInRange.get(4).getType() == SpaceType.OBSTACLE &&
                spacesInRange.get(6).getType() == SpaceType.OBSTACLE) {
            phase = 2;
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

    public Direction takingDicision(ArrayList<SpaceInfo> spacesInRange){
        int decision = (int) (random.nextDouble() * 100);
 
        if(decision > 0){
            myDirection = goToBar(spacesInRange);
        }
        return myDirection;
    }

    public Direction goToBar(ArrayList<SpaceInfo> spacesInRange){

        int minDiff = 0;
        int diff = 0;

        for(int i = 0; i < mentalMapList.size(); i++){
            if(mentalMapList.get(i).getType() == SpaceType.BAR){
                diff = Math.abs(spacesInRange.get(3).getRelativeToAvatarCoordinate().getX()
                - mentalMapList.get(i).getRelativeToAvatarCoordinate().getX());
                if(diff < minDiff){
                    minDiff = diff;
                }
            }
        }
        return Direction.DOWN;
    }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        switch (phase) {
            case 0:
                myDirection = goToStart(spacesInRange);
                break;
            case 1:
                createMentalMap(spacesInRange);
                myDirection = startMapWalk(spacesInRange);
                break;
            case 2:
                removeDuplicateCoordinates();
                sortMentalMapListByType();
                phase = 3;
                break;
            case 3:
                takingDicision(spacesInRange);
            break;
            default:
                break;
        }
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
