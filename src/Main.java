import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){

        System.out.println("Début du progamme.");
        // Création du Système multi-agent
        Scanner input = new Scanner(System.in);
        int nbAgents;
        do{
            System.out.print("Veuillez entrer le nombre d'agent à générer : ");
            nbAgents = input.nextInt();
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
        Collections.shuffle(agents, new Random(10));
        for(Agent a : agents){
            pile1.add(a);
        }
        init.add(pile1);
        init.add(pile2);
        init.add(pile3);

        Environnement monde = new Environnement(init);
        SMA sma = new SMA(agents, monde);

        int choix;
        do{
            System.out.println("Veuillez choisir la version à utiliser (1 ou 2) : ");
            System.out.println("- Version 1 : actions individuelles.");
            System.out.println("- Version 2 : plannification d'après toutes les perceptions.");
            choix = input.nextInt();
        }while (choix != 1 && choix != 2);

        long debut = System.nanoTime();
        if (choix == 1) sma.runV1();
        else sma.runV2();
        double fin = (double) (System.nanoTime() - debut)/1000000;

        System.out.println("Fin du programme. Temps de résolution : " + fin + "ms");
        System.out.print("Appuyez sur Entrée pour terminer.");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
