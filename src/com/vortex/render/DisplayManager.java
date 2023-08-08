package com.vortex.render;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import com.vortex.shaders.StaticShader;

import java.nio.*;



import static org.lwjgl.glfw.Callbacks.*;
import static  org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.MemoryStack.*;


import java.awt.Dimension;
import java.awt.Toolkit;


public class DisplayManager {
    private  int width = 800, height = 600;
    private  long window;

    float[] vertices = {
        -0.5f, 0.5f, 0f,
        -0.5f, -0.5f, 0f,
        0.5f, -0.5f, 0f,
        0.5f, 0.5f, 0f,
    };
    int[] indices ={
        0,1,3,
        3,1,2
    };

    Loader loader;
    Renderer renderer;
    RawModel model;
    StaticShader shader;


    public void run(){
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		//glfwSetErrorCallback(null).free();
    }
    
    public  void init(){

        GLFWErrorCallback.createPrint(System.err).set();

        // getWindowSize();

        
        if(!glfwInit()){throw new IllegalArgumentException("Error initializing DisplayManager");}


        glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        
        this.window = glfwCreateWindow(this.width, this.height, "Void Vortex", NULL, NULL);
        if(this.window == NULL){
            throw new RuntimeException("Could not create window");
        }
        
        glfwSetFramebufferSizeCallback(this.window, (window, width, height) ->{
            this.width = width;
            this.height = height;
            glViewport(0, 0, width, height);
        });

        glfwSetKeyCallback(this.window, (window,key,scancode,action, mods) ->{
            if(key == GLFW_KEY_ESCAPE &&  action == GLFW_RELEASE){
                glfwSetWindowShouldClose(window, true);
            }
        });
        try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
        GL.createCapabilities();
        

        loader = new Loader();
        renderer = new Renderer();
        model = loader.loadtoVAO(vertices, indices);
        shader = new StaticShader();

		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
    }
    
    public  void destroyDisplay(){ 
        glfwDestroyWindow(this.window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();//free the error callback
    }

    private  void clearColor(){            
        glClearColor(0.4f, 0.5f, 0.9f, 1.0f);
    }

    private void loop(){
        clearColor();
        while(!glfwWindowShouldClose(this.window)){
            //glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
          // renderer.prepare();
           shader.start();
           renderer.render(model);
           shader.stop();
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        shader.cleanUp();
        loader.cleaneUp();
    }


    // private void getWindowSize(){
    //     Toolkit toolkit = Toolkit.getDefaultToolkit();
    //     Dimension screenSize = toolkit.getScreenSize();
    //     this.width = screenSize.width;
    //     this.height = screenSize.height;
    // }
}