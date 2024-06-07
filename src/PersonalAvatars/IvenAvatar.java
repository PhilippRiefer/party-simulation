package PersonalAvatars;

import AvatarInterface.*;
import Environment.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

public class IvenAvatar extends SuperAvatar { // implements AvatarInterface

	// Basic Needs
	int needToDance =   1200;
	int needToPee =      500;
	int needToRest = 	2500;
	int needToTalk =   10000;
	int needToMusic =  50000; // implement needToMusic 
	int needToDrink =  90000; // implement needToDrink 

	int[] needsInOrder = { needToDance, needToPee, needToRest, needToTalk, needToMusic, needToDrink };
	SpaceType ObjectNeeded = null;
	Coordinate findCoords; 		// the Coords i Try to find
	Coordinate myCoords = new Coordinate(0, 0); 		// my Coords
	Direction nextMove;			// saved direction for the next move
 	SpaceType[][] Mind = new SpaceType[100][100];
	FoundObjects objectsfound = new FoundObjects(); // 5 needs implemented


	// Helpful Coordinates
	Coordinate Coord_Up = new Coordinate(0, -1);
	Coordinate Coord_Right = new Coordinate(1, 0);
	Coordinate Coord_Down = new Coordinate(0, 1);
	Coordinate Coord_Left = new Coordinate(-1, 0);
	Coordinate Coord_UpLeft = new Coordinate(0, 0);
	Coordinate Coord_UpRight = new Coordinate(0, 0);
	Coordinate Coord_DownLeft = new Coordinate(0, 0);
	Coordinate Coord_DownRight = new Coordinate(0, 0);

	public IvenAvatar(int id, int perceptionRange, Color color) {
		super(id, perceptionRange, color); //
	}

	public void checkBorder(ArrayList<SpaceInfo> spacesInRange) throws BorderException {
		int lenght = spacesInRange.size();
		if (lenght != 8) {
			throw new BorderException("RelativeToAvatarCoordinate has not the right size");
		}
		// Coordinate Coord_Me = new
		// Coordinate(spacesInRange.get(0).getRelativeToAvatarCoordinate().getX()+1,
		// spacesInRange.get(0).getRelativeToAvatarCoordinate().getY()+1);
		Coord_Up = spacesInRange.get(3).getRelativeToAvatarCoordinate();
		Coord_Down = spacesInRange.get(4).getRelativeToAvatarCoordinate();
		Coord_Left = spacesInRange.get(1).getRelativeToAvatarCoordinate();
		Coord_Right = spacesInRange.get(6).getRelativeToAvatarCoordinate();
		Coord_UpLeft = spacesInRange.get(0).getRelativeToAvatarCoordinate();
		Coord_UpRight = spacesInRange.get(5).getRelativeToAvatarCoordinate();
		Coord_DownLeft = spacesInRange.get(2).getRelativeToAvatarCoordinate();
		Coord_DownRight = spacesInRange.get(7).getRelativeToAvatarCoordinate();
	}

