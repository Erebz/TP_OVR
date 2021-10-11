import java.util.ArrayList;

public class Main {

    public static void main(String[] args){

        System.out.println("OVR.");

        //Créer SMA
        Agent a = new Agent("A","B");
        Agent b = new Agent("B","C");
        Agent c = new Agent("C","D");
        Agent d = new Agent("D",null);
        ArrayList<Agent> agents = new ArrayList<Agent>();
        agents.add(a);
        agents.add(b);
        agents.add(c);
        agents.add(d);

        //
        //
        // A       C
        // B*     D
        ArrayList<ArrayList<Agent>> init = new ArrayList<ArrayList<Agent>>();
        ArrayList<Agent> pile1 = new ArrayList<>();
        ArrayList<Agent> pile2 = new ArrayList<>();
        ArrayList<Agent> pile3 = new ArrayList<>();
        pile1.add(b);
        pile1.add(c);
        pile1.add(d);
        pile1.add(a);

        init.add(pile1);
        init.add(pile2);
        init.add(pile3);

        Environnement monde = new Environnement(init);
        SMA sma = new SMA(agents, monde);
        sma.run();

        System.out.println("Fin du programme");
    }
}
