package PersonalAvatars;

import AvatarInterface.*;
import Environment.*;
import java.util.ArrayList;
import java.util.Arrays;

public class IvenAvatar extends SuperAvatar { // implements AvatarInterface
    
	// Basic Needs
    int needToDance = 100;
    int needToPee = 100;
    int needToRest = 100;
	int neetToTalk = 100;

	int[] needsInOrder = {needToDance, needToPee, needToRest, neetToTalk};

	// Helpful  Coordinates
	Coordinate Coord_Up  = new Coordinate(0,-1);
	Coordinate Coord_Rigth  = new Coordinate(1, 0);
	Coordinate Coord_Down  = new Coordinate(0,1);
	Coordinate Coord_Left  = new Coordinate(-1,0);

	public IvenAvatar(int id) {
		super(id);
	}

		@Override
		public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
	        

			for (int i=0;i<4;i++){
		
				if(spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_Up)){
					SpaceType frontTyp = spacesInRange.get(i).getType();
					Coordinate frontCoord = spacesInRange.get(i).getRelativeToAvatarCoordinate();
				}
				else if(spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_Rigth)){
					SpaceType rightTyp = spacesInRange.get(i).getType();
					Coordinate rightCoord = spacesInRange.get(i).getRelativeToAvatarCoordinate();
				}
				else if(spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_Down)){
					SpaceType backTyp = spacesInRange.get(i).getType();
					Coordinate backCoord = spacesInRange.get(i).getRelativeToAvatarCoordinate();
				}
				else if(spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_Left)){
					SpaceType leftTyp = spacesInRange.get(i).getType();
					Coordinate leftCoord = spacesInRange.get(i).getRelativeToAvatarCoordinate();
				}
			     
			} 
			
			
			


			int max = 6; 	
			int min = 0;
			int directionNumber = (int) (Math.random() * ((max - min) + 1) + min); // directionNumber zwischen 0 und 6 generieren
			if (directionNumber == 0) {
				return Direction.STAY;
			} else if (directionNumber >= 1 && directionNumber <= 2) {
				return Direction.RIGHT;
			} else if (directionNumber >= 3 && directionNumber <= 4) {
				return Direction.UP;
			} else if (directionNumber == 5) {
				return Direction.DOWN;
			} else if (directionNumber == 6) {
				return Direction.LEFT;
			} else {
				return Direction.STAY;
			}
			
			//return switch (directionNumber) {
	        //    case 0 ->  Direction.LEFT;
	        //    case 1 ->  Direction.RIGHT;
	         //   case 2 ->  Direction.UP;
	        //    case 3 ->  Direction.DOWN;
			////	case 6 ->  Direction.RIGHT;
	         //   default -> Direction.STAY;
	         	
		}
		SpaceType ObjectNeeded(){
			SpaceType ObjectNeeded = null;
			Arrays.sort(needsInOrder);
			if(needsInOrder[0] == needToDance){
				ObjectNeeded = SpaceType.DANCEFLOOR; 			//Dancefloor
			}
			else if(needsInOrder[0] == needToPee){
					ObjectNeeded = SpaceType.TOILET;
			}
			else if(needsInOrder[0] == needToRest){
				ObjectNeeded = SpaceType.SEATS;
			}
			else if(needsInOrder[0] == neetToTalk){
				ObjectNeeded = SpaceType.AVATAR;
			}
			try {
				if(ObjectNeeded == null){
					throw new Exception("No Object needed found!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			return ObjectNeeded;
		}
	} 



