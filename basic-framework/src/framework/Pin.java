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
import static framework.math3d.math3d.length;
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
        double dist = length(sub(mPos, pos));
        if(dist < mRad + rad2)
        {
            mAlive = false;
        }
    }
    
    void takeoff()
    {
        mVel = mul(new vec4(1,0,0,0), axisRotation(new vec4(0,1,0,0), mRotY));
        mVel = mul(mVel, 5);
        mMoving = true;
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
