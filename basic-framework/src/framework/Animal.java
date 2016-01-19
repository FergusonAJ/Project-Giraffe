/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import framework.math3d.mat4;
import static framework.math3d.math3d.add;
import static framework.math3d.math3d.mul;
import static framework.math3d.math3d.translation;
import static framework.math3d.math3d.axisRotation;
import framework.math3d.vec4;

/**
 *
 * @author ajart
 */
public class Animal 
{
    Mesh mMesh;
    String mName;
    vec4 mPos = new vec4(0,0,0,1);
    double mRotY = Math.PI / 2;
    boolean mMoving = false;
    vec4 mVel;
    vec4 mForward;
    float mRad = 1.5f;
    public Animal(Mesh mesh)
    {
        mMesh = mesh;
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
        prog.setUniform("worldMatrix", mul(axisRotation(new vec4(0.0f,1.0f,0.0f,0.0f), mRotY), translation(mPos)));
        mMesh.draw(prog);
    }
}
