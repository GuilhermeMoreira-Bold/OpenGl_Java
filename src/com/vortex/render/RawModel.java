package com.vortex.render;

public class RawModel {
    private int vaoID;
    private int vertexCount;

    public RawModel(int vaoID,int vertexCount){
        this.vaoID = vaoID;
        this. vertexCount = vertexCount;
    }

   

    /**
     * @return int return the vaoID
     */
    public int getVaoID() {
        return vaoID;
    }

    /**
     * @return int return the vertexCount
     */
    public int getVertexCount() {
        return vertexCount;
    }

}