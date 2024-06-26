package PersonalAvatars;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import Environment.SpaceType;

/**
 * This class manages the needs and actions of an avatar.
 */
public class TimAvatarUpdateNeeds {

    public TimAvatarUpdateNeeds (){
    }

    private Random random = new Random(); // Random number generator for random events

    /**
     * Handles the avatar drinking action, adjusting thirst and bladder needs.
     */
    public void drink(HashMap<String, Double> needs) {
        needs.put("thirst", decreaseNeed(needs.get("thirst"), 3));
        needs.put("bladder", increaseNeed(needs.get("bladder"), 1));
        System.out.println("Der Avatar hat getrunken. Durst gesenkt, Blase erhöht.");
    }

    /**
     * Handles the avatar eating action, adjusting hunger and physical energy needs.
     */
    public void eat(HashMap<String, Double> needs) {
        needs.put("hunger", decreaseNeed(needs.get("hunger"), 3));
        needs.put("physical energy", increaseNeed(needs.get("physical energy"), 1));
        System.out.println("Der Avatar hat gegessen. Hunger gesenkt, physische Energie erhöht.");
    }

    /**
     * Handles the avatar dancing action, adjusting fun, physical energy, and social energy needs.
     */
    public void dance(HashMap<String, Double> needs) {
        needs.put("fun", increaseNeed(needs.get("fun"), 3));
        needs.put("physical energy", decreaseNeed(needs.get("physical energy"), 1));
        needs.put("social energy", increaseNeed(needs.get("social energy"), 1));
        System.out.println("Der Avatar hat getanzt. Spaß und soziale Energie erhöht, physische Energie gesenkt.");
    }

    /**
     * Handles the avatar socializing action, adjusting social energy and fun needs.
     */
    public void socialize(HashMap<String, Double> needs) {
        needs.put("social energy", increaseNeed(needs.get("social energy"), 3));
        needs.put("fun", increaseNeed(needs.get("fun"), 2));
        System.out.println("Der Avatar hatte soziale Interaktion. Soziale Energie und Spaß erhöht.");
    }

    /**
     * Handles the avatar playing music action, adjusting physical energy and fun needs.
     */
    public void playMusic(HashMap<String, Double> needs) {
        needs.put("physical energy", decreaseNeed(needs.get("physical energy"), 1));
        needs.put("fun", increaseNeed(needs.get("fun"), 2));
        System.out.println("Der Avatar hat Musik aufgelegt. Physische Energie gesenkt, Spaß erhöht.");
    }

    /**
     * Handles the avatar peeing action, adjusting bladder need.
     */
    public void pee(HashMap<String, Double> needs) {
        needs.put("bladder", decreaseNeed(needs.get("bladder"), 30));
        System.out.println("Der Avatar war auf Toilette.");
    }

    /**
     * Handles the avatar relaxing action, adjusting physical and social energy needs.
     */
    public void relax(HashMap<String, Double> needs) {
        needs.put("physical energy", increaseNeed(needs.get("physical energy"), 30));
        needs.put("social energy", increaseNeed(needs.get("social energy"), 30));
        System.out.println("Der Avatar hat sich entspannt.");
    }

    /**
     * Adjusts the current need value by the specified change, ensuring it stays within 0 to 100.
     */
    private double adjustNeed(double currentValue, double change) {
        double newValue = currentValue + change;
        return Math.max(0.0, Math.min(newValue, 100.0));
    }

    /**
     * Increases the current need value by the specified amount, up to a maximum of 100.
     */
    private double increaseNeed(double currentValue, double amount) {
        return Math.min(currentValue + amount, 100.0);
    }

    /**
     * Decreases the current need value by the specified amount, ensuring it doesn't go below 0.
     */
    private double decreaseNeed(double currentValue, double amount) {
        return Math.max(currentValue - amount, 0.0);
    }

