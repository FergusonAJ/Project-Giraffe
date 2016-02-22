package framework;

import Animals.*;
import framework.math3d.*;

public class MainMenu extends GameLoop
{
    Mesh signMesh, sign2Mesh;
    Pin startSign, exitSign;
    
    public MainMenu(long w) 
    {
        super(w);
        sounds.play();
        signMesh = MeshManager.getInstance().get("sign1");
        sign2Mesh = MeshManager.getInstance().get("sign2");
        animalList.add(new Pig(pigMesh,new vec4(-30,0,0,1), 3.0f));
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
            StateManager.getInstance().NewLoop(true);
        }
        if(!exitSign.mAlive)
        {
            System.exit(0);
        }
    }
}
