package PersonalAvatars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.lang.Math;

import Environment.*;
import AvatarInterface.*;
import java.awt.Color;

public class TimAvatar extends SuperAvatar {

    private final Map<String, Integer> characterTraits;
    HashMap<String, Integer> needs = new HashMap<>();
    Random random = new Random();
    String currentSpaceType;
    Coordinate currentCoordinate = new Coordinate(0, 0); // Initialisiere currentCoordinate
    SpaceType[][] knownSpace = new SpaceType[40][20];

    ArrayList<Coordinate> Dancefloor = new ArrayList<>();
    ArrayList<Coordinate> Toilet = new ArrayList<>();
    ArrayList<Coordinate> Bar = new ArrayList<>();
    ArrayList<Coordinate> Seats = new ArrayList<>();
    ArrayList<Coordinate> DJBooth = new ArrayList<>();

    Coordinate Steps = new Coordinate(0, 0);

    public TimAvatar(int id, int perceptionRange, Color color) {
        super(id, perceptionRange, color);

        HashMap<String, Integer> tempCharacterTraits = new HashMap<>();

        tempCharacterTraits.put("active", 100);
        tempCharacterTraits.put("social", 60);
        tempCharacterTraits.put("good drinker", 80);
        tempCharacterTraits.put("good eater", 40);
        tempCharacterTraits.put("strong bladder", 20);

        characterTraits = Collections.unmodifiableMap(tempCharacterTraits);

        needs.put("thirst", 60);
        needs.put("hunger", 30);
        needs.put("bladder", 20);
        needs.put("physical energy", 20);
        needs.put("fun", 20);
        needs.put("social energy", 20);
    }

    public void updateNeeds() {
        updateNeedsOverTime();
        applyRandomEvents();
        updateNeedsAfterAction(currentSpaceType);
    }

    private void updateNeedsOverTime() {
        int thirstChange = 1;
        int hungerChange = 1;
        int bladderChange = 1;
        int physicalEnergyChange = -1;
        int funChange = -1;
        int socialEnergyChange = -1;

        thirstChange *= 1.0 - (characterTraits.get("good drinker") / 100.0);
        hungerChange *= 1.0 - (characterTraits.get("good eater") / 100.0);
        bladderChange *= 1.0 - (characterTraits.get("strong bladder") / 100.0);
        physicalEnergyChange *= 1.0 + (characterTraits.get("active") / 100.0);
        socialEnergyChange *= 1.0 + (characterTraits.get("social") / 100.0);

        needs.put("thirst", adjustNeed(needs.get("thirst"), thirstChange));
        needs.put("hunger", adjustNeed(needs.get("hunger"), hungerChange));
        needs.put("bladder", adjustNeed(needs.get("bladder"), bladderChange));
        needs.put("physical energy", adjustNeed(needs.get("physical energy"), physicalEnergyChange));
        needs.put("fun", adjustNeed(needs.get("fun"), funChange));
        needs.put("social energy", adjustNeed(needs.get("social energy"), socialEnergyChange));
    }

