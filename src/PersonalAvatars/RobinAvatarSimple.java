package PersonalAvatars;

import java.util.ArrayList;

import AvatarInterface.SuperAvatar;
import Environment.Coordinate;
import Environment.Direction;
import Environment.SpaceInfo;
import Environment.SpaceType;

public class RobinAvatarSimple extends SuperAvatar {

    public RobinAvatarSimple(int id) {
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
