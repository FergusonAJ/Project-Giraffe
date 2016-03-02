package Animals;

import framework.Animal;
import framework.Mesh;
import static framework.math3d.math3d.axisRotation;
import static framework.math3d.math3d.mul;
import framework.math3d.vec4;

/**
 *
 * @author Andrew Polanco
 */
public class Ram extends Animal{

    
    public Ram(Mesh mesh, vec4 position, float yOffset) {
        super(mesh, position, yOffset);
        mDmg = 35;
        specialTimer = 2f;
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
        System.out.println("ram");
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
