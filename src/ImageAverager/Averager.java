package ImageAverager; /**
 * Created by victorchan on 5/15/17.
 * Returns an ImageAverager.Averager object that will take in images
 * and output an image of the same dimensions but with
 * a uniform color that is equal to the average color
 * in the image.
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.net.URL;

public class Averager {

    public static void displayAveragedLocalImage(String path) {
        BufferedImage input = getLocalImage(path);
        displayImage(input);
    }

    public static void displayAveragedURLImage(String path) {
        BufferedImage input = getURLImage(path);
        displayImage(input);
    }

    public static BufferedImage getAveragedLocalImage(String path) {
        return averageImage(getLocalImage(path));
    }

    public static BufferedImage getAveragedURLImage(String path) {
        return averageImage(getURLImage(path));
    }

    private static BufferedImage getLocalImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (java.io.IOException e) {
            System.out.println("Error: " + e);
        }
        throw new java.lang.RuntimeException("Error: Image not found");
    }

    private static BufferedImage getURLImage(String path) {
        try {
//            return convertToBufferedImage(Toolkit.getDefaultToolkit().createImage(new URL(path)));
            return ImageIO.read(new URL(path));
        } catch (java.io.IOException e) {
            System.out.println("Error: " + e);
        }
        throw new java.lang.RuntimeException("Error: Image not downloaded");
    }

    private static void displayImage(BufferedImage b) {
        BufferedImage original = resize(b);
        BufferedImage averaged = averageImage(original);
        displayFrame(original, averaged);
    }

    private static void displayFrame(BufferedImage a, BufferedImage b) {
        JFrame frame = new JFrame("Image Averager");
        frame.getContentPane().add(new TwoImageDisplay(a, b));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

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

    private static BufferedImage createUniform(int a, int w, int h) {
        BufferedImage b = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                b.setRGB(i, j, a);
            }
        }
        return b;
    }

    private static int averageGrid(int[][][] rgb) {
        int size = rgb[0][0].length;
        int[] average = new int[size];
        List<List<Integer>> lists = new ArrayList<List<Integer>>(size);
        for (int i = 0; i < size; i++) {
            lists.add(new ArrayList<Integer>());
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

    private static int averageList(List<Integer> l) {
        int total = 0;
        for (int i : l) {
            total += i;
        }
        return total / l.size();
    }

//    private static BufferedImage convertToBufferedImage(Image i) {
//        BufferedImage output = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_RGB);
//        Graphics2D o = output.createGraphics();
//        o.drawImage(i, 0, 0, null);
//        o.dispose();
//        return output;
//    }

    private static class TwoImageDisplay extends JPanel {

        BufferedImage a;
        BufferedImage b;

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