import java.util.ArrayList;

public class SMA {
    private ArrayList<Agent> agents;
    private int NB_MAX_ITER = 1000;
    private Environnement monde;

    public SMA(ArrayList<Agent> agents, Environnement e){
        this.agents = agents;
        this.monde = e;
    }

    public void run(){
        int nbIter = 0;
        boolean butAtteint = false;
        while(nbIter < NB_MAX_ITER && !butAtteint){
            System.out.println("# iteration #"+nbIter);
            butAtteint = true;
            for(Agent a : agents){
                Perception perception = monde.getPerception(a);
                Action action = a.getAction(perception);
                if(action != null){
                    action.setEnvironnement(monde);
                    action.executer();
                }

                // Verification si but atteint.
                Agent dessous = perception.getDessous();
                if(!a.isBut(dessous)){
                    butAtteint = false;
                }
            }
            nbIter++;
            System.out.println("");
        }
        System.out.println("But atteint : " + (butAtteint?"oui":"non"));
    }

}
