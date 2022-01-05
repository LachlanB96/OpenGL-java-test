import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import input.KeyPressed;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private String glslVersion = null;
    private long windowPtr;
    private ImGuiLayer imguiLayer;

    private static final int WINDOW_WIDTH = 1080;
    private static final int WINDOW_HEIGHT = 1080;

    private static final int NUM_HORIZ_BOXES = 20;
    private static final int NUM_VERT_BOXES = 20;

    private static final int NUM_HORIZ_LINES = NUM_HORIZ_BOXES - 1;
    private static final int NUM_VERT_LINES = NUM_VERT_BOXES - 1;

    private static final int BOX_WIDTH = WINDOW_WIDTH / NUM_VERT_LINES;
    private static final int BOX_HEIGHT = WINDOW_HEIGHT / NUM_HORIZ_LINES;

    private static Tile[][] mapData = new Tile[NUM_HORIZ_BOXES][NUM_VERT_BOXES];

    int playerPosX = 11;
    int playerPosY = 11;


    public Window(ImGuiLayer layer) {
        imguiLayer = layer;
    }

    public void init() {

        for(int x = 0; x < NUM_VERT_BOXES; x++){
            for(int y = 0; y < NUM_HORIZ_BOXES; y++){
                mapData[x][y] = new Tile(0, "NA");
            }
        }
        //Arrays.fill(mapData[0], new Tile(0, "NA"));
        System.out.println(mapData[0][0].getTileID());
        mapData[4][3] = new Tile(2, "3");
        System.out.println(mapData[0][0].getTileID());
        mapData[1][0] = new Tile(2, "3");
        mapData[1][1] = new Tile(2, "3");
        mapData[1][2] = new Tile(2, "3");
        mapData[1][3] = new Tile(2, "3");
        mapData[1][4] = new Tile(2, "3");
        System.out.println(mapData[0][0].getTileID());
        mapData[1][5] = new Tile(2, "3");
        mapData[10][10] = new Tile(2, "3");
        mapData[18][18] = new Tile(2, "3");
        mapData[19][19] = new Tile(2, "3");
        mapData[0][18] = new Tile(2, "3");
        mapData[18][0] = new Tile(2, "3");
        mapData[19][0] = new Tile(2, "3");
        mapData[11][11] = new Tile(1, "LB");
        System.out.println(mapData[0][0].getTileID());
        System.out.println(mapData[11][11].getTileID());
        System.out.println(mapData[19][19].getTileID());
        initWindow();
        initImGui();
        imGuiGlfw.init(windowPtr, true);
        imGuiGl3.init(glslVersion);
        glfwSetInputMode(windowPtr, GLFW_STICKY_KEYS, GLFW_TRUE);
        glfwSetKeyCallback(windowPtr, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                System.out.printf("key: [%s], action: [%s]", key, action);
                if(action == 0){
                    KeyPressed.pressed(key);
                }
                if(key == 87 && action == 1){
                    if(mapData[playerPosX + 1][playerPosY].getTileID() == 0){
                        mapData[playerPosX + 1][playerPosY] = new Tile(1, "LB");
                        mapData[playerPosX][playerPosY] = new Tile(0, "Dead");
                        playerPosX++;
                    }
                } else if(key == 65 && action == 1){
                    if(mapData[playerPosX][playerPosY - 1].getTileID() == 0){
                        mapData[playerPosX][playerPosY - 1] = new Tile(1, "LB");
                        mapData[playerPosX][playerPosY] = new Tile(0, "Dead");
                        playerPosY--;
                    }
                } else if(key == 83){
                    if(mapData[playerPosX - 1][playerPosY].getTileID() == 0){
                        mapData[playerPosX - 1][playerPosY] = new Tile(1, "LB");
                        mapData[playerPosX][playerPosY] = new Tile(0, "Dead");
                        playerPosX--;
                    }
                } else if(key == 68){
                    if(mapData[playerPosX][playerPosY + 1].getTileID() == 0){
                        mapData[playerPosX][playerPosY + 1] = new Tile(1, "LB");
                        mapData[playerPosX][playerPosY] = new Tile(0, "Dead");
                        playerPosY++;
                    }
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
            System.out.println("Unable to initialize GLFW");
            System.exit(-1);
        }

        glslVersion = "#version 130";
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        windowPtr = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "My Window", NULL, NULL);

        if (windowPtr == NULL) {
            System.out.println("Unable to create window");
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
    public void buildTriangle(float x1, float y1, List<Float> colour){
        glColor3f(colour.get(0), colour.get(1), colour.get(2));
        glBegin(GL_TRIANGLES);
        glVertex2f(x1, y1 + 0.03f);
        glVertex2f(x1 + 0.015f, y1);
        glVertex2f(x1 - 0.015f, y1);
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
        //glVertex2f(0.200f+DataHolder.getInstance().getDataOrZeroFloat("count")/10, 0.100f);
        glVertex2f(x1, y1);
        glVertex2f(x2, y2);
        glEnd();
    }
    public void buildQuadPoints(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4){
        glColor3f(0, 1, 1);
        glBegin(GL_QUADS);
        //glVertex2f(0.200f+DataHolder.getInstance().getDataOrZeroFloat("count")/10, 0.100f);
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
            return (float) mapRange(0, WINDOW_WIDTH, -1, 1, pixels);
        } else {
            return (float) mapRange(0, WINDOW_HEIGHT, -1, 1, pixels);
        }
    }
    public void buildMap(){
        for(int lineY = 0; lineY < NUM_VERT_LINES; lineY++){
            for(int lineX = 0; lineX < NUM_HORIZ_LINES; lineX++){
                if(mapData[lineX][lineY].getTileID() == 1){
                    float boxOffsetX = mapRangeToFloat(0, WINDOW_WIDTH, 0, 2, BOX_WIDTH / 2);
                    float boxOffsetY = mapRangeToFloat(0, WINDOW_HEIGHT, 0, 2, BOX_HEIGHT / 2);
                    float trianglePosX = mapRangeToFloat(0, NUM_VERT_LINES + 1, -1, 1, lineY) + boxOffsetX;
                    float trianglePosY = mapRangeToFloat(0, NUM_HORIZ_LINES + 1, -1, 1, lineX) + boxOffsetY;
                    List<Float> colour = new ArrayList<>();
                    colour.add(0, 1f);
                    colour.add(1, 0f);
                    colour.add(2, 0f);
                    buildTriangle(trianglePosX, trianglePosY, colour);
                    //System.out.println(mapRange(0, linesVertical, -1, 1, cell));
                } else if(mapData[lineX][lineY].getTileID() == 2){
                    float boxOffsetX = mapRangeToFloat(0, WINDOW_WIDTH, 0, 2, BOX_WIDTH / 2);
                    float boxOffsetY = mapRangeToFloat(0, WINDOW_HEIGHT, 0, 2, BOX_HEIGHT / 2);
                    float trianglePosX = mapRangeToFloat(0, NUM_VERT_LINES + 1, -1, 1, lineY) + boxOffsetX;
                    float trianglePosY = mapRangeToFloat(0, NUM_HORIZ_LINES + 1, -1, 1, lineX) + boxOffsetY;
                    List<Float> colour = new ArrayList<>();
                    colour.add(0, 0f);
                    colour.add(1, 1f);
                    colour.add(2, 0f);
                    buildTriangle(trianglePosX, trianglePosY, colour);
                }
            }
        }
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
            glColor3f(1, 1, 1);
            buildMagicTriangle();

            float horizontalLineIncrement = (float) 1 / (float) ((NUM_VERT_LINES + 1) / 2);
            float verticalLineIncrement = (float) 1 / (float) ((NUM_HORIZ_LINES + 1) / 2);
            float lineWidth = 0.001f;
            for(float x = -1f; x <= 1.0f; x += horizontalLineIncrement){
                buildQuadPoints(x, -1f, x + lineWidth, -1f, x + lineWidth, 1f, x, 1f);
            }
            for(float y = -1f; y <= 1.0f; y += verticalLineIncrement){
                buildQuadPoints(1f, y, 1f, y + lineWidth, -1f, y + lineWidth, -1f, y);
            }
            buildPlayer();
            buildMap();
            List<Float> colour = new ArrayList<>();
            colour.add(1f);
            colour.add(0f);
            colour.add(0f);
            //buildQuadLine(0f, 0f, 0.5f, 0.5f, 0.01f, 0.01f, colour);
            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupWindowPtr = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                GLFW.glfwMakeContextCurrent(backupWindowPtr);
            }
            glfwSwapBuffers(windowPtr);
            glfwWaitEventsTimeout(0.7);
        }
    }
}