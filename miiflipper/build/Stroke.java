import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: oliverbrupbacher
 * Date: Dec 30, 2012
 * Time: 8:55:15 PM
 * <p/>
 * Singl paint stroke that can be projected downards onto all lower images
 */
public class Stroke {

    private ArrayList<Point> points = new ArrayList<Point>();
    private Color color = Color.white;


    private int layer;  // layer index where the stroke was set

    private int lineThickness = 5;

    public void add(int x, int y, int layer, Color color) {

        points.add(new Point(x, y, layer));
        this.layer = layer;
        this.color = color;
    }

    public void draw(Graphics2D g2) {

        for (int i = 1; i < points.size(); i++) {
            Point pointA = points.get(i - 1);
            Point pointB = points.get(i);

            drawLine(g2, pointA.getX(), pointA.getY(), pointB.getX(), pointB.getY(), lineThickness);
        }
    }

    private void drawLine(Graphics2D g2, int ax, int ay, int bx, int by, int thickness) {

        int x = ax;
        int y = ay;

        int D = 0;
        int HX = bx - ax;
        int HY = by - ay;

        int c;
        int M;
        int xInc = 1;
        int yInc = 1;

        if (HX < 0) {
            xInc = -1;
            HX = -HX;
        }

        if (HY < 0) {
            yInc = -1;
            HY = -HY;
        }

        if (HY <= HX) {
            c = 2 * HX;
            M = 2 * HY;

            for (; ;) {

                g2.fillOval(x - (thickness / 2), y - (thickness / 2), thickness, thickness);

                if (x == bx) {
                    break;
                }

                x += xInc;
                D += M;

                if (D > HX) {
                    y += yInc;
                    D -= c;
                }
            }
        } else {
            c = 2 * HY;
            M = 2 * HX;

            for (; ;) {

                g2.fillOval(x - (thickness / 2), y - (thickness / 2), thickness, thickness);

                if (y == by) {
                    break;
                }

                y += yInc;
                D += M;

                if (D > HY) {
                    x += xInc;
                    D -= c;
                }
            }
        }
    }

    public Color getColor() {
        return color;

    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getLineThickness() {
        return lineThickness;
    }

    public void setLineThickness(int lineThickness) {
        this.lineThickness = lineThickness;
    }
}
