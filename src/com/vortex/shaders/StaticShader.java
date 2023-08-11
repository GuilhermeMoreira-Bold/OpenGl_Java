package com.vortex.shaders;

public class StaticShader extends ShaderProgam {

    private static final String VERTEX_FILE = "src/com/vortex/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "src/com/vortex/shaders/fragmentShader.txt";

    public StaticShader(){
        super(VERTEX_FILE, FRAGMENT_FILE);
    }


    @Override
    protected void bindAttributes(){
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

}
