package SimulationControl;

import java.awt.Color;
import java.util.ArrayList;
import Environment.*;
import PersonalAvatars.*;
import PersonalAvatars.TomAvatar;
import AvatarInterface.*;
import java.util.Random;
import org.reflections.Reflections;

/**
 * The SimulationControl class controls the simulation of avatars in an
 * environment.
 * It manages the creation of avatars, their perception range, and their
 * movement within the environment.
 */
public class SimulationControl {

    private ArrayList<SuperAvatar> avatars = new ArrayList<>();
    private Environment environment;
    private static int nextAvatarID = 0; // Static counter to ensure unique IDs

    /**
     * Constructs a SimulationControl object with the specified perception range.
     * Initializes the environment and creates avatars based on the available
     * personal avatar classes.
     *
     * @param perceptionRange the perception range of the avatars
     */
    public SimulationControl(int perceptionRange) {
        System.out.println("gonna create environment");
        environment = new Environment();
        System.out.println("environment created");

        // Reflections reflections = new Reflections("PersonalAvatars");

        // for (Class<? extends SuperAvatar> personalAvatarClass : reflections.getSubTypesOf(SuperAvatar.class)) {
        //     try {
        //         Color color = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), 255);
        //         SuperAvatar avatar = personalAvatarClass.getDeclaredConstructor(int.class, int.class, Color.class)
        //                 .newInstance(nextAvatarID++, perceptionRange, color);
        //         avatars.add(avatar);
        //         String avatarName = avatar.getClass().getSimpleName().replace("Avatar", "");
        //         System.out.println("Added " + avatarName + ": ID: " + avatar.getAvatarID() + ", Perception Range: "
        //                 + avatar.getPerceptionRange() + ", Color: " + avatar.getAvatarColor());
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //     }
        // }

        Color color = Color.orange;

        SuperAvatar Tom = new TomAvatar(nextAvatarID++, perceptionRange, color);
        avatars.add(Tom);

        for (SuperAvatar avatar : avatars) {
            environment.placeAvatar(avatar.getAvatarID());
        }
    }

    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Loops through all the avatars and performs their turn in the simulation.
     * Retrieves the adjacent space information for each avatar, determines their
     * move direction,
     * and updates their movement status in the environment.
     */
    public void loopThroughAvatars() {
        for (SuperAvatar avatar : avatars) {
            try {
                ArrayList<SpaceInfo> si = environment.getAdjacentToAvatar(avatar.getAvatarID(), avatar.getPerceptionRange());
                Direction dir = avatar.yourTurn(si);
                boolean hasMoved = environment.moveAvatar(avatar.getAvatarID(), dir, avatar.getAvatarColor());
                avatar.setHasMoved(hasMoved);
                System.out.println("Avatar" + avatar.getAvatarID() + " has moved = " + hasMoved);
                wait(1000);
            } catch (Exception e) {
                System.err.println("Error processing avatar " + avatar.getAvatarID() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
