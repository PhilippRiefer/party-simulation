package PersonalAvatars;

import java.util.ArrayList;
import java.util.Random;
import Environment.*;

public class TimAvatarMove {

    private Coordinate currentCoordinate = randomCoordinate();
    private int wait = 0;

    private ArrayList<SpaceInfo> Dancefloor = new ArrayList<>();
    private ArrayList<SpaceInfo> DJBooth = new ArrayList<>();
    private ArrayList<SpaceInfo> Toilet = new ArrayList<>();
    private ArrayList<SpaceInfo> Bar = new ArrayList<>();
    private ArrayList<SpaceInfo>  Seats = new ArrayList<>();

    public TimAvatarMove() {

    }

     public Coordinate pathFinding(SpaceType destinationSpaceType, Coordinate currentCoordinate) {
        Coordinate destination = null;
            switch (destinationSpaceType) {
                case DANCEFLOOR:
                    destination = getRandomLocation(Dancefloor);
                    break;
                case DJBOOTH:
                    destination = getRandomLocation(DJBooth);
                    break;
                case TOILET:
                    destination = getRandomLocation(Toilet);
                    break;
                case BAR:
                    destination = getRandomLocation(Bar);
                    break;
                case SEATS:
                    destination = getRandomLocation(Seats);
                    break;
                default:
                    break;
            }
        

        if (destination == null) {
            // Fallback to random movement if no specific destination found
            return new Coordinate(
                    randomCoordinate().getX() - currentCoordinate.getX(),
                    randomCoordinate().getY() - currentCoordinate.getY());
        }

        // Calculate steps only if a destination is found
        return new Coordinate(
                destination.getX() - currentCoordinate.getX(),
                destination.getY() - currentCoordinate.getY()
        );
    }

    private Coordinate getRandomLocation(ArrayList<SpaceInfo> spaceList) {
        if (spaceList.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return spaceList.get(random.nextInt(spaceList.size())).getRelativeToAvatarCoordinate();
    }

    public Direction coordinateSteps(Coordinate steps, ArrayList<SpaceInfo> spacesInRange) {
        Coordinate nextCoordinate = new Coordinate(0, 0);

        if (steps.getY() > 0) {
            nextCoordinate.setY(currentCoordinate.getY() + 1);
            nextCoordinate.setX(currentCoordinate.getX());

            if (isFree(nextCoordinate, spacesInRange, steps)) {
                return Direction.DOWN;
            }

        } else if (steps.getY() < 0) {
            nextCoordinate.setY(currentCoordinate.getY() - 1);
            nextCoordinate.setX(currentCoordinate.getX());

            if (isFree(nextCoordinate, spacesInRange, steps)) {
                return Direction.UP;
            }
        } else if (steps.getX() < 0) {
            nextCoordinate.setX(currentCoordinate.getX() - 1);
            nextCoordinate.setY(currentCoordinate.getY());

            if (isFree(nextCoordinate, spacesInRange, steps)) {
                return Direction.LEFT;
            }
        } else if (steps.getX() > 0) {
            nextCoordinate.setX(currentCoordinate.getX() + 1);
            nextCoordinate.setY(currentCoordinate.getY());

            if (isFree(nextCoordinate, spacesInRange, steps)) {
                return Direction.RIGHT;
            }
        }

        return Direction.STAY;
    }

    private Boolean isFree(Coordinate nextCoordinate, ArrayList<SpaceInfo> spacesInRange, Coordinate Steps){
    
        for(SpaceInfo spaceInfo : spacesInRange){
            if(spaceInfo.getRelativeToAvatarCoordinate().getX() == nextCoordinate.getX() && spaceInfo.getRelativeToAvatarCoordinate().getY() == nextCoordinate.getY()){
                if(spaceInfo.getType() == SpaceType.OBSTACLE){
                    Steps.setX(0);
                    Steps.setY(0);
                    return false;
                }
                else if(wait >= 3){
                    Steps.setX(0);
                    Steps.setY(0);
                    wait = 0;
                    return false;
                }
                else if(spaceInfo.getType() == SpaceType.AVATAR){
                    wait++;
                    return false;
                }
                else{
                    return true; 
                }
            }
        }
        return false;
    }




    private Coordinate randomCoordinate() {
        Random random = new Random();
        int randomX = random.nextInt(40); // Random number between 1 and 40
        int randomY = random.nextInt(20); // Random number between 1 and 20
        return new Coordinate(randomX, randomY);
    }
}
