package framework;

import Animals.*;
import framework.math3d.*;

/**
 * MainMenu refers to a GameLoop that has two signs, one to start the game and one to exit
 */
public class MainMenu extends GameLoop
{
    Mesh signMesh, sign2Mesh;
    Pin startSign, exitSign;
    
    /**
     * Creates a new Main Menu, and initializes a pig and two signs.
     * @param w The long that OpenGL assigned to the current window
     */
    public MainMenu(long w) 
    {
        super(w);
        sounds.play();
        signMesh = MeshManager.getInstance().get("sign1");
        sign2Mesh = MeshManager.getInstance().get("sign2");
        animalList.add(new Pig(new vec4(-30,0,0,1), (float)Math.PI / 2));
        animalList.get(0).flip = true;
        startSign = new Pin("sign1", new vec4(-30,0,-20,1), 0, true);
        exitSign = new Pin("sign2", new vec4(-20,0,-20,1), 0, true);
        pinList.add(startSign);
        pinList.add(exitSign);
        startSign.mScale = new vec3(1,5,3);
        exitSign.mScale = new vec3(1,5,3);
        cam.lookAt( new vec3(0,2,3), animalList.get(animalSelected).mPos.xyz(), new vec3(0,1,0) );
        cam.mFollowTarget = animalList.get(0);
        animalSelected = 0;
        cam.follow(animalList.get(animalSelected),false);
    }

    
    /**
     * Either starts or exits the game depending on which sign was hit
     */
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
