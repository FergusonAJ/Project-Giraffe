package framework;

import static JGL.JGL.*;
import static JSDL.JSDL.*;

/**
 * Singleton designed state manager. Accessible from anywhere to allow easy control of game flow.
 */
public class StateManager
{
    static private StateManager instance_ = new StateManager();
    static private GameLoop currentLoop_;
    static private long win_;
    
    /** Initializes the necessities for GL and fires up the MeshManager
     */
    public static void main(String[] args)
    {
        initGL();
        MeshManager.init();
        instance_.MainMenu();
    }
    
    /**
     * Sets the current loop to a new instance of the main menu and then runs it.
     */
    public void MainMenu()
    {
        currentLoop_ = new MainMenu(win_);
        currentLoop_.runLoop();
    }
    
    /**
     * Creates and runs a new main game loop. (New level)
     * @param genBasic If true, spawns in a few animals, pins, and obstacles for testing.
     */
    public void NewLoop(boolean genBasic)
    {
        currentLoop_ = new GameLoop(win_);
        if(genBasic)
        {
            currentLoop_.genBasic();
        }
        currentLoop_.runLoop();
    }
    
    /**
     * Gets the current instance of the StateManager
     * @return current StateManager instance
     */
    public static StateManager getInstance()
    {
        return instance_;
    }
    
    /**
     * Gets the window, and all supporting GL attributes ready for the game. 
     * Most code by Jim Hudson
     */
    private static void initGL()
    {
        SDL_Init(SDL_INIT_VIDEO);
        win_ = SDL_CreateWindow("Animal Bowling",40,60, 1920,1080, SDL_WINDOW_OPENGL );
        SDL_SetWindowFullscreen(win_, SDL_WINDOW_FULLSCREEN);
        SDL_GL_SetAttribute(SDL_GL_CONTEXT_PROFILE_MASK,SDL_GL_CONTEXT_PROFILE_CORE);
        SDL_GL_SetAttribute(SDL_GL_DEPTH_SIZE,24);
        SDL_GL_SetAttribute(SDL_GL_STENCIL_SIZE,8);
        SDL_GL_SetAttribute(SDL_GL_CONTEXT_MAJOR_VERSION,3);
        SDL_GL_SetAttribute(SDL_GL_CONTEXT_MINOR_VERSION,2);
        SDL_GL_SetAttribute(SDL_GL_CONTEXT_FLAGS, SDL_GL_CONTEXT_DEBUG_FLAG);
        SDL_GL_CreateContext(win_);
        
        glDebugMessageControl(GL_DONT_CARE,GL_DONT_CARE,GL_DONT_CARE, 0,null, true );
        glEnable(GL_DEBUG_OUTPUT_SYNCHRONOUS);
        glDebugMessageCallback(
                (int source, int type, int id, int severity, String message, Object obj ) -> {
                    System.out.println("GL message: "+message);
                    //if( severity == GL_DEBUG_SEVERITY_HIGH )
                    //    System.exit(1);
                },
                null);

        int[] tmp = new int[1];
        glGenVertexArrays(1,tmp);
        int vao = tmp[0];
        glBindVertexArray(vao);
        glClearColor(0.2f,0.4f,0.6f,1.0f);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);   
    }
}
