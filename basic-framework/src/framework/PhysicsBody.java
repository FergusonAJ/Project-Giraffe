package framework;

import static framework.math3d.math3d.add;
import static framework.math3d.math3d.mul;
import framework.math3d.vec4;

/**
 *
 * @author Andrew Polanco
 */
public class PhysicsBody {
    
    vec4 mPos = new vec4(0,0,0,0);
    vec4 mVel = new vec4(0,0,0,0);
    vec4 mGravity = new vec4(0,-100,0,0);
    boolean toggleGravity = false;
    
    void update(float elapsed)
    {
        if(toggleGravity)
            mVel = add(mVel,mul(mGravity,elapsed));
        mPos = add(mPos, mul(mVel,elapsed));
    }

    public void setToggleGravity(boolean toggleGravity) {
        this.toggleGravity = toggleGravity;
    }
    
    
    
    
    
    
    
}
