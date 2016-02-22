package Animals;

import framework.Animal;
import framework.Mesh;
import framework.math3d.vec4;

/**
 *
 * @author Andrew Polanco
 */
public class Pig extends Animal
{

    
    public Pig(Mesh mesh, vec4 position, float yOffset)
    {
        super(mesh, position, yOffset);
        mDmg = 25;
        specialTimer = 1;
        
    }
    @Override
    public void specialAbility()
    {
        isSpecialActive = true;
        usedSpecial = true;
        if(isSpecialActive && specialTimer>0)
        {
            mMoving = false;
            mDmg = 1000;
            mRad = 10;
            mAlive = false;
        }
    }
    
}
