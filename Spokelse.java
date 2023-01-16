import java.awt.Color;

public class Spokelse {
    
    private int posLengde, posBredde;
    private Color farge;
    private Rettning rettning;

    public Spokelse(int l, int b, Color f, Rettning r){
        posLengde = l;
        posBredde = b;
        farge = f;
        rettning = r;
    }

    public void flytt(){
        if(rettning == Rettning.NORD) posLengde--;
        if(rettning == Rettning.SOR) posLengde++;
        if(rettning == Rettning.VEST) posBredde--;
        if(rettning == Rettning.OST) posBredde++;
    }

    public int hentLengde(){
        return posLengde;
    }

    public int hentBredde(){
        return posBredde;
    }

    public Color hentFarge(){
        return farge;
    }

    public Rettning hentRetning(){
        return rettning;
    }

    public boolean likKordinat(int lengde, int bredde){
        return posLengde==lengde && posBredde==bredde;
    }

    public void settRetning(Rettning nyRettning){
        rettning = nyRettning;
    }
}
