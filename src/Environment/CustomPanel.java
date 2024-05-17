package Environment;

/********************************************
 * Author: Soodeh, Ole
 * Version: v.2
 * Date:   20240412
 * ------------------------------------------
 * Description: The frame and the panels it contains 
 * are to be created in this class. The functionality 
 * will follow in another class
 ********************************************/

 import javax.swing.*;

import java.awt.*;
 
 public class CustomPanel {
 
    //  private SliderPanel inputs = new SliderPanel();
 
    //  public void createFrame() {
    //      // Frame
    //      // --------------------------------------
    //      JFrame frame = new JFrame("Nightclub Simulation SS24");
    //      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //      frame.setSize(1000, 500);
    //      // Panel in frame
    //      // --------------------------------------
    //      JPanel environmentPanel = new JPanel(new GridBagLayout());
    //      // Left-Panelside -> 90% of the frame
    //      // --------------------------------------
    //      GridBagConstraints leftPanelConstraints = new GridBagConstraints();
    //      leftPanelConstraints.gridx = 0;
    //      leftPanelConstraints.gridy = 0;
    //      leftPanelConstraints.weightx = 0.9;
    //      leftPanelConstraints.weighty = 1.0;
    //      leftPanelConstraints.fill = GridBagConstraints.BOTH;
    //      JPanel environmentLeft = new JPanel();
    //      environmentLeft.setBackground(Color.WHITE);
    //      environmentLeft.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    //      environmentPanel.add(environmentLeft, leftPanelConstraints);
    //      // Right-Panelside -> 10% of the frame
    //      // --------------------------------------
    //      GridBagConstraints rightPanelConstraints = new GridBagConstraints();
    //      rightPanelConstraints.gridx = 1;
    //      rightPanelConstraints.gridy = 0;
    //      rightPanelConstraints.weightx = 0.1;
    //      rightPanelConstraints.weighty = 1.0;
    //      rightPanelConstraints.fill = GridBagConstraints.BOTH;
    //      JPanel environmentRight = new JPanel();
    //      environmentRight.setBackground(Color.WHITE);
    //      environmentRight.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    //      environmentPanel.add(environmentRight, rightPanelConstraints);
    //      // panel inside the Panel on the rightside
    //      // --------------------------------------
    //      JPanel environmentRightInside = new JPanel(new GridBagLayout());
    //      environmentRightInside.setBackground(Color.WHITE);
    //      environmentRightInside.setPreferredSize(new Dimension(100, 450));
    //      environmentRight.add(environmentRightInside);
    //      // Top-Panel of the inside-panel -> 20%
    //      // --------------------------------------
    //      GridBagConstraints topPanelConstraints = new GridBagConstraints();
    //      topPanelConstraints.gridx = 0;
    //      topPanelConstraints.gridy = 0;
    //      topPanelConstraints.weightx = 1.0;
    //      topPanelConstraints.weighty = 0.2;
    //      topPanelConstraints.fill = GridBagConstraints.BOTH;
    //      JPanel topRight = new JPanel();
    //      topRight.setBackground(Color.WHITE);
    //      inputs.createLabels(topRight);                                          // create a label as headline
    //      environmentRightInside.add(topRight, topPanelConstraints);
    //      // Bottom-Panel of the inside-panel -> 80%
    //      // --------------------------------------
    //      GridBagConstraints bottomPanelConstraints = new GridBagConstraints();
    //      bottomPanelConstraints.gridx = 0;
    //      bottomPanelConstraints.gridy = 1;
    //      bottomPanelConstraints.weightx = 1.0;
    //      bottomPanelConstraints.weighty = 0.8;
    //      bottomPanelConstraints.fill = GridBagConstraints.BOTH;
    //      JPanel bottomRight = new JPanel();
    //      bottomRight.setBackground(Color.WHITE);
    //      inputs.createSliderAndGrid(bottomRight, environmentLeft);               // create the slider and the grid
    //      environmentRightInside.add(bottomRight, bottomPanelConstraints);
    //      // make frame visible
    //      // --------------------------------------
    //      frame.add(environmentPanel);
    //      frame.setVisible(true);
    //  }

    // public SpaceType getSpace(int currentX, int downY) {
    //     SpaceType space = inputs.getSpace(currentX, downY);
    //     return space;
    // }
 }
 
 // public class CustomPanel extends JPanel {
 // private int width = 100;
 // private int height = 80;
 
 // private int rows = 10;
 // private int columns = 10;
 
 // public static final int ORIGINAL_ROWS = 20; // Change as needed
 // public static final int ORIGINAL_COLUMNS = 20; // Change as needed
 
 // private int margin = 100;
 // private boolean[][] occupancyStatus; // 2D array to store occupancy status of
 // each grid point
 
 // private List<Point> highlightedCells;
 
 // // Constructor
 // public CustomPanel() {
 // // for panel properties if needed
 // }
 
 // // Method to highlight specific grid cells
 // public void highlightCells(List<Point> cells) {
 // highlightedCells = cells;
 // repaint(); // Redraw the panel to reflect the highlights
 // }
 
 // @Override
 // protected void paintComponent(Graphics g) {//override the paintComponent
 // method to draw the grid floor based on the size and coordinates
 // super.paintComponent(g);
 
 // width = getWidth() - margin;
 // height = getHeight() - margin;
 
 // // Calculate cell size based on grid size
 // int cellWidth = width / columns; //100
 // int cellHeight = height / rows; //100
 
 // g.setColor(Color.LIGHT_GRAY);
 
 // // Draw horizontal grid lines
 // for (int i = 0; i <= rows; i++) {
 // int y = i * cellHeight;
 // g.drawLine(0, y, width, y);
 // }
 
 // // Draw vertical grid lines
 // for (int i = 0; i <= columns; i++) {
 // int x = i * cellWidth;
 // g.drawLine(x, 0, x, height);
 // }
 
 // /* g.setColor(Color.PINK);
 // g.drawRect(0, 0, width, height); */
 
 // if (highlightedCells != null) {
 // g.setColor(new Color(0, 0, 255, 128)); // Set the highlight color
 // for (Point cell : highlightedCells) {
 // int x = cell.x * cellWidth;
 // int y = cell.y * cellHeight;
 // g.fillRect(x, y, cellWidth - 1, cellHeight - 1); // Fill the highlighted cell
 // with color
 // }
 // }
 // }
 
 // //DO we need this after creating the grids??..............
 // /* public void setPanelSize(int width, int height) {
 // this.width = width;
 // this.height = height;
 // repaint();
 // } */
 // //.......................................................
 
 // // Method to set the size of the grid
 // public void setGridSize(int rows, int columns) {
 // this.rows = rows;
 // this.columns = columns;
 // repaint(); // Redraw the panel with the new grid size
 // occupancyStatus = new boolean[rows][columns]; // Initialize occupancy status
 // array
 // // By default, all grid points are free initially
 
 // }
 // // set occupancy status of a grid point
 // public void setOccupancyStatus(int row, int column, boolean occupied) {
 // occupancyStatus[row][column] = occupied;
 // }
 // //check if a grid point is occupied
 // public boolean isOccupied(int row, int column) {
 // return occupancyStatus[row][column];
 // }
 
 // // Method to retrieve the coordinates of the grid points
 // public Coordinate[][] getGridCoordinates() {
 // Coordinate[][] coordinates = new Coordinate[rows][columns];
 // for (int i = 0; i < rows; i++) {
 // for (int j = 0; j < columns; j++) {
 // coordinates[i][j] = new Coordinate(j, i); // Swap x and y to match (row,
 // column) convention
 // }
 // }
 
 // return coordinates;
 // }
 // }