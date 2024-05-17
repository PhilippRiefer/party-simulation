package Environment;

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
import java.util.EventListener;
import java.util.Hashtable;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderPanel implements ChangeListener {

    JSlider slider;

    // private Room grid = new Room(20, 20);

    public SliderPanel(JPanel panel) {
        panel.setLayout(new BorderLayout());
        JSlider slider = new JSlider(SwingConstants.VERTICAL, 1, 3, 2);
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

        // slider.addChangeListener(new ChangeListener() {
        // @Override
        // public void stateChanged(ChangeEvent e) {
        // int value = slider.getValue();
        // switch (value) {
        // case 1: // Zoom in
        // System.out.println("Zoom in");
        // break;
        // case 2: // original
        // System.out.println("Original");
        // break;
        // case 3: // Zoom out
        // System.out.println("Zoom out");
        // break;
        // default:
        // break;
        // }
        // }
        // });
        panel.add(slider, BorderLayout.CENTER);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stateChanged'");
    }

    public void addChangeListener(EventListener listenForSlider) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addChangeListener'");
    }

    public int getValue() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getValue'");
    }

    // // Slider for the Right-Panelside &
    // // calling the grid
    // // --------------------------------------
    // public void createSliderAndGrid(JPanel panel, JPanel environmentLeft) {
    // panel.setLayout(new BorderLayout());
    // JSlider slider = new JSlider(SwingConstants.VERTICAL, 1, 3, 1);
    // this.slider = slider;
    // slider.setPaintTicks(true);
    // slider.setSnapToTicks(true);
    // slider.setMajorTickSpacing(1);
    // // label for each tick mark
    // slider.setPaintLabels(true);
    // Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
    // labelTable.put(1, new JLabel("ZoomIn"));
    // labelTable.put(2, new JLabel("Original"));
    // labelTable.put(3, new JLabel("ZoomOut"));
    // slider.setLabelTable(labelTable);
    // slider.setBackground(Color.WHITE);
    // // slider.addChangeListener(new ChangeListener() {
    // // @Override
    // // public void stateChanged(ChangeEvent e) {
    // // int value = slider.getValue();
    // // switch (value) {
    // // case 1: // Zoom in
    // // environmentLeft.removeAll();
    // // grid.createPitch(environmentLeft, LINES_SMALL, COLUMNS_SMALL);
    // // environmentLeft.revalidate();
    // // environmentLeft.repaint();
    // // break;
    // // case 2: // original
    // // environmentLeft.removeAll();
    // // grid.createPitch(environmentLeft, LINES_MEDIUM, COLUMNS_MEDIUM);
    // // environmentLeft.revalidate();
    // // environmentLeft.repaint();
    // // break;
    // // case 3: // Zoom out
    // // environmentLeft.removeAll();
    // // grid.createPitch(environmentLeft, LINES_LARGE, COLUMNS_LARGE);
    // // environmentLeft.revalidate();
    // // environmentLeft.repaint();
    // // break;
    // // default:
    // // break;
    // // }
    // // }
    // // });
    // panel.add(slider, BorderLayout.CENTER);
    // }

    // @Override
    // public void stateChanged(ChangeEvent e) {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method
    // 'stateChanged'");
    // }

    // public SpaceType getSpace(int x, int y) {
    // SpaceType space = grid.getSpace(x, y);
    // return space;
    // }

    // public void addChangeListener(EventListener listenForSlider) {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method
    // 'addChangeListener'");
    // }

    // public int getValue() {
    // return slider.getValue();
    // }
}