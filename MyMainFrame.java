import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.event.MenuEvent;
import javax.swing.JMenuItem;
import javax.swing.event.MenuListener;


public class MyMainFrame extends JFrame{
     
    public static void main(String[] args){
        MyMainFrame frame = new MyMainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static final int WIDTH = 720;
    private static final int HEIGHT = 400;

    private JMenuItem showAxisMenuItem;
    private JMenuItem showDotsMenuItem;
    private JMenuItem showTestMenuItem;
    private JMenuItem showIntegralsMenuItem;
    // private JMenuItem createGraphMenuItem;

    private JFileChooser fileChooser = null;
    private boolean fileLoaded;

    private MyGraphicsDisplay graphicsDisplay = new MyGraphicsDisplay();

    public MyMainFrame(){
        super("Построение графиков функции на основе заранее подготовленного файла");

        Toolkit kit = Toolkit.getDefaultToolkit();
        setSize(WIDTH, HEIGHT);
        setLocation((kit.getScreenSize().width - WIDTH)/2, (kit.getScreenSize().height - HEIGHT)/2);
        // setExtendedState(MAXIMIZED_BOTH);
    //@ Реализация основного функционала программы
        //^ Меню
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        JMenu fileMenu = new JMenu("Файл");
        JMenu graphicsMenu = new JMenu("График");

        menuBar.add(fileMenu);
        menuBar.add(graphicsMenu);

        Action openGraphicsAction = new AbstractAction("Открыть файл"){
            public void actionPerformed(ActionEvent event){
                if(fileChooser == null){
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if(fileChooser.showOpenDialog(MyMainFrame.this) == JFileChooser.APPROVE_OPTION){
                    openGraphics(fileChooser.getSelectedFile());
                }
            }
        };

        fileMenu.add(openGraphicsAction);

        menuBar.add(graphicsMenu);

        Action showAxisAction = new AbstractAction("Показать оси координат"){
            public void actionPerformed(ActionEvent event){
                graphicsDisplay.setShowAxis(showAxisMenuItem.isSelected());
            }
        };

        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
        graphicsMenu.add(showAxisMenuItem);

        showAxisMenuItem.setSelected(true);

        Action showDotsAction = new AbstractAction("Показать маркеры точек"){
            public void actionPerformed(ActionEvent event){
                graphicsDisplay.setShowDots(showDotsMenuItem.isSelected());
            }
        };

        showDotsMenuItem = new JCheckBoxMenuItem(showDotsAction);
        graphicsMenu.add(showDotsMenuItem);

        showDotsMenuItem.setSelected(true);
        graphicsMenu.addMenuListener(new GraphicsMenuListener());

        Action showTest = new AbstractAction("test"){
            public void actionPerformed(ActionEvent event){
                graphicsDisplay.showTest(showTestMenuItem.isSelected());
            }
        };

        showTestMenuItem = new JCheckBoxMenuItem(showTest);
        graphicsMenu.add(showTestMenuItem);

        showTestMenuItem.setSelected(true);
        graphicsMenu.addMenuListener(new GraphicsMenuListener());

        Action showIntegrals = new AbstractAction("Интегралы"){
            public void actionPerformed(ActionEvent event){
                graphicsDisplay.setShowIntegrals(showIntegralsMenuItem.isSelected());
            }
        };

        showIntegralsMenuItem = new JCheckBoxMenuItem(showIntegrals);
        graphicsMenu.add(showIntegralsMenuItem);

        showAxisMenuItem.setSelected(false);
        graphicsMenu.addMenuListener(new GraphicsMenuListener());

        getContentPane().add(graphicsDisplay, BorderLayout.CENTER);
    //@
    }

    private class GraphicsMenuListener implements MenuListener{
        public void menuSelected(MenuEvent event){
            showAxisMenuItem.setEnabled(fileLoaded);
            showDotsMenuItem.setEnabled(fileLoaded);
            showTestMenuItem.setEnabled(fileLoaded);
        }

        public void menuDeselected(MenuEvent event){}

        public void menuCanceled(MenuEvent event){}
    }

    protected void openGraphics(File selectedFile){
        try{
            DataInputStream in = new DataInputStream(new FileInputStream(selectedFile));

            Double[][] graphicsData = new Double[in.available()/(Double.SIZE/8)/2][];

            int i = 0; 
            while(in.available() > 0){
                Double x = in.readDouble();
                Double y = in.readDouble();
                graphicsData[i++] = new Double[]{x, y};
                
            }
            if(graphicsData != null && graphicsData.length > 0){
                fileLoaded = true;
                graphicsDisplay.showGraphics(graphicsData);
            }

            in.close();
        }catch(FileNotFoundException ex){
            JOptionPane.showMessageDialog(
                MyMainFrame.this,
                "Указанный файл не найден",
                "Ошибка загрузки данных", 
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }catch(IOException ex){
            JOptionPane.showMessageDialog(
                MyMainFrame.this,
                "Ошибка чтения координат точек из файла",
                "Ошибка загрузки данных",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
    }
}