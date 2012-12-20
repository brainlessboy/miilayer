import java.io.File;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: oliverbrupbacher
 * Date: Dec 1, 2012
 * Time: 12:05:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class TravelDown implements Runnable {

    MiicraftImager miicraft;

    public TravelDown(MiicraftImager miicraft) {

        this.miicraft = miicraft;
    }

    public void run() {

        ArrayList<File> images = miicraft.getFiles();

        for (int i = 0; i < images.size(); i++) {

            miicraft.down();
            miicraft.repaint();

            try {
                Thread.sleep(100l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}