	@Override
	public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {

		ObjectNeeded();
		try {
			checkBorder(spacesInRange);
		} catch (BorderException e) {
			//System.out.println("RelativeToAvatarCoordinate has not the right size \n I moved randomly");
			return move();
		}
		myCoords.setX(spacesInRange.get(3).getRelativeToAvatarCoordinate().getX()); 
		myCoords.setY((spacesInRange.get(3).getRelativeToAvatarCoordinate().getY()+1)); 
		memory(spacesInRange);

		int lenght = spacesInRange.size();

		for (int i = 0; i < lenght; i++) {

			if (ObjectNeeded == spacesInRange.get(i).getType()) {
				// Wo liegt das?
				if (spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_Up)) {
					// SpaceType frontTyp = spacesInRange.get(i).getType();
					refreshNeeds(ObjectNeeded);
					return Direction.UP;
				} else if (spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_UpLeft)) {
					// SpaceType frontTyp = spacesInRange.get(i).getType();
					refreshNeeds(ObjectNeeded);
					return Direction.UP;
				} else if (spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_UpRight)) {
					// SpaceType frontTyp = spacesInRange.get(i).getType();
					refreshNeeds(ObjectNeeded);
					return Direction.UP;
				} else if (spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_Right)) {
					// SpaceType rightTyp = spacesInRange.get(i).getType();
					refreshNeeds(ObjectNeeded);
					return Direction.RIGHT;
				} else if (spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_Down)) {
					// SpaceType backTyp = spacesInRange.get(i).getType();
					refreshNeeds(ObjectNeeded);
					return Direction.DOWN;
				} else if (spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_DownRight)) {
					// SpaceType backTyp = spacesInRange.get(i).getType();
					refreshNeeds(ObjectNeeded);
					return Direction.DOWN;
				} else if (spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_DownLeft)) {
					// SpaceType backTyp = spacesInRange.get(i).getType();
					refreshNeeds(ObjectNeeded);
					return Direction.DOWN;
				} else if (spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_Left)) {
					// SpaceType leftTyp = spacesInRange.get(i).getType();
					refreshNeeds(ObjectNeeded);
					return Direction.LEFT;
				}
			}
		}
		refreshNeeds(null);
		wayfinding(spacesInRange);
		return nextMove;
		// if nothing needed that is in range.
		
	}

	void ObjectNeeded() {
		needsInOrder[0] = needToDance;
		needsInOrder[1] = needToPee;
		needsInOrder[2] = needToRest;
		needsInOrder[3] = needToTalk;
		needsInOrder[4] = needToDrink;
		needsInOrder[5] = needToMusic;

		Arrays.sort(needsInOrder);
		if (needsInOrder[0] == needToDance) {
			ObjectNeeded = SpaceType.DANCEFLOOR; // Dancefloor
		} else if (needsInOrder[0] == needToPee) {
			ObjectNeeded = SpaceType.TOILET;
		} else if (needsInOrder[0] == needToRest) {
			ObjectNeeded = SpaceType.SEATS;
		} else if (needsInOrder[0] == needToTalk) {
			ObjectNeeded = SpaceType.AVATAR;
		} else if (needsInOrder[0] == needToDrink) {
			ObjectNeeded = SpaceType.BAR;
		} else if (needsInOrder[0] == needToMusic) {
			ObjectNeeded = SpaceType.DJBOOTH;

			//System.out.println("----- Avatar want to talked");
		}
		try {
			if (ObjectNeeded == null) {
				throw new Exception("No Object needed found!");
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	Direction move() {
		//System.out.println("--------- I will move random");
		int max = 4;
		int min = 0;
		int directionNumber = (int) (Math.random() * ((max - min) + 1) + min); // directionNumber zwischen 0 und 6
																				// generieren
		if (directionNumber == 0) {
			return Direction.STAY;
		} else if (directionNumber >= 1 && directionNumber <= 1) {
			return Direction.RIGHT;
		} else if (directionNumber >= 2 && directionNumber <= 2) {
			return Direction.UP;
		} else if (directionNumber == 3) {
			return Direction.DOWN;
		} else if (directionNumber == 4) {
			return Direction.LEFT;
		} else {
			return Direction.STAY;
		}
		// return switch (directionNumber) {
		// case 0 -> Direction.LEFT;
		// case 1 -> Direction.RIGHT;
		// case 2 -> Direction.UP;
		// case 3 -> Direction.DOWN;
		//// case 6 -> Direction.RIGHT;
		// default -> Direction.STAY;
	}

	void refreshNeeds(SpaceType objectDone) {

		if (objectDone == SpaceType.AVATAR) {
			needToTalk += 3; // need to talk gets less
			//System.out.println("--------------------- Avatar talked");
		}
		if (objectDone == SpaceType.TOILET) {
			needToPee = 5000; // Reset need
		}
		if (objectDone == SpaceType.SEATS) {
			needToRest += 60; // need to rest gets less
		}
		if (objectDone == SpaceType.DANCEFLOOR) {
			needToDance += 10; // need to dance gets less
		}
		if (objectDone == SpaceType.BAR) {
			needToDrink += 90; // need to Drink gets less
		}
		if (objectDone == SpaceType.DJBOOTH) {
			needToMusic += 180; // need to dance gets less
		}

		if (needToDance < 0) {
			needToDance = 0;
		}
		if (needToPee < 0) {
			needToPee = 0;
		}
		if (needToRest < 0) {
			needToRest = 0;
		}
		if (needToTalk < 0) {
			needToTalk = 0;
		}
		if (needToDrink < 0) {
			needToDrink = 0;
		}
		if (needToMusic < 0) {
			needToMusic = 0;
		}
		needToDance--;
		needToPee--;
		needToRest--;
		needToTalk--;
		needToMusic--;
		needToDrink--;
	}

	public void memory(ArrayList<SpaceInfo> spacesInRange) {
		for (int i = 0; i < spacesInRange.size(); i++) {

			if (spacesInRange.get(i).getType() != SpaceType.AVATAR) {
				Mind[spacesInRange.get(i).getRelativeToAvatarCoordinate().getX()][spacesInRange.get(i).getRelativeToAvatarCoordinate().getY()] = spacesInRange.get(i).getType();
				if ((spacesInRange.get(i).getType() != SpaceType.EMPTY) && (spacesInRange.get(i).getType() != SpaceType.OBSTACLE))
						objectsfound.addElement(spacesInRange.get(i).getType(), spacesInRange.get(i).getRelativeToAvatarCoordinate().getX(), spacesInRange.get(i)
						.getRelativeToAvatarCoordinate().getY());						
			}

		}
	}

	public boolean findAim() {
		for (int i =0; i< 5; i++){
			if(objectsfound.getTypeOfSpace(i) == ObjectNeeded){
				findCoords = objectsfound.getCoords(i); 
				return true;
			}
		}
		return false;
	}
	
	public void wayfinding(ArrayList<SpaceInfo> spacesInRange){
		if(findAim()){
			if(findCoords.getX()<myCoords.getX()){
				nextMove=Direction.LEFT;
			}
			else if(findCoords.getX()>myCoords.getX()){
				nextMove=Direction.RIGHT;
			}
			else if (findCoords.getX()==myCoords.getX()) {
				
				if(findCoords.getY()>myCoords.getY()){
					nextMove=Direction.DOWN;
				}
				else if(findCoords.getY() <myCoords.getY()){
					nextMove = Direction.UP;
				}
				else if(findCoords.getY() == myCoords.getY()){
					nextMove = Direction.STAY;	//All coords are the same. The object was found
					refreshNeeds(ObjectNeeded);
					// TODO Make it noticeable that the object has been found
				}
			}
		}
		else{
			nextMove = move(); // Just move
		}
	}

	private class BorderException extends Exception {
		public BorderException(String arg) {
			super(arg);
		}
	}

	private class FoundObjects {

		SpaceType typeOfSpace[] = new SpaceType[5];
		Coordinate coords[] = new Coordinate[5];
		int numberOfElements = 0;

		public FoundObjects() {

		}

		public Coordinate getCoords(int i) {
			return coords[i];
		}

		public SpaceType getTypeOfSpace(int i) {
			return typeOfSpace[i];
		}

		public void addElement(SpaceType typeOfSpace, int xCoord, int yCoord) {

			for (int i = 0; i < numberOfElements; i++) {
				if (typeOfSpace == this.typeOfSpace[i]) {
					return;
				}
			}
			
			if (numberOfElements < 4) { // 5 because of 5 elements i want to remember
				this.typeOfSpace[numberOfElements] = typeOfSpace;
				this.coords[numberOfElements] = new Coordinate(xCoord, yCoord);
				numberOfElements++;
			}
		}
	}
}
