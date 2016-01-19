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

public class Main{
    
    
    public static void main(String[] args){
        
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

        Set<Integer> keys = new TreeSet<>();
        Camera cam;
        Program prog;
        Program blurprog;
        float prev;
        Pin testPin;
        UnitSquare usq;
        Framebuffer fbo1;
        Framebuffer fbo2;
        Texture2D dummytex = new SolidTexture(GL_UNSIGNED_BYTE,0,0,0,0);
        Mesh pigMesh = new Mesh("assets/giraffe.obj.mesh");
        Mesh pinMesh = new Mesh("assets/zom.obj.mesh");
        Mesh planeMesh = new Mesh("assets/grassPlane.obj.mesh");
        Animal pig = new Animal(pigMesh);
        ArrayList<Pin> pinList = new ArrayList();
        pinList.add(new Pin(pinMesh, new vec4(0,2,-30,1)));
        pinList.add(new Pin(pinMesh, new vec4(3,2,-30,1)));
        pinList.add(new Pin(pinMesh, new vec4(-3,2,-30,1)));
        usq = new UnitSquare();

        fbo1 = new Framebuffer(512,512);
        fbo2 = new Framebuffer(512,512);

        prog = new Program("vs.txt","fs.txt");
        blurprog = new Program("blurvs.txt","blurfs.txt");


        cam = new Camera();
        cam.lookAt( new vec3(0,2,3), new vec3(0,0,0), new vec3(0,1,0) );

        prev = (float)(System.nanoTime()*1E-9);
        SDL_Event ev=new SDL_Event();
        while(true){
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
                    //System.out.println("Key press "+ev.key.keysym.sym+" "+ev.key.keysym.sym);
                    keys.add(ev.key.keysym.sym);
                }
                if( ev.type == SDL_KEYUP ){
                    keys.remove(ev.key.keysym.sym);
                }
            }

            float now = (float)(System.nanoTime()*1E-9);
            float elapsed = now-prev;
            pig.update(elapsed);
            cam.update();
            for (Pin p : pinList)
            {
                p.checkCollision(pig.mPos, pig.mRad);
            }
            prev=now;

            if( keys.contains(SDLK_w ))
                cam.walk(0.5f*elapsed);
            if( keys.contains(SDLK_s))
                cam.walk(-0.5f*elapsed);
            if( keys.contains(SDLK_a))
            {
                if(!cam.mFollowing)
                {
                    pig.rotate(2 * elapsed);
                    cam.follow(pig,false);
                }
            }
            if( keys.contains(SDLK_d))
            {
                if(!cam.mFollowing)
                {
                    pig.rotate(-2 * elapsed);
                    cam.follow(pig, false);
                }
            }
            if( keys.contains(SDLK_r))
                cam.tilt(0.4f*elapsed);
            if( keys.contains(SDLK_t))
                cam.tilt(-0.4f*elapsed);
            if( keys.contains(SDLK_SPACE))
            {
                if(!cam.mFollowing)
                {
                    pig.takeoff();
                    cam.follow(pig, true);
                }
            }
            if(keys.contains(SDLK_ESCAPE))
            {
                System.exit(0);
            }

            //the fbo stuff is for later...
            //fbo1.bind();
            
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            prog.use();
            prog.setUniform("lightPos", new vec3(0,10,-5));//new vec3(cam.eye.x, cam.eye.y, cam.eye.z));//50,50,50) )
            prog.setUniform("worldMatrix", mul(scaling(new vec3(100,1,100)),translation(new vec3(0,-1.0f,0))));
            planeMesh.draw(prog);
            cam.draw(prog);
            pig.draw(prog);
            for(Pin p : pinList)
            {
                p.draw(prog);
            }
            //fbo1.unbind();

            //this is also for later...
/*
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            blurprog.use();
            blurprog.setUniform("diffuse_texture",fbo1.texture);
            usq.draw(blurprog);
            blurprog.setUniform("diffuse_texture",dummytex);
*/

            SDL_GL_SwapWindow(win);


        }//endwhile
    }//end main
}
