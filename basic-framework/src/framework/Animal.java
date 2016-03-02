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
 * Basic Animal class which each individual animal should extend in order to add their own functionality.
 */
public class Animal extends PhysicsBody
{
    //<editor-fold defaultstate="collapsed" desc="Variables">
    
    protected Mesh mMesh;
    protected double mRotY = Math.PI / 2;
    protected boolean mMoving = false;
  
    
    protected vec4 mForward;
    protected float mYOffset;
    protected float mRad = 1.5f;
    protected boolean mAlive = true;
    //Used for debugging, if the model is facing the wrong direction
    protected boolean flip = false;
    protected int mDmg;
    protected float mHealth;
    
    
    //Variables purely for special abilities
    protected boolean isSpecialActive;
    protected boolean usedSpecial;
    protected float specialTimer;
//</editor-fold>
    
    /**
     * Creates a simple animal
     * @param mesh The mesh this animal will use
     * @param position The initial position of this animal
     * @param yOffset A translation along the y axis to put the animal on its feet
     */
    public Animal(Mesh mesh, vec4 position, float yOffset)
    {
        mMesh = mesh;
        mPos = position;
        mYOffset = yOffset;
    }
    
    /**
     * Moves the animal and checks the special ability
     * @param elapsed 
     */
    @Override
    public void update(float elapsed)
    {
        if(mMoving)
        {
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
    
    /**
     * Rotates the animal around the y axis
     * @param angle The amount to rotate (In Radians)
     */
    protected void rotate(double angle)
    {
        if(!mMoving)
        {
            mRotY += angle;
            mForward = mul(new vec4(0,0,-1,0), axisRotation(new vec4(0,1,0,0), mRotY));
        }
    }
    
    /** 
     * Launches the animal's special ability
     */
    public void specialAbility()
    {
        isSpecialActive = true;
        usedSpecial = true;
    }
    
    /**
     * Resets the animal's special ability
     */
    protected void resetSpecialAbility()
    {
        isSpecialActive = false;
        usedSpecial = false;
    }
    
    /**
     * Launches the animal in the direction it is facing
     */
    protected void takeoff()
    {
        mVel = mul(new vec4(1,0,0,0), axisRotation(new vec4(0,1,0,0), mRotY));
        mVel = mul(mVel, 25);
        mMoving = true;
    }
    
    /**
     * Renders the animal to the screen
     * @param prog The Program to use for drawing
     */
    public void draw(Program prog)
    {
        prog.setUniform("worldMatrix", mul(mul(axisRotation(new vec4(0.0f,1.0f,0.0f,0.0f), mRotY), translation(mPos)), translation(new vec3(0,mYOffset, 0))));
        mMesh.draw(prog);
    }
    
}
