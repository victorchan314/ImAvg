package ImAvg;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.net.URL;

/**
 * Created by victorchan on 5/15/17.
 * Takes in local or remote images and creates a uniform image with the average color of the original image.
 * Returns an ImageAverager.Averager object that will take in images
 * and output an image of the same dimensions but with
 * a uniform color that is equal to the average color
 * in the image.
 * @author Victor Chan
 * @version 1.00, 05/16/17
 */
public class Averager {

    public static void displayAveragedLocalImage(String path) throws java.io.IOException {
        BufferedImage input = getLocalImage(path);
        displayImage(input);
    }

    public static void displayAveragedURLImage(String path) throws java.io.IOException {
        BufferedImage input = getURLImage(path);
        displayImage(input);
    }

    public static BufferedImage getAveragedLocalImage(String path) throws java.io.IOException {
        return averageImage(getLocalImage(path));
    }

    public static BufferedImage getAveragedURLImage(String path) throws java.io.IOException {
        return averageImage(getURLImage(path));
    }

    /**
     * Helps retrieve a local image.
     *
     * @param path The path to the file. Can be relative or absolute
     * @return The image at the path, as a BufferedImage
     * @throws java.io.IOException if File cannot be created or image cannot be read
     */
    private static BufferedImage getLocalImage(String path) throws java.io.IOException {
        return ImageIO.read(new File(path));
    }

    /**
     * Helps retrieve a remote image at the url.
     *
     * @param path The url of the file
     * @return The image at the url, as a BufferedImage
     * @throws java.io.IOException if image cannot be read
     */
    private static BufferedImage getURLImage(String path) throws java.io.IOException {
//        return convertToBufferedImage(Toolkit.getDefaultToolkit().createImage(new URL(path)));
        return ImageIO.read(new URL(path));
    }

    /**
     * Resizes, then displays image to be averaged alongside averaged image.
     *
     * @param b Image to be resized, averaged, and displayed
     */
    private static void displayImage(BufferedImage b) {
        BufferedImage original = resize(b);
        BufferedImage averaged = averageImage(original);
        displayFrame(original, averaged);
    }

