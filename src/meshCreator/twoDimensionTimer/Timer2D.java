package meshCreator.twoDimensionTimer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import meshCreator.GsonAdapters;
import meshCreator.Info;
import meshCreator.Patch;
import meshCreator.Timer;
import meshCreator.Timing;
import meshCreator.threeDimensions.RMCreator3D;

public class Timer2D extends Application {

	private TimerPane panel;
	// private JFileChooser fc;
	private ToolBar toolbar;
	Stage primary_stage;
	TreeView<PaneSupplier> treeView;

	public void start(Stage primaryStage) {
		primary_stage = primaryStage;
		toolbar = new ToolBar();

		Map<Integer, Color> rank_color_map = new HashMap<Integer, Color>();
		rank_color_map.put(0, Color.WHITE);
		rank_color_map.put(1, Color.AQUA);
		rank_color_map.put(2, Color.GREEN);
		rank_color_map.put(3, Color.ORANGE);
		panel = new TimerPane(new ArrayList<Patch>(), rank_color_map, patch -> {
		});
		BorderPane root = new BorderPane();
		root.setCenter(panel);

		treeView = new TreeView<PaneSupplier>();
		treeView.setShowRoot(false);
		treeView.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> root.setCenter(newValue.getValue().get()));
		root.setLeft(treeView);

		VBox vbox = new VBox();
		Menu file_menu = new Menu("File");
		MenuBar menuBar = new MenuBar();
		MenuItem open_item = new MenuItem("Open");
		open_item.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				open();
			}
		});
		file_menu.getItems().addAll(open_item);
		menuBar.getMenus().add(file_menu);
		vbox.getChildren().addAll(menuBar, toolbar);
		root.setTop(vbox);

		Scene scene = new Scene(root, 500, 500);
		primaryStage.setTitle("2D Refined Mesh Creator");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void open() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		File in_file = fileChooser.showOpenDialog(primary_stage);
		if (in_file != null) {
			readTimer(in_file);
		}
	}

	private static String mmaString(double min, double max, double avg) {
		String s = "min: " + Double.toString(min) + "\n";
		s += "max: " + Double.toString(max) + "\n";
		s += "avg: " + Double.toString(avg);
		return s;
	}

	private void setTimer(Timer timer) {
		panel.setPatches(timer.domains.get(0));
		Map<Integer, String> text_map = new HashMap<Integer, String>();
		for (Timing timing : timer.timings.get(2).timings.get(0).timings.get(0).timings) {
			String s = "min: " + Double.toString(timing.infos.get(0).min) + "\n";
			s += "max: " + Double.toString(timing.infos.get(0).max) + "\n";
			s += "avg: " + Double.toString(timing.infos.get(0).sum / timing.infos.get(0).num_calls);
			text_map.put(timing.patch_id, s);
		}
		panel.setPatchText(text_map);
		TreeItem<PaneSupplier> root = new TreeItem<>();
		AddTimingsToItem(timer.domains, timer.timings, root);
		treeView.setRoot(root);
	};

	private static void AddStats(TreeItem<PaneSupplier> item, Map<Integer, ArrayList<Patch>> domains,
			Map<Pair<Integer, String>, Map<String, TreeItem<PaneSupplier>>> domain_item_children, Timing timing,
			String name, double min, double max, double avg) {
		if (domain_item_children.get(new Pair<>(timing.domain_id, timing.name)) == null) {
			domain_item_children.put(new Pair<>(timing.domain_id, timing.name), new HashMap<>());
		}
		TreeItem<PaneSupplier> tps_item = domain_item_children.get(new Pair<>(timing.domain_id, timing.name)).get(name);
		if (tps_item == null) {
			tps_item = new TreeItem<>(new TimerPaneSupplier(name, domains.get(timing.domain_id)));
			domain_item_children.get(new Pair<>(timing.domain_id, timing.name)).put(name, tps_item);
			item.getChildren().add(tps_item);
		}
		TimerPaneSupplier tps = (TimerPaneSupplier) tps_item.getValue();
		tps.addText(timing.patch_id, mmaString(min, max, avg));
	}

	private static void AddTimingsToItem(Map<Integer, ArrayList<Patch>> domains, ArrayList<Timing> timings,
			TreeItem<PaneSupplier> parent) {
		if (timings != null) {
			Map<Pair<Integer, String>, TreeItem<PaneSupplier>> domain_item_map = new HashMap<>();
			Map<Pair<Integer, String>, Map<String, TreeItem<PaneSupplier>>> domain_item_children = new HashMap<>();
			for (Timing timing : timings) {
				TreeItem<PaneSupplier> item = domain_item_map.get(new Pair<>(timing.domain_id, timing.name));
				if (item == null) {
					item = new TreeItem<>(new EmptyPaneSupplier(timing.name));
					domain_item_map.put(new Pair<>(timing.domain_id, timing.name), item);
					parent.getChildren().add(item);
				}
				if (timing.patch_id != null) {
					AddStats(item, domains, domain_item_children, timing, "Time", timing.min, timing.max,
							timing.sum / timing.num_calls);
					if (timing.infos != null) {
						for (Info info : timing.infos) {
							AddStats(item, domains, domain_item_children, timing, info.name, info.min, info.max,
									info.sum / info.num_calls);
						}
					}

				}
				AddTimingsToItem(domains, timing.timings, item);
			}
		}
	}

	private void readTimer(File in_file) {
		Gson gson = GsonAdapters.getNewGson();

		try {
			FileReader reader = new FileReader(in_file);
			Timer timer = gson.fromJson(reader, Timer.class);
			reader.close();
			setTimer(timer);
		} catch (JsonIOException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
