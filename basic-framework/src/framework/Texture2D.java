package framework;

import static JGL.JGL.*;


/**
 * Written by Jim Hudson
 */
public class Texture2D extends Texture 
{
    int w,h;
    public Texture2D() {
        super(GL_TEXTURE_2D);
    }
    
    public Texture2D(int w, int h) {
        super(GL_TEXTURE_2D);
        this.w=w;
        this.h=h;
    }
}