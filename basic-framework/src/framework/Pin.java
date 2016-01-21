/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import static framework.math3d.math3d.add;
import static framework.math3d.math3d.sub;
import static framework.math3d.math3d.mul;
import static framework.math3d.math3d.translation;
import static framework.math3d.math3d.axisRotation;
import static framework.math3d.math3d.dot;
import static framework.math3d.math3d.length;
import static framework.math3d.math3d.normalize;
import framework.math3d.vec4;

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
    float mRad = 3;
    boolean mAlive = true;
    
    public Pin(Mesh mesh, vec4 position)
    {
        mMesh = mesh;
        mPos = position;
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
    
    void checkCollision(vec4 pos, float rad2)
    {
        double dist = length(sub(pos, mPos));
        if(dist < mRad + rad2)
        {
            mAlive = false;
        }
    }
    
    void takeoff(vec4 animalPos)
    {
        mVel = normalize(sub(animalPos, mPos));
        mVel = mul(mVel, 2);
        mMoving = true;
    }
    
    void checkAnimalPosition(vec4 animalPos)
    {
        float dist = length(sub(mPos,animalPos));
        System.out.println(dist);

        if(dist<=25f)
        {

            takeoff(animalPos);

        }
    }
    
    
    public void draw(Program prog)
    {
        if(mAlive)
        {
            prog.setUniform("worldMatrix", mul(axisRotation(new vec4(0.0f,1.0f,0.0f,0.0f), mRotY), translation(mPos)));
            mMesh.draw(prog);
        }
    }
}
