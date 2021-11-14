import java.util.ArrayList;
import java.util.HashMap;

public class SMA {
    private ArrayList<Agent> agents;
    private int NB_MAX_ITER = 10000;
    private Environnement monde;
    private HashMap<Agent,Perception> perceptions;

    public SMA(ArrayList<Agent> agents, Environnement e){
        this.agents = agents;
        this.monde = e;
        this.perceptions = new HashMap<Agent,Perception>();
    }

    /**
     * Version 1
     * Stratégie : Déplacement aléatoire si non satisfait ou poussé. Si le déplacement est impossible, l'agent pousse.
     */
    public void runV1(){
        int nbIter = 0;
        boolean butAtteint = false;
        while(nbIter < NB_MAX_ITER && !butAtteint){
            System.out.println("# Iteration n°"+ (nbIter+1));
            System.out.println("  > Environnement (avant) : " + monde);
            butAtteint = true;
            for(Agent a : agents){
                Perception perception = monde.getPerception(a);
                Action action = a.getAction(perception);
                if(action != null){
                    // Exécution de l'action.
                    action.setEnvironnement(monde);
                    System.out.println("  > Action : " + action);
                    action.executer();
                }

                // Verification si but atteint.
                Agent dessous = perception.getDessous();
                if(!a.isBut(dessous)){
                    butAtteint = false;
                }
            }
            nbIter++;
            System.out.println("  > Environnement (après) : " + monde+"\n");
        }
        System.out.println("But atteint : " + (butAtteint?"oui":"non"));
    }

    /**
     * Version 2
     * Stratégie : On se base sur la perception de tous les agents pour trouver la meilleure pièce à déplacer à chaque itération.
     *
     * Hypothèses importantes :
     * -> Comme un agent peut toujours connaître la pièce sur laquelle il est, alors il peut choisir d'éviter ou de cibler
     * une pièce en tête de pile lorsqu'il se déplace.
     * -> La classe "SMA" se base sur les perceptions locales de tous les agents pour planifier les décisions. Les agents ont
     * moins d'impact individuel (pas de poussée) : c'est la classe SMA qui détermine les actions à réaliser en analysant
     * l'ensemble des perceptions. Sur le terrain, cela se concrétiserait par une unité de contrôle centrale qui guidera
     * les agents lors de la résolution du problème.
     *
     * On distingue deux cas principaux :
     * 1) Il y a une pile quasi-satisfaite (toutes les pièces y sont bien placées).
     *    - Si la pièce suivante est libre, alors elle se déplace en ciblant la pile quasi-satisfaite.
     *    - Sinon, on déplace la pièce gênante en évitant la pile quasi-satisfaite.
     * 2) Il y n'a pas de pile quasi-satisfaite :
     *    - S'il n'y a qu'une seule pile, le déplacement est aléatoire.
     *    - Deux piles : On essaye de déplacer la pièce dont le but est le sol (racine). Sinon, on déplace la pièce qui gêne.
     *    - Trois piles : On essaye de fusionner les deux piles qui ne possèdent pas la racine.
     *
     * Remarque : Toutes les opérations de la fonction se basent exclusivement sur les perceptions des agents. Pour connaitre
     * les têtes de piles, on regarde les agents qui sont libres en parcourant la liste des perceptions. Pour savoir si une
     * pile est quasi-satisfaite ou si elle possède une certaine pièce, on le détermine par récursivité sur les perceptions.
     */
    public void runV2(){
        int nbIter = 0;
        boolean butAtteint = false;
        while(nbIter < NB_MAX_ITER && !butAtteint){
            System.out.println("# Iteration n°"+ (nbIter+1));
            updatePerceptions();
            ArrayList<Agent> tetesPiles = getTetesPiles();
            Agent bonnePile = null;
            Action action = null;
            for (int i = 0; bonnePile==null && i < tetesPiles.size(); i++) {
                Agent a = tetesPiles.get(i);
                if(isBonnePile(perceptions.get(a))) bonnePile = a;
            }

            if(bonnePile != null){
                if(tetesPiles.size() == 1){
                    butAtteint = true;
                }else{
                    ArrayList<Agent> autresPiles = new ArrayList<>(tetesPiles);
                    autresPiles.remove(bonnePile);
                    // Pile qui possède la prochaine pièce à déplacer
                    Agent nextDeplacement = null;
                    for (int i = 0; nextDeplacement==null && i < autresPiles.size(); i++) {
                        Agent a = autresPiles.get(i);
                        if(pilePossedeBut(perceptions.get(a), bonnePile)) nextDeplacement = a;
                    }
                    if(nextDeplacement != null){
                        if(nextDeplacement.isBut(bonnePile)){
                            action = new ActionDeplacementCibler(monde, nextDeplacement, bonnePile);
                        }else{
                            action = new ActionDeplacementEviter(monde, nextDeplacement, bonnePile);
                        }
                    }
                }
            }else{
                if(tetesPiles.size() == 1){
                    // Une pile : on déplace aléatoirement la tête de pile.
                    action = new ActionDeplacement(monde, tetesPiles.get(0));
                }else if(tetesPiles.size() == 2){
                    // Deux piles : On essaye de déplacer la racine (1) Sinon, on déplace la pièce qui gêne (2)
                    Agent nextDeplacement = null;
                    for (int i = 0; nextDeplacement==null && i < tetesPiles.size(); i++) {
                        Agent a = tetesPiles.get(i);
                        if(pilePossedeBut(perceptions.get(a), null)) nextDeplacement = a;
                    }
                    if(nextDeplacement != null){
                        if(nextDeplacement.isBut(null)){
                            action = new ActionDeplacementCibler(monde, nextDeplacement, null); // (1)
                        }else{
                            action = new ActionDeplacementEviter(monde, nextDeplacement, null); // (2)
                        }
                    }
                }else{
                    // Trois piles : il faut fusionner les deux piles qui ne possèdent pas la racine.
                    Agent pileOK = null;
                    for (int i = 0; pileOK==null && i < tetesPiles.size(); i++) {
                        Agent a = tetesPiles.get(i);
                        if(pilePossedeBut(perceptions.get(a), null)) pileOK = a;
                    }
                    ArrayList<Agent> autresPiles = new ArrayList<>(tetesPiles); // les deux piles à fusionner
                    autresPiles.remove(pileOK);
                    action = new ActionDeplacementCibler(monde, autresPiles.get(0), autresPiles.get(1));
                }
            }

            System.out.println("  > Environnement : " + monde);
            if(action != null){
                // Exécution de l'action.
                System.out.println("  > Action : " + action);
                action.executer();
            }

            nbIter++;
            System.out.println("");
        }
        System.out.println("But atteint : " + (butAtteint?"oui":"non"));
    }

