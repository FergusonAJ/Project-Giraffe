package framework;

import Animals.*;
import Collisions.HashSet;
import Collisions.LinkedList;
import Collisions.LinkedListIterator;
import Collisions.QuadTreeSolver;
import static JGL.JGL.*;
import JSDL.JSDL;
import static JSDL.JSDL.*;
import Pins.Melt;
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
import framework.Animal.AnimalType;
import framework.PhysicsBody.ObjectType;
import framework.Pin.PinType;
import framework.math3d.mat4;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.stream.Stream;

public class GameLoop 
{
    //<editor-fold defaultstate="collapsed" desc="Application Variables">
    Program prog, shadowProg;
    Set<Integer> keys = new TreeSet<>();
    long win;
    JSDL.SDL_Event ev;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Initialize Animals, Pins, and Obstacles">
        ArrayList<ArrayList> bowlingList = new ArrayList();
        ArrayList<Animal> animalList = new ArrayList();
        ArrayList<Obstacle> obstacleList = new ArrayList();
        LinkedList mCircles = new LinkedList();
        ArrayList<Pin> pinList = new ArrayList();
//</editor-fold> 
    //<editor-fold defaultstate="collapsed" desc="Loop Variables">
    int numLaunches = 0;
    int numHits = 0;
    int stampedeLaunches = 0;
    float prev;
    float elapsed;
    int animalSelected = 0;
    boolean stampedeActive = false;
    boolean firstRun = true;
    Camera cam = new Camera();
    boolean inConsole = false;
    
    QuadTreeSolver mSolver = new QuadTreeSolver(mCircles,500,500,5);
    String consoleText = "";
    int totalPins;
    //implement this when it comes near to controller development
    String gameState;   //GameState should be either:  "Default","Launching","Paused" 
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Mesh variables">
    Mesh rockMesh = MeshManager.getInstance().get("rock");
    Mesh treeMesh = MeshManager.getInstance().get("tree");
    Mesh giraffeMesh = MeshManager.getInstance().get("giraffe");
    Mesh ramMesh = MeshManager.getInstance().get("ram");
    Mesh cheetahMesh = MeshManager.getInstance().get("cheetah");
    Mesh pigMesh = MeshManager.getInstance().get("pig");
    Mesh zomMesh = MeshManager.getInstance().get("zombie");
    Mesh wallMesh = MeshManager.getInstance().get("rockWall");
    Mesh pinMesh = zomMesh;
    Mesh planeMesh = MeshManager.getInstance().get("plane");
    Mesh portalMesh = MeshManager.getInstance().get("portal");
    //Sound sounds = new Sound("assets/audio/2016-02-01-1038-12.wav");
    ImageTexture dummyTex = new ImageTexture("assets/Models/blank.png");
    UnitSquare stampedeUS = new UnitSquare();
    static Sound sounds = new Sound("assets/Audio/funkbox_music_stuff.wav");
    Water water= new Water(new vec4(0,0,0,1), 0f);
    //Sound sounds = new Sound("assets/audio/trump.wav");
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Fonts">
    Font testFont = null;
    ImageTexture alphabet = new ImageTexture("assets/Fonts/cooperBlack.png");
    ImageTexture stampedeImg = new ImageTexture("assets/Models/stampede.png");
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Portals and Misc.">

    UnitSquare usq = new UnitSquare();
    vec3 skyColor = new vec3(0.2f,0.4f,0.6f);
    Framebuffer fbo1 = new Framebuffer(512,512);
    Framebuffer fbo2 = new Framebuffer(512,512);
    Framebuffer2D shadowBuffer = new Framebuffer2D(1920,1080, GL_R32F, GL_FLOAT);//, GL_R32F, GL_FLOAT);
    Camera lightCam = new Camera();
    public PortalPair portals;
    //blurprog = new Program("blurvs.txt","blurfs.txt");
//</editor-fold>
    OpenSimplexNoise noise = new OpenSimplexNoise();
    vec3 sunPosition = new vec3(0,100,0);
    
