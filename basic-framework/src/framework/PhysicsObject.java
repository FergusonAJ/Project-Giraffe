package framework;

import static framework.math3d.math3d.add;
import static framework.math3d.math3d.mul;
import framework.math3d.vec4;

/**
 *
 * @author Andrew Polanco
 */
public class PhysicsObject
{
    vec4 mPos = new vec4(0,0,0,1);
    vec4 mVel;
    vec4 mGravity = new vec4(0,-100,0,0);
    
    public void update(float elapsed)
    {
        System.out.println(mVel);
        mVel = add(mVel,mul(mGravity,elapsed));
        mPos = add(mPos, mul(mVel,elapsed));
    }
    
    
}
