package view;

import com.sun.xml.internal.ws.api.ResourceLoader;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import model.DataHolder;
import model.Texture;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;
import services.Logger;
import sun.font.TrueTypeFont;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static input.KeyPressed.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();


    private String glslVersion = null;
    private long windowPtr;
    private final ImGuiLayer imguiLayer;

    private static final int WINDOW_WIDTH = 1080;
    private static final int WINDOW_HEIGHT = 1080;

    private static final int NUM_HORIZ_BOXES = 20;
    private static final int NUM_VERT_BOXES = 20;

    private static final int NUM_HORIZ_LINES = NUM_HORIZ_BOXES - 1;
    private static final int NUM_VERT_LINES = NUM_VERT_BOXES - 1;

    private static final int BOX_WIDTH = WINDOW_WIDTH / NUM_VERT_LINES;
    private static final int BOX_HEIGHT = WINDOW_HEIGHT / NUM_HORIZ_LINES;

    //Texture texture = new Texture("src/main/resources/font1.png");

    int playerPosX = 11;
    int playerPosY = 11;

    private TrueTypeFont font;


    public Window(ImGuiLayer layer) {
        imguiLayer = layer;
    }

    public void init() {

        DataHolder.initMapData();
        DataHolder.initPlayerData();
        DataHolder.setMapData(0,0, 2, "tree");
        DataHolder.setMapData(1,0, 2, "tree");
        DataHolder.setMapData(2,0, 2, "tree");
        DataHolder.setMapData(10,10, 2, "tree");
        DataHolder.setMapData(15,15, 2, "tree");
        DataHolder.setMapData(8,10, 2, "tree");
        DataHolder.setMapData(10,8, 2, "tree");
        DataHolder.setMapData(9,8, 2, "tree");
        DataHolder.setMapData(8,8, 2, "tree");
        DataHolder.setMapData(10,10, 1, "LB");


        initWindow();
        initImGui();
        imGuiGlfw.init(windowPtr, true);
        imGuiGl3.init(glslVersion);
        glfwSetInputMode(windowPtr, GLFW_STICKY_KEYS, GLFW_TRUE);
        glfwSetKeyCallback(windowPtr, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if(action == 0){
                    pressed(key);
                }
            }
        });
    }

    public void destroy() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
        Callbacks.glfwFreeCallbacks(windowPtr);
        glfwDestroyWindow(windowPtr);
        glfwTerminate();
    }
    private void initWindow() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() ) {
            Logger.logAndPrint("Unable to initialize GLFW", Logger.LOG_SEVERITY.CRIT);
            System.exit(-1);
        }

        glslVersion = "#version 130";
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        windowPtr = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "My Window", NULL, NULL);

        if (windowPtr == NULL) {
            Logger.logAndPrint("Unable to create window", Logger.LOG_SEVERITY.CRIT);
            System.exit(-1);
        }

        glfwMakeContextCurrent(windowPtr);
        glfwSwapInterval(1);
        glfwShowWindow(windowPtr);

        GL.createCapabilities();


    }
    private void initImGui() {
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
    }
    public void buildMagicTriangle(){
        glBegin(GL_TRIANGLES);
        glVertex2f(0.200f+DataHolder.getInstance().getDataOrZeroFloat("count")/10, 0.100f);
        glVertex2f(0.00f, 0.400f);
        glVertex2f(0.00f, 0.600f);
        glEnd();
    }
    public void buildTriangle(float x1, float y1, List<Float> colour, int scale){
        float triangleSize = 0.015f;
        glColor3f(colour.get(0), colour.get(1), colour.get(2));
        glBegin(GL_TRIANGLES);
        glVertex2f(x1, y1 + triangleSize * 2 * scale);
        glVertex2f(x1 + triangleSize * scale, y1);
        glVertex2f(x1 - triangleSize * scale, y1);
        glEnd();
    }
    public void buildTriangleTex(float x1, float y1, List<Float> colour, int scale){
        float triangleSize = 0.015f;
        //texture.bind();
        
        glBegin(GL_TRIANGLES);
       //glTexCoord2f(0,0);
        glVertex2f(x1, y1 + triangleSize * 2 * scale);
        glVertex2f(x1 + triangleSize * scale, y1);
        glVertex2f(x1 - triangleSize * scale, y1);
        glEnd();
    }
    public void buildPlayer(){
        glColor3f(1, 0, 0);
        glBegin(GL_TRIANGLES);
        float playerFloatPosX = playerPosX * 0.01f;
        float playerFloatPosY = playerPosY * 0.01f;
        glVertex2f(playerFloatPosX, playerFloatPosY + 0.03f);
        glVertex2f(playerFloatPosX + 0.015f, playerFloatPosY);
        glVertex2f(playerFloatPosX - 0.015f, playerFloatPosY);
        glEnd();
    }
    public void buildLine(float x1, float y1, float x2, float y2){
        glColor3f(0, 0, 1);
        glBegin(GL_LINES);
        //glVertex2f(0.200f+model.DataHolder.getInstance().getDataOrZeroFloat("count")/10, 0.100f);
        glVertex2f(x1, y1);
        glVertex2f(x2, y2);
        glEnd();
    }
    public void buildQuadPoints(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4){
        glColor3f(0, 1, 1);
        glBegin(GL_QUADS);
        //glVertex2f(0.200f+model.DataHolder.getInstance().getDataOrZeroFloat("count")/10, 0.100f);
        glVertex2f(x1, y1);
        glVertex2f(x2, y2);
        glVertex2f(x3, y3);
        glVertex2f(x4, y4);
        glEnd();
    }
    public void buildQuadLine(float x1, float y1, float x2, float y2, float width, float height, List<Float> colour){
        glColor3f(colour.get(1), colour.get(2), colour.get(3));
        glBegin(GL_QUADS);
        glVertex2f(x1, y1);
        glVertex2f(x1 + width / 2, y1);
        glVertex2f(x2 + width / 2, y2);
        glVertex2f(x2, y2);
        glEnd();
    }
    public static double mapRange(double a1, double a2, double b1, double b2, double s){
        return b1 + ((s - a1)*(b2 - b1))/(a2 - a1);
    }
    public static float mapRangeToFloat(double a1, double a2, double b1, double b2, double s){
        return (float) (b1 + ((s - a1)*(b2 - b1))/(a2 - a1));
    }
    public static float pixelIntToFloat(int pixels, boolean directionX){
        if(directionX){
            return (float) mapRange(0, WINDOW_WIDTH, -1, 0, pixels);
        } else {
            return (float) mapRange(0, WINDOW_HEIGHT, -1, 0, pixels);
        }
    }
    public void buildMap(){
        for(int lineX = 0; lineX < NUM_HORIZ_LINES; lineX++){
            for(int lineY = 0; lineY < NUM_VERT_LINES; lineY++){
                if(DataHolder.getMapData()[lineX][lineY].getTileID() == 1){
                    float boxOffsetX = mapRangeToFloat(0, WINDOW_WIDTH, 0, 2, BOX_WIDTH / 4);
                    float boxOffsetY = mapRangeToFloat(0, WINDOW_HEIGHT, 0, 2, BOX_HEIGHT / 4);
                    float trianglePosX = mapRangeToFloat(0, NUM_HORIZ_BOXES, -1, 0, lineX) + boxOffsetX;
                    float trianglePosY = mapRangeToFloat(0, NUM_VERT_BOXES, 0, 1, lineY) + boxOffsetY;
                    List<Float> colour = new ArrayList<>();
                    colour.add(0, 1f);
                    colour.add(1, 0f);
                    colour.add(2, 0f);
                    buildTriangleTex(trianglePosX, trianglePosY, colour, 1);
                } else if(DataHolder.getMapData()[lineX][lineY].getTileID() == 2){
                    float boxOffsetX = mapRangeToFloat(0, WINDOW_WIDTH, 0, 2, BOX_WIDTH / 4);
                    float boxOffsetY = mapRangeToFloat(0, WINDOW_HEIGHT, 0, 2, BOX_HEIGHT / 4);
                    float trianglePosX = mapRangeToFloat(0, NUM_VERT_LINES + 1, -1, 0, lineX) + boxOffsetX;
                    float trianglePosY = mapRangeToFloat(0, NUM_HORIZ_LINES + 1, 0, 1, lineY) + boxOffsetY;
                    List<Float> colour = new ArrayList<>();
                    colour.add(0, 0f);
                    colour.add(1, 1f);
                    colour.add(2, 0f);
                    buildTriangle(trianglePosX, trianglePosY, colour, 1);
                }
            }
        }
    }

    public void buildHUD(){
        int playerPosX = (int) DataHolder.getPlayerData().get("playerPosX");
        int playerPosY = (int) DataHolder.getPlayerData().get("playerPosY");
        if(DataHolder.getMapData()[playerPosX][playerPosY + 1].getTileID() == 2){
            List<Float> colour = new ArrayList<>();
            colour.add(0, 0f);
            colour.add(1, 1f);
            colour.add(2, 0f);
            buildTriangle(0.5f, 0.5f, colour, 3);
        }
////            InputStream inputStream = new ResourceLoader("res/8-Bit-Madness.ttf");
////            Font awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
////            awtFont = awtFont.deriveFont(36f);
////            font = new TrueTypeFont(awtFont, false);
////            font.draw(0, 0, "This text is showing");
////        }
        //Texture texture = new Texture("../resources/font1.png");
        //texture.bind();
    }





    public void run() {
        while (!glfwWindowShouldClose(windowPtr)) {

            glClearColor(0.1f, 0.09f, 0.1f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            imGuiGlfw.newFrame();
            ImGui.newFrame();
            imguiLayer.imgui();
            ImGui.render();
            imGuiGl3.renderDrawData(ImGui.getDrawData());

            buildMapGridLines();
            buildMap();
            buildHUD();
            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupWindowPtr = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                GLFW.glfwMakeContextCurrent(backupWindowPtr);
            }
            glfwSwapBuffers(windowPtr);
            glfwWaitEventsTimeout(1);
        }
    }

    public void buildMapGridLines(){
        float horizontalLineIncrement = 1 / (float) (NUM_VERT_BOXES);
        float verticalLineIncrement = 1 / (float) (NUM_HORIZ_BOXES);
        float lineWidth = 0.001f;
        for(float x = -1f; x <= 0f; x += horizontalLineIncrement){
            buildQuadPoints(x, 1f, x + lineWidth, 1f, x + lineWidth, 0f, x, 0f);
        }
        for(float y = 1f; y >= 0f; y -= verticalLineIncrement){
            buildQuadPoints(-1f, y, -1f, y + lineWidth, 0f, y + lineWidth, 0f, y);
        }
    }
}