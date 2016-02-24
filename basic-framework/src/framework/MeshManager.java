package framework;

import java.util.HashMap;

/**
 * Basic MeshManager that loads all the meshes in at runtime, and allows them to be accessed with get()
 */
public class MeshManager 
{
    private static MeshManager instance_;
    private static HashMap meshMap_ = new HashMap();
    
    /**
     * Load in all the meshes that we are using
     */
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
    
    /**
     * Creates the instance
     */
    public static void init()
    {
        instance_ = new MeshManager();
    }
    
    /**
     * @return The current MeshManager
     */
    public static MeshManager getInstance()
    {
            return instance_;
    }
    
    /**
     * Grabs the mesh for the respective key
     * @param key Key to look for in the HashMap
     * @return The mesh for the associated key
     */
    public Mesh get(String key)
    {
        return (Mesh)meshMap_.get(key);
    }
}
