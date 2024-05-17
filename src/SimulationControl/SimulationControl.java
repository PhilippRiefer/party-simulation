package SimulationControl;

import java.awt.Color;
import java.util.ArrayList;
import Environment.*;
import AvatarInterface.*;
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

        Reflections reflections = new Reflections("PersonalAvatars");

        for (Class<? extends SuperAvatar> personalAvatarClass : reflections.getSubTypesOf(SuperAvatar.class)) {
            try {
                SuperAvatar avatar = personalAvatarClass.getDeclaredConstructor(int.class, int.class)
                        .newInstance(nextAvatarID++, perceptionRange);
                avatars.add(avatar);
                String avatarName = avatar.getClass().getSimpleName().replace("Avatar", "");
                System.out.println("Added " + avatarName + ": ID: " + avatar.getAvatarID() + ", Perception Range: "
                        + avatar.getPerceptionRange());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
            ArrayList<SpaceInfo> si = environment.getAdjacentToAvatar(avatar.getAvatarID(),
                    avatar.getPerceptionRange());
            Direction dir = avatar.yourTurn(si);
            boolean hasMoved = environment.moveAvatar(avatar.getAvatarID(), dir);
            avatar.setHasMoved(hasMoved);
            System.out.println("Avatar" + avatar.getAvatarID() + " has moved = " + hasMoved);
            wait(300);
        }
    }
}
