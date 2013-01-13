import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class MiicraftImager extends JPanel {

    private ArrayList<File> files = new ArrayList<File>();
    private ArrayList<Point> points = new ArrayList<Point>();
    private ArrayList<Stroke> strokes = new ArrayList<Stroke>();

    private int index = 0;

    private String directory;

    private Color currentColor = Color.white;

    private int lastx, lasty;

    private int structureWidth = 9;

    private BufferedImage currentImage;

    private JTextPane textPane;

    private ArrayList<Voxel> voxels = new ArrayList<Voxel>();

    private Stroke currentStroke;

    private boolean isFloodFill = false;
    private Stack fillStack = new Stack();

    public MiicraftImager(String directory) {

        if (directory != null && directory != "") {

            final File folder = new File(directory);
            listFilesForFolder(folder);

            loadImage(files.get(0));

        } else {

            currentImage = new BufferedImage(768, 480, BufferedImage.TYPE_INT_RGB);

        }

        final Graphics2D g2 = (Graphics2D) this.getGraphics();

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                if (isFloodFill) {

                    floodFill(e.getX(), e.getY(), Color.BLACK, Color.WHITE);

                } else {

                    if (points.size() > 0) {
                        for (int i = 0; i < points.size(); i++) {
                            Point point = points.get(i);

                            if (point.inside(e.getX(), e.getY(), structureWidth)) {
                                points.remove(i);
                                break;
                            } else {
                                points.add(new Point(e.getX(), e.getY(), index));
                                break;
                            }
                        }
                    } else {
                        points.add(new Point(e.getX(), e.getY(), index));
                    }

                }

                repaint();
            }
        });


        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Graphics2D gg = (Graphics2D) currentImage.getGraphics();
                gg.setColor(currentColor);

                drawLine(gg, lastx, lasty, e.getX(), e.getY(), 5);

                lastx = e.getX();
                lasty = e.getY();

                if (currentStroke != null) currentStroke.add(lastx, lasty, index, currentColor);

                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                lastx = e.getX();
                lasty = e.getY();

                currentStroke = new Stroke();
                currentStroke.add(lastx, lasty, index, currentColor);
                strokes.add(currentStroke);
            }
        });
    }

    public void addPoint(Point point) {

        if (point == null) {
            return;
        }

        points.add(point);

    }

    public void initiateImage() {


        File file = new File(directory);
        listFilesForFolder(file);
        currentImage = getLayer(0);
        repaint();
    }

    public void listFilesForFolder(File folder) {
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                // listFilesForFolder(fileEntry); // recursive call
            } else {

                if (fileEntry.getName().endsWith("png")) {
                    files.add(fileEntry);
                }
            }
        }
    }

    public BufferedImage getLayer(int index) {

        try {
            return ImageIO.read(files.get(index));
        } catch (IOException e) {
            insertMessage("error loading layer " + index + " " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public void setLayer(int index) {
        currentImage = getLayer(index);
    }

    public void loadImage(File imageFile) {

        try {
            currentImage = ImageIO.read(imageFile);
        } catch (IOException e) {
            insertMessage("error loading image " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void save() {
        saveImage(files.get(index));
    }

    public void save(int index) {
        saveImage(files.get(index));
    }

    public void saveImage(File imageFile) {
        try {
            ImageIO.write(currentImage, "png", imageFile);
            insertMessage("saved - " + imageFile.getName());
        } catch (IOException e) {
            insertMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    public void paint(Graphics graphics) {

        Graphics2D g2 = (Graphics2D) this.getGraphics();
        if (currentImage != null) {
            g2.drawImage(currentImage, null, 0, 0);
        }

        //make all strokes visible
        for (int i = 0; i < strokes.size(); i++) {
            Stroke stroke = strokes.get(i);

            if (index == stroke.getLayer())
                g2.setColor(Color.red);
            else
                g2.setColor(Color.gray);

            stroke.draw(g2);
        }

        //make all points visible
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);


            if (index == point.getLayer())
                g2.setColor(Color.red);
            else
                g2.setColor(Color.gray);

            g2.drawOval(point.getX() - (structureWidth / 2), point.getY() - (structureWidth / 2), structureWidth, structureWidth);
        }

        // draw some info
        if (isFloodFill()) {
            g2.setColor(Color.red);
            g2.drawString("layer: " + index + " flood fill on", 20, 20);
        } else {
            g2.setColor(Color.red);
            g2.drawString("layer: " + index, 20, 20);
        }
    }

    /**
     * draws the supporting dots and strokes into all images (CANNOT BE UNDONE! FINAL!)
     */
    public void generate(int i) {

        File file = files.get(i);
        loadImage(file);

        Graphics2D g2 = (Graphics2D) currentImage.getGraphics();

        boolean somethingHasChanged = false;

        for (int j = 0; j < points.size(); j++) {

            Point point = points.get(j);
            if (i <= point.getLayer()) {
                g2.setColor(Color.white);
                g2.fillOval(point.getX() - (structureWidth / 2), point.getY() - (structureWidth / 2), structureWidth, structureWidth);
                somethingHasChanged = true;
            }
        }

        for (int j = 0; j < strokes.size(); j++) {

            Stroke stroke = strokes.get(j);
            if (i <= stroke.getLayer()) {
                g2.setColor(stroke.getColor());
                stroke.draw(g2);
                somethingHasChanged = true;
            }
        }

        insertMessage("layer - " + i);

        if (somethingHasChanged) save(i);

        repaint();

    }


    public void setWhite() {
        this.currentColor = Color.white;
    }

    public void setBlack() {
        this.currentColor = Color.black;
    }

    public void up() {
        index++;
        if (index > files.size()) {
            index = files.size();
        }
        loadImage(files.get(index));

    }

    public void down() {
        index--;
        if (index < 0) {
            index = 0;
        }
        loadImage(files.get(index));
    }

    /**
     * Draw each line to the buffer in order to make selectalbe on pixel
     */
    public void drawLine(Graphics2D g2, int ax, int ay, int bx, int by, int thickness) {

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

    public void floodFill(int x, int y, Color targetColor, Color replacementColor) {

        Stack<FloodFill> stack = new Stack();

        FloodFill fill = new FloodFill(currentImage,stack,x,y,targetColor,replacementColor);
        stack.push(fill);

        while(!stack.isEmpty()){
            FloodFill f = stack.pop();
            f.fill();
        }

        return;
    }


    public JTextPane getMessages() {
        return textPane;
    }

    public void setMessages(JTextPane messages) {
        this.textPane = messages;
    }

    public void insertMessage(String message) {

        StyledDocument doc = (StyledDocument) textPane.getDocument();
        try {
            doc.insertString(doc.getLength(), message, null);
            doc.insertString(doc.getLength(), "\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void insertMessageNOBR(String message) {

        StyledDocument doc = (StyledDocument) textPane.getDocument();
        try {
            doc.insertString(doc.getLength(), message, null);
            doc.insertString(doc.getLength(), " ", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public BufferedImage getCurrentImage() {
        return currentImage;
    }

    public ArrayList<Voxel> getVoxels() {
        return voxels;
    }

    public void setVoxels(ArrayList<Voxel> voxels) {
        this.voxels = voxels;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public void clearPoints() {
        points.clear();
    }

    public void clearStrokes() {
        strokes.clear();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isFloodFill() {
        return isFloodFill;
    }

    public void setFloodFill(boolean floodFill) {
        isFloodFill = floodFill;
    }
}
