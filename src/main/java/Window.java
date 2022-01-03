import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static java.lang.Float.parseFloat;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private String glslVersion = null;
    private long windowPtr;
    private ImGuiLayer imguiLayer;

    private float offset = 0f;

    public Window(ImGuiLayer layer) {
        imguiLayer = layer;
    }

    public void init() {
        initWindow();
        initImGui();
        imGuiGlfw.init(windowPtr, true);
        imGuiGl3.init(glslVersion);
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
        windowPtr = glfwCreateWindow(1920, 1080, "My Window", NULL, NULL);

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

    public void buildTriangle(){
        glBegin(GL_TRIANGLES);
        glVertex2f(0.200f+DataHolder.getInstance().getDataOrZeroFloat("count")/10, 0.100f);
        glVertex2f(0.00f, 0.400f);
        glVertex2f(0.00f, 0.600f);
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

    public void run() {
        while (!glfwWindowShouldClose(windowPtr)) {
            offset += 0.01f;
            glClearColor(0.1f, 0.09f, 0.1f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            imGuiGlfw.newFrame();
            ImGui.newFrame();

            imguiLayer.imgui();

            ImGui.render();
            imGuiGl3.renderDrawData(ImGui.getDrawData());
            glColor3f(1, 1, 1);
            buildTriangle();
            int linesVertical = 20;
            //todo make line increment an int then convert it to float. currently oes not work beause of int and float bug error
            float horizontalLineIncrement = (100/linesVertical);
            for(float x = -1f; x < 1.0f; x += horizontalLineIncrement){
                buildLine(x, 1f, x, -1f);
            }
            buildLine(0.1f, 0.1f, 0.9f, 0.5f);


            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupWindowPtr = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                GLFW.glfwMakeContextCurrent(backupWindowPtr);
            }

            GLFW.glfwSwapBuffers(windowPtr);
            GLFW.glfwPollEvents();
        }
    }
}