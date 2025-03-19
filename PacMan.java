import java.io.File;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PacMan {
    public static void main(String[] args) {
        JFrame meny = new JFrame("PacMan MENY");
        meny.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.BLACK);
        JButton start = new JButton("Start Spillet");
        start.setForeground(Color.BLACK);
        start.setBackground(new Color(204, 204, 255));
        start.setPreferredSize(new Dimension(30,30));
        class StartAction implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e){
                meny.dispose();
                new Controller(new File("pacman.txt"));
            }
        }
        start.addActionListener(new StartAction());
        panel.add(start, BorderLayout.NORTH);
        //Pᗣᗧ•••MᗣN

        JButton exit = new JButton("Exit");
        exit.setForeground(Color.BLUE);
        exit.setBackground(Color.BLACK);
        exit.setPreferredSize(new Dimension(30,30));
        class StoppAction implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        }
        exit.addActionListener(new StoppAction());
        panel.add(exit, BorderLayout.SOUTH);
        
        meny.add(panel);
        meny.pack();
        meny.setVisible(true);
    }
}
