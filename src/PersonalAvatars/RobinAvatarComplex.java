package PersonalAvatars;

import java.util.ArrayList;

import AvatarInterface.SuperAvatar;
import Environment.Coordinate;
import Environment.Direction;
import Environment.SpaceInfo;
import Environment.SpaceType;

public class RobinAvatarComplex extends SuperAvatar {

    enum State{
        FIND_WALL,
        FOLLOW_WALL,
        FIND_EMPTY,
        MOVE_TO_EMPTY
    }

    enum PersonalFieldType{
        EMPTY(true),
        WALKED(true),
        WALL(false),
        UNKNOWN(false),
        REACHABLE(false);

        private final boolean walkable;

        PersonalFieldType(boolean walkable){
            this.walkable = walkable;
        }
    }


    private int environmentWidth;
    private int environmentHeight;
    private Enum [][][] environment;
    private Coordinate position;
    private State state;
    private Direction lastDirection;
    private Direction lastWall;
    private Coordinate destination;


    public RobinAvatarComplex(int id) {
        super(id, 1);
        //TODO Auto-generated constructor stub
    }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'yourTurn'");
        for (SpaceInfo space : spacesInRange) {
            if(space.getType() == SpaceType.EMPTY)
                return coordinateToDirection(space.getRelativeToAvatarCoordinate());
        }
        
        
        return Direction.STAY;
    }

    private Direction coordinateToDirection(Coordinate coordinate){
        if(coordinate.getY() < 0)
            return Direction.UP;
        if(coordinate.getY() > 0)
            return Direction.DOWN;
        if(coordinate.getX() < 0)
            return Direction.LEFT;
        if(coordinate.getX() > 0)
            return Direction.RIGHT;
        return Direction.STAY;
    }

}