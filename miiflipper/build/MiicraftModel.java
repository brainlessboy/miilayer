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

public class MiicraftModel extends JPanel {

    private ArrayList<File> files = new ArrayList<File>();
    private ArrayList<Point> points = new ArrayList<Point>();

    private int index = 0;

    private String directory;

    private int lastx, lasty;

    private BufferedImage currentImage;

    private JTextPane textPane;

    private ArrayList<Voxel> voxels = new ArrayList<Voxel>();

    public MiicraftModel() {

        currentImage = new BufferedImage(768, 480, BufferedImage.TYPE_INT_RGB);

        final Graphics2D g2 = (Graphics2D) this.getGraphics();

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                lastx = e.getX();
                lasty = e.getY();

                repaint();
            }
        });


        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {

                lastx = e.getX();
                lasty = e.getY();

                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {

                lastx = e.getX();
                lasty = e.getY();
            }
        });
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

    public void loadImage(File imageFile) {

        try {
            currentImage = ImageIO.read(imageFile);
        } catch (IOException e) {
            insertMessage("error loading image " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void paint(Graphics graphics) {

        Graphics2D g2 = (Graphics2D) this.getGraphics();
        if (currentImage != null) {
            g2.drawImage(currentImage, null, 0, 0);
        }
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
}