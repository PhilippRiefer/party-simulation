package PersonalAvatars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import Environment.*;
import AvatarInterface.*;

/**
 * This class represents a template avatar that extends the SuperAvatar class and implements the AvatarInterface.
 * It provides a basic implementation of the avatar's behavior and perception range.
 */
public class TimAvatar extends SuperAvatar { // implements AvatarInterface

    this.id = id;
        this.perceptionRange = perceptionRange;
        this.random = new Random();
        this.knownSpace = new HashMap<>();
        this.currentCoordinate = new Coordinate(0, 0); // Initiale Position des Avatars

        HashMap<String, Integer> tempCharacterTraits = new HashMap<>();
        tempCharacterTraits.put("active", 100);
        tempCharacterTraits.put("social", 60);
        tempCharacterTraits.put("good drinker", 80);
        tempCharacterTraits.put("good eater", 40);
        tempCharacterTraits.put("strong bladder", 20);
        characterTraits = Collections.unmodifiableMap(tempCharacterTraits);

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

       updateNeedsAfterAction();
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

    // Methoden für Interaktionen
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
    	System.out.println("Der Avatar hat sozialisiert. Soziale Energie gesenkt, Spaß erhöht.");
}

	

	private void extendKnownSpace(ArrayList<SpaceInfo> spacesInRange){
	


	}

	private void calculationNeeds(){
				
	}

	private SpaceType makeSpaceTypeDecision(){
		SpaceType spaceType;

		return spaceType; 
	}

	

	private Coordinate searchInKnownSpace(SpaceType spaceType){
		Coordinate coordinate;

		return coordinate;
	}

	private int pathFinding(Coordinate coordinate){
		int direction = 4;


		return direction;
	}

	private Direction doAction(int direction){
		switch (direction) {
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
		extendKnownSpace(spacesInRange);
		calculationNeeds();
		SpaceType spaceType = makeSpaceTypeDecision();
		Coordinate coordinate = searchInKnownSpace(spaceType);
		int direction = pathFinding(coordinate);
        return doAction(direction);
    }
	


    /**
     * Generates a random direction for the avatar to move.
     *
     * @return a random direction
     */
 
  

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


/*EMPTY,
OBSTACLE,
AVATAR,
DANCEFLOOR,
DJBOOTH,
TOILET,
BAR,
SEATS*/


