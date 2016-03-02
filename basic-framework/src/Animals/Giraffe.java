package Animals;

import framework.Animal;
import framework.Mesh;
import framework.MeshManager;
import framework.math3d.vec4;

/**
 *
 * @author Andrew Polanco
 */
public class Giraffe extends Animal{
    
    
    public Giraffe(vec4 position, float yRot) {
        super(MeshManager.getInstance().get("giraffe"), position, 2.0f);
        mDmg = 50;
        specialTimer = 2f;
        mSpecies = "giraffe";
        mRotY = yRot;
        
    }
    
    @Override
    public void specialAbility()
    {
        isSpecialActive = true;
        usedSpecial = true;
        if(isSpecialActive && specialTimer>0)
        {
            mDmg = 100;
            mRad = 2;
        }
        else
        {
            isSpecialActive = false;
            mDmg = 50;
            mRad = (float) 1.5;
        }
        
    }
    @Override
    protected void resetSpecialAbility()
    {
        isSpecialActive = false;
        mDmg = 50;
        mRad = 1.5f;
        usedSpecial = false;
    }
    
}
