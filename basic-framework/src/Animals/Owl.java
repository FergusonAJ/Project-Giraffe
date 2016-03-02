package Animals;

import framework.Animal;
import framework.Mesh;
import framework.MeshManager;
import framework.Program;
import framework.math3d.mat4;
import static framework.math3d.math3d.*;
import framework.math3d.vec3;
import framework.math3d.vec4;

/**
 *
 * @author Andrew Polanco
 */
public class Owl extends Animal{

    public Owl(vec4 position, float yRot) 
    {
        super(MeshManager.getInstance().get("owlStanding"), position, 1);
        mDmg = 10;
        mSpecies = "owl";
        mRotY = yRot;
    }
    
    public void specialAbility()
    {
        
    }
    public void draw(Program prog)
    {
        mat4 tempMat = scaling(0.75f, 1, 0.75f);
        tempMat = tempMat.mul(axisRotation(new vec4(0.0f,1.0f,0.0f,0.0f), mRotY));
        tempMat = tempMat.mul(translation(mPos));
        tempMat = tempMat.mul(translation(new vec3(0,mYOffset, 0)));
        prog.setUniform("worldMatrix", tempMat);
        mMesh.draw(prog);
    }
}
