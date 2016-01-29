package framework;

import framework.math3d.vec3;
import framework.math3d.mat4;
import java.util.Set;
import java.util.TreeSet;
import static JGL.JGL.*;
import static JSDL.JSDL.*;
import static framework.math3d.math3d.mul;
import static framework.math3d.math3d.sub;
import static framework.math3d.math3d.translation;
import static framework.math3d.math3d.scaling;
import framework.math3d.vec2;
import framework.math3d.vec4;
import java.util.ArrayList;
import TextRendering.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main
{ 
    public static void main(String[] args)
    {
//<editor-fold defaultstate="collapsed" desc="OpenGL Initialization">
       
        SDL_Init(SDL_INIT_VIDEO);
        long win = SDL_CreateWindow("Animal Bowling",40,60, 1920,1080, SDL_WINDOW_OPENGL );
        SDL_SetWindowFullscreen(win, SDL_WINDOW_FULLSCREEN);
        SDL_GL_SetAttribute(SDL_GL_CONTEXT_PROFILE_MASK,SDL_GL_CONTEXT_PROFILE_CORE);
        SDL_GL_SetAttribute(SDL_GL_DEPTH_SIZE,24);
        SDL_GL_SetAttribute(SDL_GL_STENCIL_SIZE,8);
        SDL_GL_SetAttribute(SDL_GL_CONTEXT_MAJOR_VERSION,3);
        SDL_GL_SetAttribute(SDL_GL_CONTEXT_MINOR_VERSION,2);
        SDL_GL_SetAttribute(SDL_GL_CONTEXT_FLAGS, SDL_GL_CONTEXT_DEBUG_FLAG);
        SDL_GL_CreateContext(win);
        
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
        GameLoop mainMenu = new MainMenu(win);
        mainMenu.runLoop();
    }//end main
}
