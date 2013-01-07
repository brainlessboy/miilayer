import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: oliverbrupbacher
 * Date: Dec 2, 2012
 * Time: 7:26:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class GenerateStructure implements Runnable {

    private MiicraftImager miicraftImager;

    private boolean[][][] voxels;

    int white = Color.white.getRGB();
    int black = Color.black.getRGB();

    private static int res_x = 768;
    private static int res_y = 480;

    private static int cubeSize = 15;

    public static int center_x = res_x / 2;
    public static int center_y = res_y / 2;

    public GenerateStructure(MiicraftImager miicraftImager) {

        this.miicraftImager = miicraftImager;
    }

    public boolean[][][] getVoxels() {
        return voxels;
    }

    public void run() {

        BufferedImage b = this.miicraftImager.getCurrentImage();

        int max_x = b.getWidth();
        int max_y = b.getHeight();
        int max_z = this.miicraftImager.getFiles().size();

        // generate voxel space


        for (int z = 1; z < this.miicraftImager.getFiles().size(); z++) {

            // grab images and generate voxel spaces

            voxels = new boolean[res_x][res_y][2];

            BufferedImage layer = this.miicraftImager.getLayer(z);
            for (int y = 1; y < max_y - 1; y++) {

                for (int x = 1; x < max_x - 1; x++) {

                    int c = layer.getRGB(x, y);

                    if (c == white) {
                        voxels[x][y][1] = true;
                    } else {
                        voxels[x][y][1] = false;
                    }
                }
            }

            BufferedImage layerMinus = this.miicraftImager.getLayer(z - 1);
            for (int y = 1; y < max_y - 1; y++) {

                for (int x = 1; x < max_x - 1; x++) {

                    int c = layerMinus.getRGB(x, y);

                    if (c == white) {
                        voxels[x][y][0] = true;
                    } else {
                        voxels[x][y][0] = false;
                    }
                }
            }


            for (int x = 0; x < res_x; x = x + cubeSize) {

                for (int y = 0; y < res_y; y = y + cubeSize) {

                    boolean isIn = true;
                    boolean isInLower = true;

                    if (x + cubeSize > res_x || y + cubeSize > res_y) {
                        continue;
                    }

                    // cube analyzing if spot is in surface
                    for (int c = 0; c < cubeSize; c++) {
                        for (int d = 0; d < cubeSize; d++) {

                            if (!voxels[x + c][y + d][1]) { // if one pixel not white break go next
                                isIn = false;
                                break;
                            }

                        }
                    }
                    //

                    // if in layer and lower layer none
                    if (isIn) {

                        // analyzing if lower layer has no support
                        for (int c = 0; c < cubeSize; c++) {
                            for (int d = 0; d < cubeSize; d++) {

                                if (voxels[x + c][y + d][0]) { // if one pixel white,  break go next
                                    isInLower = false;
                                    break;
                                }
                            }
                        }
                        //
                    }

                    if (isIn && isInLower) {

                        Point p = new Point(x + (cubeSize / 2), y + (cubeSize / 2), z);
                        this.miicraftImager.addPoint(p);
                    }
                }
            }

            this.miicraftImager.insertMessageNOBR(z + " ");
            this.miicraftImager.setLayer(z);

            this.miicraftImager.repaint();

            try {
                Thread.sleep(100l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}