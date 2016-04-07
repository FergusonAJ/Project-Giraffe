package Animals;

import framework.Animal;
import framework.Mesh;
import framework.MeshManager;
import framework.math3d.vec4;

/**
 *
 * @author Andrew Polanco
 */
public class Pig extends Animal
{

    
    public Pig(vec4 position, float yRot)
    {
        super(MeshManager.getInstance().get("pig"), position, 3.0f);
        mDmg = 25;
        specialTimer = 1;
        mSpecies = "pig";
        mRotY = yRot;
        at = AnimalType.PIG;
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
