/**
 * Created by victorchan on 5/16/17.
 */

import ImageAverager.Averager;

public class ImageAverager {

    public static void main(String[] args) {
        if (args.length == 1) {
            Averager.displayAveragedLocalImage(args[0]);
        } else if (args.length == 2 && args[0].equals("-u")) {
            Averager.displayAveragedURLImage(args[1]);
        } else {
            throw new java.lang.RuntimeException("Error: Illegal command format");
        }
    }

}
