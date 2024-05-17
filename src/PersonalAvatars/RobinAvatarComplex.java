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
        EMPTY(true,false),
        WALKED(true,false),
        WALL(false,false),
        UNKNOWN(false,true),
        REACHABLE(false,true);

        private final boolean walkable;
        private final boolean unknown;

        PersonalFieldType(boolean walkable, boolean unknown){
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


    private final int environmentWidth = 20;
    private final int environmentHeight = 20;
    private Enum [][][] environment;
    private Coordinate position;
    private State state;
    private Direction lastDirection;
    private Direction lastWall;
    private Coordinate destination;


    public RobinAvatarComplex(int id) {
        super(id, 1);
        environment = new Enum[2*environmentWidth+1][2*environmentHeight+1][2];
        position = new Coordinate(environmentWidth, environmentHeight);
        state = State.FIND_WALL;
        lastDirection = Direction.UP;
        destination = new Coordinate(0, 0);
    }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'yourTurn'");
        updateEnvironment(spacesInRange);
        
        
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

    private void updateEnvironment(ArrayList<SpaceInfo> spacesInRange){
        for (SpaceInfo spaceInfo : spacesInRange) {
            Coordinate spaceRelPos = spaceInfo.getRelativeToAvatarCoordinate();
            Coordinate spaceAbsPos = new Coordinate(position.getX() + spaceRelPos.getX(), position.getY() + spaceRelPos.getY());
            if(((PersonalFieldType) (environment[spaceAbsPos.getX()][spaceAbsPos.getY()][1])).isUnknown()){
                environment[spaceAbsPos.getX()][spaceAbsPos.getY()][0] = spaceInfo.getType();
                if(spaceInfo.getType() == SpaceType.EMPTY || spaceInfo.getType() == SpaceType.AVATAR){
                    environment[spaceAbsPos.getX()][spaceAbsPos.getY()][1] = PersonalFieldType.EMPTY;
                }
                else{
                    environment[spaceAbsPos.getX()][spaceAbsPos.getY()][1] = PersonalFieldType.WALL;
                }
            }
        }
    }

}