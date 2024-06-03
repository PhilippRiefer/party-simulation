package PersonalAvatars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.lang.Math;

import Environment.*;
import AvatarInterface.*;


public class TimAvatar extends SuperAvatar { // implements AvatarInterface

    this.id = id;
        this.perceptionRange = perceptionRange;
        this.random = new Random();
        this.knownSpace = new HashMap<>();
        this.currentCoordinate = new Coordinate(0, 0); // Initiale Position des Avatars

	private final Map<String, Integer> characterTraits;
	HashMap<String, Integer> needs = new HashMap<>();
	Random random = new Random();
	String currentSpaceType;
	Coordinate currentCoordinate;
	SpaceType[][] knownSpace = new SpaceType[40][20];
	int NumberOfStepsDown = 0;
	int NumberOfStepsUp = 0;
	int NumberOfStepsRight = 0;
	int NumberOfStepsLeft = 0;

        needs = new HashMap<>();
        needs.put("thirst", 60);
        needs.put("hunger", 30);
        needs.put("bladder", 20);
        needs.put("physical energy", 20);
        needs.put("fun", 20);
        needs.put("social energy", 20);
    }

    public void updateNeeds() {
        // Zeitbasierte Änderungen unter Berücksichtigung der Charaktereigenschaften
        updateNeedsOverTime();

        // Zufällige Ereignisse
        applyRandomEvents();

		characterTraits = Collections.unmodifiableMap(tempCharacterTraits);

        // Anpassung basierend auf Charaktereigenschaften
        thirstChange *= 1.0 - (characterTraits.get("good drinker") / 100.0);
        hungerChange *= 1.0 - (characterTraits.get("good eater") / 100.0);
        bladderChange *= 1.0 - (characterTraits.get("strong bladder") / 100.0);
        physicalEnergyChange *= 1.0 + (characterTraits.get("active") / 100.0);
        socialEnergyChange *= 1.0 + (characterTraits.get("social") / 100.0);

        // Aktualisieren der Bedürfnisse
        needs.put("thirst", adjustNeed(needs.get("thirst"), thirstChange));
        needs.put("hunger", adjustNeed(needs.get("hunger"), hungerChange));
        needs.put("bladder", adjustNeed(needs.get("bladder"), bladderChange));
        needs.put("physical energy", adjustNeed(needs.get("physical energy"), physicalEnergyChange));
        needs.put("fun", adjustNeed(needs.get("fun"), funChange));
        needs.put("social energy", adjustNeed(needs.get("social energy"), socialEnergyChange));
    }

    public void updateNeeds() {

        updateNeedsOverTime();
        applyRandomEvents();
        updateNeedsAfterAction(currentSpaceType);

    }

    private void updateNeedsOverTime() {
        // Grundlegende Änderungen pro Schleifendurchlauf
        int thirstChange = 1;
        int hungerChange = 1;
        int bladderChange = 1;
        int physicalEnergyChange = -1;
        int funChange = -1;
        int socialEnergyChange = -1;

        // Anpassung basierend auf Charaktereigenschaften
        thirstChange *= 1.0  - (characterTraits.get("good drinker") / 100.0);
        hungerChange *= 1.0 - (characterTraits.get("good eater") / 100.0);
        bladderChange *= 1.0 - (characterTraits.get("strong bladder") / 100.0);
        physicalEnergyChange *= 1.0 + (characterTraits.get("active") / 100.0);
        socialEnergyChange *= 1.0 + (characterTraits.get("social") / 100.0);

        // Aktualisieren der Bedürfnisse
        needs.put("thirst", adjustNeed(needs.get("thirst"), thirstChange));
        needs.put("hunger", adjustNeed(needs.get("hunger"), hungerChange));
        needs.put("bladder", adjustNeed(needs.get("bladder"), bladderChange));
        needs.put("physical energy", adjustNeed(needs.get("physical energy"), physicalEnergyChange));
        needs.put("fun", adjustNeed(needs.get("fun"), funChange));
        needs.put("social energy", adjustNeed(needs.get("social energy"), socialEnergyChange));
    }

    private void applyRandomEvents() {
        if (random.nextDouble() < 0.1) { // 10% Wahrscheinlichkeit für ein zufälliges Ereignis
            int randomThirstIncrease = random.nextInt(10);
            needs.put("thirst", increaseNeed(needs.get("thirst"), randomThirstIncrease));
            System.out.println("Zufälliges Ereignis: Durst erhöht um " + randomThirstIncrease);
        }
    }

    private int adjustNeed(int currentValue, double change) {
        int newValue = (int) (currentValue + change);
        return Math.max(0, Math.min(newValue, 100));
    }

    private int increaseNeed(int currentValue, int amount) {
        return Math.min(currentValue + amount, 100);
    }

    private int decreaseNeed(int currentValue, int amount) {
        return Math.max(currentValue - amount, 0);
    }

    // Update Needs Methoden bei Interaktionen
    public void drink() {
        needs.put("thirst", decreaseNeed(needs.get("thirst"), 30));
        needs.put("bladder", increaseNeed(needs.get("bladder"), 10));
        System.out.println("Der Avatar hat getrunken. Durst gesenkt, Blase erhöht.");
    }

    public void eat() {
        needs.put("hunger", decreaseNeed(needs.get("hunger"), 30));
        needs.put("physical energy", increaseNeed(needs.get("physical energy"), 10));
        System.out.println("Der Avatar hat gegessen. Hunger gesenkt, physische Energie erhöht.");
    }

    public void dance() {
        needs.put("fun", increaseNeed(needs.get("fun"), 30));
        needs.put("physical energy", decreaseNeed(needs.get("physical energy"), 10));
        needs.put("social energy", increaseNeed(needs.get("social energy"), 10));
        System.out.println("Der Avatar hat getanzt. Spaß und soziale Energie erhöht, physische Energie gesenkt.");
    }


	public void socialize() {
   		needs.put("social energy", decreaseNeed(needs.get("social energy"), 30));
   	 	needs.put("fun", increaseNeed(needs.get("fun"), 20));
    	System.out.println("Der Avatar hate Soziale interaktion. Soziale Energie gesenkt, Spaß erhöht.");
	}

	//NICHT FERTIG
	public void playMusic() {
		needs.put("physical energy", decreaseNeed(needs.get("social energy"), 30));
		needs.put("fun", increaseNeed(needs.get("fun"), 20));
	 	System.out.println("Der Avatar Musik aufgelegt. Physische Energie gesenkt, Spaß erhöht");
 }

 	//NICHT FERTIG
	 public void pee() {
		needs.put("bladder", increaseNeed(needs.get("social energy"), 30));
 		System.out.println("Der Avatar war auf Toilette.");
}

 	//NICHT FERTIG
 	public void relax() {
		needs.put("physical energy", increaseNeed(needs.get("social energy"), 30));
		needs.put("social energy", increaseNeed(needs.get("social energy"), 30));
 		System.out.println("Der Avatar hat sich entspannt.");
}

 
private void updateNeedsAfterAction(String currentSpaceType){
	
	switch(currentSpaceType){

		case "AVATAR":
			socialize();
		break;

		case "DANCEFLOOR":
			dance();
		break;

		case "DJBOOTH":
			playMusic();
		break;

		case "TOILET":
			pee();
		break;

		case "BAR":
			drink();
			eat();
		break;

		case "SEATS":
			relax();
		break;

		default:
		break;

	}
}




