/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import framework.math3d.mat4;
import static framework.math3d.math3d.*;
import framework.math3d.vec4;
import framework.math3d.vec3;

/**
 *
 * @author ajart
 */
public class Animal extends PhysicsBody
{
    Mesh mMesh;
    String mName;
    
    double mRotY = Math.PI / 2;
    boolean mMoving = false;
    
    vec4 mForward;
    float mYOffset;
    float mRad = 1.5f;
    boolean mAlive = true;
    
    public Animal(Mesh mesh, vec4 position, float yOffset)
    {
        mMesh = mesh;
        mPos = position;
        mYOffset = yOffset;
    }
    
    public void update(float elapsed)
    {
        if(mMoving)
        {
            //mPos = add(mPos, mul(mVel,elapsed));
            super.update(elapsed);
            mVel = sub(mVel,mul(mVel, 0.5f * elapsed));
            if(length(mVel) < 0.7f)
            {
                mMoving = false;
                mVel = new vec4(0,0,0,0);
            }
        }
    }
    void checkObstacleCollision(Obstacle o)
    {
    }
    void rotate(double angle)
    {
        if(!mMoving)
        {
            mRotY += angle;
            mForward = mul(new vec4(0,0,-1,0), axisRotation(new vec4(0,1,0,0), mRotY));
        }
    }
    
    void takeoff()
    {
        mVel = mul(new vec4(1,0,0,0), axisRotation(new vec4(0,1,0,0), mRotY));
        mVel = mul(mVel, 25);
        mMoving = true;
    }
    
    public void draw(Program prog)
    {
        prog.setUniform("worldMatrix", mul(mul(axisRotation(new vec4(0.0f,1.0f,0.0f,0.0f), mRotY), translation(mPos)), translation(new vec3(0,mYOffset, 0))));
        mMesh.draw(prog);
    }
}
