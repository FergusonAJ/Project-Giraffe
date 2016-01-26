/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import framework.math3d.mat4;
import framework.math3d.vec4;
import static framework.math3d.math3d.*;
import framework.math3d.vec3;
public class Obstacle 
{
    Mesh mMesh;
    vec4 mPos;
    float mRotY;
    vec3 mScale;
    vec4 collisionCorner;
    vec4 U = new vec4(1,0,0);
    vec4 V = new vec4(0,1,0);
    vec4 W = new vec4(0,0,1);
    
    public Obstacle(Mesh mesh, vec4 position, float yRot)
    {
        mMesh = mesh;
        mPos = position;
        mRotY = yRot;
        mScale = new vec3(1,2,1);
        collisionCorner = mPos.add(new vec4(mScale.x * 2, mScale.y, mScale.z * 0.2f, 0));
        mat4 rot = axisRotation(new vec4(0.0f, 1.0f, 0.0f, 0.0f), mRotY);
        U = mul(U, rot);
        V = mul(V, rot);
        W = mul(W, rot);
        vec4 midX = mul(U, dot(U, collisionCorner));
        vec4 midY = mul(V, dot(V, collisionCorner));
        vec4 midZ = mul(W, dot(W, collisionCorner));
    }
    public void draw(Program prog)
    {
        prog.setUniform("worldMatrix", mul(scaling(mScale), mul(axisRotation(new vec4(0.0f,1.0f,0.0f,0.0f), mRotY), translation(mPos))));
        mMesh.draw(prog);
    }
}
