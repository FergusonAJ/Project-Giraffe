package Terrains;

/**
 *
 * @author Andrew Polanco
 */
import framework.Mesh;
import framework.Program;
import framework.math3d.vec3;
import framework.math3d.vec4;
public class Water 
{
    Mesh mMesh;
    vec4 mPos;
    float mYOffset;
    public Water(vec4 position, float yOffset)
    {
        mPos = position;
        mYOffset = yOffset;
        
    }
        
    void animation()
    {
        
    }
    
    public void draw(Program prog)
    {
        mMesh.draw(prog);
    }
    
    
    
}
