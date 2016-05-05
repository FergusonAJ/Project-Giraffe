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
    float freq = 0.2f;
    float speed = 0.25f;
    float steepness = 2.5f;
    float amplitude = .3f;
    Mesh mMesh = new Mesh("assets/Models/waterPlane.obj.mesh",false, true);
    vec4 mPos;
    float mYOffset;
    boolean upDir = true;
    float mode = 3;
    float waveTime = (float) ((2*Math.PI)/(speed*freq));
    float waterTracker =0;
    float elapsed = 0;

    public Water(vec4 position, float yOffset)
    {
        mPos = new vec4(position.x,position.y+yOffset,position.z,position.w);
        mYOffset = yOffset;
        
    }
        
    public void update(float elapsed)
    {
        waterTracker += elapsed*10;
        this.elapsed = elapsed;
        if(waterTracker >= waveTime)
            waterTracker -= waveTime;
        
    }
    
    
    public void draw(Program prog)
    {
        prog.setUniform("elapsed",waterTracker);
        prog.setUniform("freq",freq);
        prog.setUniform("speed", speed);
        prog.setUniform("steepness", steepness);
        prog.setUniform("amplitude", amplitude);
        prog.setUniform("worldMatrix", scaling(1000,1000,1000).mul(axisRotation(new vec3(1,0,0), Math.PI/2)).mul(translation(new vec3(-500,mPos.y,-500))));
        mMesh.draw(prog);
    }
    
    
    
}
