import com.jtattoo.plaf.noire.NoireLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

public class mii extends JFrame implements ActionListener {

    private MiicraftImager miicraft = new MiicraftImager(null);
    private MiicraftModel miimodel = new MiicraftModel();

    private String path;


    public mii() {

        try {

            Properties props = new Properties();
            props.put("logoString", "miiImager");
            props.put("licenseKey", "39q5-dpgu-fat5-xz81");
            NoireLookAndFeel.setCurrentTheme(props);
            com.jtattoo.plaf.noire.NoireLookAndFeel.setTheme("Noire");
            UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });


        BorderLayout bl = new BorderLayout();

        setName("miilayer");
        setLayout(bl);
        setBounds(0, 0, 1000, 800);
        setVisible(true);

        // text output
        JTextPane messages = new JTextPane();
        messages.setBackground(Color.DARK_GRAY);
        messages.setForeground(Color.white);
        messages.setPreferredSize(new Dimension(350, 480));
        messages.setEditable(false);

        JScrollPane messageScroller = new JScrollPane(messages);
        messageScroller.setAutoscrolls(true);
        messageScroller.setWheelScrollingEnabled(true);
        ///

        //text pan
        JTextPane messagesx = new JTextPane();
        messagesx.setBackground(Color.BLUE);
        messagesx.setForeground(Color.yellow);
        messagesx.setPreferredSize(new Dimension(350, 480));
        messagesx.setEditable(false);

        JScrollPane messageScrollerx = new JScrollPane(messagesx);
        messageScrollerx.setAutoscrolls(true);
        messageScrollerx.setWheelScrollingEnabled(true);
        ///


        miicraft.setMessages(messages);
        miicraft.setVisible(true);
        miicraft.setSize(768, 480);

        miimodel.setMessages(messagesx);
        miimodel.setVisible(true);
        miimodel.setSize(768, 480);

        //menue

        JMenuBar menubar = new JMenuBar();
        JMenu a = new JMenu("Files");
        a.add(menuitem("Open"));
        a.addSeparator();
        a.add(menuitem("Save Layer"));
        a.addSeparator();
        a.add(menuitem("Travel UP"));
        a.add(menuitem("Travel DOWN"));
        a.addSeparator();
        a.add(menuitem("Generate Structure"));
        menubar.add(a);

        JMenu b = new JMenu("Model");
        b.add(menuitem("Reconstruct Model"));
        menubar.add(b);


        JButton up = new JButton("up");
        up.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                miicraft.up();
                miicraft.repaint();
            }
        });

        JButton down = new JButton("down");
        down.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                miicraft.down();
                miicraft.repaint();
            }
        });

        JButton black = new JButton("black");
        black.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                miicraft.setBlack();

            }
        });

        JButton white = new JButton("white");
        white.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                miicraft.setWhite();
            }
        });

        // save a single layer
        JButton save = new JButton("save");
        save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                miicraft.save();
            }
        });

        // orbit 3d
        JButton orbitX = new JButton("orbitX");
        orbitX.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                miimodel.getCurrentImage().getGraphics().clearRect(0, 0, miimodel.getCurrentImage().getWidth(), miimodel.getCurrentImage().getHeight());

                Reconstruct.orbitX(miimodel.getVoxels());
                Reconstruct.project(miimodel.getCurrentImage(), miimodel.getVoxels());
                miimodel.repaint();
            }
        });
        JButton orbitY = new JButton("orbitY");
        orbitY.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                miimodel.getCurrentImage().getGraphics().clearRect(0, 0, miimodel.getCurrentImage().getWidth(), miimodel.getCurrentImage().getHeight());

                Reconstruct.orbitY(miimodel.getVoxels());
                Reconstruct.project(miimodel.getCurrentImage(), miimodel.getVoxels());
                miimodel.repaint();
            }
        });

        JPanel controllButtonsA = new JPanel();
        controllButtonsA.add(up);
        controllButtonsA.add(down);
        controllButtonsA.add(white);
        controllButtonsA.add(black);
        controllButtonsA.add(save);

        JPanel controllButtonsB = new JPanel();
        controllButtonsB.add(orbitX);
        controllButtonsB.add(orbitY);

        JPanel layers = new JPanel(new BorderLayout());
        layers.add(BorderLayout.PAGE_END, controllButtonsA);
        layers.add(BorderLayout.CENTER, miicraft);
        layers.add(BorderLayout.LINE_START, messageScroller);

        JPanel threedee = new JPanel(new BorderLayout());
        threedee.add(BorderLayout.PAGE_END, controllButtonsB);
        threedee.add(BorderLayout.CENTER, miimodel);
        threedee.add(BorderLayout.LINE_START, messageScrollerx);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("layers", layers);
        tabs.add("model", threedee);

        setJMenuBar(menubar);
        add(tabs);

        miicraft.insertMessage("miiLayer designer ready ...");

        repaint();

    }

    public JMenuItem menuitem(String itemName) {
        JMenuItem item = new JMenuItem(itemName);
        item.addActionListener(this);
        return item;
    }

    public static void main(String[] params) {

        new mii();
    }

    public void actionPerformed(ActionEvent event) {

        if (event.getActionCommand().equals("Open")) {

            JFrame f = new JFrame();
            FileDialog fd = new FileDialog(f, "load", FileDialog.LOAD);
            fd.setLocation(50, 50);

            System.setProperty("apple.awt.fileDialogForDirectories", "true");
            fd.setVisible(true);
            System.setProperty("apple.awt.fileDialogForDirectories", "false");

            miicraft.setDirectory(fd.getDirectory() + fd.getFile());
            miicraft.initiateImage();

            miimodel.setDirectory(fd.getDirectory() + fd.getFile());
            miimodel.initiateImage();

        } else if (event.getActionCommand().equals("Save Layer")) {

            miicraft.save();

        } else if (event.getActionCommand().equals("Travel UP")) {

            Thread t = new Thread(new TravelUp(miicraft));
            t.start();

        } else if (event.getActionCommand().equals("Travel DOWN")) {

            Thread t = new Thread(new TravelDown(miicraft));
            t.start();

        } else if (event.getActionCommand().equals("Generate Structure")) {

            Thread t = new Thread(new Generator(miicraft));
            t.start();

        } else if (event.getActionCommand().equals("Reconstruct Model")) {

            Thread t = new Thread(new Reconstruct(miimodel));
            t.start();

            miimodel.getCurrentImage().getGraphics().clearRect(0, 0, miimodel.getCurrentImage().getWidth(), miimodel.getCurrentImage().getHeight());
            Reconstruct.project(miimodel.getCurrentImage(), miimodel.getVoxels());
            miimodel.repaint();

        }
    }
}
