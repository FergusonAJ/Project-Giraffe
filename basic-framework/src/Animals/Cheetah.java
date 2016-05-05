package Animals;

import framework.Animal;
import framework.Mesh;
import framework.MeshManager;
import static framework.math3d.math3d.axisRotation;
import static framework.math3d.math3d.mul;
import framework.math3d.vec4;
import PerlinNoise.Noise;
import java.util.Random;
import framework.ImageTexture;
import framework.Program;
import static framework.math3d.math3d.axisRotation;
import static framework.math3d.math3d.mul;
import static framework.math3d.math3d.translation;
import framework.math3d.vec3;
/**
 *
 * @author Andrew Polanco
 */
public class Cheetah extends Animal{
    static PerlinNoise.Noise noise;
    static Random rand;
    ImageTexture tex;
    
    public Cheetah(vec4 position, float yRot) 
    {
        super(MeshManager.getInstance().get("cheetah"), position, 3.0f);
        if(noise == null)
        {
            noise = new PerlinNoise.Noise();
        }
        if(rand == null)
        {
            rand = new Random();
        }
        mMesh.setTexture(null);
        tex = new ImageTexture(noise.renderCheetahImage(rand.nextInt(1000), rand.nextInt(1000)));
        mDmg = 30;
        mSpecies = "cheetah";
        mRotY = yRot;
        at = AnimalType.CHEETAH;
    }
    
    @Override
    protected void takeoff()
    {
        mVel = mul(new vec4(1,0,0,0), axisRotation(new vec4(0,1,0,0), mRotY));
        mVel = mul(mVel, 100);
        mMoving = true;
    }
    
    @Override
    public void draw(Program prog)
    {
        prog.setUniform("worldMatrix", mul(mul(axisRotation(new vec4(0.0f,1.0f,0.0f,0.0f), mRotY), translation(mPos)), translation(new vec3(0,mYOffset, 0))));
        prog.setUniform("diffuse_texture",this.tex);
        mMesh.draw(prog);
    }
    
    @Override
    public void drawFur(Program prog)
    {
        prog.setUniform("worldMatrix", mul(mul(axisRotation(new vec4(0.0f,1.0f,0.0f,0.0f), mRotY), translation(mPos)), translation(new vec3(0,mYOffset, 0))));
        prog.setUniform("diffuse_texture",this.tex);
        mMesh.drawFur(prog);
    }
}
