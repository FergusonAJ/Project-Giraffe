package framework;

import framework.math3d.mat4;
import framework.math3d.vec4;
import static framework.math3d.math3d.*;
import framework.math3d.vec3;
public class Obstacle 
{
    static OpenSimplexNoise noise = new OpenSimplexNoise();
    Mesh mMesh;
    vec4 mPos;
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
    
    public Obstacle(Mesh mesh, vec4 position, float yRot)
    {
        float yOffset = position.y;
        position.y  = (float)noise.eval(position.x/100*4, position.z/100*4) * 10;
        if(position.y < 0)
        {
            position.y = 0;
        }
        position.y += yOffset;
        mMesh = mesh;
        mPos = position;
        mRotY = yRot;
        mScale = new vec3(1,2,1);
        min = new vec3(mPos.x - width / 2 * mScale.x, mPos.y - height / 2 * mScale.y, mPos.z - depth / 2 * mScale.z);
        max = new vec3(mPos.x + width / 2 * mScale.x, mPos.y + height / 2 * mScale.y, mPos.z + depth / 2 * mScale.z);
        mat4 rot = axisRotation(new vec4(0.0f, 1.0f, 0.0f, 0.0f), mRotY);
        U = mul(U, rot);
        V = mul(V, rot);
        W = mul(W, rot);
    }
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
    
     void calculateDamage(vec4 vel, int mDmg)
    {
        double result = length(mul(vel,mDmg));
        mHealth-= result;
        if(mHealth<=0)
            mAlive = false;
        
    }
     
    public void draw(Program prog)
    {
        prog.setUniform("worldMatrix", mul(scaling(mScale), mul(axisRotation(new vec4(0.0f,1.0f,0.0f,0.0f), mRotY), translation(mPos))));
        mMesh.draw(prog);
    }
}
