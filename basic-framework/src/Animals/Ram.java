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
public class Ram extends Animal{

    
    public Ram(vec4 position, float yRot) {
        super(MeshManager.getInstance().get("ram"), position, 2.0f);
        mDmg = 35;
        specialTimer = 2f;
        mSpecies = "ram";
        mRotY = yRot;
        at = AnimalType.RAM;
    }
    
    @Override
    protected void takeoff()
    {
        mVel = mul(new vec4(1,0,0,0), axisRotation(new vec4(0,1,0,0), mRotY));
        mVel = mul(mVel, 50);
        mMoving = true;
        
    }
    
    @Override
    public void specialAbility()
    {
        isSpecialActive = true;
        usedSpecial = true;
        
        if(isSpecialActive && specialTimer > 0)
        {
            isSpecialActive = true;
            mDmg = 50;
            mRad = (float) 1.5;
        }
        
    }
    
    
}
