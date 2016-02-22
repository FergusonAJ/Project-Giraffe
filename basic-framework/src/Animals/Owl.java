package Animals;

import framework.Animal;
import framework.Mesh;
import framework.math3d.vec4;

/**
 *
 * @author Andrew Polanco
 */
public class Owl extends Animal{

    public Owl(Mesh mesh, vec4 position, float yOffset) {
        super(mesh, position, yOffset);
        mDmg = 10;
    }
    
    public void specialAbility()
    {
        
    }
    
}
