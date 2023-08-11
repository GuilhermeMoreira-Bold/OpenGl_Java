package com.vortex.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Loader {
    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    

    public RawModel loadtoVAO(float[] positions,float[] textureCoordinates,int[] indices){
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3,positions);
        storeDataInAttributeList(1, 2,textureCoordinates);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    public void cleaneUp(){
        for(int vao:vaos){
            GL30.glDeleteVertexArrays(vao);
        }
        for(int vbo:vbos){
            GL15.glDeleteBuffers(vbo);
        }
    }

    private int createVAO (){
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeDataInAttributeList(int attributeNumber,int coordinateSize, float[] data){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        //FloatBuffer buffer  = storeDataFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber,coordinateSize,GL11.GL_FLOAT, false, 0,0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO(){
        GL30.glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int [] indices){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }
    private IntBuffer storeDataInIntBuffer(int [] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
    public void loadTexture(String filePath){
        Texture texture = new Texture(filePath);
        int textureId = texture.getTexture();
        glBindTexture(GL_TEXTURE_2D, textureId);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex2f(0, 0); // Canto superior esquerdo
        glTexCoord2f(1, 0); glVertex2f(texture.getWidth(), 0); // Canto superior direito
        glTexCoord2f(1, 1); glVertex2f(texture.getWidth(), texture.getHeight()); // Canto inferior direito
        glTexCoord2f(0, 1); glVertex2f(0,texture.getHeight()); // Canto inferior esquerdo
        glEnd();
    }

    public void clearTexture(){
        glBindTexture(GL_TEXTURE_2D,0);
    }
}