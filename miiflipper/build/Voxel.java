/**
 * Created by IntelliJ IDEA.
 * User: oliverbrupbacher
 * Date: Dec 2, 2012
 * Time: 7:47:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class Voxel {

    public float x;
    public float y;
    public float z;

    public Voxel(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
