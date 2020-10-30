package meshCreator.twoDimensionTimer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import meshCreator.Patch;

public class TimerPaneSupplier extends PaneSupplier {
    private Map<Integer, String> text_map = new HashMap<Integer, String>();
    private ArrayList<Patch> patches;

    public TimerPaneSupplier(String name, ArrayList<Patch> patches) {
        super(name);
        this.patches = patches;
    }

    @Override
    public Node get() {
        Map<Integer, Color> rank_color_map = new HashMap<Integer, Color>();
        rank_color_map.put(0, Color.WHITE);
        rank_color_map.put(1, Color.AQUA);
        rank_color_map.put(2, Color.GREEN);
        rank_color_map.put(3, Color.ORANGE);
        TimerPane pane = new TimerPane(patches, rank_color_map, patch -> {
        });
        pane.setPatches(patches);
        pane.setPatchText(text_map);
        return pane;
    }

    public void addText(Integer patch_id, String text) {
        text_map.put(patch_id, text);
    }

}