    /**
     * Creates JFrame displays parameter images next to each other.
     *
     * @param a Image displayed on the left
     * @param b Image displayed on the right
     */
    private static void displayFrame(BufferedImage a, BufferedImage b) {
        JFrame frame = new JFrame("Image Averager");
        frame.getContentPane().add(new TwoImageDisplay(a, b));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Resizes an image if its dimensions are over 500px.
     * Checks to see the aspect ratio and changes larger
     * dimension to 500px if it is greater than 500px.
     *
     * @param b Image to be resized
     * @return Original image if both dimensions are smaller than 500px
     * Otherwise, image with larger dimension shrunk to 500px with same aspect ratio
     */
    private static BufferedImage resize(BufferedImage b) {
        int w = b.getWidth();
        int h = b.getHeight();
        if (w / h >= 1) {
            if (w > 500) {
                double ratio = 500.0 / w;
                int width = (int) Math.round(w * ratio);
                int height = (int) Math.round(h * ratio);
                BufferedImage output = new BufferedImage(width, height, b.getType());
                Graphics2D g2d = output.createGraphics();
                g2d.drawImage(b, 0, 0, width, height, null);
                g2d.dispose();
                return output;
            }
            return b;
        }
        return transpose(resize(transpose(b)));
    }

    /**
     * Transposes a BufferedImage b.
     * Used only for resize method to avoid having to copy code twice.
     *
     * @param b Image to be transposed
     * @return Transposed image
     */
    private static BufferedImage transpose(BufferedImage b) {
        int width = b.getHeight();
        int height = b.getWidth();
        BufferedImage output = new BufferedImage(width, height, b.getType());
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                output.setRGB(i, j, b.getRGB(j, i));
            }
        }
        return output;
    }

    /**
     * Averages image to find average color.
     * Iterates through each pixel and finds the average Red, Green, and Blue
     * color RGB values. This is used as the average RGB color and returned.
     *
     * @param b BufferedImage to find th average color of
     * @return BufferedImage of same dimensions as input and uniform color equal to the average color
     */
    public static BufferedImage averageImage(BufferedImage b) {
        int width = b.getWidth();
        int height = b.getHeight();
        int[][][] rgb = new int[width][height][3];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color c = new Color(b.getRGB(i, j));
                rgb[i][j][0] = c.getRed();
                rgb[i][j][1] = c.getGreen();
                rgb[i][j][2] = c.getBlue();
            }
        }
        int averaged = averageGrid(rgb);
        return createUniform(averaged, width, height);
    }

    /**
     * Creates a BufferedImage with width w, height h, and color a.
     *
     * @param a RGB int value of color for the BufferedImage to take on
     * @param w Width of the BufferedImage
     * @param h Height of the BufferedImage
     * @return BufferedImage with width w, height h, and color a
     */
    private static BufferedImage createUniform(int a, int w, int h) {
        BufferedImage b = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                b.setRGB(i, j, a);
            }
        }
        return b;
    }

    /**
     * Helper function to find the average values of a grid
     * Only works on 3-dimensional arrays with values at most 8 bits. The
     * function acts on the 2-D array of arrays, and finds the average value
     * across each index of the arrays in the grid. It then converts these
     * values into the single RGB value of the color.
     *
     * @param rgb 3-dimensional array to find average values of
     * @return Integer value of average RGB color
     */
    private static int averageGrid(int[][][] rgb) {
        int size = rgb[0][0].length;
        int[] average = new int[size];
        List<List<Integer>> lists = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            lists.add(new ArrayList<>());
        }
        for (int i = 0; i < rgb.length; i++) {
            for (int j = 0; j < rgb[0].length; j++) {
                for (int k = 0; k < size; k++) {
                    lists.get(k).add(rgb[i][j][k]);
                }
            }
        }
        for (int i = 0; i < size; i++) {
            average[i] = averageList(lists.get(i));
        }
        int a = average[0];
        for (int i = 1; i < size; i++) {
            a = (a << 8) + average[i];
        }
        return a;
    }

    /**
     * Simple helper function to find the average value in a list of ints.
     *
     * @param l List to find the average value of
     * @return Integer value of average value of a list
     */
    private static int averageList(List<Integer> l) {
        int total = 0;
        for (int i : l) {
            total += i;
        }
        return total / l.size();
    }

//    /**
//     * Unnecessary helper function to convert an Image to a BufferedImage
//     * @param i     Image to convert to BufferedImage
//     * @return      BufferedImage equivalent of Image
//     */
//    private static BufferedImage convertToBufferedImage(Image i) {
//        BufferedImage output = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_RGB);
//        Graphics2D o = output.createGraphics();
//        o.drawImage(i, 0, 0, null);
//        o.dispose();
//        return output;
//    }

    /**
     * Private helper class to display two images.
     * Subclass of JPanel that can be created to display two images with
     * a 15px boundary on all sides, as well as between the two images.
     */
    private static class TwoImageDisplay extends JPanel {

        BufferedImage a;
        BufferedImage b;

        /**
         * Constructor to create a TwoImageDisplay.
         *
         * @param a Image to be displayed on the left
         * @param b Image to be displayed on the right
         */
        private TwoImageDisplay(BufferedImage a, BufferedImage b) {
            this.a = a;
            this.b = b;
            this.setPreferredSize(new Dimension(45 + a.getWidth() + b.getWidth(),
                    30 + Math.max(a.getHeight(), b.getHeight())));
        }

        public void paint(Graphics g) {
            g.drawImage(a, 15, 15, this);
            g.drawImage(b, 15 + a.getWidth() + 15, 15, this);
        }

    }

}