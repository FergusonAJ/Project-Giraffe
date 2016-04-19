package Animals;

import framework.Animal;
import framework.Mesh;
import framework.MeshManager;
import static framework.math3d.math3d.axisRotation;
import static framework.math3d.math3d.mul;
import framework.math3d.vec4;

/**
 *
 * @author Andrew Polanco
 */
public class Cheetah extends Animal{

    
    public Cheetah(vec4 position, float yRot) 
    {
        super(MeshManager.getInstance().get("cheetah"), position, 3.0f);
        mDmg = 30;
        mSpecies = "cheetah";
        mRotY = yRot;
        at = AnimalType.CHEETAH;
    }
    
    @Override
    protected void takeoff()
    {
        mVel = mul(new vec4(1,0,0,0), axisRotation(new vec4(0,1,0,0), mRotY));
        mVel = mul(mVel, 100);
        mMoving = true;
        
    }
    
}
