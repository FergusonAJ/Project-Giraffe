package framework;

import static framework.math3d.math3d.add;
import static framework.math3d.math3d.axisRotation;
import static framework.math3d.math3d.length;
import static framework.math3d.math3d.mul;
import static framework.math3d.math3d.normalize;
import static framework.math3d.math3d.scaling;
import static framework.math3d.math3d.sub;
import static framework.math3d.math3d.translation;
import framework.math3d.vec3;
import framework.math3d.vec4;

/**
 *
 * @author Andrew Polanco
 */
public class Projectile extends PhysicsBody
{
    
    /**
     * Renders the pin to the screen
     * @param prog The program to use for rendering
     */
    public boolean mAlive = true;
    float lifetime = 1.5f;
    protected Mesh mMesh = MeshManager.getInstance().get("zomBullet");
    Object owner;
    protected vec4 mPos;
    protected double mRotY = Math.PI / 2 * 3;
    protected vec3 mScale = new vec3(1,1,1);
    protected float mRad = .2f;
    protected float mYOffset;
    
    public Projectile(Object owner, vec4 pos,  vec4 vel, float offSet)
    {
        
        this.owner = owner;
        mPos = pos;
        //mMesh = MeshManager.getInstance().get("pig");
        
        //mRotY = rotY;
        mYOffset = offSet;
        
        mVel = normalize(vel);
        mVel = mul(mVel, 50);
        //mVel = mul(new vec4(1,0,0,0), axisRotation(new vec4(0,1,0,0), mRotY));
        //mVel = mul(mVel, 100);
        
    }
    @Override
    public void update(float elapsed)
    {
        if(toggleGravity)
            mVel = add(mVel,mul(mGravity,elapsed));
        
        mPos = add(mPos, mul(mVel,elapsed));
        lifetime -=elapsed;
        if(lifetime<=0)
            mAlive = false;
    }
    
//    public void takeoff()
//    {
//        
//        mVel = mul(new vec4(1,0,0,0), axisRotation(new vec4(0,1,0,0), mRotY));
//        mVel = mul(mVel, 100);
//    }
    
    public boolean checkCollision(vec4 pos, float rad2)
    {
        double dist = length(sub(pos, mPos));
        if(dist < mRad + rad2)
        {
            System.out.println("gotem");
                return true;
                
            
        }
        return false;   
    }
    
    public void draw(Program prog)
    {
            prog.setUniform("worldMatrix", mul(mul(mul(scaling(mScale),axisRotation(new vec4(0.0f,1.0f,0.0f,0.0f), mRotY), translation(mPos), translation(new vec3(0,mYOffset, 0))))));
            mMesh.draw(prog);
        
    }
}
