/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Animals;

import framework.Animal;
import framework.Mesh;
import framework.math3d.vec4;

/**
 *
 * @author Andrew Polanco
 */
public class Pig extends Animal
{

    int mDmg = 25;
    public Pig(Mesh mesh, vec4 position, float yOffset)
    {
        super(mesh, position, yOffset);
    
    }
    
}