package Terrains;

/**
 *
 * @author Andrew Polanco
 */
import framework.Mesh;
import framework.Program;
import static framework.math3d.math3d.axisRotation;
import static framework.math3d.math3d.mul;
import static framework.math3d.math3d.scaling;
import static framework.math3d.math3d.translation;
import framework.math3d.vec3;
import framework.math3d.vec4;
public class Water 
{
    Mesh mMesh = new Mesh("assets/Models/waterPlane.obj.mesh",false, true);
    vec4 mPos;
    float mYOffset;
    boolean upDir = true;
    float mode = 3;
    public Water(vec4 position, float yOffset)
    {
        mPos = position;
        mYOffset = yOffset;
        
    }
        
    public void update(float elapsed)
    {
        
//        if(mPos.y <10 && upDir)
//        mPos.y+=.1*elapsed;
//        if(mPos.y >=10)
//            upDir=!upDir;
//        if
        if(upDir)
            mPos.y+= 1*elapsed;
        else
            mPos.y-= 1*elapsed;
        //System.out.println(mPos.y);
        if(mPos.y >=1)
            upDir = false;
        if(mPos.y <=-6)
            upDir = true;
        
    }
    
    void waves()
    {
        
    }
    
    public void draw(Program prog)
    {
        prog.setUniform("mode", mode);
        prog.setUniform("worldMatrix", scaling(5,5,5).mul(translation(mPos)));
        mMesh.draw(prog);
    }
    
    
    
}
