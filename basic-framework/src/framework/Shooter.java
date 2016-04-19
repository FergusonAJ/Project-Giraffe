/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import framework.math3d.vec4;
import java.util.ArrayList;

/**
 *
 * @author Andrew Polanco
 */
public abstract class Shooter 
{
    protected ArrayList <Projectile> bulletList = new ArrayList();
    final float shootRate = 3;
    float bRecharge = 0;
    
    /**
     *  
     * bRecharge+=elapsed;
        if(bRecharge >= shootRate)
        {
            bulletList.add(new Projectile(this,mPos,mVel,mRotY,mYOffset));
            bRecharge = 0;
  
        }
        updateBulletList(elapsed);
        
       insert this code
     * @param elapsed
     */
    
    public abstract void rechargeUpdate(float elapsed);
        
        
   
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
