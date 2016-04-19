package Pins;

import framework.Pin;
import static framework.math3d.math3d.axisRotation;
import static framework.math3d.math3d.mul;
import static framework.math3d.math3d.normalize;
import static framework.math3d.math3d.sub;
import framework.math3d.vec4;

/**
 *
 * @author Andrew Polanco
 */
public class Angus extends Pin{

    
    public Angus(String meshStr, vec4 position, float yOffset, boolean isStatic) 
    {
        super(meshStr, position, yOffset, isStatic);
        mHealth = 50;
        pt = PinType.ANGUS;
        
    }
   
    @Override
    protected void takeoff(vec4 animalPos)
    {
        mVel = normalize(sub(animalPos, mPos));
        mVel = mul(mVel, 2);
        mMoving = true;
    }
    
    
    
    
}
