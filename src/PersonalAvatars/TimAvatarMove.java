package PersonalAvatars;

import java.util.ArrayList;
import java.util.Random;
import Environment.*;

public class TimAvatarMove {

    private int wait = 0;


    public TimAvatarMove() {

    }

     public Coordinate pathFinding(SpaceType destinationSpaceType, Coordinate currentCoordinate) {
        Coordinate destination = null;
            switch (destinationSpaceType) {
                case DANCEFLOOR:
                    destination = getRandomLocation(TimAvatarCurrentSpace.Dancefloor);
                    break;
                case DJBOOTH:
                    destination = getRandomLocation(TimAvatarCurrentSpace.DJBooth);
                    break;
                case TOILET:
                    destination = getRandomLocation(TimAvatarCurrentSpace.Toilet);
                    break;
                case BAR:
                    destination = getRandomLocation(TimAvatarCurrentSpace.Bar);
                    break;
                case SEATS:
                    destination = getRandomLocation(TimAvatarCurrentSpace.Seats);
                    break;
                default:
                    break;
            }
        

        if (destination == null) {
            // Fallback to random movement if no specific destination found
            System.out.println("Random Destionation");
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

    private Coordinate getRandomLocation(ArrayList<Coordinate> spaceList) {
        if (spaceList.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return spaceList.get(random.nextInt(spaceList.size()));
    }

    public Direction coordinateSteps(Coordinate steps, ArrayList<SpaceInfo> spacesInRange, Coordinate currentCoordinate) {
        Coordinate nextCoordinate = new Coordinate(0, 0);

        if (steps.getY() > 0) {
            nextCoordinate.setY(currentCoordinate.getY() + 1);
            nextCoordinate.setX(currentCoordinate.getX());

            if (isFree(nextCoordinate, spacesInRange, steps)) {
                steps.setY(steps.getY() - 1);
                return Direction.DOWN;
            }

        } else if (steps.getY() < 0) {
            nextCoordinate.setY(currentCoordinate.getY() - 1);
            nextCoordinate.setX(currentCoordinate.getX());

            if (isFree(nextCoordinate, spacesInRange, steps)) {
                steps.setY(steps.getY() + 1);
                return Direction.UP;
            }
        } else if (steps.getX() < 0) {
            nextCoordinate.setX(currentCoordinate.getX() - 1);
            nextCoordinate.setY(currentCoordinate.getY());

            if (isFree(nextCoordinate, spacesInRange, steps)) {
                steps.setX(steps.getX() + 1);
                return Direction.LEFT;
            }
        } else if (steps.getX() > 0) {
            nextCoordinate.setX(currentCoordinate.getX() + 1);
            nextCoordinate.setY(currentCoordinate.getY());

            if (isFree(nextCoordinate, spacesInRange, steps)) {
                steps.setX(steps.getX() - 1);
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
