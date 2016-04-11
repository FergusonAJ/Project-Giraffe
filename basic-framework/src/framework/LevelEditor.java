/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import Animals.Pig;
import static JSDL.JSDL.*;
import static framework.math3d.math3d.add;
import static framework.math3d.math3d.mul;
import framework.math3d.vec3;
import framework.math3d.vec4;

/**
 *
 * @author ajart
 */
public class LevelEditor extends GameLoop
{
    
    public LevelEditor(long w) 
    {
        super(w);
        animalList.add(new Pig(new vec4(-30,0,0,1), (float)Math.PI / 2));
        animalList.get(0).flip = true;
        cam.lookAt( new vec3(0,2,3), animalList.get(animalSelected).mPos.xyz(), new vec3(0,1,0) );
        cam.mFollowTarget = animalList.get(0);
        animalSelected = 0;
        animalList.get(animalSelected).mMoving = true;
        cam.follow(animalList.get(animalSelected),true);
    }
    
    @Override
    protected void UpdateAnimals()
    {
        for(Animal a: animalList)
        {
            a.mPos.y  = (float)noise.eval(a.mPos.x/100*4, a.mPos.z/100*4) * 10;
            if(a.mPos.y < 0)
            {
                a.mPos.y = 0;
            }
        }
    }
    
    @Override
    protected void HandleInput()
    {
        animalList.get(animalSelected).mMoving = true;
        if( keys.contains(SDLK_x))
        {
        }

        if( keys.contains(SDLK_w ))
        {
            animalList.get(animalSelected).mPos.z -= 0.5f;
        }
        if( keys.contains(SDLK_s))
        {
            animalList.get(animalSelected).mPos.z += 0.5f;
        }
        if( keys.contains(SDLK_a))
        {
            animalList.get(animalSelected).mPos.x -= 0.5f;
        }
        if( keys.contains(SDLK_d))
        {
            animalList.get(animalSelected).mPos.x += 0.5f;
        }
        if( keys.contains(SDLK_r))
            cam.tilt(0.4f*elapsed);
        if( keys.contains(SDLK_t))
            cam.tilt(-0.4f*elapsed);
        if( keys.contains(SDLK_SPACE))
        {
            //if the animal is currently being launched then you can activate your special ability
            if(cam.mFollowTarget.mMoving)
            {
                animalList.get(animalSelected).specialAbility();
            }
            //if the animal is first launched then do launch logic
            if(!cam.mFollowTarget.mMoving)
            {
                animalList.get(animalSelected).takeoff();
                animalList.get(animalSelected).resetSpecialAbility();
                cam.follow(animalList.get(animalSelected), true);
                float rot = (float)animalList.get(animalSelected).mRotY;
                for(int i=0;i<animalList.size();i++)
                {

                    if(animalList.get(i).isInStampede)
                    {
                        animalList.get(i).stampedeTakeoff(rot);
                        //this is just to disable stuff for later
                        animalList.get(i).isInStampede = false;
                    }
                }
                numLaunches++;
            }

            keys.remove(SDLK_SPACE);
        }
        if(keys.contains(SDLK_RETURN))
        {
            //System.out.println("Number of launches: " + numLaunches);
            //System.out.println("Number of hits: " + numHits);
            keys.remove(SDLK_RETURN);
        }
        if(keys.contains(SDLK_BACKQUOTE))
        {
            inConsole = !inConsole;
            consoleText = "";
        }
    if(keys.contains(SDLK_o))
    {
            saveFile("Test.txt");
    }
    if(keys.contains(SDLK_p))
    {
        loadFile("Test.txt");
        keys.remove(SDLK_p);
    }
    if(keys.contains(SDLK_LSHIFT)||keys.contains(SDLK_RSHIFT))
        {
            int sizeStampede = 1;
            vec4 stampedeDirection = cam.mFollowTarget.getDirection();
            stampedeActive = !stampedeActive;

            if(stampedeActive)
            {
                for(int i=0;i<animalList.size();i++)
                {
                    if(!cam.mFollowTarget.mMoving)
                    {
                        double rotation = cam.mFollowTarget.mRotY;
                        if(cam.mFollowTarget.canStampede(animalList.get(i).mPos) && !cam.mFollowTarget.equals(animalList.get(i)))
                        {
                            animalList.get(i).prevPos = animalList.get(i).mPos;
                            animalList.get(i).mPos = new vec4(add(cam.mFollowTarget.mPos,mul(stampedeDirection,sizeStampede*4)));
                            animalList.get(i).isInStampede = true;
                            animalList.get(i).mRotY = rotation;
                            sizeStampede++;
                        }
                        else if(i == animalSelected)
                        {
                            animalList.get(i).prevPos = animalList.get(i).mPos;
                        }
                    }
                }
            }
            else
            {
                for(int i=0;i<animalList.size();i++)
                {

                    animalList.get(i).mPos = animalList.get(i).prevPos;
                }
            }
            if(keys.contains(SDLK_LSHIFT))
            keys.remove(SDLK_LSHIFT);
            if(keys.contains(SDLK_RSHIFT))
            keys.remove(SDLK_RSHIFT);
        }
        if(keys.contains(SDLK_BACKSPACE))
        {
            isPaused = !isPaused;
            keys.remove(SDLK_BACKSPACE);
        }
            
    }
    
}
