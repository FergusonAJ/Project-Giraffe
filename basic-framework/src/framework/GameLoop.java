package framework;

import Animals.*;
import static JGL.JGL.*;
import JSDL.JSDL;
import static JSDL.JSDL.*;
import TextRendering.*;
import static framework.math3d.math3d.*;
import framework.math3d.vec3;
import framework.math3d.vec4;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import Terrains.*;

public class GameLoop 
{
    //<editor-fold defaultstate="collapsed" desc="Application Variables">
    Program prog;
    Set<Integer> keys = new TreeSet<>();
    long win;
    JSDL.SDL_Event ev;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Initialize Animals, Pins, and Obstacles">
        ArrayList<Animal> animalList = new ArrayList();
        ArrayList<Obstacle> obstacleList = new ArrayList();
        ArrayList<Pin> pinList = new ArrayList();
//</editor-fold> 
    //<editor-fold defaultstate="collapsed" desc="Loop Variables">
    int numLaunches = 0;
    int numHits = 0;
    float prev;
    float elapsed;
    int animalSelected = 0;
    Camera cam = new Camera();
    boolean inConsole = false;
    String consoleText = "";
    int totalPins;
    //implement this when it comes near to controller development
    String gameState;   //GameState should be either:  "Default","Launching","Paused" 
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Mesh variables">
    Mesh rockMesh = MeshManager.getInstance().get("rock");
    Mesh treeMesh = MeshManager.getInstance().get("tree");
    Mesh giraffeMesh = MeshManager.getInstance().get("giraffe");
    Mesh pigMesh = MeshManager.getInstance().get("pig");
    Mesh zomMesh = MeshManager.getInstance().get("zombie");
    Mesh wallMesh = MeshManager.getInstance().get("rockWall");
    Mesh pinMesh = zomMesh;
    Mesh planeMesh = MeshManager.getInstance().get("plane");
    Mesh portalMesh = MeshManager.getInstance().get("portal");
    //Sound sounds = new Sound("assets/audio/2016-02-01-1038-12.wav");
    ImageTexture dummyTex = new ImageTexture("assets/Models/blank.png");
    static Sound sounds = new Sound("assets/Audio/funkbox_music_stuff.wav");
    Water water= new Water(new vec4(0,0,0,1), 0f);
    //Sound sounds = new Sound("assets/audio/trump.wav");
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Fonts">
    Font testFont = null;
    ImageTexture alphabet = new ImageTexture("assets/Fonts/cooperBlack.png");
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Portals and Misc.">

