package framework;

/*
 * Written by Jim Hudson
 */
public class UnitSquare {
    Mesh m;
    
    public UnitSquare(){
        m = new Mesh("assets/usq.obj.mesh");
    }
    
    public void draw(Program p){
        m.draw(p);
    }
}
