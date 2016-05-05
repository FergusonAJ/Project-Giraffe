package framework;
import framework.math3d.*;

/**
 *
 * @author Austin Ferguson
 */
public class OrientedBoundingBox
{
    vec4 mPoint, mU, mV, mW;
    public OrientedBoundingBox(vec4 p, vec4 u, vec4 v, vec4 w)
    {
        mPoint = p;
        mU = u;
        mV = v;
        mW = w;
    }
    public float [] projectOntoAxis(vec4 o, vec4 axis)
    {
        float [] limits = new float[2];
        limits[0] = -(Float.MAX_VALUE);
        limits[1] = Float.MAX_VALUE;
        for(int i = 0; i < 2; i++)
        {
            for(int j = 0; j < 0; j++)
            {
                for(int k = 0; k < 0; k++)
                {
                    vec4 q = mPoint.add(mU.mul(i)).add(mV.mul(j).add(mW.mul(k)));
                    float dp = axis.dot(q.sub(o));
                    if(dp < limits[0])
                    {
                        limits[0] = dp;
                    }
                    if(dp > limits[1])
                    {
                        limits[1] = dp;
                    }
                }
            }
        }
        return limits;
    }
    
    public boolean checkCollision(OrientedBoundingBox other)
    {
        if(checkRanges(projectOntoAxis(mPoint, mU), other.projectOntoAxis(mPoint, mU)))
        {
            return true;
        }
        if(checkRanges(projectOntoAxis(mPoint, mV), other.projectOntoAxis(mPoint, mV)))
        {
            return true;
        }
        if(checkRanges(projectOntoAxis(mPoint, mW), other.projectOntoAxis(mPoint, mW)))
        {
            return true;
        }
        if(checkRanges(projectOntoAxis(other.mPoint, other.mU), other.projectOntoAxis(other.mPoint, other.mU)))
        {
            return true;
        }
        if(checkRanges(projectOntoAxis(other.mPoint, other.mV), other.projectOntoAxis(other.mPoint, other.mV)))
        {
            return true;
        }
        if(checkRanges(projectOntoAxis(other.mPoint, other.mW), other.projectOntoAxis(other.mPoint, other.mW)))
        {
            return true;
        }
        vec4 c1 =  math3d.cross(mU, other.mU);
        vec4 c2 =  math3d.cross(mU, other.mV);
        vec4 c3 =  math3d.cross(mU, other.mW);
        vec4 c4 =  math3d.cross(mV, other.mU);
        vec4 c5 =  math3d.cross(mV, other.mV);
        vec4 c6 =  math3d.cross(mV, other.mW);
        vec4 c7 =  math3d.cross(mW, other.mU);
        vec4 c8 =  math3d.cross(mW, other.mV);
        vec4 c9 =  math3d.cross(mW, other.mW);
        if(checkRanges(projectOntoAxis(mPoint, c1), other.projectOntoAxis(mPoint, c1)))
        {
            return true;
        }
        if(checkRanges(projectOntoAxis(mPoint, c2), other.projectOntoAxis(mPoint, c2)))
        {
            return true;
        }
        if(checkRanges(projectOntoAxis(mPoint, c3), other.projectOntoAxis(mPoint, c3)))
        {
            return true;
        }
        if(checkRanges(projectOntoAxis(mPoint, c4), other.projectOntoAxis(mPoint, c4)))
        {
            return true;
        }
        if(checkRanges(projectOntoAxis(mPoint, c5), other.projectOntoAxis(mPoint, c5)))
        {
            return true;
        }
        if(checkRanges(projectOntoAxis(mPoint, c6), other.projectOntoAxis(mPoint, c6)))
        {
            return true;
        }
        if(checkRanges(projectOntoAxis(mPoint, c7), other.projectOntoAxis(mPoint, c7)))
        {
            return true;
        }
        if(checkRanges(projectOntoAxis(mPoint, c8), other.projectOntoAxis(mPoint, c8)))
        {
            return true;
        }
        if(checkRanges(projectOntoAxis(mPoint, c9), other.projectOntoAxis(mPoint, c9)))
        {
            return true;
        }
        
        return false;
    }
    
    private boolean checkRanges(float [] a, float [] b)
    {
        if(b[1] < a[0] || b[0] > a[1])
        {
            return false;
        }
        return true;
    }
}