    /**
     * Mise à jour des perceptions stockées.
     */
    private void updatePerceptions() {
        this.perceptions = new HashMap<Agent,Perception>();
        for(Agent a : agents){
            Perception p = monde.getPerception(a);
            perceptions.put(a, p);
        }
    }

    /**
     * Détermine les agents qui se trouvent au dessus de leur pile, en se basant sur leur perception.
     * @return Liste des têtes de piles.
     */
    private ArrayList<Agent> getTetesPiles() {
        ArrayList<Agent> tetesPiles = new ArrayList<Agent>();
        for(Agent a : perceptions.keySet()){
            Perception p = perceptions.get(a);
            if(p.estLibre()) tetesPiles.add(a);
        }
        return tetesPiles;
    }

    /**
     * Fonction récursive basée sur la perception des agents.
     * Détermine si une pile est satisfaisante.
     */
    private boolean isBonnePile(Perception perception) {
        //Conditiion d'arrêt
        if(perception == null){
            return true;
        }else{
            Agent agent = perception.getObserveur();
            Agent dessous = perception.getDessous();
            return agent.isBut(dessous) && isBonnePile(perceptions.get(dessous));
        }
    }

    /**
     * Fonction récursive basée sur la perception des agents.
     * Détermine si une pile possède une pièce dont le but est donné en paramètre.
     */
    private boolean pilePossedeBut(Perception perception, Agent but) {
        //Conditiion d'arrêt
        if(perception == null){
            return false;
        }else{
            Agent agent = perception.getObserveur();
            Agent dessous = perception.getDessous();
            return agent.isBut(but) || pilePossedeBut(perceptions.get(dessous), but);
        }
    }

}
