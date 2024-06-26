package PersonalAvatars;

import java.util.ArrayList;
import Environment.Coordinate;
import Environment.Direction;
import Environment.SpaceInfo;
import Environment.SpaceType;

public class TimAvatarCurrentSpace {


    public TimAvatarCurrentSpace() {
    }

    public static  ArrayList<Coordinate> Dancefloor = new ArrayList<>();
    public static ArrayList<Coordinate> DJBooth = new ArrayList<>();
    public static ArrayList<Coordinate> Toilet = new ArrayList<>();
    public static ArrayList<Coordinate> Bar = new ArrayList<>();
    public static ArrayList<Coordinate>  Seats = new ArrayList<>();

     public Coordinate calculateCurrentCoordinate(ArrayList<SpaceInfo> spacesInRange) {
        Coordinate currentCoordinate = new Coordinate(0, 0);
        currentCoordinate.setX(spacesInRange.get(4).getRelativeToAvatarCoordinate().getX());
        currentCoordinate.setY(spacesInRange.get(4).getRelativeToAvatarCoordinate().getY() - 1);
        return currentCoordinate;
    }

    public SpaceType calculateCurrentSpaceType(ArrayList<SpaceInfo> spacesInRange, ArrayList<SpaceInfo> oldSpaceInRange, Direction lastMove, SpaceType oldSpaceType){
        SpaceType currentSpaceType;
        if(lastMove != null){
            switch (lastMove) {
            case RIGHT:
                currentSpaceType = oldSpaceInRange.get(6).getType(); 
 
                 break;
            case LEFT:
                currentSpaceType = oldSpaceInRange.get(1).getType();
 
            break;
            case DOWN:
                currentSpaceType = oldSpaceInRange.get(4).getType(); 
 
                break;
            case UP:
                currentSpaceType = oldSpaceInRange.get(3).getType();
            break;
            default:
            currentSpaceType = oldSpaceType;
                break;
            }
            oldSpaceInRange.clear();
            oldSpaceInRange.addAll(spacesInRange);
    
        }
        else{
            currentSpaceType = SpaceType.EMPTY;
            oldSpaceInRange.clear();
            oldSpaceInRange.addAll(spacesInRange);
        }
        return currentSpaceType;
        
    }

    public static void extendKnownSpace(ArrayList<SpaceInfo> spacesInRange) {
        for(SpaceInfo spaceInfo : spacesInRange) {
            Coordinate coordinate = new Coordinate(spaceInfo.getRelativeToAvatarCoordinate().getX(), spaceInfo.getRelativeToAvatarCoordinate().getY());
            SpaceType type = spaceInfo.getType();

            switch(type) {
                case DANCEFLOOR:
                    if(!Dancefloor.contains(coordinate )) {
                        Dancefloor.add(coordinate);
                    }
                    break;
                case DJBOOTH:
                    if(!DJBooth.contains(coordinate )) {
                        DJBooth.add(coordinate );
                    }
                    break;
                case TOILET:
                    if(!Toilet.contains(coordinate )) {
                        Toilet.add(coordinate );
                    }
                    break;
                case BAR:
                    if(!Bar.contains(coordinate )) {
                        Bar.add(coordinate);
                    }
                    break;
                case SEATS:
                    if(!Seats.contains(coordinate )) {
                        Seats.add(coordinate );
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
