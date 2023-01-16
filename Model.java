import java.util.*;
import java.awt.Color;

public class Model {

    private GUI gui;
    private int pacPosLengde, pacPosBredde, lengde, bredde;
    private String[][] brett;
    private Controller c;
    private Spokelse[] spokelser = new Spokelse[4];
    private int liv = 3;
    private int antallTatt, antallPaaBrettet = 0;

    public Model(GUI gui, String[][] brett, int lengde, int bredde, Controller c){
        this.gui = gui;
        this.brett = brett;
        this.lengde = lengde;
        this.bredde = bredde;
        this.c = c;
        plasserPackmanStart();
        tellAntallPaaBrett();
        plasserSpokelserStart();
    }

    public void flytt(Rettning rettning){
        int midPacPosLengde = pacPosLengde;
        int midPacPosBredde = pacPosBredde;
        if (Rettning.NORD == rettning) midPacPosLengde--;
        if (Rettning.SOR == rettning) midPacPosLengde++;
        if (Rettning.VEST == rettning) midPacPosBredde--;
        if (Rettning.OST == rettning) midPacPosBredde++;

        flyttSpokelser();
        if(truffetSpokelse()){
            liv--;
            gui.oppdaterLiv(liv);
        }
        if (gyldigPosisjon(midPacPosLengde, midPacPosBredde)){
            besokRute(midPacPosLengde, midPacPosBredde);
        }
        if(truffetSpokelse()){
            liv--;
            gui.oppdaterLiv(liv);
        }
        if (liv == 0) c.sluttTaper();
        if (antallTatt == antallPaaBrettet) c.sluttVinner();
    }

    private void besokRute(int nyPosLengde, int nyPosBredde){
        if(brett[nyPosLengde][nyPosBredde].equals(".")){
            antallTatt++;
            brett[nyPosLengde][nyPosBredde] = " ";
            gui.tellPoeng(antallTatt);
        }
        gui.pacmanHarBesokt(pacPosLengde, pacPosBredde);
        pacPosLengde = nyPosLengde;
        pacPosBredde = nyPosBredde;

        gui.tegnPacMan(pacPosLengde, pacPosBredde);
    }

    private boolean truffetSpokelse(){
        for(Spokelse spo : spokelser){
            if(spo != null && spo.likKordinat(pacPosLengde, pacPosBredde)){
                return true;
            }
        }
        return false;
    }

    private boolean gyldigPosisjon(int posLengde, int posBredde){
        if (posLengde < 0 || posLengde >= lengde){
            return false;
        }
        if (posBredde < 0 || posBredde >= bredde){
            return false;
        }
        if (brett[posLengde][posBredde].equals("#")){
            return false;
        }
        return true;
    }

    private void plasserPackmanStart(){
        Random r = new Random();
        pacPosLengde = r.nextInt(lengde);
        pacPosBredde = r.nextInt(bredde);

        while(!gyldigPosisjon(pacPosLengde, pacPosBredde)){
            pacPosLengde = r.nextInt(lengde);
            pacPosBredde = r.nextInt(bredde);
        }
        besokRute(pacPosLengde, pacPosBredde);
    }

    private void flyttSpokelser(){
        Random r = new Random();
        for(Spokelse spo : spokelser){
            int spoLengde = spo.hentLengde();
            int spoBredde = spo.hentBredde();
            if(spo.likKordinat(pacPosLengde, pacPosBredde)){
                gui.tegnPacMan(pacPosLengde, pacPosBredde);
            } else{
                gui.tegnRute(brett[spoLengde][spoBredde], spoLengde, spoBredde);
            }
            List<Rettning> gyldigeRettninger = gyldigeRettninger(spoLengde, spoBredde);
            fjernRettningKomFra(gyldigeRettninger, spo.hentRetning());
            spo.settRetning(gyldigeRettninger.get(r.nextInt(gyldigeRettninger.size())));
            spo.flytt();
            gui.plasserSpokelse(spo.hentFarge(), spo.hentLengde(), spo.hentBredde());
        }
    }

    private void fjernRettningKomFra(List<Rettning> gyldigeRettninger, Rettning rettning){
        if (gyldigeRettninger.size() == 1) return;
        if (Rettning.NORD == rettning) gyldigeRettninger.remove(rettning.SOR);
        if (Rettning.SOR == rettning) gyldigeRettninger.remove(rettning.NORD);
        if (Rettning.VEST == rettning) gyldigeRettninger.remove(rettning.OST);
        if (Rettning.OST == rettning) gyldigeRettninger.remove(rettning.VEST);
    }

    private void plasserSpokelserStart(){
        spokelser[0] = plasserSpokelse(Color.PINK);
        spokelser[1] = plasserSpokelse(Color.GREEN);
        spokelser[2] = plasserSpokelse(Color.RED);
        spokelser[3] = plasserSpokelse(Color.CYAN);

        for(Spokelse spo : spokelser){
            gui.plasserSpokelse(spo.hentFarge(), spo.hentLengde(), spo.hentBredde());
        }
    }

    private Spokelse plasserSpokelse(Color farge){
        Random r = new Random();
        int spoLengde = r.nextInt(lengde);
        int spoBredde = r.nextInt(bredde);

        while(! gyldigPosisjon(spoLengde, spoBredde) || noenAndrePlassert(spoLengde, spoBredde)){
            spoLengde = r.nextInt(lengde);
            spoBredde = r.nextInt(bredde);
        }
        List<Rettning> gyldigeRettninger = gyldigeRettninger(spoLengde, spoBredde);
        return new Spokelse(spoLengde, spoBredde, farge, gyldigeRettninger.get(r.nextInt(gyldigeRettninger.size())));
    }

    private boolean noenAndrePlassert(int nyPosLengde, int nyPosBredde){
        if(pacPosLengde == nyPosLengde && nyPosBredde == pacPosBredde) return true;
        for (Spokelse spo : spokelser) {
            if(spo != null && spo.likKordinat(nyPosLengde, nyPosBredde)) return true;
        }
        return false;
    }

    private List<Rettning> gyldigeRettninger(int nyPosLengde, int nyPosBredde){
        List<Rettning> gyldigeRettninger = new LinkedList<>();
        if(gyldigPosisjon(nyPosLengde-1, nyPosBredde)) gyldigeRettninger.add(Rettning.NORD);
        if(gyldigPosisjon(nyPosLengde+1, nyPosBredde)) gyldigeRettninger.add(Rettning.SOR);
        if(gyldigPosisjon(nyPosLengde, nyPosBredde-1)) gyldigeRettninger.add(Rettning.VEST);
        if(gyldigPosisjon(nyPosLengde, nyPosBredde+1)) gyldigeRettninger.add(Rettning.OST);
        return gyldigeRettninger;
    }

    private void tellAntallPaaBrett(){
        for(int i = 0; i < lengde; i++){
            for(int j = 0; j < bredde; j++){
                if (brett[i][j].equals(".")){
                    antallPaaBrettet++;
                }
            }
        }
    }
}
