import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: oliverbrupbacher
 * Date: Dec 2, 2012
 * Time: 7:26:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class Reconstruct implements Runnable {

    private MiicraftModel miicraftmodel;

    private ArrayList<Voxel> voxels = new ArrayList<Voxel>();

    int white = Color.white.getRGB();
    int black = Color.black.getRGB();

    public static int center_x = 768 / 2;
    public static int center_y = 480 / 2;

    public Reconstruct(MiicraftModel miicraftmodel) {

        this.miicraftmodel = miicraftmodel;

    }

    public ArrayList<Voxel> getVoxels() {
        return voxels;
    }

    public void run() {

        BufferedImage b = miicraftmodel.getCurrentImage();

        int max_x = b.getWidth();
        int max_y = b.getHeight();
        int max_z = miicraftmodel.getFiles().size();

        for (int z = 0; z < max_z; z++) {

            BufferedImage layer = miicraftmodel.getLayer(z);
            miicraftmodel.insertMessage("recon layer: " + z);

            for (int y = 1; y < max_y - 1; y++) {

                for (int x = 1; x < max_x - 1; x++) {


                    int c = layer.getRGB(x, y);
                    if (c != white) {
                        continue;
                    }

                    if (c != layer.getRGB(x - 1, y)) {
                        voxels.add(new Voxel(x, y, z));
                        continue;
                    }

                    if (c != layer.getRGB(x + 1, y)) {
                        voxels.add(new Voxel(x, y, z));
                        continue;
                    }

                    if (c != layer.getRGB(x, y - 1)) {
                        voxels.add(new Voxel(x, y, z));
                        continue;
                    }

                    if (c != layer.getRGB(x, y + 1)) {
                        voxels.add(new Voxel(x, y, z));
                        continue;
                    }
                }
            }

            try {
                Thread.sleep(10l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        // reduce
        miicraftmodel.insertMessage("voxels:" + voxels.size());

        for (int t = 0; t < 6; t++) {
            for (int i = 0; i < voxels.size(); i = i + 2) {
                voxels.remove(i);
            }
            miicraftmodel.insertMessage("voxel reduced " + t + " result:" + voxels.size());
        }

        miicraftmodel.setVoxels(voxels);
    }


    public static void project(BufferedImage img, ArrayList<Voxel> voxels) {

        int w = img.getWidth() - 1;
        int h = img.getHeight() - 1;

        int white = Color.white.getRGB();

        //Voxel eye = new Voxel(320, 240, 800);
        Voxel eye = new Voxel(center_x, center_y, 2000);

        for (int i = 0; i < voxels.size(); i++) {
            Voxel voxel = voxels.get(i);

            try {

                float x = eye.z * (voxel.x - eye.x + 200) / (eye.z + (voxel.z + 400)) + eye.x;
                float y = eye.z * (voxel.y - eye.y + 200) / (eye.z + (voxel.z + 400)) + eye.y;
                if (x > 0 && x < w && y > 0 && y < h) img.setRGB((int) x, (int) y, white);

            } catch (Exception e) {
                ;
            }
        }
    }

    public static void orbitX(ArrayList<Voxel> voxels) {

        double co = Math.cos(rad(5)); //takes radiant not degrees
        double si = Math.sin(rad(5));

        for (int i = 0; i < voxels.size(); i++) {

            Voxel voxel = voxels.get(i);

            float rx = (float) ((voxel.x * co) + (voxel.z * si));
            float rz = (float) ((voxel.x * -si) + (voxel.z * co));

            voxel.x = rx;
            voxel.z = rz;
        }
    }

    public static void orbitY(ArrayList<Voxel> voxels) {

        double co = Math.cos(rad(5)); //takes radiant not degrees
        double si = Math.sin(rad(5));

        for (int i = 0; i < voxels.size(); i++) {

            Voxel voxel = voxels.get(i);

            float ry = (float) ((voxel.y * co) - (voxel.z * si));
            float rz = (float) ((voxel.y * si) + (voxel.z * co));

            voxel.y = ry;
            voxel.z = rz;
        }
    }

    public static double rad(double angle) {
        return (angle * Math.PI / 180d);
    }
}
