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
    Mesh signMesh;
    Pin startSign;
    
    public MainMenu(long w) 
    {
        super(w);
        signMesh = new Mesh("assets/sign1.obj.mesh");
        animalList.add(new Animal(pigMesh,new vec4(-30,0,0,1), 3.0f));
        startSign = new Pin(signMesh, new vec4(-30,0,-20,1), 0, true);
        pinList.add(startSign);
        pinList.get(0).mScale = new vec3(1,5,3);
        cam.lookAt( new vec3(0,2,3), animalList.get(animalSelected).mPos.xyz(), new vec3(0,1,0) );
        cam.mFollowTarget = animalList.get(0);
    }

    
    protected void CullDeadObjects()
    {
        if(!startSign.mAlive)
        {
            GameLoop mainGame = new GameLoop(win);
            mainGame.genBasic();
            mainGame.runLoop();
        }
    }
}
