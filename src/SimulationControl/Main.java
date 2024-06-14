package SimulationControl;

/**
 * The main class of the program.
 * It prompts the user to enter a whole number above 0 for the default perception range,
 * creates an instance of SimulationControl with the given perception range,
 * and starts the simulation by calling the loopThroughAvatars method.
 */
public class Main {
    /**
     * The main method of the program.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Scanner scanner = new Scanner(System.in);
        int perceptionRange = 1;
        System.out.println("Perception range is set to " + perceptionRange);
        
        SimulationControl simcontrol = new SimulationControl(perceptionRange);
        while(true){
            simcontrol.loopThroughAvatars();
            try {
                // Make the program wait for one second (1000 milliseconds)
                Thread.sleep(300);
            } catch (InterruptedException e) {
                // Handle the exception if the sleep is interrupted
                e.printStackTrace();
            }
        }
    }
}
