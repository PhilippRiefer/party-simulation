
/********************************************
 * Author: Ole
 * Version: v.1
 * Date:   20240423
 * ------------------------------------------
 * Description: The objects for the functionality 
 * of the frame are to be created in this class. 
 * Like sliders etc. 
 ********************************************/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class InputsPanel implements ChangeListener {

    private Room grid = new Room();

    // Sizes of the grid
    // --------------------------------------
    final int LINES_SMALL = 10;
    final int COLUMNS_SMALL = 20;
    final int LINES_MEDIUM = 20;
    final int COLUMNS_MEDIUM = 40;
    final int LINES_LARGE = 40;
    final int COLUMNS_LARGE = 80;

    // Labels for the Right-Panelside
    // --------------------------------------
    public void createLabels(JPanel panel) {
        JLabel labelHeadline = new JLabel("SIZE");
        labelHeadline.setHorizontalAlignment(SwingConstants.CENTER);
        labelHeadline.setVerticalAlignment(SwingConstants.CENTER);
        Font font = labelHeadline.getFont();
        labelHeadline.setFont(new Font(font.getName(), Font.BOLD, 16));
        panel.add(labelHeadline);
    }

    // Slider for the Right-Panelside &
    // calling the grid
    // --------------------------------------
    public void createSliderAndGrid(JPanel panel, JPanel environmentLeft) {
        panel.setLayout(new BorderLayout());
        JSlider slider = new JSlider(SwingConstants.VERTICAL, 1, 3, 1);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        slider.setMajorTickSpacing(1);
        // label for each tick mark
        slider.setPaintLabels(true);
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(1, new JLabel("ZoomIn"));
        labelTable.put(2, new JLabel("Original"));
        labelTable.put(3, new JLabel("ZoomOut"));
        slider.setLabelTable(labelTable);
        slider.setBackground(Color.WHITE);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = slider.getValue();
                switch (value) {
                    case 1: // Zoom in
                        environmentLeft.removeAll();
                        grid.createPitch(environmentLeft, LINES_SMALL, COLUMNS_SMALL);
                        environmentLeft.revalidate();
                        environmentLeft.repaint();
                        break;
                    case 2: // original
                        environmentLeft.removeAll();
                        grid.createPitch(environmentLeft, LINES_MEDIUM, COLUMNS_MEDIUM);
                        environmentLeft.revalidate();
                        environmentLeft.repaint();
                        break;
                    case 3: // Zoom out
                        environmentLeft.removeAll();
                        grid.createPitch(environmentLeft, LINES_LARGE, COLUMNS_LARGE);
                        environmentLeft.revalidate();
                        environmentLeft.repaint();
                        break;
                    default:
                        break;
                }
            }
        });
        panel.add(slider, BorderLayout.CENTER);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stateChanged'");
    }
}
