package PersonalAvatars;

import AvatarInterface.*;
import Environment.*;
import java.util.ArrayList;

public class IvenAvatar extends SuperAvatar { // implements AvatarInterface
    
    int needToDance = 100;
    int needToPee = 100;
    int needToRest = 100;
	int neetToTalk = 100;

	public IvenAvatar(int id) {
		super(id);
	}

	@Override
	public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        
		Coordinate dir_  = new Coordinate(1,0);
		for (int i=0;i<4;i++){
	
			if(spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(test)){}
        SpaceType frontTyp = spacesInRange.get(1).getType();
        Coordinate frontCoord = spacesInRange.get(1).getRelativeToAvatarCoordinate();
        SpaceType rightTyp = spacesInRange.get(1).getType();
        Coordinate rightCoord = spacesInRange.get(1).getRelativeToAvatarCoordinate();
        SpaceType backTyp = spacesInRange.get(1).getType();
        Coordinate backCoord = spacesInRange.get(1).getRelativeToAvatarCoordinate();
        SpaceType leftTyp = spacesInRange.get(1).getType();
        Coordinate leftCoord = spacesInRange.get(1).getRelativeToAvatarCoordinate();        
	}
		int max = 3;
		int min = 0;
		int directionNumber = (int) (Math.random() * ((max - min) + 1) + min);

        return switch (directionNumber) {
            case 0 -> Direction.LEFT;
            case 1 -> Direction.RIGHT;
            case 2 -> Direction.UP;
            case 3 -> Direction.DOWN;
            default -> Direction.STAY;
        };	
	}


	
}