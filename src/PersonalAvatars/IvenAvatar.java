package PersonalAvatars;

import AvatarInterface.*;
import Environment.*;
import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class IvenAvatar extends SuperAvatar { // implements AvatarInterface

	// Basic Needs
	int needToDance =   3000;
	int needToPee =    	6000;
	int needToRest = 	1500;
	int needToTalk =   	2500;
	int needToMusic =  	9000; // implement needToMusic 
	int needToDrink =   4000; // implement needToDrink 

	int[] needsInOrder = { needToDance, needToPee, needToRest, needToTalk, needToMusic, needToDrink };
	SpaceType ObjectNeeded = null;
	Coordinate findCoords; 		// the Coords i Try to find
	int orientate = 0;			// counter if I am Orientated 

	Coordinate myCoords = new Coordinate(0, 0); 		// my Coords
	Direction nextMove;			// saved direction for the next move
 	SpaceType[][] Mind = new SpaceType[40][20];
	FoundObjects objectsfound = new FoundObjects(); // 5 needs implemented
	Boolean everySecondRound = false;		// zeigt an ob eseine gerade oder ungerade runde ist. 

	//TODO: needs sollten sich immer ändern für alles was in der nähe ist 
	//TODO: betrunkenheit soll random bewegung häufiger machen 
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
		super(id, perceptionRange, color.BLACK); //
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
		try {
			checkBorder(spacesInRange);
		} catch (BorderException e) {
			return moveRandom();
		}
		createTxtFile();
		setMyCoord(spacesInRange);
		memory(spacesInRange);

		if ( ObjectNeeded == SpaceType.AVATAR){		// Do I want to talk?		
			nextMove = isItInRangeAndWhere(spacesInRange); // Is some one around me?
			if ( nextMove != null){	
				ObjectNeeded();
				refreshNeeds(ObjectNeeded);				// Update needs				
				return nextMove;		
			}
			else{
				refreshNeeds(null);			// Update needs	
				return moveRandom();				// If no Avatar around me. Move randomly
			}
		}	
		// if the avatar doesn't want to talk
		if (everySecondRound){
			mirroredwayfinding();
			everySecondRound = false;
			return nextMove;			
		}
		else{
			wayfinding();
			everySecondRound = true;
			return nextMove;			
		}
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
			ObjectNeeded = SpaceType.DANCEFLOOR; 	// Dancefloor is most needed
		} else if (needsInOrder[0] == needToPee) {
			ObjectNeeded = SpaceType.TOILET;		// Toilet is most needed
		} else if (needsInOrder[0] == needToRest) {
			ObjectNeeded = SpaceType.SEATS;			// Seats is most needed
		} else if (needsInOrder[0] == needToTalk) {
			ObjectNeeded = SpaceType.AVATAR;		// Avatar is most needed
			//System.out.println("----- Avatar want to talked");
		} else if (needsInOrder[0] == needToDrink) {
			ObjectNeeded = SpaceType.BAR;			// Bar is most needed
		} else if (needsInOrder[0] == needToMusic) {
			ObjectNeeded = SpaceType.DJBOOTH;		// DJbooth is most needed

			
		}
		try {
			if (ObjectNeeded == null) {
				throw new Exception("No Object needed found!");
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	Direction moveRandom() {
		//System.out.println("------------ I will move random");
		int max = 4;
		int min = 0;
		int directionNumber = (int) (Math.random() * ((max - min) + 1) + min); // directionNumber zwischen 0 und 4 generieren
		refreshNeeds(null);

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
	}

	void refreshNeeds(SpaceType objectDone) {

		if (objectDone == SpaceType.AVATAR) {
			needToTalk += 300; // need to talk gets less
			System.out.println("------------------------------------ Avatar talked");
		}
		if (objectDone == SpaceType.TOILET) {
			needToPee = 5000; // need to pee gets less
		}
		if (objectDone == SpaceType.SEATS) {
			needToRest += 1250; // need to rest gets less
		}
		if (objectDone == SpaceType.DANCEFLOOR) {
			needToDance += 80; // need to dance gets less
		}
		if (objectDone == SpaceType.BAR) {
			needToDrink += 90; // need to Drink gets less
			needToPee -= 10;
		}
		if (objectDone == SpaceType.DJBOOTH) {
			needToMusic += 180; // need to make Music gets less
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
		needToDance -= 5;
		needToPee -= 22;
		needToRest -= 3;
		needToTalk -= 1;
		needToMusic -= 3;
		needToDrink -= 10;
	}

	public void memory(ArrayList<SpaceInfo> spacesInRange) {
		for (int i = 0; i < spacesInRange.size(); i++) {

			if (spacesInRange.get(i).getType() != SpaceType.AVATAR) {
				Mind[spacesInRange.get(i).getRelativeToAvatarCoordinate().getX()][spacesInRange.get(i).getRelativeToAvatarCoordinate().getY()] = spacesInRange.get(i).getType();
				if ((spacesInRange.get(i).getType() != SpaceType.EMPTY) && (spacesInRange.get(i).getType() != SpaceType.OBSTACLE)){
					objectsfound.addElement(spacesInRange.get(i).getType(), spacesInRange.get(i).getRelativeToAvatarCoordinate().getX(), spacesInRange.get(i).getRelativeToAvatarCoordinate().getY());						
				}
			}
		}
	}

	public boolean findAim() {

		int min = 0;
		int max = 4;
		if( (int) (Math.random() * ((max - min) + 1) + min) == 0){	//1/5 chance das Avatar random bewegt
			return false;
		}
		if (orientate < 5){	
			switch(orientate) {
				case 0: findCoords = new Coordinate(2, 2); break;
				case 1: findCoords = new Coordinate(36,17); break;
				case 2: findCoords = new Coordinate(3,17); break;
				case 3: findCoords = new Coordinate(36,2); break;
				case 4: findCoords = new Coordinate(17,3); break;
			} 
			return true;
		}
		else {
			for (int i =0; i< 5; i++){
				if(objectsfound.getTypeOfSpace(i) == ObjectNeeded){
					findCoords = objectsfound.getCoords(i); 
					return true;
				}
			}

			return false;
		}
	}
	
	public void wayfinding(){
		if(findAim()){							//Have I seen the SpaceType I need and where is it? 
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
					ObjectNeeded();
					if (orientate<5){			// Am I oriented yet? 
						orientate++;
					}					
				}
			}
		}
		else{
			nextMove = moveRandom(); // Just move
		}
	}

	public void mirroredwayfinding(){
		if(findAim()){
			if(findCoords.getY()<myCoords.getY()){
				nextMove=Direction.UP;
			}
			else if(findCoords.getY()>myCoords.getY()){
				nextMove=Direction.DOWN;
			}
			else if (findCoords.getY()==myCoords.getY()) {
				
				if(findCoords.getX()>myCoords.getX()){
					nextMove=Direction.RIGHT;
				}
				else if(findCoords.getX() <myCoords.getX()){
					nextMove = Direction.LEFT;
				}
				else if(findCoords.getX() == myCoords.getX()){
					nextMove = Direction.STAY;	//All coords are the same. The object was found
					refreshNeeds(ObjectNeeded);
					ObjectNeeded();
					if (orientate<5){
						orientate++;
					}				
				}
			}
		}
		else{
			nextMove = moveRandom(); // Just move
		}
	}
	private Direction isItInRangeAndWhere(ArrayList<SpaceInfo> spacesInRange){
		for (int i = 0; i < spacesInRange.size(); i++) {
			if (ObjectNeeded == spacesInRange.get(i).getType()) {
				// Is somethink I need in range 
				if (spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_Up)) {
					return Direction.UP;

				} else if (spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_UpLeft)) {
					return Direction.UP;

				} else if (spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_UpRight)) {
					return Direction.UP;

				} else if (spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_Right)) {	
					return Direction.RIGHT;

				} else if (spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_Down)) {	
					return Direction.DOWN;

				} else if (spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_DownRight)) {	
					return Direction.DOWN;

				} else if (spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_DownLeft)) {
					return Direction.DOWN;

				} else if (spacesInRange.get(i).getRelativeToAvatarCoordinate().equals(Coord_Left)) {	
					return Direction.LEFT;
				}
			}
		}
		return null;
	}
	private void setMyCoord(ArrayList<SpaceInfo> spacesInRange){
		myCoords.setX(spacesInRange.get(3).getRelativeToAvatarCoordinate().getX()); 
		myCoords.setY((spacesInRange.get(3).getRelativeToAvatarCoordinate().getY()+1)); 
	}

	    private void createTxtFile() {
        // Write the visited grid to a text file -> "OlesVisitedSpaces.txt"
        try (FileWriter writer = new FileWriter("IvensMindmap.txt")) {
            // writer.write(" The avatar moves " + desicion + "\n");
            writer.write("+=============================================================================+\n");
            writer.write("\t\t\t\t\t\t\tMap saved in Iven's head:\n");
            writer.write("-------------------------------------------------------------------------------\n");
            // show which characteristic the avatar has
                writer.write(" need to Talk: " + needToTalk + "\n");
				writer.write(" need to Dance: " + needToDance + "\n");
				writer.write(" need to Rest: " + needToRest + "\n");
				writer.write(" need to Drink: " + needToDrink + "\n");
				writer.write(" need to Pee: " + needToPee + "\n");
				writer.write(" need to make Music: " + needToMusic + "\n");
				writer.write(" Object I am looking for " + ObjectNeeded + "\n");

            writer.write("+=============================================================================+\n");
            // run through the array with the seen environment and replace the integer
            // values with more understandable char values
			char cellSymbol;
            for (int i = 0; i < Mind[0].length; i++) {
                for (int j = 0; j < Mind.length; j++) {
                    
					if(Mind[j][i]== null){
						cellSymbol = '?';
					}
					else if(Mind[j][i] == SpaceType.BAR){
						cellSymbol = 'B';
					}
					else if(Mind[j][i]== SpaceType.DANCEFLOOR){
						cellSymbol = 'D';
					}
					else if(Mind[j][i]== SpaceType.EMPTY){
						cellSymbol = ' ';
					}
					else if(Mind[j][i]== SpaceType.DJBOOTH){
						cellSymbol = 'J';
					}
					else if(Mind[j][i]== SpaceType.SEATS){
						cellSymbol = 'S';
					}
					else if(Mind[j][i]== SpaceType.TOILET){
						cellSymbol = 'T';
					}
					else if(Mind[j][i]== SpaceType.OBSTACLE){
						cellSymbol = 'X';
					}
					else{
						cellSymbol = 'E';
					}
                    writer.write(cellSymbol + " ");
                }
                writer.write("\n");
            }
            // also create a legend to explain the char values
            writer.write("+=============================================================================+\n");
            writer.write("\n\t  Legend:\n");
            writer.write("------------------\n");
            writer.write("1 -> B:\tBAR\n");
            //writer.write("2 -> A:\tAVATAR\n");
            writer.write("3 -> D:\tDANCEFLOOR\n");
            writer.write("4 ->  :\tEMPTY\n");
            writer.write("5 -> J:\tDJBOOTH\n");
            writer.write("6 -> S:\tSEATS\n");
            writer.write("7 -> T:\tTOILET\n");
            writer.write("8 -> X:\tOBSTACLE\n");
			} catch (IOException e) {
				e.printStackTrace();
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
			
			if (numberOfElements <= 4) { // 5 because of 5 elements i want to remember
				this.typeOfSpace[numberOfElements] = typeOfSpace;
				this.coords[numberOfElements] = new Coordinate(xCoord, yCoord);
				numberOfElements++;
			}
		}
	}
}
