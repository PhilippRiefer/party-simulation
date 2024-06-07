package Environment;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The SimulationGUI class represents the graphical user interface for the
 * nightclub simulation.
 * It provides a grid-based environment for the simulation and allows the user
 * to adjust the size of the grid using a slider.
 */
public class SimulationGUI {

    // Sizes of the grid
    // --------------------------------------
    final int LINES_SMALL = 10;// y
    final int COLUMNS_SMALL = 20;// x
    final int LINES_MEDIUM = 20;// y
    final int COLUMNS_MEDIUM = 40;// x
    final int LINES_LARGE = 40;// y
    final int COLUMNS_LARGE = 80;// x
    private JPanel environmentLeft;
    private int numCols;
    private int numRows;
    JPanel pitchPanel;
    private JPanel[][] gridCells;

    /**
     * The SimulationGUI class represents the graphical user interface for the
     * nightclub simulation.
     * It creates a frame with panels to display the environment and controls.
     */
    public SimulationGUI() {
        // Frame
        JFrame frame = new JFrame("Nightclub Simulation SS24");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);

        // Panel in frame
        JPanel environmentPanel = new JPanel(new GridBagLayout());

        GridBagConstraints panelConstraints = new GridBagConstraints();
        panelConstraints.gridx = 0;
        panelConstraints.gridy = 0;
        panelConstraints.weightx = 1.0;
        panelConstraints.weighty = 1.0;
        panelConstraints.fill = GridBagConstraints.BOTH;
        JPanel environment = new JPanel();
        this.environmentLeft = environment;
        environment.setBackground(Color.WHITE);
        environment.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        environmentPanel.add(environment, panelConstraints);

        frame.add(environmentPanel);
        frame.setVisible(true);
    }

    /**
     * Sets the number of columns in the simulation grid.
     *
     * @param numCols the number of columns to set
     */
    public void setNumCols(int numCols) {
        this.numCols = numCols;
    }

    /**
     * Sets the number of rows in the simulation GUI.
     *
     * @param numRows the number of rows to set
     */
    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    /**
     * Returns the number of columns in the simulation grid.
     *
     * @return the number of columns
     */
    public int getNumCols() {
        return numCols;
    }

    /**
     * Returns the number of rows in the simulation GUI.
     *
     * @return the number of rows
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * Repaints the simulation GUI based on the given value.
     * The value determines the size of the pitch to be created.
     * 
     * @param value the value indicating the size of the pitch
     */
    public void repaint() {
        environmentLeft.removeAll();
        createPitch(environmentLeft, COLUMNS_MEDIUM, LINES_MEDIUM);
        setNumCols(COLUMNS_MEDIUM);
        setNumRows(LINES_MEDIUM - 1);
        environmentLeft.revalidate();
        environmentLeft.repaint();
    }

    /**
     * Creates a pitch panel with a grid layout and adds it to the specified panel.
     * Each cell in the grid has a white background and a light gray border.
     * Tooltips are created for each cell to identify their position in the grid.
     *
     * @param panelLeft the panel to which the pitch panel will be added
     * @param lines     the number of lines in the grid
     * @param numCols   the number of columns in the grid
     */
    public void createPitch(JPanel panelLeft, int x, int y) {
        this.pitchPanel = new JPanel(new GridLayout(y, x));// these need to be y, x
        pitchPanel.setBackground(Color.WHITE);
        pitchPanel.setPreferredSize(panelLeft.getSize());

        // Initialize the gridCells array
        gridCells = new JPanel[x][y];

        // Add border to each cell in the grid
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                JPanel cell = new JPanel();

                cell.setBackground(Color.WHITE);

                cell.setBorder(BorderFactory.createLineBorder(Color.lightGray));
                // create tool tip for each cell to identify it later

                cell.setToolTipText("(" + j + "," + i + ")");
                pitchPanel.add(cell);

                // Store the cell in the array
                gridCells[j][i] = cell;
            }
        }

        panelLeft.add(pitchPanel);
    }

    /**
     * Paints the avatar on the simulation GUI at the specified coordinate with the
     * given color.
     * If the color is null, the default color is blue.
     *
     * @param coordinate the coordinate where the avatar should be painted
     * @param color      the color of the avatar
     */
    public void paintAvatar(Coordinate coordinate, Color color) {
        if (color == null) {
            color = Color.BLUE; // Default color is blue
        }

        JPanel cellToRepaint = gridCells[coordinate.getX()][coordinate.getY()];
        cellToRepaint.setBackground(color);
        cellToRepaint.repaint();
    }

    // TODO
    public void paintComponent(int x, int y, Color color) {
        if (color == null) {
            color = Color.BLUE; // Default color is blue
        }

        JPanel cellToRepaint = gridCells[x][y];
        cellToRepaint.setBackground(color);
        cellToRepaint.repaint();
    }




    public void eraseAvatar(Coordinate coordinate, SpaceType spaceType) {
        repaintCellToDefault(coordinate, spaceType);
    }

    private void repaintCellToDefault(Coordinate coordinate, SpaceType spaceType) {

        
            // Get the cell to repaint
        JPanel cellToRepaint = gridCells[coordinate.getX()][coordinate.getY()];
       

        // Set the cell's background color to the default color (white)
        switch (spaceType) {
            case DANCEFLOOR:
                cellToRepaint.setBackground(Color.YELLOW);
                break;
            case DJBOOTH:
                cellToRepaint.setBackground(Color.PINK);
                break;
            case TOILET:
                cellToRepaint.setBackground(Color.GREEN);
                break;
            case BAR:
                cellToRepaint.setBackground(Color.BLACK);
                break;
            case SEATS:
                cellToRepaint.setBackground(Color.CYAN);
                break;
            default:
                cellToRepaint.setBackground(Color.WHITE);
                break;
        }
        // cellToRepaint.setBackground(Color.WHITE);
        // cellToRepaint.setBackground(Color.GRAY);
        
        // Set the cell's border to the default border (light gray)
        cellToRepaint.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        // Repaint the cell to reflect the changes
        cellToRepaint.repaint();
            
        
    }
    
}