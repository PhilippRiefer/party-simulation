package PersonalAvatars;

import AvatarInterface.*;
import Environment.*;
import java.util.ArrayList;
import java.util.Arrays;

public class IvenAvatar extends SuperAvatar { // implements AvatarInterface
    
	// Basic Needs
    int needToDance = 1200;
    int needToPee = 5000;
    int needToRest = 1500;
	int needToTalk = 10;

	int[] needsInOrder = {needToDance, needToPee, needToRest, needToTalk};
	SpaceType ObjectNeeded = null;

	// Helpful  Coordinates
	final Coordinate Coord_Up  = new Coordinate(0,-1);
	final Coordinate Coord_Rigth  = new Coordinate(1, 0);
	final Coordinate Coord_Down  = new Coordinate(0,1);
	final Coordinate Coord_Left  = new Coordinate(-1,0);

	public IvenAvatar(int id, int perceptionRange) {
		super(id, perceptionRange);
	}

		@Override
		public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
	        
			ObjectNeeded();
			int lenght = spacesInRange.size();
			for (int i=0;i<lenght;i++){

				if (ObjectNeeded == spacesInRange.get(i).getType()){
					//Wo liegt das? 
					if(spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_Up)){
						SpaceType frontTyp = spacesInRange.get(i).getType();
						refreshNeeds(ObjectNeeded);
						return Direction.UP;
					}
					else if(spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_Rigth)){
						SpaceType rightTyp = spacesInRange.get(i).getType();
						refreshNeeds(ObjectNeeded);
						return Direction.RIGHT;
					}
					else if(spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_Down)){
						SpaceType backTyp = spacesInRange.get(i).getType();
						refreshNeeds(ObjectNeeded);
						return Direction.DOWN;
					}
					else if(spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_Left)){
						SpaceType leftTyp = spacesInRange.get(i).getType();
						refreshNeeds(ObjectNeeded);
						return Direction.LEFT;
					} 
				}

			}
			refreshNeeds(null);
			//if nothing needed that is in range. 
			return move(); 	//Just move
		}

		void ObjectNeeded(){
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
			else if(needsInOrder[0] == needToTalk){
				ObjectNeeded = SpaceType.AVATAR;
				
				System.out.println("----- Avatar want to talked");
			}
			try {
				if(ObjectNeeded == null){
					throw new Exception("No Object needed found!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Direction move(){
			System.out.println("----- I will move random");
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
		void 
		refreshNeeds(SpaceType objectDone){
			if(objectDone == SpaceType.AVATAR){
				needToTalk += 3;		//need to talk gets less
				
				System.out.println("--------------------- Avatar talked");
			}
			if(objectDone == SpaceType.TOILET){
				needToPee = 500;		//Reset need
			}
			if(objectDone == SpaceType.SEATS){
				needToRest +=20;		// need to rest gets less
			}
			if(objectDone == SpaceType.DANCEFLOOR){
				needToDance +=6;		// need to dance gets less
			}
			
			if(needToDance<0){
				needToDance=0;
			}
			if(needToPee<0){
				needToPee=0;
			}
			if(needToRest<0){
				needToRest=0;
			}
			if(needToTalk<0){
				needToTalk=0;
			}
			needToDance--;
			needToPee--;
			needToRest--;
			needToTalk--;

		}
	} 




