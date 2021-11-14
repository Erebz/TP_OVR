import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){

        System.out.println("Début du progamme.");
        // Création du Système multi-agent
        Scanner myInput = new Scanner(System.in);
        int nbAgents = 4;
        do{
            System.out.print("Veuillez entrer le nombre d'agent à générer : ");
            nbAgents = myInput.nextInt();
        }while (nbAgents <= 0);

        ArrayList<Agent> agents = new ArrayList<Agent>();
        for(int i=0; i < nbAgents; i++){
            String id = ""+(i+1);
            String but = i==0 ? null : ""+i;
            Agent a = new Agent(id, but);
            agents.add(a);
        }

        ArrayList<ArrayList<Agent>> init = new ArrayList<ArrayList<Agent>>();
        ArrayList<Agent> pile1 = new ArrayList<>();
        ArrayList<Agent> pile2 = new ArrayList<>();
        ArrayList<Agent> pile3 = new ArrayList<>();
        Collections.shuffle(agents);
        for(Agent a : agents){
            pile1.add(a);
        }

        init.add(pile1);
        init.add(pile2);
        init.add(pile3);

        Environnement monde = new Environnement(init);
        SMA sma = new SMA(agents, monde);
        long debut = System.nanoTime();
        sma.runV1();
        double fin = (double) (System.nanoTime() - debut)/1000000;

        System.out.println("Fin du programme. Temps de résolution : " + String.format("%.3g", fin) + "ms");
    }
}
