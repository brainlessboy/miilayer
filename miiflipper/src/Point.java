import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: oliverbrupbacher
 * Date: Nov 28, 2012
 * Time: 11:59:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class Point {

    private int x;
    private int y;

    private int layer;  // starts at layer index to bottom layer

    public Point(int x, int y, int layer) {
        this.x = x;
        this.y = y;
        this.layer = layer;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public boolean inside(int x, int y, int width) {

        Rectangle r = new Rectangle(this.x - (width / 2), this.y - (width / 2), width, width);
        return r.inside(x, y);

    }
}
