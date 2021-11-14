import java.util.Objects;

public class Agent {
    private String id;
    private String but;

    public Agent(String id, String but) {
        this.id = id;
        this.but = but;
    }

    public Action getAction(Perception perception){
        Agent dessous = perception.getDessous();
        if(isBut(dessous) && !perception.estPousse()){
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
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return Objects.equals(id, agent.id) && Objects.equals(but, agent.but);
    }
}
