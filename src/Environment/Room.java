/********************************************
 * Author: Ole
 * Version: v.1
 * Date:   20240423
 * ------------------------------------------
 * Description: The actual grid of the environment 
 * is to be created in this class. Additional 
 * functions for creating objects in space are 
 * also added
 ********************************************/

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.*;

public class Room{

    // create the pitch 
	// ------------------------------------------
	public void createPitch(JPanel panelLeft, int lines, int columns) {
        JPanel pitchPanel = new JPanel(new GridLayout(lines, columns));
        pitchPanel.setBackground(Color.WHITE);
        pitchPanel.setPreferredSize(panelLeft.getSize());
        // Add border to each cell in the grid
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                JPanel cell = new JPanel();
                cell.setBackground(Color.WHITE);
                cell.setBorder(BorderFactory.createLineBorder(Color.lightGray));
                // create tool tip for each cell to identify it later
                cell.setToolTipText("(" + i + "," + j + ")");
                pitchPanel.add(cell);
            }
        }

        // create some components
        // createComponent(pitchPanel,"RED",  new int[]{0, 0}, new int[]{1, 0}, 
        // new int[]{2, 0}, new int[]{3, 0}, new int[]{0, 1}, new int[]{0, 2}, new int[]{0, 3});


        panelLeft.add(pitchPanel);
    }

    // // create components in the grid
	// // ------------------------------------------
    // public static void createComponent(JPanel panel,String colorName, int[]... cellCoordinates) {
	// 	Color color = getColorFromString(colorName);            // Convert color string to Color object
    //     if (color == null) {
    //         // Handle invalid color string
    //         System.out.println("Invalid color string: " + colorName);
    //         return;
    //     }
        
	//     for (int[] coordinates : cellCoordinates) {				// Iterate over each passed int array of cell coordinates
	//         int row = coordinates[0];							// Extract the row number from the cell coordinates
	//         int col = coordinates[1];							// Extract the column number from the cell coordinates
	//         Component[] components = panel.getComponents();							// Get all components in the passed JPanel
	//         int index = row * ((GridLayout) panel.getLayout()).getColumns() + col;	// Calculate the index of the component
	//         if (index >= 0 && index < components.length) {		// Check if the calculated index is within the valid range
	//             JPanel cell = (JPanel) components[index];		// Get the cell at the calculated index position from the panel's components
	//             cell.setBackground(color);						// Set the background color
	//         }
	//     }
    // }
    // // create a JAVA Color from a string
	// // ------------------------------------------
    // private static Color getColorFromString(String colorName) {
    //     try {
    //         return Color.decode(colorName);					// Try to create color from string
    //     } catch (NumberFormatException e) {
    //         try {											// If not a hex string, try to get color by name
    //             return (Color) Color.class.getField(colorName).get(null);
    //         } catch (Exception ex) {
    //             return null;								// Handle invalid color string
    //         }
    //     }
    // }
}
