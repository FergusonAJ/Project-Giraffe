
package Pins;

import framework.Pin;
import framework.Program;
import framework.Projectile;
import framework.Shooter;
import static framework.math3d.math3d.mul;
import static framework.math3d.math3d.normalize;
import static framework.math3d.math3d.sub;
import framework.math3d.vec4;
import java.util.ArrayList;

/**
 *
 * @author Andrew Polanco
 */
public class Melt extends Pin
{
    protected ArrayList <Projectile> bulletList = new ArrayList();
    final float shootRate = 5;
    float bRecharge = 0;
    
    public Melt(String meshStr, vec4 position, float yOffset, boolean isStatic) 
    {
        super(meshStr, position, yOffset, isStatic);
        mHealth = 50;
        pt = PinType.MELT;
        
    }
   
    @Override
    protected void takeoff(vec4 animalPos)
    {
        mVel = normalize(sub(animalPos, mPos));
        mVel = mul(mVel, 2);
        mMoving = true;
    }    
    
    @Override
    public void update(float elapsed)
    {
        if(mMoving)
        {
            super.update(elapsed);
        }
        
        bRecharge+=elapsed;
        if(bRecharge >= shootRate)
        {
            bulletList.add(new Projectile(this,mPos,mVel,mYOffset));
            bRecharge = 0;
  
        }
        updateBulletList(elapsed);

    }
    
    public boolean checkBulletCollision(vec4 pos, float rad, int index)
    {
        return bulletList.get(index).checkCollision(pos,rad);
    }
    
    public void updateBulletList(float elapsed)
    {
        for(int i = 0; i < bulletList.size();i++)
        {
            bulletList.get(i).update(elapsed);
            if(!bulletList.get(i).mAlive)
                bulletList.remove(i);
            
        }
    }
    public void drawBullets(Program prog)
    {
        for(int i =0; i < bulletList.size();i++)
        {
            bulletList.get(i).draw(prog);
        }
    }

    public int getBulletListSize() {
        return bulletList.size();
    }
   
        
}