    /**
     * Determines the most urgent need based on current need values.
     * Uses a priority calculation based on need type and current value.
     */
    public static String getMostUrgentNeed(Map<String, Double> needs) {
        String mostUrgentNeed = null;
        double highestPriority = Double.MIN_VALUE;

        for (Map.Entry<String, Double> entry : needs.entrySet()) {
            String need = entry.getKey();
            double value = entry.getValue();
            double priority = calculatePriority(need, value);

            if (priority > highestPriority) {
                highestPriority = priority;
                mostUrgentNeed = need;
            }
        }
        return mostUrgentNeed;
    }

    /**
     * Calculates a priority value for a given need based on its current value.
     * Placeholder logic: inversely proportional to the current value for thirst, hunger, bladder;
     * directly proportional for physical energy, fun, social energy.
     */
    private static double calculatePriority(String need, double value) {
        switch (need) {
            case "thirst":
            case "hunger":
            case "bladder":
                return value;
            case "physical energy":
            case "fun":
            case "social energy":
                return 100 - value;
            default:
                return 0.0;
        }
    }

    /**
     * Updates avatar needs over time based on character traits and predefined changes.
     */
    public void updateNeedsOverTime(HashMap<String, Double> needs, Map<String, Double> characterTraits) {
        double thirstChange = 0.4;
        double hungerChange = 0.4;
        double bladderChange = 0.4;
        double physicalEnergyChange = -0.4;
        double funChange = -0.4;
        double socialEnergyChange = -0.4;

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

        System.out.println("Thirst " + needs.get("thirst"));
        System.out.println("hunger " + needs.get("hunger"));
        System.out.println("bladder " + needs.get("bladder"));
        System.out.println("physical energy " + needs.get("physical energy"));
        System.out.println("fun " + needs.get("fun"));
        System.out.println("social energy " + needs.get("social energy"));

    }

    /**
     * Applies random events affecting avatar needs, such as sudden increase in thirst.
     */
    public void applyRandomEvents(HashMap<String, Double> needs) {

        if (random.nextDouble() < 0.01) { // 10% chance of random event
            int randomThirstIncrease = random.nextInt(80); // Random increase in thirst up to 80

            int randomInt = random.nextInt(7);
            String key;
            switch (randomInt) {
                case 1:
                    key = "thirst";
                    break;
                case 2:
                    key = "hunger";
                    break;
    
                case 3:
                    key = "bladder";
                    break;
    
                case 4:
                    key = "physical energy";
                    break;
    
                case 5:
                    key = "fun";
                    break;
    
                case 6:
                    key = "social energy";
                    break;
            
                default:
                key = "thirst";
                    break;
            }

            needs.put(key, increaseNeed(needs.get(key), randomThirstIncrease));
            System.out.println("Zufälliges Ereignis:" + key + " erhöht um " + randomThirstIncrease);
        }
    }

    /**
     * Updates avatar needs after performing a specific action in a particular space type.
     */
    public void updateNeedsAfterAction(SpaceType currentSpaceType, HashMap<String, Double> needs) {
        switch(currentSpaceType) {
            case AVATAR:
                socialize(needs);
                break;
            case DANCEFLOOR:
                dance(needs);
                break;
            case DJBOOTH:
                playMusic(needs);
                break;
            case TOILET:
                pee(needs);
                break;
            case BAR:
                drink(needs);
                eat(needs);
                break;
            case SEATS:
                relax(needs);
                break;
            default:
                break;
        }
    }

    /**
     * Makes a decision on which space type the avatar should move to based on its most urgent need.
     */
    SpaceType makeSpaceTypeDecision(HashMap<String, Double> needs) {
        String mostNeed = getMostUrgentNeed(needs);

        switch (mostNeed) {
            case "thirst":
            case "hunger":
                return SpaceType.BAR;
            case "bladder":
                return SpaceType.TOILET;
            case "physical energy":
                return SpaceType.SEATS;
            case "fun":
                return SpaceType.DJBOOTH;
            case "social energy":
                return SpaceType.DANCEFLOOR;
            default:
                return SpaceType.EMPTY;
        }
    }
}
