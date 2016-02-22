package framework;

import static JGL.JGL.*;
import static JSDL.JSDL.*;

public class StateManager
{
    static private StateManager instance_ = new StateManager();
    static private GameLoop currentLoop_;
    static private long win_;
    
    private StateManager()
    {
        
    }
    
    public static void main(String[] args)
    {
        initGL();
        MeshManager.initialize();
        instance_.MainMenu();
    }
     
    public void MainMenu()
    {
        currentLoop_ = new MainMenu(win_);
        currentLoop_.runLoop();
    }
    
    public void NewLoop(boolean genBasic)
    {
        currentLoop_ = new GameLoop(win_);
        if(genBasic)
        {
            currentLoop_.genBasic();
        }
        currentLoop_.runLoop();
    }
    
    public static StateManager getInstance()
    {
        return instance_;
    }
    
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