    private void applyRandomEvents() {
        if (random.nextDouble() < 0.1) {
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
        needs.put("social energy", increaseNeed(needs.get("social energy"), 30));
        needs.put("fun", increaseNeed(needs.get("fun"), 20));
        System.out.println("Der Avatar hatte soziale Interaktion. Soziale Energie und Spaß erhöht.");
    }

    public void playMusic() {
        needs.put("physical energy", decreaseNeed(needs.get("physical energy"), 30));
        needs.put("fun", increaseNeed(needs.get("fun"), 20));
        System.out.println("Der Avatar hat Musik aufgelegt. Physische Energie gesenkt, Spaß erhöht.");
    }

    public void pee() {
        needs.put("bladder", decreaseNeed(needs.get("bladder"), 30));
        System.out.println("Der Avatar war auf Toilette.");
    }

    public void relax() {
        needs.put("physical energy", increaseNeed(needs.get("physical energy"), 30));
        needs.put("social energy", increaseNeed(needs.get("social energy"), 30));
        System.out.println("Der Avatar hat sich entspannt.");
    }

    private void updateNeedsAfterAction(String currentSpaceType) {
        switch(currentSpaceType) {
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

    public Direction doAction(ArrayList<SpaceInfo> spacesInRange) {
        extendKnownSpace(spacesInRange);

        if(Steps.getX() == 0 && Steps.getY() == 0) {
            Steps = pathFinding(makeSpaceTypeDecision());
        }

        return coordianteSteps(Steps);
    }

    private void extendKnownSpace(ArrayList<SpaceInfo> spacesInRange) {
        for(SpaceInfo spaceInfo : spacesInRange) {
            Coordinate space = new Coordinate(spaceInfo.getRelativeToAvatarCoordinate().getX(), spaceInfo.getRelativeToAvatarCoordinate().getY());
            SpaceType type = spaceInfo.getType();

            knownSpace[space.getX()][space.getY()] = type;

            switch(type) {
                case DANCEFLOOR:
                    if(!Dancefloor.contains(space)) {
                        Dancefloor.add(space);
                    }
                    break;
                case DJBOOTH:
                    if(!DJBooth.contains(space)) {
                        DJBooth.add(space);
                    }
                    break;
                case TOILET:
                    if(!Toilet.contains(space)) {
                        Toilet.add(space);
                    }
                    break;
                case BAR:
                    if(!Bar.contains(space)) {
                        Bar.add(space);
                    }
                    break;
                case SEATS:
                    if(!Seats.contains(space)) {
                        Seats.add(space);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private SpaceType makeSpaceTypeDecision() {
        SpaceType decision = SpaceType.BAR;
        // Hier kann man die Entscheidung basierend auf den Bedürfnissen des Avatars erweitern
        return decision;
    }

    private Coordinate pathFinding(SpaceType spacetype) {
        Coordinate destination = new Coordinate(0, 0);

        switch(spacetype) {
            case DANCEFLOOR:
                if (!Dancefloor.isEmpty()) {
                    destination = Dancefloor.get(0);
                }
                break;
            case DJBOOTH:
                if (!DJBooth.isEmpty()) {
                    destination = DJBooth.get(0);
                }
                break;
            case TOILET:
                if (!Toilet.isEmpty()) {
                    destination = Toilet.get(0);
                }
                break;
            case BAR:
                if (!Bar.isEmpty()) {
                    destination = Bar.get(0);
                }
                break;
            case SEATS:
                if (!Seats.isEmpty()) {
                    destination = Seats.get(0);
                }
                break;
            default:
                break;
        }

        Coordinate steps = new Coordinate(
            destination.getX() - currentCoordinate.getX(),
            destination.getY() - currentCoordinate.getY()
        );

        return steps;
    }

    private Direction coordianteSteps(Coordinate Steps) {
        if (Steps.getY() > 0) {
            Steps.setY(Steps.getY() - 1);
            return Direction.DOWN;
        } else if (Steps.getY() < 0) {
            Steps.setY(Steps.getY() + 1);
            return Direction.UP;
        } else if (Steps.getX() < 0) {
            Steps.setX(Steps.getX() + 1);
            return Direction.LEFT;
        } else if (Steps.getX() > 0) {
            Steps.setX(Steps.getX() - 1);
            return Direction.RIGHT;
        } else {
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
                return Direction.STAY;
        }
    }

    @Override
    public Direction yourTurn(ArrayList<SpaceInfo> spacesInRange) {
        updateNeeds();
        return doAction(spacesInRange);
    }

    @Override
    public int getPerceptionRange() {
        return super.getPerceptionRange();
    }

    @Override
    public void setPerceptionRange(int perceptionRange) {
        super.setPerceptionRange(perceptionRange);
    }
}
