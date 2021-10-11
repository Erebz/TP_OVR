import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

public class Environnement {

    /**
     * Sur quel emplacement se trouve l'agent
     * */
    private HashMap<Agent, Integer> emplacement;
    /**
     * Quel agent se trouve à la tête de la pile
     */
    private HashMap<Integer, Agent> pile;
    private HashMap<Agent, Agent> surQui;
    private HashMap<Agent, Agent> sousQui;
    private HashMap<Agent, Boolean> estPousse;
    private int nbEmplacement;

    /**
     * Initialise l'environnement.
     * @param init dispotion initiale des agents. Si une pile est vide, elle doit tout de même se trouver dans la liste.
     */
    public Environnement(ArrayList<ArrayList<Agent>> init){
        emplacement = new HashMap<Agent, Integer>();
        pile = new HashMap<Integer, Agent>();
        surQui = new HashMap<Agent, Agent>();
        sousQui = new HashMap<Agent, Agent> ();
        estPousse = new HashMap<Agent, Boolean>();

        nbEmplacement = init.size();
        for(int i = 0; i < nbEmplacement; i++){
            Agent lastAgent = null;
            ArrayList<Agent> pile = init.get(i);
            if(pile.size() != 0) {
                this.pile.put(i, pile.get(pile.size()-1));
            } else {
                this.pile.put(i,null);
            }
            for(int j = pile.size() - 1; j >= 0; j--){
                Agent a = pile.get(j);
                if (a != null){
                    emplacement.put(a, i);
                    estPousse.put(a, false);
                }
                sousQui.put(a, lastAgent);
                if(lastAgent != null){
                    surQui.put(lastAgent, a);
                }
                if (j == 0) {
                    surQui.put(a, null);
                }
                lastAgent = a;
            }

        }
    }

    /**
     * Permet de déplacer un agent vers un emplacement donné.
     * @param a L'agent à déplacer
     * @param empl L'emplacement cible (index min : 0)
     */
    public void deplacer(Agent a, int empl){
        int origine = emplacement.get(a);
        emplacement.replace(a, empl);
        Agent surQui = this.surQui.get(a); // agent sous l'agent en déplacement
        Agent lastPile = pile.get(empl); // ancienne tête de pile destination avant le déplacement
        this.surQui.replace(a, lastPile);
        if (lastPile != null) this.sousQui.replace(lastPile, a);
        if (surQui != null) this.sousQui.replace(surQui, null);
        pile.replace(empl, a);
        pile.replace(origine, surQui);
        estPousse.replace(a, false);
    }

    /**
     * L'agent pousse l'agent au dessus.
     * @param a L'agent qui réalise la poussée
     */
    public void pousser(Agent a) {
        Agent cible = this.sousQui.get(a);
        if(cible != null) estPousse.replace(cible, true);
    }

    /**
     * Permet de créer la perception d'un agent
     * @param a L'agent qui perçoit.
     * @return un objet Perception qui résume les informations propres à l'agent
     */
    public Perception getPerception(Agent a){
        Agent dessous = this.surQui.get(a);
        boolean estPousse = this.estPousse.get(a);
        boolean estLibre = this.sousQui.get(a) == null;
        return new Perception(a, dessous, estPousse, estLibre);
    }

    public int getDeplacementPossibleAleatoire(Agent agent) {
        int choix = 0;
        int empl = emplacement.get(agent);
        Random r = new Random();
        do {
            choix = r.nextInt(nbEmplacement);
        }while(choix == empl);

        return choix;
    }
}
