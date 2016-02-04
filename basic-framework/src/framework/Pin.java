/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import static framework.math3d.math3d.*;
import framework.math3d.vec4;
import framework.math3d.vec3;
import java.util.ArrayList;

/**
 *
 * @author ajart
 */
public class Pin extends PhysicsBody
{
    Mesh mMesh;
    String mName;
    
    double mRotY = Math.PI / 2 * 3;
    boolean mMoving = false;
    vec3 mScale = new vec3(1,1,1);
    float mYOffset;
    float mRad = 1.5f;
    boolean mAlive = true;
    boolean mIsStatic = false;
    
    public Pin(Mesh mesh, vec4 position, float yOffset)
    {
        mMesh = mesh;
        mPos = position;
        mYOffset = yOffset;
    }
    public Pin(Mesh mesh, vec4 position, float yOffset, boolean isStatic)
    {
        mMesh = mesh;
        mPos = position;
        mYOffset = yOffset;
        mIsStatic = isStatic;
    }
    public void update(float elapsed)
    {
        if(mMoving)
        {
            super.update(elapsed);
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
        if(!mIsStatic && mVel != null)
        {
            float dist = length(sub(mPos,animalPos));
            if(dist<=30f)
            {
                takeoff(animalPos);
            }
        }
    }
    
    void checkAnimalPositions(ArrayList<Animal> aList)
    {
        if(!mIsStatic && mVel != null)
        {
            float minDist = 10000.0f;
            vec4 targetPos = null;
            for(Animal a : aList)
            {
                float dist = length(sub(mPos,a.mPos));
                if(dist<=30f)
                {
                    if(dist < minDist)
                    {
                        minDist = dist;
                        targetPos = a.mPos;
                    }
                }
            }
            if(targetPos != null)
            {
                takeoff(targetPos);
            }
        }
    }
    
    public void draw(Program prog)
    {
        if(mAlive)
        {
            prog.setUniform("worldMatrix", mul(mul(mul(scaling(mScale),axisRotation(new vec4(0.0f,1.0f,0.0f,0.0f), mRotY), translation(mPos), translation(new vec3(0,mYOffset, 0))))));
            mMesh.draw(prog);
        }
    }
}
