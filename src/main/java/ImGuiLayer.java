import imgui.ImGui;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class ImGuiLayer {
    private boolean showText = false;

    public void imgui() {
        ImGui.begin("Cool Window");

        if (ImGui.button("I am a button")) {
            System.out.println("TEST");
            showText = true;
            //DataHolder.getInstance().addData("count", "1");
            Map<String, String> map = new HashMap<>();
            map.put("count", "1");
            DataHolder.getInstance().setData(map);
        }

        else if (ImGui.button("buttonf")) {
            System.out.println("YES");
            String val = DataHolder.getInstance().getData().get("count");
            String newVal = String.valueOf((parseInt(val) + 1));
            DataHolder.addData("count", newVal);
            System.out.println(newVal);
        }

        if (showText) {
            if (ImGui.button("Stop showing text")) {
                System.out.println("1");
                showText = false;
            }
            if (ImGui.button("Stop showing tefxt")) {
                System.out.println("2");
                showText = false;
            }
        }

        ImGui.end();
    }
}