Coordinate Steps = new Coordinate(1, 1);

public Direction doAction(ArrayList<SpaceInfo> spacesInRange) {

	extendKnownSpace(spacesInRange);

	if(Steps.getX() == 0 && Steps.getY() == 0){
		Steps = pathFinding(makeSpaceTypeDecision());
	}

	return coordianteSteps(Steps);
}


ArrayList<Coordinate> Dancefloor = new ArrayList<Coordinate>();
ArrayList<Coordinate> Toilet = new ArrayList<Coordinate>();
ArrayList<Coordinate> Bar = new ArrayList<Coordinate>();
ArrayList<Coordinate> Seats = new ArrayList<Coordinate>();
ArrayList<Coordinate> DJBooth = new ArrayList<Coordinate>();



	private void extendKnownSpace(ArrayList<SpaceInfo> spacesInRange){
		SpaceType type;
		Coordinate space = new Coordinate(0, 0);
	
		for(int i = 0; i < spacesInRange.size(); i++){
		
			space.setX(spacesInRange.get(i).getRelativeToAvatarCoordinate().getX());
			space.setY(spacesInRange.get(i).getRelativeToAvatarCoordinate().getY());
			type = spacesInRange.get(i).getType();
	
			knownSpace[space.getX()][space.getY()] = type;

			switch(type){
				case DANCEFLOOR:
				if(!Dancefloor.contains(space)){
					Dancefloor.add(space);
				}
				break;

				case DJBOOTH:
				if(!DJBooth.contains(space)){
					DJBooth.add(space);
				}
				break;

				case TOILET:
				if(!Toilet.contains(space)){
					Toilet.add(space);
				}
				break;

				case BAR:
				if(!Bar.contains(space)){
					Bar.add(space);
				}
				break;

				case SEATS:
				if(!Seats.contains(space)){
					Seats.add(space);
				}
				break;

				default:
				break;

			}

		}
	}

	private SpaceType makeSpaceTypeDecision(){
		SpaceType decision = SpaceType.BAR;
		return decision;
	}


	
	private Coordinate pathFinding(SpaceType spacetype){
		Coordinate destination = new Coordinate(0, 0);
		Coordinate Steps = new Coordinate(0, 0);
		
		
		switch(spacetype){
			case DANCEFLOOR:
			destination = Dancefloor.get(1);
			break;

			case DJBOOTH:
			destination = DJBooth.get(1);
			break;

			case TOILET:
			destination = Toilet.get(1);
			break;

			case BAR:
			destination = Bar.get(1);
			break;

			case SEATS:
			destination = Seats.get(1);
			break;

			default:
			break;
		}
		
		Steps.setX(destination.getX() - currentCoordinate.getX());
		Steps.setY(destination.getY() - currentCoordinate.getX());
		return Steps;
	}

	private Direction coordianteSteps(Coordinate Steps){
		
				// Postiver X Wert nach Rechts
				// Positver Y Wert nach Unten
		if(Steps.getY() > 0){
			Steps.setY(Steps.getY() - 1);
			return Direction.DOWN;
		}
		else if (Steps.getY() < 0) {
			Steps.setY(Steps.getY() + 1);
			return Direction.UP;
		}
		else if (Steps.getX() < 0) {
			Steps.setX(Steps.getY() + 1);
			return Direction.LEFT;
		}
		else if (Steps.getX() > 0) {
			Steps.setX(Steps.getY() - 1);
			return Direction.RIGHT;
		}
		else{
			return Direction.STAY;
		}
	
	}


	private Direction randomDirection() {
        int directionNumber = (int) (Math.random() * 4);

        switch (directionNumber) {
            case 0:
                return Direction.LEFT;
            case 1:
                return Direction.RIGHT;
            case 2:
                return Direction.UP;
            case 3:
                return Direction.DOWN;
            default:
                return Direction.STAY; // Safety net, though unnecessary as directionNumber is bound by 0-3
        }
    }







    /**
     * Determines the direction for the avatar's next turn based on the spaces within its perception range.
     * This method can be overridden to implement a more sophisticated strategy.
     *
     * @param spacesInRange the list of spaces within the avatar's perception range
     * @return the direction for the avatar's next turn
     */
    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
		updateNeeds();
        return randomDirection();
    }
	

  

    /**
     * Returns the perception range of the avatar.
     *
     * @return the perception range
     */
    @Override
    public int getPerceptionRange() {
        return super.getPerceptionRange(); // Assuming SuperAvatar has a method to get the perception range
    }

    /**
     * Sets the perception range of the avatar.
     *
     * @param perceptionRange the perception range to set
     */
    @Override
    public void setPerceptionRange(int perceptionRange) {
        super.setPerceptionRange(perceptionRange); // Set the perception range via the superclass method
    }


}






