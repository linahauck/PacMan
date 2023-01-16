import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Controller{
    private GUI gui;
    private Model model;
    private int lengde, bredde;
    private Thread traad;
    private Rettning rettning = Rettning.SOR;

    public Controller(File fil){
        String[][] brett = lesFraFil(fil);
        gui = new GUI(brett, this, lengde, bredde);
        model = new Model(gui, brett, lengde, bredde, this);
        traad = new Thread(new Teller());
        traad.start();
    }

    private String[][] lesFraFil(File fil){
        Scanner sc = null;
        try {
            sc = new Scanner(fil);
        } catch (FileNotFoundException e) {
            System.out.println("[ERROR] : fil ikke funnet.");
            System.exit(1);
        }

        String[] linje = sc.nextLine().split(" ");
        lengde = Integer.parseInt(linje[0]);
        bredde = Integer.parseInt(linje[1]);

        String[][] brett = new String[lengde][bredde];
        for (int i=0; i<lengde; i++){
            linje = sc.nextLine().split(" ");
            for (int j=0; j<bredde; j++){
                brett[i][j] = linje[j];
            } 
        }
        return brett;
    }

    //klokketråd!
    class Teller implements Runnable{
        @Override 
        public void run(){
            while(true){
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    return;
                }
                flytt();
            }
        }
    }

    private void flytt(){
        model.flytt(rettning);
    }

    public void settRetning(Rettning retning){
        this.rettning = retning;
    }

    public void sluttVinner(){
        traad.interrupt();
        gui.vinner();
    }

    public void sluttTaper(){
        traad.interrupt();
        gui.taper();
    }

    public void avslutt(){
        System.exit(1);
    }
}