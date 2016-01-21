/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import static framework.math3d.math3d.*;
import framework.math3d.vec4;
import framework.math3d.vec3;

/**
 *
 * @author ajart
 */
public class Pin 
{
    Mesh mMesh;
    String mName;
    vec4 mPos = new vec4(0,2,-30,1);
    double mRotY = Math.PI / 2 * 3;
    boolean mMoving = false;
    vec4 mVel;
    float mYOffset;
    float mRad = 3;
    boolean mAlive = true;
    
    public Pin(Mesh mesh, vec4 position, float yOffset)
    {
        mMesh = mesh;
        mPos = position;
        mYOffset = yOffset;
    }
    
    public void update(float elapsed)
    {
        if(mMoving)
        {
            mPos = add(mPos, mul(mVel,elapsed));
        }
            
    }
    
    void rotate(double angle)
    {
        if(!mMoving)
        {
            mRotY += angle;
        }
    }
    
    boolean checkCollision(vec4 pos, float rad2, boolean animalMoving)
    {
        double dist = length(sub(pos, mPos));
        if(dist < mRad + rad2)
        {
            if (animalMoving)
                mAlive = false;
            else
                return true;
        }
        
        
        return false;
        
    }
    
    void takeoff(vec4 animalPos)
    {
        mVel = normalize(sub(animalPos, mPos));
        mVel = mul(mVel, 5);
        mMoving = true;
    }
    
    void checkAnimalPosition(vec4 animalPos)
    {
        float dist = length(sub(mPos,animalPos));

        if(dist<=30f)
        {

            takeoff(animalPos);

        }
    }
    
    
    public void draw(Program prog)
    {
        if(mAlive)
        {
            prog.setUniform("worldMatrix", mul(mul(axisRotation(new vec4(0.0f,1.0f,0.0f,0.0f), mRotY), translation(mPos), translation(new vec3(0,mYOffset, 0)))));
            mMesh.draw(prog);
        }
    }
}
