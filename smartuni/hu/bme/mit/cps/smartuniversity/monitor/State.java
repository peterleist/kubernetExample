public class State {
    private String id;
    private StateType type;

    public State() {
        this.id = "q0";
        this.type = StateType.NORMAL;
    }

    public State(String id, StateType stateType) {
        this.id = id;
        this.type = stateType;
    }

    public String getId() {
        return id;
    }

    public StateType getType() {
        return type;
    }

    public void setType(StateType type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public void writeState(){
    	System.out.println(this.id + " " + this.type);
    }
}
