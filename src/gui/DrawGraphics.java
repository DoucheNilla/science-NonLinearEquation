package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class DrawGraphics extends JPanel {

    private final int PLOT_WIDTH;
    private final int PLOT_HEIGHT;

    private int xShift;
    private int yShift;
    //private final int Y_AXIS;

    private int[] pixelsArrayX;
    private int[] pixelsArrayY;

    public DrawGraphics(int width, int height) {
        PLOT_WIDTH = width;
        PLOT_HEIGHT = height;
    }

    private void convertToPixels(double[][] points) {
        pixelsArrayX = new int[points[0].length];
        pixelsArrayY = new int[points[1].length];

        double maxXValue = Arrays.stream(points[0]).max().isPresent() ? Arrays.stream(points[0]).max().getAsDouble() : 0;
        double minXValue = Arrays.stream(points[0]).min().isPresent() ? Arrays.stream(points[0]).min().getAsDouble() : 0;

        double maxYValue = Arrays.stream(points[1]).max().isPresent() ? Arrays.stream(points[1]).max().getAsDouble() : 0;
        double minYValue = Arrays.stream(points[1]).min().isPresent() ? Arrays.stream(points[1]).min().getAsDouble() : 0;

        double weightsX = Math.abs(maxXValue - minXValue) / PLOT_WIDTH;
        double weightsY = Math.abs(maxYValue - minYValue) / PLOT_HEIGHT;

        xShift = (int) Math.round(minXValue / weightsX);
        yShift = (int) Math.round(minYValue / weightsY) + PLOT_HEIGHT;

        for (int i = 0; i < points[0].length; i++) {
            pixelsArrayX[i] = (int) Math.round(points[0][i] / weightsX) - xShift;
            pixelsArrayY[i] = (int) - Math.round(points[1][i] / weightsY) + yShift;
        }
    }

    public void drawing(double[][] points) {
        convertToPixels(points);
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int minX = Arrays.stream(pixelsArrayX).min().isPresent() ? Arrays.stream(pixelsArrayX).min().getAsInt() : 0;
        int maxX = Arrays.stream(pixelsArrayX).max().isPresent() ? Arrays.stream(pixelsArrayX).max().getAsInt() : 0;
        int minY = Arrays.stream(pixelsArrayY).min().isPresent() ? Arrays.stream(pixelsArrayY).min().getAsInt() : 0;
        int maxY = Arrays.stream(pixelsArrayY).max().isPresent() ? Arrays.stream(pixelsArrayY).max().getAsInt() : 0;

        g.setColor(Color.BLACK);
        g.drawLine(minX, yShift, maxX, yShift);
        g.drawLine(- xShift, minY, - xShift, maxY);
        for (int i = 1; i < pixelsArrayX.length; i++) {
            g.drawLine(pixelsArrayX[i - 1], pixelsArrayY[i - 1], pixelsArrayX[i], pixelsArrayY[i]);
        }
    }
}
