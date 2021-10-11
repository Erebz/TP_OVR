public class Agent {

    private String id;
    private String but;

    public Agent(String id, String but) {
        this.id = id;
        this.but = but;
    }

    public Action getAction(Perception perception){
        Agent dessous = perception.getDessous();
        if(perception.estPousse()){
            System.out.println(">> "+ this + " : Je me fais pousser !!");
        }
        if(isBut(dessous) && !perception.estPousse()){
            System.out.println(">> "+ this + " : Tout va bien !!");
            return null;
        } else{
            if(perception.estLibre()){
                return new ActionDeplacement(this);
            }else{
                return new ActionPousser(this);
            }
        }
    }

    public String getBut(){
        return this.but;
    }

    public boolean isBut(Agent a){
        if(a == null || this.but == null){
            return this.but == null;
        }else{
            return this.but.equals(a.id);
        }
    }

    @Override
    public String toString() {
        return "Agent_" + id;
    }
}
