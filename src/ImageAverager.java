import ImageAverager.Averager;

/**
 * Created by victorchan on 5/16/17.
 * Demo of how to display images using the ImageAverager package.
 * @author Victor Chan
 * @version 1.00, 05/16/17
 */
public class ImageAverager {
    
    /**
     * Displays images with their averaged version with the ImageAverager package.
     * @param args If 1 argument: calls displayImage with the path specified at args[0]
     *             If 2 arguments: flag must be -u, calls displayImage with the path specified at args[1]
     */
    public static void main(String[] args) {
        try {
            if (args.length == 1) {
                Averager.displayAveragedLocalImage(args[0]);
            } else if (args.length == 2 && args[0].equals("-u")) {
                Averager.displayAveragedURLImage(args[1]);
            } else {
                throw new java.lang.RuntimeException("Error: Illegal command format");
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

}