    public GameLoop(long w)
    {
//        System.out.println("yo");
        win = w;
       
        try {
        testFont = new Font("assets/Fonts/CooperBlack.fnt");
        } 
        catch (IOException ex) 
        {
        Logger.getLogger(StateManager.class.getName()).log(Level.SEVERE, null, ex);
        
        }
        prog = new Program("vs.txt","fs.txt");
        shadowProg = new Program("shadowvs.txt", "shadowfs.txt");
        
        
        if(animalList.size() > 0)
        {
            cam.lookAt( new vec3(0,2,3), animalList.get(animalSelected).mPos.xyz(), new vec3(0,1,0) );
            cam.mFollowTarget = animalList.get(0);
        }
          totalPins = pinList.size();
    }
    protected void genBasic()
    {
        animalList.add(new Pig(new vec4(-30,0,0,1), (float)Math.PI / 2));
        //animalList.add(new Pig(new vec4(-30,0,0,1), (float)Math.PI / 2));
        //animalList.add(new Pig(new vec4(-30,0,0,1), (float)Math.PI / 2));
        //animalList.get(0).flip = true;
        animalList.add(new Cheetah(new vec4(80,0,0,1), (float)Math.PI / 2));
        animalList.add(new Giraffe(new vec4(0,0,0,1), (float)Math.PI / 2));
        animalList.add(new Ram(new vec4(60,0,0,1), (float)Math.PI / 2));
        animalList.add(new Owl(new vec4(-50,0,0,1), (float)Math.PI / 2));
        
        
        
        //animalList.add(new Animal(zomMesh,new vec4(30,1000,0,1), 0.0f));
        
        obstacleList.add(new Obstacle("rockWall", new vec4(0,-1,-20,1), -1.0f));
        obstacleList.add(new Obstacle("tree", new vec4(-30,-2,20,1), -2.0f));
        obstacleList.add(new Obstacle("tree", new vec4(0,-2,-40,1), -2.0f));
        obstacleList.add(new Obstacle("tree", new vec4(30,-2,-40,1), -2.0f));
        
        
        
        
        pinList.add(new Pin("zombie", new vec4(0,-1,-30,1), 2.0f, false));
        pinList.add(new Pin("zombie", new vec4(30,-1,-30,1), 2.0f, false));
        pinList.add(new Pin("zombie", new vec4(-30,-1,-30,1), 2.0f, false));
        pinList.add(new Melt("angus",new vec4(-45,-1,-45, 1), 2.0f, false));
        

        updateCollisionList();
        
        portals = new PortalPair(new vec4(10,-2,0,1), (float)Math.PI/4);
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
           // System.out.println(mCircles.toString());
            if(!firstRun)
            mSolver.updateList(mCircles);
            water.update(elapsed);
            if(pinList.size() <= 0 || animalList.size() <= 0)
            {
               StateManager.getInstance().MainMenu();      
            }
            if(!inConsole)
            {
                HandleInput(); 
            }
            shadowBuffer.bind();
            calculateShadowMap();
            shadowBuffer.unbind();
            Render();
            //SDL_GL_SwapWindow(win);
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
                        if((id > 96 && id < 123) || id == 32 || (id > 47 && id < 58) || id == 46)//Add a-z and spaces to the console line
                        {
                            consoleText += (char)ev.key.keysym.sym;
                        }
                        if(id == 13)//Enter
                        {
                            parseConsole();
                            inConsole = false;
                        }
                        if(id == 45)
                        {
                            consoleText += "-";
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
                   if (a.at == AnimalType.RAM && a.isSpecialActive) 
                   {
                       //System.out.println("ram");
                        o.calculateDamage(a.mVel,a.mDmg);
                   }
                   a.mVel = new vec4();
               }
            }
        }
        if(portals != null)
        {
            portals.update(animalList.get(animalSelected), elapsed);
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
                    a.mAlive = false;
                    if(a.equals(animalList.get(animalSelected)))
                    {
                        getPrevAnimal();
                    }
                }
                
                //Does hit detection for melts bullet list to animals
                if (p.pt == PinType.MELT) 
                {
                    for(int i = 0; i < ((Melt)p).getBulletListSize(); i++)
                    {
                        if(((Melt)p).checkBulletCollision(a.mPos,a.mRad,i))
                            a.mAlive = false;
                    }
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
    
    public void updateCollisionList()
    {
        LinkedList temp = new LinkedList();
        //LinkedListIterator I = temp.iterator();
//        LinkedListIterator J = mCircles.iterator();
        for(int i = 0; i < obstacleList.size(); i++)
        {
            temp.addToEnd(obstacleList.get(i));
        }
        for(int i = 0; i < pinList.size(); i++)
        {
            temp.addToEnd(pinList.get(i));
        }
        for(int i = 0; i < animalList.size(); i++)
        {
            temp.addToEnd(animalList.get(i));
        }
        
        // System.out.println(temp.toString());
        mCircles = temp;
       // System.out.println(mCircles);
//        while(I.hasNext())
//        {
//           
//            mCircles.addToEnd((PhysicsBody)I.next());
//        }
        
        mSolver.updateList(mCircles);
        
        
        
        
    }
     public void findOverlap(char type)
    {
        LinkedListIterator I = mCircles.iterator();

        HashSet<PhysicsBody> tempSet = new HashSet(mCircles.Length(), mCircles.Length() / 2, 0.5f);
        //mSolver = new QuadTreeSolver(mCircles,200,200,2);
        
        mSolver.findOverlap(tempSet);

    }
    protected void CullDeadObjects()
    {
        for(int i = 0; i < pinList.size(); i++)
        {
            if(!pinList.get(i).mAlive)
            {
                mCircles.removeAll(pinList.get(i));
                pinList.remove(i);
                numHits++;
                if(pinList.size() <= 0)
                {
                    StateManager.getInstance().MainMenu();    
                    
                }
            }
        }
        for(int i = 0; i < animalList.size(); i++)
        {
            if(!animalList.get(i).mAlive)
            {
                getPrevAnimal();
                mCircles.removeAll(animalList.get(i));
                animalList.remove(i);
                if(animalList.size() <= 0)
                {
                    StateManager.getInstance().MainMenu();     
                    
                }
            }
        }
        for(int i = 0; i <obstacleList.size(); i++)
        {
            if(!obstacleList.get(i).mAlive)
            {
                mCircles.removeAll(obstacleList.get(i));
                obstacleList.remove(i);
            }
        }
        updateCollisionList();
        
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
                    
                    if(stampedeActive)
                    stampedeLaunches++;
                    stampedeActive = false;
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
                            if(cam.mFollowTarget.canStampede(animalList.get(i).mPos) && !cam.mFollowTarget.equals(animalList.get(i)))
                            {
                                animalList.get(i).prevPos = animalList.get(i).mPos;
                                animalList.get(i).mPos = new vec4(add(cam.mFollowTarget.mPos,mul(stampedeDirection,sizeStampede*4)));
                                animalList.get(i).isInStampede = true;
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
            
    }
    public void saveFile(String filename)
    {
        saveFile(filename, true);
    }
    public void saveFile(String filename, boolean saveAnimals)
    {
        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter(new FileWriter(filename));
            if(writer != null)
            {
                if(saveAnimals)
                {
                    for(Animal a : animalList)
                    {
                        writer.write("A " + a.mSpecies + " " + a.mPos.toString() + " " + a.mRotY + "\n");
                    }
                }
                for(Pin p : pinList)
                {
                    //System.out.println(p.mPos);
                    writer.write("P " + p.mMeshString + " " + p.mPos.toString() + " " + p.mRotY + " " + p.mIsStatic + " " + p.mYOffset + " " + p.mScale + "\n");
                }

                for(Obstacle o : obstacleList)
                {
                    writer.write("O " + o.mMeshString + " " + o.mPos.toString() + " " + o.mRotY + " " + o.mScale + " " + o.mYOffset + "\n");
                }
                writer.close();
            }
        }
        catch(IOException e)
        {
        }
    }
    public void loadFile(String filename)
    {
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new FileReader(filename));
        }
        catch(FileNotFoundException e)
        {
            
        }
        if(br != null)
        {
            Stream<String> lines = br.lines();
            Iterator I = lines.iterator();
            while(I.hasNext())
            {
                String line = I.next().toString();
                String[] parts = line.split("\\s");
                //Animal
                if(parts[0].contains("A"))
                {
                    parts[2] = parts[2].substring(1);
                    parts[4] = parts[4].substring(0, parts[4].length() - 1);
                    vec4 pos = new vec4(Float.parseFloat(parts[2]),Float.parseFloat(parts[3]),Float.parseFloat(parts[4]), 1);
                    float yRot = Float.parseFloat(parts[6]);
                    switch(parts[1])
                    {
                        case "pig":
                            animalList.add(new Pig(pos, yRot));
                            break;
                        case "giraffe":
                            animalList.add(new Giraffe(pos, yRot));
                            break;
                        case "ram":
                            animalList.add(new Ram(pos, yRot));
                            break;
                        case "cheetah":
                            animalList.add(new Cheetah(pos, yRot));
                            break;
                        case "owl":
                            animalList.add(new Owl(pos, yRot));
                            break;
                    }
                }
                //Pin
                else if(parts[0].contains("P"))
                {
                    String meshStr = parts[1];
                    parts[2] = parts[2].substring(1);
                    parts[5] = parts[5].substring(0, parts[5].length() - 1);
                    parts[9] = parts[9].substring(1);
                    parts[11] = parts[11].substring(0, parts[11].length() - 1);
                    vec4 pos = new vec4(Float.parseFloat(parts[2]),Float.parseFloat(parts[3]),Float.parseFloat(parts[4]), 1);
                    double yRot = Double.parseDouble(parts[6]);
                    boolean isStatic = (parts[7].toString().equals("true"));
                    float yOffset = Float.parseFloat(parts[8]);
                    vec3 scale = new vec3(Float.parseFloat(parts[9]),Float.parseFloat(parts[10]),Float.parseFloat(parts[11]));
                    //System.out.println(meshStr + " " + pos + " " + yRot + " " + isStatic + " " + yOffset);
                    Pin p = new Pin(meshStr, pos, yOffset, isStatic);
                    p.mRotY = yRot;
                    p.mScale = scale;
                    pinList.add(p);
                }
                //Pin
                else if(parts[0].contains("O"))
                {
                    String meshStr = parts[1];
                    parts[2] = parts[2].substring(1);
                    parts[5] = parts[5].substring(0, parts[5].length() - 1);
                    parts[7] = parts[7].substring(1);
                    parts[9] = parts[9].substring(0, parts[9].length() - 1);
                    vec4 pos = new vec4(Float.parseFloat(parts[2]),Float.parseFloat(parts[3]),Float.parseFloat(parts[4]), 1);
                    float yRot = Float.parseFloat(parts[6]);
                    vec3 scale = new vec3(Float.parseFloat(parts[7]),Float.parseFloat(parts[8]),Float.parseFloat(parts[9]));
                    float yOffset = Float.parseFloat(parts[10]);
                    Obstacle o = new Obstacle(meshStr, pos, yOffset);
                    o.mRotY = yRot;
                    o.mScale = scale;
                    obstacleList.add(o);
                }
            }
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
                if (parts[1].equals("ram"))
                {
                    spawnAnimal(giraffeMesh, parts);
                }
                if (parts[1].equals("cheetah"))
                {
                    spawnAnimal(cheetahMesh, parts);
                }
                
                if (parts[1].equals("zombie"))
                {
                    spawnZombie(parts);
                }
                if (parts[1].equals("wall"))
                {
                    System.out.println("Wall Spawned");
                    spawnObstacle(parts);
                }
                break;
            case "save":
                if(parts.length > 1)
                {
                    this.saveFile(parts[1] + ".txt");
                }
                else
                {
                    this.saveFile("Test.txt");
                }
                break;
                
            case "savenoa":
                if(parts.length > 1)
                {
                    this.saveFile(parts[1] + ".txt", false);
                }
                else
                {
                    this.saveFile("Test.txt", false);
                }
                break;
                
            case "load":
                if(parts.length > 1)
                {
                    this.loadFile(parts[1] + ".txt");
                }
                else
                {
                    this.loadFile("Test.txt");
                }
                break;
            case "clear":
                Animal selected = animalList.get(animalSelected);
                //for(int i = 0; i < animalList.size(); i++)
                //{
                //    System.out.println(animalList.get(i).mSpecies);
                //    System.out.println(animalList.toString());
                //    if(i != animalSelected)
                //    {
                //        animalList.remove(i);
                //    }
                //}
                pinList = new ArrayList();
                obstacleList = new ArrayList();
                animalList = new ArrayList();
                animalList.add(selected);
                animalSelected = 0;
                portals = null;
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
    private void spawnZombie(String[] s)
    {
        if (s == null || s.length <= 2)
        {
            pinList.add(new Pin("zombie", new vec4(0,0,0,1), 3.0f, false));
        }
        else
        {
            if(s[2].equals("here") || s[2].equals("me"))
            {
                if(s.length != 6)
                {
                    pinList.add(new Pin("zombie", animalList.get(animalSelected).mPos, 3.0f, false));
                }
                else
                {
                    vec4 tempVec = new vec4(animalList.get(animalSelected).mPos);
                    tempVec.x += Float.parseFloat(s[3]);
                    tempVec.y += Float.parseFloat(s[4]);
                    tempVec.z += Float.parseFloat(s[5]);
                    pinList.add(new Pin("zombie", tempVec, 3.0f, false));
                }
            }
            else if(s.length == 5)
            {
                vec4 tempVec = new vec4(0,0,0,0);
                tempVec.x += Float.parseFloat(s[2]);
                tempVec.y += Float.parseFloat(s[3]);
                tempVec.z += Float.parseFloat(s[4]);
                pinList.add(new Pin("zombie", tempVec, 3.0f, false));
            }
        }
    }
    private void spawnObstacle(String[] s)
    {
        if (s == null || s.length <= 2)
        {
            obstacleList.add(new Obstacle("rockWall", new vec4(0,0,0,1), 0));
        }
        else
        {
            if(s[2].equals("here") || s[2].equals("me"))
            {
                if(s.length != 6)
                {
                    obstacleList.add(new Obstacle("rockWall", animalList.get(animalSelected).mPos, 0));
                }
                else
                {
                    vec4 tempVec = new vec4(animalList.get(animalSelected).mPos);
                    tempVec.x += Float.parseFloat(s[3]);
                    tempVec.y += Float.parseFloat(s[4]);
                    tempVec.z += Float.parseFloat(s[5]);
                    obstacleList.add(new Obstacle("rockWall", tempVec, 0));
                }
            }
            else if(s.length == 5)
            {
                vec4 tempVec = new vec4(0,0,0,0);
                tempVec.x += Float.parseFloat(s[2]);
                tempVec.y += Float.parseFloat(s[3]);
                tempVec.z += Float.parseFloat(s[4]);
                obstacleList.add(new Obstacle("rockWall", tempVec, 0));
            }
        }
    }
    private void calculateShadowMap()
    {
        shadowProg.use();
        lightCam.lookAt(sunPosition, new vec3(0,0,0), new vec3(0,0,1));
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        lightCam.draw(shadowProg);
        shadowProg.setUniform("worldMatrix", mul(scaling(new vec3(100,1,100)),translation(new vec3(0,-1.0f,0))));
        planeMesh.draw(shadowProg);
        for(Animal a: animalList)
        {
            a.draw(shadowProg);
        }
        for(Pin p : pinList)
        {
            p.draw(shadowProg);
        }
        for(Obstacle o : obstacleList)
        {
            o.draw(shadowProg);
        }
        water.draw(shadowProg);
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
        
        
        prog.setUniform("diffuse_texture",stampedeImg);
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
        prog.setUniform("lights[0].position",new vec3(0,50,0));//cam.eye.x, cam.eye.y, cam.eye.z));
        prog.setUniform("lights[0].color",new vec3(1,1,1));
        prog.setUniform("shadowBufferTex", shadowBuffer.texture);
        prog.setUniform("lightProjMatrix", lightCam.projMatrix);
        prog.setUniform("lightViewMatrix", lightCam.viewMatrix);
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
            if (p instanceof Melt)
            {
                
                ((Melt)p).drawBullets(prog);
            }
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
        if(stampedeActive)
        {
            prog.setUniform("worldMatrix", mul(scaling(new vec3(.25f, .25f, 1.0f)), translation(-1.0f +
                    (250 / StateManager.getInstance().resolution.x) * 2, 1.0f -
                            (150 / StateManager.getInstance().resolution.y) * 2, 0.0f)));
            prog.setUniform("diffuse_texture",stampedeImg);
            //prog.setUniform("worldMatrix", mat4.identity());
            stampedeUS.draw(prog);
        }
        if(inConsole)
        {
            console.draw(prog);
        }
        prog.setUniform("unitSquare", 0.0f);
        prog.setUniform("diffuse_texture", dummyTex);
        prog.setUniform("shadowBufferTex", dummyTex);
        SDL_GL_SwapWindow(win);
    }
}