    UnitSquare usq = new UnitSquare();
    vec3 skyColor = new vec3(0.2f,0.4f,0.6f);
    Framebuffer fbo1 = new Framebuffer(512,512);
    Framebuffer fbo2 = new Framebuffer(512,512);
    PortalPair portals;
    //blurprog = new Program("blurvs.txt","blurfs.txt");
//</editor-fold>
    OpenSimplexNoise noise = new OpenSimplexNoise();
    public GameLoop(long w)
    {
        
        win = w;
       
        try {
        testFont = new Font("assets/Fonts/CooperBlack.fnt");
        } 
        catch (IOException ex) 
        {
        Logger.getLogger(StateManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        prog = new Program("vs.txt","fs.txt");
        
        
        if(animalList.size() > 0)
        {
            cam.lookAt( new vec3(0,2,3), animalList.get(animalSelected).mPos.xyz(), new vec3(0,1,0) );
            cam.mFollowTarget = animalList.get(0);
        }
          totalPins = pinList.size();
    }
    protected void genBasic()
    {
        animalList.add(new Pig(pigMesh,new vec4(-30,0,0,1), 3.0f));
        animalList.get(0).flip = true;
        animalList.add(new Cheetah(pigMesh,new vec4(80,0,0,1), 3.0f));
        animalList.add(new Giraffe(giraffeMesh,new vec4(0,0,0,1), 2.0f));
        animalList.add(new Ram(giraffeMesh,new vec4(60,0,0,1), 2.0f));
        
        //animalList.add(new Animal(zomMesh,new vec4(30,1000,0,1), 0.0f));
        
        
        obstacleList.add(new Obstacle(wallMesh, new vec4(0,-1,-20,1), 0.0f));
        obstacleList.add(new Obstacle(treeMesh, new vec4(-30,-2,20,1), 0.0f));
        obstacleList.add(new Obstacle(treeMesh, new vec4(0,-2,-40,1), 0.0f));
        obstacleList.add(new Obstacle(treeMesh, new vec4(30,-2,-40,1), 0.0f));
        
        
        pinList.add(new Pin(pinMesh, new vec4(0,-1,-30,1), 2.0f));
        pinList.add(new Pin(pinMesh, new vec4(30,-1,-30,1), 2.0f));
        pinList.add(new Pin(pinMesh, new vec4(-30,-1,-30,1), 2.0f));
        portals = new PortalPair(portalMesh, new vec4(10,-2,0,1), (float)Math.PI/4);
        if(animalList.size() > 0)
        {
            cam.lookAt( new vec3(0,2,3), animalList.get(animalSelected).mPos.xyz(), new vec3(0,1,0) );
            cam.mFollowTarget = animalList.get(0);
        }
        totalPins = pinList.size();
        cam.follow(animalList.get(animalSelected),false);
    }
    public void runLoop()
    {
        prev = (float)(System.nanoTime()*1E-9);
        ev=new JSDL.SDL_Event();
        while(true)
        {
            HandleEvents();
            float now = (float)(System.nanoTime()*1E-9);
            elapsed = now-prev;
            prev=now;
            UpdateAnimals();
            cam.update();
            UpdatePins();
            CullDeadObjects();
            water.update(elapsed);
            if(pinList.size() <= 0 || animalList.size() <= 0)
            {
                StateManager.getInstance().MainMenu();      
            }
            if(!inConsole)
            {
                HandleInput(); 
            }
            Render();
        }
    }
    private void HandleEvents()
    {
        while(true){
                int rv = SDL_PollEvent(ev);
                if( rv == 0 )
                    break;
                //System.out.println("Event "+ev.type);
                if( ev.type == SDL_QUIT )
                    System.exit(0);
                if( ev.type == SDL_MOUSEMOTION ){
                    //System.out.println("Mouse motion "+ev.motion.x+" "+ev.motion.y+" "+ev.motion.xrel+" "+ev.motion.yrel);
                }
                if( ev.type == SDL_KEYDOWN ){
                    keys.add(ev.key.keysym.sym);
                    if(inConsole)
                    {
                        int id = ev.key.keysym.sym;
                        System.out.println(id);
                        if((id > 96 && id < 123) || id == 32 || (id > 47 && id < 58) || id == 46)//Add a-z and spaces to the console line
                        {
                            consoleText += (char)ev.key.keysym.sym;
                        }
                        if(id == 13)//Enter
                        {
                            parseConsole();
                            inConsole = false;
                        }
                        if(id == 1073741898)//Home?
                        {
                            inConsole = false;
                            keys.remove(1073741898);
                        }
                    }
                }
                if( ev.type == SDL_KEYUP ){
                    keys.remove(ev.key.keysym.sym);
                }
            }
        if(keys.contains(SDLK_ESCAPE))
        {
            System.exit(0);
        }
    }
    private void UpdateAnimals()
    {
        for(Animal a: animalList)
        {
            a.update(elapsed);
            a.mPos.y  = (float)noise.eval(a.mPos.x/100*4, a.mPos.z/100*4) * 10;
            if(a.mPos.y < 0)
            {
                a.mPos.y = 0;
            }
            for(Obstacle o : obstacleList)
            {
               if(o.checkSphereCollision(a.mPos.xyz(), a.mRad))
               {
                   
                   //if animal selected is a ram and special is active then you can dmg obstacles
                   if (a instanceof Ram && a.isSpecialActive) 
                   {
                        o.calculateDamage(a.mVel,a.mDmg);
                   }
                   a.mVel = new vec4();
               }
            }
        }
    }
    private void UpdatePins()
    {
        for (Pin p : pinList)
        {
            p.mPos.y  = (float)noise.eval(p.mPos.x/100*4, p.mPos.z/100*4) * 10;
            if(p.mPos.y < 0)
            {
                p.mPos.y = 0;
            }
            p.checkAnimalPositions(animalList);
            for(Animal a: animalList)
            {
                if(p.checkCollision(a.mPos, a.mRad,a.mMoving, a.mVel,a.mDmg))
                {
                    //if(a.equals(animalList.get(animalSelected)))
                    //    animalSelected = 0;
                    animalList.get(animalSelected).mAlive = false;
                }

                //p.checkAnimalPosition(a.mPos);
            }
            for(Obstacle o : obstacleList)
            {
               if(o.checkSphereCollision(p.mPos.xyz(), p.mRad))
               {
                   p.mVel = new vec4(0,0,0,0);
               }
            }
            p.update(elapsed);
        }
    }
    protected void CullDeadObjects()
    {
        for(int i = 0; i < pinList.size(); i++)
        {
            if(!pinList.get(i).mAlive)
            {
                pinList.remove(i);
                numHits++;
            }
        }
        for(int i = 0; i < animalList.size(); i++)
        {
            if(!animalList.get(i).mAlive)
            {
                getPrevAnimal();
                animalList.remove(i);
            }
        }
        for(Obstacle o : obstacleList)
        {
            if(!o.mAlive)
                obstacleList.remove(obstacleList.indexOf(o));
        }
    }
    private void HandleInput()
    {
        if( keys.contains(SDLK_z))
            {
                getPrevAnimal();
                keys.remove(SDLK_z);
            }
            
            if( keys.contains(SDLK_x))
            {
                animalSelected++;
                if(animalSelected > animalList.size()-1)
                    animalSelected = 0;
                cam.follow(animalList.get(animalSelected),false);
                cam.mFollowTarget = animalList.get(animalSelected);
                if(animalList.get(animalSelected).mMoving)
                {
                    cam.follow(animalList.get(animalSelected), true);
                }
                else
                {
                    cam.mFollowing = false;
                }
                keys.remove(SDLK_x);
            }
                
            if( keys.contains(SDLK_w ))
                cam.walk(3f*elapsed);
            if( keys.contains(SDLK_s))
                cam.walk(-3f*elapsed);
            if( keys.contains(SDLK_a))
            {
                if(!cam.mFollowTarget.mMoving)
                {
                    animalList.get(animalSelected).rotate(2 * elapsed);
                    cam.follow(animalList.get(animalSelected),false);
                }
            }
            if( keys.contains(SDLK_d))
            {
                if(!cam.mFollowTarget.mMoving)
                {
                    animalList.get(animalSelected).rotate(-2 * elapsed);
                    cam.follow(animalList.get(animalSelected), false);
                }
            }
            if( keys.contains(SDLK_r))
                cam.tilt(0.4f*elapsed);
            if( keys.contains(SDLK_t))
                cam.tilt(-0.4f*elapsed);
            if( keys.contains(SDLK_SPACE))
            {
                if(!cam.mFollowTarget.mMoving)
                {
                    animalList.get(animalSelected).takeoff();
                    cam.follow(animalList.get(animalSelected), true);
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
    }
    private void getPrevAnimal()
    {
        animalSelected--;
        if(animalSelected < 0)
            animalSelected = animalList.size()-1;
        cam.follow(animalList.get(animalSelected),false);
        cam.mFollowTarget = animalList.get(animalSelected);
        if(animalList.get(animalSelected).mMoving)
        {
            cam.follow(animalList.get(animalSelected), true);
        }
        else
        {
            cam.mFollowing = false;
        }
    }
    private void parseConsole()
    {
        String[] parts = consoleText.split("\\s");
        //System.out.println(parts.length);
        for(int i = 0; i < parts.length; i++)
        {
            System.out.println(parts[i] + " " + i);
        }
        switch(parts[0])
        {
            case "spawn":
                if (parts[1].equals("pig"))
                {
                    spawnAnimal(pigMesh, parts);
                }
                if (parts[1].equals("giraffe"))
                {
                    spawnAnimal(giraffeMesh, parts);
                }
                break;
        }
    }
    private void spawnAnimal(Mesh m, String[] s)
    {
        if (s == null || s.length <= 2)
        {
            animalList.add(new Animal(m, new vec4(0,0,0,1), 3.0f));
        }
        else
        {
            if(s[2].equals("here") || s[2].equals("me"))
            {
                if(s.length != 6)
                {
                    animalList.add(new Animal(m, animalList.get(animalSelected).mPos, 3.0f));
                }
                else
                {
                    vec4 tempVec = new vec4(animalList.get(animalSelected).mPos);
                    tempVec.x += Float.parseFloat(s[3]);
                    tempVec.y += Float.parseFloat(s[4]);
                    tempVec.z += Float.parseFloat(s[5]);
                    animalList.add(new Animal(m, tempVec, 3.0f));
                }
            }
            else if(s.length == 5)
            {
                vec4 tempVec = new vec4(0,0,0,0);
                tempVec.x += Float.parseFloat(s[2]);
                tempVec.y += Float.parseFloat(s[3]);
                tempVec.z += Float.parseFloat(s[4]);
                animalList.add(new Animal(m, tempVec, 3.0f));
            }
        }
    }
    private void DrawWorld(Camera c)
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        prog.setUniform("mode",0.1);
        prog.setUniform("skyColor",skyColor);
        prog.setUniform("lights[0].position",new vec3(cam.eye.x, cam.eye.y, cam.eye.z));
        prog.setUniform("lights[0].color",new vec3(1,1,1));
        for(int i =1;i<8;i++)
        {
            prog.setUniform("lights["+i+"].position",new vec3(0,0,0));
            prog.setUniform("lights["+i+"].color",new vec3(0,0,0));
        }
        c.draw(prog);
        prog.setUniform("worldMatrix", mul(scaling(new vec3(100,1,100)),translation(new vec3(0,-1.0f,0))));
        planeMesh.draw(prog);
        for(Animal a: animalList)
        {
            a.draw(prog);
        }
        for(Pin p : pinList)
        {
            p.draw(prog);
        }
        for(Obstacle o : obstacleList)
        {
            o.draw(prog);
        }
        water.draw(prog);
        prog.setUniform("diffuse_texture",alphabet);
        prog.setUniform("unitSquare", 1.0f);
        prog.setUniform("unitSquare", 0.0f);
    }
    private void Render()
    {
        DrawableString scoreText = new DrawableString("Enemies hit: " + numHits, 10, 20, 20, testFont);
        DrawableString launchText = new DrawableString("Launches: " + numLaunches, 10, 40, 20, testFont);
        DrawableString console = null;
        if(inConsole)
        {
            console = new DrawableString(consoleText, 10, 1040, 20, testFont);
        }
        prog.use();
        if(portals != null)
        {
            fbo1.bind();
            vec4 temp = portals.getPortal1Pos().sub(cam.eye);
            vec3 tempVec3 = new vec3(temp.x, 0, temp.z);
            //portals.cam1LookAt(tempVec3.neg());
            DrawWorld(portals.getCam1());
            fbo1.unbind();
            fbo2.bind();
            //portals.cam2LookAt(tempVec3);
            DrawWorld(portals.getCam2());
            fbo2.unbind();
        }
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        prog.setUniform("mode",0.1);
        prog.setUniform("skyColor",skyColor);
        prog.setUniform("lights[0].position",new vec3(cam.eye.x, cam.eye.y, cam.eye.z));
        prog.setUniform("lights[0].color",new vec3(1,1,1));
        for(int i =1;i<8;i++)
        {
            prog.setUniform("lights["+i+"].position",new vec3(0,0,0));
            prog.setUniform("lights["+i+"].color",new vec3(0,0,0));
        }
        cam.draw(prog);
        prog.setUniform("worldMatrix", mul(scaling(new vec3(100,1,100)),translation(new vec3(0,-1.0f,0))));
        planeMesh.draw(prog);
        for(Animal a: animalList)
        {
            a.draw(prog);
        }
        for(Pin p : pinList)
        {
            p.draw(prog);
        }
        for(Obstacle o : obstacleList)
        {
            o.draw(prog);
        }
        if(portals != null)
        {
            portals.draw(prog, fbo1, fbo2);
        }
        water.draw(prog);
        //fbo1.unbind();

        //this is also for later...
/*
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        blurprog.use();
        blurprog.setUniform("diffuse_texture",fbo1.texture);
        usq.draw(blurprog);
        blurprog.setUniform("diffuse_texture",dummytex);*/
        prog.setUniform("diffuse_texture",alphabet);
        prog.setUniform("unitSquare", 1.0f);
        scoreText.draw(prog);
        launchText.draw(prog);
        if(inConsole)
        {
            console.draw(prog);
        }
        prog.setUniform("unitSquare", 0.0f);
        prog.setUniform("diffuse_texture", dummyTex);
        SDL_GL_SwapWindow(win);
    }
}
