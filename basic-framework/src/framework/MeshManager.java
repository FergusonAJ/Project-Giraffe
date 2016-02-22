package framework;

import java.util.HashMap;

public class MeshManager 
{
    private static MeshManager instance_;// = new MeshManager();
    private static HashMap meshMap_ = new HashMap();
    
    private MeshManager()
    {
        meshMap_.put("tree", new Mesh("assets/bobbleTree.obj.mesh"));
        meshMap_.put("rock", new Mesh("assets/toonRocks.obj.mesh"));
        meshMap_.put("giraffe", new Mesh("assets/giraffe.obj.mesh"));
        meshMap_.put("pig", new Mesh("assets/goodPig.obj.mesh"));
        meshMap_.put("zombie", new Mesh("assets/basicZombie.obj.mesh"));
        meshMap_.put("rockWall", new Mesh("assets/RockWall.obj.mesh"));
        meshMap_.put("plane", new Mesh("assets/SimplexPlane.obj.mesh", true));
        meshMap_.put("portal", new Mesh("assets/portalPlane.obj.mesh"));
        meshMap_.put("sign1", new Mesh("assets/sign1.obj.mesh"));
        meshMap_.put("sign2", new Mesh("assets/sign2.obj.mesh"));
        
    }
    protected static void initialize()
    {
        instance_ = new MeshManager();
    }
    public static MeshManager getInstance()
    {
            return instance_;
    }
    public Mesh get(String key)
    {
        return (Mesh)meshMap_.get(key);
    }
}
