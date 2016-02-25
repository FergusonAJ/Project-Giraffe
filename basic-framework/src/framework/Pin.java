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
 * Very simple pin class.
 * Able to store, move, and draw a pin.
 * Can chase the player and check hit detection.
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
    float mHealth = 10;
    boolean mIsStatic = false;
    
    /**
     * Basic constructor
     * @param mesh The mesh this Pin will use
     * @param position The pin's starting position
     * @param yOffset A scalar to translate the pin up the y axis.
     */
    public Pin(Mesh mesh, vec4 position, float yOffset)
    {
        mMesh = mesh;
        mPos = position;
        mYOffset = yOffset;
    }
    
    /**
     * 
     * Basic constructor
     * @param mesh The mesh this Pin will use
     * @param position The pin's starting position
     * @param yOffset A scalar to translate the pin up the y axis.
     * @param isStatic If true, the pin will not move
     */
    public Pin(Mesh mesh, vec4 position, float yOffset, boolean isStatic)
    {
        mMesh = mesh;
        mPos = position;
        mYOffset = yOffset;
        mIsStatic = isStatic;
    }
    
    /**
     * Moves the pin if necessary
     * @param elapsed Time since last frame
     */
    public void update(float elapsed)
    {
        if(mMoving)
        {
            super.update(elapsed);
        }
    }
    
    /**
     * Rotates the pin around the y axis
     * @param angle The amount of rotation (In radians)
     */
    void rotate(double angle)
    {
        if(!mMoving)
        {
            mRotY += angle;
        }
    }
    
    /**
     * Checks to see if the pin collides with the given animal
     * Uses sphere to sphere collision detection for now
     * @param pos Position of the animal
     * @param rad2 Radius of the animal
     * @param animalMoving Whether or not the animal is moving
     * @param vel The animal's velocity
     * @param dmg Base damage of the animal
     * @return 
     */
    boolean checkCollision(vec4 pos, float rad2, boolean animalMoving, vec4 vel, int dmg)
    {
        double dist = length(sub(pos, mPos));
        if(dist < mRad + rad2)
        {
            if (animalMoving)
            {
                calculateDamage(vel, dmg);
                if (mHealth<=0)
                    mAlive = false;
            }
            else
            {
                return true;
            }
        }
        return false;   
    }
    
    /**
     * Calculates the damage done to the pin by the indicated velocity
     * @param vel The velocity used to calculate the damage
     * @param mDmg The base damage of the animal
     */
    void calculateDamage(vec4 vel, int mDmg)
    {
        double result = length(mul(vel,mDmg));
        mHealth-= result;
    }
    
    /**
     * Sets the pin's velocity to be pointing in the direction of the given animal
     * @param animalPos Animal position to launch towards
     */
    void takeoff(vec4 animalPos)
    {
        mVel = normalize(sub(animalPos, mPos));
        mVel = mul(mVel, 5);
        mMoving = true;
    }
    
    /**
     * Checks to see if the given animal's position is within the awareness radius of the pin
     * @param animalPos Position of the animal to check
     */
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
    
    /**
     * Checks an ArrayList of animals to see if any are within the awareness radius of the pin
     * @param aList The list of animals to check
     */
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
    
    /**
     * Renders the pin to the screen
     * @param prog The program to use for rendering
     */
    public void draw(Program prog)
    {
        if(mAlive)
        {
            prog.setUniform("worldMatrix", mul(mul(mul(scaling(mScale),axisRotation(new vec4(0.0f,1.0f,0.0f,0.0f), mRotY), translation(mPos), translation(new vec3(0,mYOffset, 0))))));
            mMesh.draw(prog);
        }
    }
}
