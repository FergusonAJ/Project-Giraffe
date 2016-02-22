/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import framework.Mesh;
import framework.Mesh;
import framework.Obstacle;
import framework.Obstacle;
import framework.PhysicsBody;
import framework.PhysicsBody;
import framework.Program;
import framework.Program;
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
    protected Mesh mMesh;
    
    protected double mRotY = Math.PI / 2;
    protected boolean mMoving = false;
    
    protected vec4 mForward;
    protected float mYOffset;
    protected float mRad = 1.5f;
    protected boolean mAlive = true;
    protected boolean flip = false;
    protected int mDmg;
    protected float mHealth;
    
    
    //variables purely for special abilities
    protected boolean isSpecialActive;
    protected boolean usedSpecial;
    protected float specialTimer;
    
    public Animal(Mesh mesh, vec4 position, float yOffset)
    {
        mMesh = mesh;
        mPos = position;
        mYOffset = yOffset;
    }
    
    @Override
    public void update(float elapsed)
    {
        if(mMoving)
        {
            //mPos = add(mPos, mul(mVel,elapsed));
            super.update(elapsed);
            mVel = sub(mVel,mul(mVel, 0.5f * elapsed));
            if(length(mVel) < 1f)
            {
                mMoving = false;
                mVel = new vec4(0,0,0,0);
                resetSpecialAbility();
            }
        }
        if(isSpecialActive)
        {
            specialTimer-=elapsed;
        }
    }
    void checkObstacleCollision(Obstacle o)
    {
    }
    protected void rotate(double angle)
    {
        if(!mMoving)
        {
            mRotY += angle;
            mForward = mul(new vec4(0,0,-1,0), axisRotation(new vec4(0,1,0,0), mRotY));
        }
    }
    
    public void specialAbility()
    {
        isSpecialActive = true;
        usedSpecial = true;
    }
    
    protected void resetSpecialAbility()
    {
        isSpecialActive = false;
        usedSpecial = false;
    }
    
    
    
    protected void takeoff()
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
