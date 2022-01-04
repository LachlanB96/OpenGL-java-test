import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
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

    private static final int linesVertical = 20;
    private static final int linesHorizontal = 20;

    private static final int[][] mapData = new int[linesVertical][linesHorizontal];

    int playerPosX = 5;
    int playerPosY = 10;


    public Window(ImGuiLayer layer) {
        imguiLayer = layer;
    }

    public void init() {
        Arrays.fill(mapData[0], 0);
        mapData[4][3] = 1;
        System.out.println(mapData);
        initWindow();
        initImGui();
        imGuiGlfw.init(windowPtr, true);
        imGuiGl3.init(glslVersion);
        glfwSetInputMode(windowPtr, GLFW_STICKY_KEYS, GLFW_TRUE);
        glfwSetKeyCallback(windowPtr, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                System.out.println(key);
                if(key == 87){
                    playerPosY += 1;
                } else if(key == 65){
                    playerPosX -= 1;
                } else if(key == 83){
                    playerPosY -= 1;
                } else if(key == 68){
                    playerPosX += 1;
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
        windowPtr = glfwCreateWindow(1080, 1080, "My Window", NULL, NULL);

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
    public void buildTriangle(float x1, float y1){
        glColor3f(1, 1, 0);
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
    //TODO MAP the object in mapData to the screen using mapRange
    public void buildMap(){
        int rowCount = 0;
        for(int[] row : mapData){
            for(int cell : row){
                if(cell == 1){
                    System.out.println(row);
                    System.out.println(cell);
                    System.out.println(mapRange(0, linesVertical, -1, 1, cell));
                }
            }
            rowCount++;
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

            float horizontalLineIncrement = (float) 1 / (float) linesVertical * 2;
            float verticalLineIncrement = (float) 1 / (float) linesHorizontal * 2;
            float lineWidth = 0.001f;
            for(float x = -1f; x < 1.0f; x += horizontalLineIncrement){
                buildQuadPoints(x, -1f, x + lineWidth, -1f, x + lineWidth, 1f, x, 1f);
            }
            for(float y = -1f; y < 1.0f; y += verticalLineIncrement){
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