package framework;

import framework.math3d.vec4;
import java.util.HashMap;

/**
 * Simple obstacle manager so we do not have to manually have to set the size and such of each obstacle we create.
 * @author Austin Ferguson
 */
public class ObstacleManager 
{
    private HashMap obstacleMap_;
    private static ObstacleManager instance_;
    
    private ObstacleManager()
    {
        MeshManager meshManager = MeshManager.getInstance();
        obstacleMap_ = new HashMap();
        //System.out.println(meshManager.get("rockWall"));
        obstacleMap_.put("rockWall", new ObstacleInfo("rockWall", 3.0f, 1.0f, 0.2f));
        obstacleMap_.put("tree", new ObstacleInfo("tree", 2.0f, 6.0f, 2.0f));
    }
    public static ObstacleManager getInstance()
    {
        return instance_;
    }
    public static void init()
    {
        instance_ = new ObstacleManager();
    }
    public ObstacleInfo getInfo(String key)
    {
        return (ObstacleInfo)obstacleMap_.get(key);
    }
}

class ObstacleInfo
{
    Mesh mMesh;
    float mWidth, mHeight, mDepth;
    String mMeshName;
    ObstacleInfo(String meshName, float w, float h, float d)
    {
        System.out.println(meshName);
        mMeshName = meshName;
        mMesh = MeshManager.getInstance().get(meshName);
        mWidth = w;
        mHeight = h;
        mDepth = d;
        
    }
}
