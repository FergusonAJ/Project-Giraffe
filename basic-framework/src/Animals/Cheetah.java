/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Animals;

import framework.Animal;
import framework.Mesh;
import static framework.math3d.math3d.axisRotation;
import static framework.math3d.math3d.mul;
import framework.math3d.vec4;

/**
 *
 * @author Andrew Polanco
 */
public class Cheetah extends Animal{

    
    public Cheetah(Mesh mesh, vec4 position, float yOffset) {
        super(mesh, position, yOffset);
        mDmg = 30;
        
    }
    
    protected void takeoff()
    {
        mVel = mul(new vec4(1,0,0,0), axisRotation(new vec4(0,1,0,0), mRotY));
        mVel = mul(mVel, 100);
        mMoving = true;
        
    }
    
}
