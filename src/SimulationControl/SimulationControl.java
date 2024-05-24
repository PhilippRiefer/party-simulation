package SimulationControl;

import java.util.ArrayList;
import Environment.*;
import AvatarInterface.*;

public class SimulationControl {

	private ArrayList<SuperAvatar> avatars = new ArrayList<SuperAvatar>();
	private Environment environment;
	
	public SimulationControl() {
		System.out.println("Hallo");

		environment = new Environment();

<<<<<<< Updated upstream
		for (int i = 0; i < 12; i++){
			avatars.add(new TestAvatar(i));
		}
		// avatars.add(new AvatarNasser(i+1));
	}
	
	public void loopThroughAvatars() {
		for (SuperAvatar avatar : avatars) {
			ArrayList<SpaceInfo> si = environment.getAdjacentToAvatar(avatar.getAvatarID());
			Direction dir = avatar.yourTurn(si);
			// boolean hasMoved = environment.moveAvatar(avatar.getAvatarID(), dir);
			// avatars.setHasMoved(hasMoved);
			// Avatar mitteilen, ob er sich tatsächlich bewegt hat: hasMoved
		}
	}
	
}
=======
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

        Color color = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), 255);
        OleAvatar s1 = new OleAvatar(nextAvatarID++, perceptionRange, color);
        avatars.add(s1);
        OleAvatar s2 = new OleAvatar(nextAvatarID++, perceptionRange, color);
        avatars.add(s2);
        OleAvatar s3 = new OleAvatar(nextAvatarID++, perceptionRange, color);
        avatars.add(s3);
        OleAvatar s4 = new OleAvatar(nextAvatarID++, perceptionRange, color);
        avatars.add(s4);

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
                wait(50);
            } catch (Exception e) {
                System.err.println("Error processing avatar " + avatar.getAvatarID() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
>>>>>>> Stashed changes
