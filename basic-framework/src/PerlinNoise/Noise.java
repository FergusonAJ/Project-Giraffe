package PerlinNoise;

import framework.math3d.math3d;
import java.util.Random;
import framework.math3d.vec3;
import framework.math3d.vec2;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
/**
 *
 * @author Austin
 */
public class Noise 
{
    static Random rand = new Random();
    vec3 gradients[] = new vec3[256];
    int indices[] = new int[256];
    public Noise()
    {
        for(int i = 0; i < 256; i++)
        {
            vec3 temp = new vec3(rand.nextFloat() * 2 - 1,rand.nextFloat() * 2 - 1,rand.nextFloat() * 2 - 1);
            gradients[i] = math3d.normalize(temp);
            indices[i] = i;
        }
        for(int i = 0; i < 256; i++)
        {
            int oldVal = indices[i];
            int index = rand.nextInt(256);
            indices[i] = indices[index];
            indices[index] = oldVal;
        }
    }
    
    public float get2D(vec2 p)
    {
        int xLower = (int)p.x;
        int xUpper = xLower + 1;
        int yLower = (int)p.y;
        int yUpper = yLower + 1;
        vec2 a = new vec2(xLower, yUpper);  // a----b
        vec2 b = new vec2(xUpper, yUpper);  // |    |
        vec2 c = new vec2(xLower, yLower);  // |    |
        vec2 d = new vec2(xUpper, yLower);  // c----d
        vec2 va = a.sub(p);
        vec2 vb = b.sub(p);
        vec2 vc = c.sub(p);
        vec2 vd = d.sub(p);
        vec2 ga = getGradient(a).xy();
        vec2 gb = getGradient(b).xy();
        vec2 gc = getGradient(c).xy();
        vec2 gd = getGradient(d).xy();
        float da = va.dot(ga);
        float db = vb.dot(gb);
        float dc = vc.dot(gc);
        float dd = vd.dot(gd);
        float frac = p.x - xLower;
        float pct = frac*frac*(3 - 2 * frac);
        float avg_ab = da + pct*(db-da);
        float avg_cd = dc + pct*(dd-dc);
        frac = p.y - yLower;
        pct = frac*frac*(3-2*frac);
        return avg_cd + pct*(avg_ab-avg_cd);
    }
    
    private vec3 getGradient(vec2 v)
    {
        return gradients[indices[((int)v.x + indices[(int)v.y % 255]) % 255]];
    }
    
    public BufferedImage renderImage2D(int width, int height, float xOffset, float yOffset)
    {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                float val = get2D(new vec2((float)(x+xOffset) / height * 30, (float)(y +yOffset)/height * 30));
                int v = ((int)((val + 1)/2 * 255)%255);
                if(v > 150) 
                {
                    v = -500;
                }
                v = v/5 + 150;
                Color color = new Color(v,v-50,0);
                image.setRGB(x, y, color.getRGB());
            }
        }
        //float val = get2D(new vec2(image.getRGB(10,10)));
        //int numSpots = ((int)((val + 1)/2 * 255)%255);
        int a = image.getRGB(1,1);
        if (a < 0)
        {
            a *= -1;
        }
        float val = a / 5.0f;
        float v = get2D(new vec2(val,val)) * 10000;
        if(v < 0)          
        {
            v *= -1;
        }
        int numSpots = (int)(v % ((width - height) / 2));
        for(int i = 0; i < numSpots; i++)
        {
            int x = (int)(get2D(new vec2((float)i/width,(float)i/width)) * 10000);
            if(x < 0)
            {
                x *= -1;
            }
            int y = (int)(get2D(new vec2((float)i/height,(float)i/height)) * 10000);
            if (y < 0)
            {
                y *= -1;
            }
        }
        try {
            ImageIO.write(image, "png", new File("Render_Test.png"));
            System.out.println("\n\nImage rendered\n\n");
        } catch (IOException ex) {
            Logger.getLogger(Noise.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }
    
    public BufferedImage renderCheetahImage(float xOffset, float yOffset)
    {
        try 
        {
            BufferedImage image = ImageIO.read(new File("assets/Models/cheetah.png"));
            int width = image.getWidth();
            int height = image.getHeight();
            int white = new Color(255,255,255).getRGB();
            int black = new Color(0,0,0).getRGB();
            for(int y = 0; y < height; y++)
            {
                for(int x = 0; x < width; x++) 
                {
                    if(image.getRGB(x, y) != white && image.getRGB(x,y) != black)
                    {
                        float val = get2D(new vec2((float)(x+xOffset) / height * 30, (float)(y +yOffset)/height * 30));
                        int v = ((int)((val + 1)/2 * 255)%255);
                        if(v > 150)
                        {
                             v = -500;
                        }
                        v = v/5 + 150;
                        Color color = new Color(v,v-50,0);
                        image.setRGB(x, y, color.getRGB());
                    }
                }
            }
                ImageIO.write(image, "png", new File("Render_Test.png"));
            
            return image;
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Noise.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
