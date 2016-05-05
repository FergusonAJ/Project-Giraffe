package framework;

import static framework.math3d.math3d.add;
import static framework.math3d.math3d.mul;
import framework.math3d.vec4;

/**
 *
 * @author Andrew Polanco
 */
public class PhysicsBody {
    
    public vec4 mPos = new vec4(0,0,0,1);
    public float mRad = 0;
    protected vec4 mVel = new vec4(0,0,0,0);
    protected vec4 mGravity = new vec4(0,-100,0,0);
    protected boolean toggleGravity = false;
    protected vec4 prevPos = new vec4(0,0,0,1);
    public ObjectType ot;
    public PhysicsBody partner = null;
    
    public enum ObjectType
    {
        ANIMAL, PIN, OBSTACLE
    }
    
    public void update(float elapsed)
    {
        if(toggleGravity)
            mVel = add(mVel,mul(mGravity,elapsed));
        mPos = add(mPos, mul(mVel,elapsed));
    }

    public void setToggleGravity(boolean toggleGravity) {
        this.toggleGravity = toggleGravity;
    }

    public vec4 getmPos() {
        return mPos;
    }

    public vec4 getmVel() {
        return mVel;
    }

    public vec4 getmGravity() {
        return mGravity;
    }

    public boolean isToggleGravity() {
        return toggleGravity;
    }
    
    
    
    
    
    
    
    
}
