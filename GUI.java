import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI {

    //spøkelse: ᗣ
    //pacman: ᗧ
    
    private JFrame vindu;
    private JPanel panel, spillbrett;
    private JLabel[][] ruter;
    private JLabel poeng, liv;
    private JButton exit;
    private Controller c;
    private int lengde, bredde;

    public GUI(String[][] brettString, Controller c, int lengde, int bredde){
        this.c = c;
        this.lengde = lengde;
        this.bredde = bredde;
        ruter = new JLabel[lengde][bredde];

        vindu = new JFrame("PacMan");
        vindu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.BLACK);
        panel.add(pacmanTekst(), BorderLayout.NORTH);
        
        spillbrett = lagBrett(brettString);
        panel.add(spillbrett, BorderLayout.CENTER);
    
        panel.add(lagExit(), BorderLayout.SOUTH);

        vindu.addKeyListener(new TrykketKnapp());
        vindu.setFocusable(true);
        vindu.requestFocus();

        vindu.add(panel);
        vindu.pack();
        vindu.setVisible(true);
    }

    class TrykketKnapp implements KeyListener{
        @Override
        public void keyPressed(KeyEvent e){
            int nokkel = e.getKeyCode();
            if (nokkel == KeyEvent.VK_LEFT){
                c.settRetning(Rettning.VEST);
            }
            if (nokkel == KeyEvent.VK_UP){
                c.settRetning(Rettning.NORD);
            }
            if (nokkel == KeyEvent.VK_RIGHT){
                c.settRetning(Rettning.OST);
            }
            if (nokkel == KeyEvent.VK_DOWN){
                c.settRetning(Rettning.SOR);
            }
            
        }

        @Override
        public void keyReleased(KeyEvent e){
        }

        @Override
        public void keyTyped(KeyEvent e){
        }
    }


    private JPanel pacmanTekst(){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.BLACK);

        JLabel tittel = new JLabel("Pᗣᗧ•••MᗣN");
        tittel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
        tittel.setForeground(Color.YELLOW);
        panel.add(tittel, BorderLayout.LINE_START);

        liv = new JLabel("Liv: ● ● ●", SwingConstants.CENTER);
        liv.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        liv.setForeground(Color.YELLOW);
        panel.add(liv, BorderLayout.CENTER);

        poeng = new JLabel("0 Poeng");
        poeng.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        poeng.setForeground(Color.YELLOW);
        panel.add(poeng, BorderLayout.LINE_END);


        return panel;
    }

    private JPanel lagBrett(String[][] brettString){
        JPanel brett = new JPanel();
        brett.setLayout(new GridLayout(lengde, bredde));

        for (int i = 0; i < lengde; i++){
            for (int j=0; j<bredde; j++){
                JLabel rute = lagRute(brettString[i][j]);
                brett.add(rute);
                ruter[i][j] = rute;
            }
        }

        return brett;
    }

    private JLabel lagRute(String tegn){
        JLabel rute;
        if (tegn.equals("#")){
            rute = new JLabel();
            rute.setBackground(Color.BLUE);
        }
        else {
            rute = new JLabel("●", SwingConstants.CENTER);
            rute.setBackground(Color.BLACK);
            rute.setForeground(Color.WHITE);
        }
        rute.setOpaque(true);
        rute.setPreferredSize(new Dimension(30,30));
        return rute;
    }

    public void tegnPacMan(int posLengde, int posBredde){
        JLabel rute = ruter[posLengde][posBredde];
        rute.setText("ᗧ");
        rute.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        rute.setForeground(Color.YELLOW);
    }

    public void tegnRute(String tegn, int lengde, int bredde){
        JLabel rute = ruter[lengde][bredde];
        if (tegn.equals(".")){
            rute.setText("●");
        }
        else{
            rute.setText(" ");
        }

        rute.setForeground(Color.WHITE);
        rute.setBackground(Color.BLACK);
    }

    public void pacmanHarBesokt(int posLengde, int posBredde){
        JLabel rute = ruter[posLengde][posBredde];
        rute.setText("");
    }

    public void tellPoeng(int p){
        poeng.setText(p+" Poeng");
    }

    public void plasserSpokelse(Color farge, int posLengde, int posBredde){
        JLabel rute = ruter[posLengde][posBredde];
        rute.setText("ᗣ");
        rute.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        rute.setForeground(farge);
    }

    public void oppdaterLiv(int nyeLiv){
        String txt = "Liv: ";
        for (int i=0; i < nyeLiv; i++){
            txt += "● ";
        }
        liv.setText(txt);
    }

    public void vinner(){
        JPanel vinnerPanel = new JPanel();
        vinnerPanel.setLayout(new BorderLayout());
        JLabel vinnerTxt = new JLabel("GRᗣTULERER! Du VᗣNT!!", SwingConstants.CENTER);
        vinnerTxt.setForeground(Color.CYAN);
        vinnerTxt.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
        vinnerPanel.add(vinnerTxt, BorderLayout.CENTER);
        vinnerPanel.setBackground(Color.BLACK);

        panel.remove(spillbrett);
        panel.add(vinnerPanel, BorderLayout.CENTER);
        vindu.validate();
    }

    public void taper(){
        JPanel taperPanel = new JPanel();
        taperPanel.setLayout(new BorderLayout());
        JLabel gameOver = new JLabel("GᗣME OVER!", SwingConstants.CENTER);
        gameOver.setForeground(Color.RED);
        gameOver.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
        taperPanel.add(gameOver, BorderLayout.CENTER);
        taperPanel.setBackground(Color.BLACK);

        panel.remove(spillbrett);
        panel.add(taperPanel, BorderLayout.CENTER);
        vindu.validate();
    }

    private JButton lagExit(){
        exit = new JButton("Exit");
        exit.setBackground(Color.BLACK);
        exit.setOpaque(true);
        exit.setBorderPainted(false);
        exit.setForeground(Color.BLUE);
        exit.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        
        class StoppAction implements ActionListener{
            @Override
            public void actionPerformed (ActionEvent e){
                c.avslutt();
            }
        }
        exit.addActionListener(new StoppAction());

        return exit;
    }
}
