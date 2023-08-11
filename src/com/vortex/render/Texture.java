package com.vortex.render;

import static org.lwjgl.opengl.GL11.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import com.vortex.util.BufferUtils;

public class Texture {
 private int width, height, texture;
 

 public Texture(String filePath){
    texture = load(filePath);
 }

 private int load(String filePath) {
    int [] pixels = null;
    try{
        BufferedImage image = ImageIO.read(new FileInputStream(filePath));
        width = image.getWidth();
        height = image.getHeight();
        pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0,width);
    }catch(IOException e){
        e.printStackTrace();
    }

    int[] data = new int[width * height];
    for(int i = 0; i < width * height; i ++){
        int a =(pixels[i] & 0xff000000) >> 24; // move 24 bits and store it
        int r =(pixels[i] & 0xff0000) >> 16; // move 16 bits and store it
        int g =(pixels[i] & 0xff00) >> 8; // the same
        int b =(pixels[i] & 0xff);// the same

        data[i] = a << 24 | b << 16 | g << 8 | r;
    }
    int tex = glGenTextures();
    glBindTexture(GL_TEXTURE_2D, tex);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data));
    glBindTexture(GL_TEXTURE_2D, 0);
    return tex;
 }



    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getTexture() {
        return this.texture;
    }

}
