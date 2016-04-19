package framework;
import static framework.Obstacle.noise;
import framework.math3d.*;
import static framework.math3d.math3d.axisRotation;
import static framework.math3d.math3d.scaling;
import static framework.math3d.math3d.translation;

public class PortalPair 
{
    private vec4 mPos1,mPos2;
    private Mesh mMesh;
    private Camera mCam1, mCam2;
    private float mYRot1, mYRot2;
    private float mTimer = 0.0f;
    private float mMaxTimer = 1.0f;
    private vec4 mForwardVec;
    static OpenSimplexNoise noise = new OpenSimplexNoise(); //Used for placing the obstacle on the ground
    
    public PortalPair(vec4 pos1, float yRot1)
    {
        mPos1 = pos1.add(new vec4(0,2,0,0));
        mForwardVec = new vec4(Math.cos(yRot1), 0 , -Math.sin(yRot1), 0);
        mYRot1 = yRot1;
        mYRot2 = yRot1 + (float)Math.PI;
        mPos2 = pos1.add(mForwardVec.mul(20));
        mPos2 = mPos2.add(new vec4(0,2,0,0));
        mMesh = MeshManager.getInstance().get("portal");
        mMesh.texture = null;
        mCam1 = new Camera();
        mCam1.lookAt(mPos1.xyz().add(new vec3(0,2,0)), mPos1.sub(mForwardVec).xyz().add(new vec3(0,2,0)), new vec4(0,1,0,0).xyz());
        mCam2 = new Camera();
        mCam2.lookAt(mPos2.xyz().add(new vec3(0,2,0)), mPos2.add(mForwardVec).xyz().add(new vec3(0,2,0)), new vec4(0,1,0,0).xyz());
        mCam1.fov_h = 22.5f;
        mCam2.fov_h = 22.5f;
        mTimer = 0.0f;
        mPos1.y  = (float)noise.eval(mPos1.x/100*4, mPos1.z/100*4) * 10;
        if(mPos1.y < 0)
        {
            mPos1.y = 0;
        }
        mPos2.y  = (float)noise.eval(mPos2.x/100*4, mPos2.z/100*4) * 10;
        if(mPos2.y < 0)
        {
            mPos2.y = 0;
        }
    }
    public Camera getCam1()
    {
        return mCam1;
    }
    public Camera getCam2()
    {
        return mCam2;
    }
    public void cam1LookAt(vec3 v)
    {
        vec3 temp = new vec3(mCam1.eye.z, mCam1.eye.y, mCam1.eye.z);
        mCam1.lookAt(temp, temp.add(v), new vec3(0,1,0));
    }
    public void cam2LookAt(vec3 v)
    {
        vec3 temp = new vec3(mCam2.eye.z, mCam2.eye.y, mCam2.eye.z);
        mCam2.lookAt(temp, temp.add(v), new vec3(0,1,0));
    }
    public vec4 getPortal1Pos()
    {
        return mPos1;
    }
    
    public vec4 getPortal2Pos()
    {
        return mPos2;
    }
    
    public void update(Animal a, float deltaTime)
    {
        if(mTimer <= 0)
        {
            if(math3d.length(a.mPos.sub(mPos1)) < a.mRad)
            {
                a.mPos = mPos2.add(mForwardVec);
                mTimer = mMaxTimer;
            }
            else if(math3d.length(a.mPos.sub(mPos2)) < a.mRad)
            {
                a.mPos = mPos1.sub(mForwardVec);
                mTimer = mMaxTimer;
            }
        }
        else
        {
            mTimer -= deltaTime;
        }
    }
    
    public void draw(Program prog, Framebuffer fbo1, Framebuffer fbo2)
    {
        prog.setUniform("diffuse_texture", fbo2.texture);
        prog.setUniform("worldMatrix", scaling(2,3,2).mul(axisRotation(new vec4(0,1,0,0), mYRot2).mul(translation(mPos1))));
        mMesh.draw(prog);
        prog.setUniform("diffuse_texture", fbo1.texture);
        prog.setUniform("worldMatrix", scaling(2,3,2).mul(axisRotation(new vec4(0,1,0,0), mYRot1).mul(translation(mPos2))));
        mMesh.draw(prog);
    }
}
