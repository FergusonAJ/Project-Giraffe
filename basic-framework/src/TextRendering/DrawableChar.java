/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TextRendering;

import static JGL.JGL.GL_ARRAY_BUFFER;
import static JGL.JGL.GL_FLOAT;
import static JGL.JGL.GL_STATIC_DRAW;
import static JGL.JGL.GL_TRIANGLES;
import static JGL.JGL.glBindBuffer;
import static JGL.JGL.glBindVertexArray;
import static JGL.JGL.glBufferData;
import static JGL.JGL.glDrawArrays;
import static JGL.JGL.glEnableVertexAttribArray;
import static JGL.JGL.glGenBuffers;
import static JGL.JGL.glDeleteBuffers;
import static JGL.JGL.glGenVertexArrays;
import static JGL.JGL.glDeleteVertexArrays;
import static JGL.JGL.glVertexAttribPointer;
import framework.Program;
import framework.math3d.mat4;
import static framework.math3d.math3d.mul;
import static framework.math3d.math3d.scaling;
import static framework.math3d.math3d.translation;
import framework.math3d.vec3;

/**
 *
 * @author ajart
 */
public class DrawableChar 
{
    int vao;
    int mX = 0, mY = 0;
    float mScale = 1.0f;
    CharInfo mInfo;
    int vbuff;
    
    public DrawableChar(char c, Font font)
    {
        genVAO(c, font);
    }
    public DrawableChar(char c, int x, int y, Font font)
    {
        mX = x;
        mY = y;
        genVAO(c, font);
    }
    public DrawableChar(char c, int x, int y, float scale, Font font)
    {
        mX = x;
        mY = y;
        mScale = scale;
        genVAO(c, font);
    }
    private void genVAO(char c, Font font)
    {
        mInfo = font.getCharInfo((int)c);
        int[] tmp = new int[1];
        glGenBuffers(1,tmp);
        vbuff = tmp[0];
        glBindBuffer(GL_ARRAY_BUFFER,vbuff);
        byte[] bb = font.getMeshForChar(c).array();
        glBufferData(GL_ARRAY_BUFFER,bb.length,bb,GL_STATIC_DRAW);
        glGenVertexArrays(1,tmp);
        vao = tmp[0];
        glBindVertexArray(vao);
        //set the vao data
        glEnableVertexAttribArray(Program.POSITION_INDEX);
        glEnableVertexAttribArray(Program.TEXCOORD_INDEX);
        glEnableVertexAttribArray(Program.NORMAL_INDEX);
        glVertexAttribPointer(Program.POSITION_INDEX, 3, GL_FLOAT, false, 8*4,   0);
        glVertexAttribPointer(Program.TEXCOORD_INDEX, 2, GL_FLOAT, false, 8*4,   3*4);
        glVertexAttribPointer(Program.NORMAL_INDEX, 3, GL_FLOAT, false, 8*4,     5*4);
        glBindVertexArray(0);
    }
    public void draw(Program prog)
    {
        prog.setUniform("worldMatrix", mul(scaling(new vec3(mScale, mScale, 1.0f)), translation(-1.0f + (mX / 1920.0f) * 2, 1.0f - (mY / 1080.0f) * 2, 0.0f)));
        prog.setUniform("projMatrix", mat4.identity());
        prog.setUniform("viewMatrix", mat4.identity());
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES,0,6);
        int[] temp = {vbuff};
        glDeleteBuffers(1, temp);
        temp[0] = vao;
        glDeleteVertexArrays(1, temp);
    }
}
