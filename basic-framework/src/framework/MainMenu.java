/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import framework.math3d.*;

/**
 *
 * @author ajart
 */
public class MainMenu extends GameLoop
{
    Mesh signMesh, sign2Mesh;
    Pin startSign, exitSign;
    
    public MainMenu(long w) 
    {
        super(w);
        signMesh = new Mesh("assets/sign1.obj.mesh");
        sign2Mesh = new Mesh("assets/sign2.obj.mesh");
        animalList.add(new Animal(pigMesh,new vec4(-30,0,0,1), 3.0f));
        animalList.get(0).flip = true;
        startSign = new Pin(signMesh, new vec4(-30,0,-20,1), 0, true);
        exitSign = new Pin(sign2Mesh, new vec4(-20,0,-20,1), 0, true);
        pinList.add(startSign);
        pinList.add(exitSign);
        startSign.mScale = new vec3(1,5,3);
        exitSign.mScale = new vec3(1,5,3);
        cam.lookAt( new vec3(0,2,3), animalList.get(animalSelected).mPos.xyz(), new vec3(0,1,0) );
        cam.mFollowTarget = animalList.get(0);
        animalSelected = 0;
        cam.follow(animalList.get(animalSelected),false);
    }

    
    protected void CullDeadObjects()
    {
        if(!startSign.mAlive)
        {
            Main.runLoop(win, true);
        }
        if(!exitSign.mAlive)
        {
            System.exit(0);
        }
    }
}
