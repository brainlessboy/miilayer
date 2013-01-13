import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;

/**
 * Created by IntelliJ IDEA.
 * User: oliverbrupbacher
 * Date: Jan 13, 2013
 * Time: 1:07:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class FloodFill {

    private int x;
    private int y;

    private Color replacementColor = Color.black;
    private Color targetColor = Color.white;

    private BufferedImage image;
    private Stack stack;

    public FloodFill(BufferedImage image, Stack stack, int x, int y, Color targetColor,Color replacementColor) {

        this.x = x;
        this.y = y;
        this.targetColor = targetColor;
        this.replacementColor = replacementColor;
        this.image = image;
        this.stack = stack;
    }

    public void fill() {


        if (x < 0 || x > image.getWidth() - 1 || y < 0 || y > image.getHeight() - 1) {
            return;
        }

        if (image.getRGB(x, y) != targetColor.getRGB()) return;

        image.setRGB(x, y, replacementColor.getRGB());

        stack.push(new FloodFill(image,stack,x - 1, y,targetColor,replacementColor));
        stack.push(new FloodFill(image,stack,x + 1, y,targetColor,replacementColor));
        stack.push(new FloodFill(image,stack,x, y-1,targetColor,replacementColor));
        stack.push(new FloodFill(image,stack,x, y+1,targetColor,replacementColor));

        return;

    }
}
