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
    private Direction myDirectionMoveBack = Direction.STAY;
    private static final Random random = new Random();
    private Coordinate minCoordinate = new Coordinate(0, 0);
    private int minDiffX = 40, minX = 0, minY = 0, minDiffY = 20;
    private int stayCounter = 0;
    private int decision = (int) (random.nextDouble() * 100);
    private int howLongStaying = random.nextInt(15);
    private int waitOnPosition = 0;
    private int merker = 0;
    private int someOneAround = 0;
    private int merkerStay = 0;

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

        if (spacesInRange.get(1).getType() == SpaceType.OBSTACLE) {
            if (spacesInRange.get(3).getType() == SpaceType.OBSTACLE) {
                phase = 1;
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

    public Direction hitAvatarMoveBack(ArrayList<SpaceInfo> spacesInRange) {

        if (myDirectionMoveBack == Direction.DOWN && spacesInRange.get(3).getType() != SpaceType.AVATAR) {
            howLongStaying = 0;
            merker = 0;
            return Direction.UP;
        } else if (myDirectionMoveBack == Direction.UP && spacesInRange.get(4).getType() != SpaceType.AVATAR) {
            howLongStaying = 0;
            merker = 0;
            return Direction.DOWN;
        } else if (myDirectionMoveBack == Direction.LEFT && spacesInRange.get(6).getType() != SpaceType.AVATAR) {
            howLongStaying = 0;
            merker = 0;
            return Direction.RIGHT;
        } else if (myDirectionMoveBack == Direction.RIGHT && spacesInRange.get(1).getType() != SpaceType.AVATAR) {
            howLongStaying = 0;
            merker = 0;
            return Direction.LEFT;
        } else {
            return Direction.STAY;
        }
    }

    public Direction hitAvatarMoveAway(ArrayList<SpaceInfo> spacesInRange) {

        if (myDirection == Direction.RIGHT && spacesInRange.get(6).getType() == SpaceType.AVATAR
                && spacesInRange.get(4).getType() != SpaceType.OBSTACLE) {
            merker = 1;
            waitOnPosition++;
            return Direction.DOWN;

        } else if (myDirection == Direction.LEFT && spacesInRange.get(1).getType() == SpaceType.AVATAR
                && spacesInRange.get(3).getType() != SpaceType.OBSTACLE) {
            merker = 1;
            waitOnPosition++;
            return Direction.UP;
        }
        if (waitOnPosition == 2) {
            if (myDirection == Direction.UP && spacesInRange.get(3).getType() == SpaceType.AVATAR
                    && spacesInRange.get(6).getType() != SpaceType.OBSTACLE) {
                merker = 1;
                return Direction.RIGHT;

            } else if (myDirection == Direction.DOWN && spacesInRange.get(4).getType() == SpaceType.AVATAR
                    && spacesInRange.get(1).getType() != SpaceType.OBSTACLE) {
                merker = 1;
                return Direction.LEFT;
            }
        } else {
            return Direction.STAY;
        }
        return Direction.STAY;

    }

    public Direction takingDicision(ArrayList<SpaceInfo> spacesInRange) {

        if (decision < 100) {
            myDirection = goToBar(spacesInRange);
        }
        return myDirection;
    }

    public Direction goToBar(ArrayList<SpaceInfo> spacesInRange) {

        int diffX = 0;

        if (minDiffX == 40) {
            for (int i = 0; i < mentalMapList.size(); i++) {
                if (mentalMapList.get(i).getType() == SpaceType.BAR) {
                    diffX = Math.abs(spacesInRange.get(3).getRelativeToAvatarCoordinate().getX()
                            - mentalMapList.get(i).getRelativeToAvatarCoordinate().getX());

                    if (diffX < minDiffX) {
                        minDiffX = diffX;
                        minX = mentalMapList.get(i).getRelativeToAvatarCoordinate().getX();
                    }
                    if (mentalMapList.get(i).getRelativeToAvatarCoordinate().getY() > minY) {
                        minY = mentalMapList.get(i).getRelativeToAvatarCoordinate().getY();

                    }
                }
            }
            if(minX == 38)
                minX--;
            else if(minX == 1)
                minX++;
        }

        System.out.println("minY: " + minY);
        System.out.println("minX: " + minX);


        if (minX >= 20) {
            if (minX - 1 < spacesInRange.get(3).getRelativeToAvatarCoordinate().getX()) {
                return Direction.LEFT;
            } else if (minX - 1 > spacesInRange.get(3).getRelativeToAvatarCoordinate().getX()) {
                return Direction.RIGHT;
            } else if (minY < spacesInRange.get(1).getRelativeToAvatarCoordinate().getY()) {
                return Direction.UP;
            } else if (minY > spacesInRange.get(1).getRelativeToAvatarCoordinate().getY()) {
                return Direction.DOWN;
            }
        }

        if (minX <= 10) {
            if (minX + 1 < spacesInRange.get(3).getRelativeToAvatarCoordinate().getX()) {
                return Direction.LEFT;
            } else if (minX + 1 > spacesInRange.get(3).getRelativeToAvatarCoordinate().getX()) {
                return Direction.RIGHT;
            } else if (minY < spacesInRange.get(1).getRelativeToAvatarCoordinate().getY()) {
                return Direction.UP;
            } else if (minY > spacesInRange.get(1).getRelativeToAvatarCoordinate().getY()) {
                return Direction.DOWN;
            }
        }

        if (merkerStay == 1) {
            stayCounter++;
            return Direction.STAY;
        }

        if (minX <= 10 && (spacesInRange.get(1).getType() == SpaceType.BAR ||
                spacesInRange.get(1).getType() == SpaceType.EMPTY)) {
            merkerStay = 1;
            minX--;
        } else if (minX >= 20 && (spacesInRange.get(6).getType() == SpaceType.BAR ||
                spacesInRange.get(6).getType() == SpaceType.EMPTY)) {
            merkerStay = 1;
            minX++;
        } else {
            minY--;
        }

        return Direction.STAY;
    }

    public Direction goToTest(ArrayList<SpaceInfo> spacesInRange) {

        if (spacesInRange.get(3).getRelativeToAvatarCoordinate().getX() < 22)
            return Direction.RIGHT;
        else if (spacesInRange.get(1).getRelativeToAvatarCoordinate().getY() > 1)
            return Direction.UP;
        else if (spacesInRange.get(1).getRelativeToAvatarCoordinate().getY() < 1)
            return Direction.DOWN;
        else if (spacesInRange.get(3).getRelativeToAvatarCoordinate().getX() > 22)
            return Direction.LEFT;
        else {
            phase = 4;
            return Direction.STAY;
        }

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
                myDirection = goToTest(spacesInRange);

                break;
            case 4:
                myDirection = takingDicision(spacesInRange);
                break;
            default:
                break;
        }

        if (hitAvatarMoveAway(spacesInRange) != Direction.STAY) {
            return myDirectionMoveBack = hitAvatarMoveAway(spacesInRange);
        }

        if (merker == 1) {
            return myDirection = hitAvatarMoveBack(spacesInRange);
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
