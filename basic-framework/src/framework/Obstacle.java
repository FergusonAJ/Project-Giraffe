package framework;

import framework.math3d.mat4;
import framework.math3d.vec4;
import static framework.math3d.math3d.*;
import framework.math3d.vec3;

/**
 * Basic Obstacle class, that can use any mesh, but does collision with an AABB
 */
public class Obstacle extends PhysicsBody
{
    static OpenSimplexNoise noise = new OpenSimplexNoise(); //Used for placing the obstacle on the ground
    Mesh mMesh;
    String mMeshString;
    //vec4 mPos;
    float mRotY;
    vec3 mScale;
    boolean mAlive = true;
    vec4 U = new vec4(1,0,0,0);
    vec4 V = new vec4(0,1,0,0);
    vec4 W = new vec4(0,0,1,0);
    int width = 6;
    int height = 2;
    float depth = 0.4f;
    vec3 min,max;
    float mHealth = 20;
    float mYOffset;
    OrientedBoundingBox mBox;
    
    /**
     * Basic Obstacle constructor that calculates the position and the AABB
     * @param mesh The Mesh to use for this obstacle
     * @param position The obstacle's position (Y coordinate is calculated)
     * @param yRot The obstacle's rotation around the Y Axis
     */
    public Obstacle(String meshName, vec4 position, float yOffset)
    {
        //mYOffset = position.y;
        position.y  = (float)noise.eval(position.x/100*4, position.z/100*4) * 10;
        if(position.y < 0)
        {
            position.y = 0;
        }
        position.y += mYOffset;
        mMesh = MeshManager.getInstance().get(meshName);
        mMeshString = meshName;
        mPos = position;
        mRotY = 0.0f;
        mScale = new vec3(1,2,1);
        min = new vec3(mPos.x - width / 2 * mScale.x, mPos.y - height / 2 * mScale.y, mPos.z - depth / 2 * mScale.z);
        max = new vec3(mPos.x + width / 2 * mScale.x, mPos.y + height / 2 * mScale.y, mPos.z + depth / 2 * mScale.z);
        mat4 rot = axisRotation(new vec4(0.0f, 1.0f, 0.0f, 0.0f), mRotY);
        U = mul(U, rot);
        V = mul(V, rot);
        W = mul(W, rot);
        vec4 u = U.mul(width);
        vec4 v = V.mul(height);
        vec4 w = W.mul(depth);
        mBox = new OrientedBoundingBox(mPos.sub(u.mul(.5f)).sub(v.mul(.5f)).sub(w.mul(.5f)), u, v, w); 
        ot = PhysicsBody.ObjectType.OBSTACLE;
    }
    
    public Obstacle(ObstacleInfo oInfo, vec4 position, float yOffset, float yRot)
    {
        //mYOffset = position.y;
        position.y  = (float)noise.eval(position.x/100*4, position.z/100*4) * 10;
        if(position.y < 0)
        {
            position.y = 0;
        }
        position.y += mYOffset;
        mMesh = oInfo.mMesh;
        mMeshString = oInfo.mMeshName;
        mPos = position;
        mRotY = yRot;
        mScale = new vec3(1,2,1);
        min = new vec3(mPos.x - width / 2 * mScale.x, mPos.y - height / 2 * mScale.y, mPos.z - depth / 2 * mScale.z);
        max = new vec3(mPos.x + width / 2 * mScale.x, mPos.y + height / 2 * mScale.y, mPos.z + depth / 2 * mScale.z);
        mat4 rot = axisRotation(new vec4(0.0f, 1.0f, 0.0f, 0.0f), mRotY);
        U = mul(U, rot);
        V = mul(V, rot);
        W = mul(W, rot);
        ot = PhysicsBody.ObjectType.OBSTACLE;
    }
    
    /**
     * Checks the passed sphere against the obstacle's Axis-Aligned Bounding Box
     * @param point Origin of the sphere
     * @param rad Radius of the sphere
     * @return 
     */
    public boolean checkSphereCollision(vec3 point, float rad)
    {
        vec3 closest = new vec3();
        if(point.x < min.x)
            closest.x = min.x;
        else if(point.x > max.x)
            closest.x = max.x;
        else
            closest.x = point.x;
        
        if(point.y < min.y)
            closest.y = min.y;
        else if(point.y > max.y)
            closest.y = max.y;
        else
            closest.y = point.y;
        
        if(point.z < min.z)
            closest.z = min.z;
        else if(point.z > max.z)
            closest.z = max.z;
        else
            closest.z = point.z;
        
        vec3 d = closest.sub(point);
        if(length(d) < rad)
        {
            return true;
        }
        return false;
    }
    
    /**
     * Calculates the damage an animal deals to the obstacle
     * @param vel Animal's velocity
     * @param mDmg Base Damage
     */
     void calculateDamage(vec4 vel, int mDmg)
    {
        double result = length(mul(vel,mDmg));
        mHealth-= result;
        if(mHealth<=0)
            mAlive = false;   
    }
     
     /**
      * Renders the obstacle to the screen
      * @param prog The program to use for rendering.
      */
    public void draw(Program prog)
    {
        prog.setUniform("worldMatrix", mul(scaling(mScale), mul(axisRotation(new vec4(0.0f,1.0f,0.0f,0.0f), mRotY), translation(new vec4(0,mYOffset,0,0)).mul(translation(mPos)))));
        mMesh.draw(prog);
    }
}
