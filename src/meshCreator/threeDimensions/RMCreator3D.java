package meshCreator.threeDimensions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import meshCreator.Forest;
import meshCreator.GsonAdapters;
import meshCreator.Levels;
import meshCreator.Side;
import meshCreator.twoDimensions.RMCreator2D;

public class RMCreator3D extends Application {

	private RMScenePane panel;
	private Forest forest;
	Stage primary_stage;

	public void start(Stage primaryStage) {
		primary_stage = primaryStage;
		forest = new Forest(3);
		setupToolBar();
		panel = new RMScenePane(forest);
		BorderPane root = new BorderPane();
		VBox vbox = new VBox();
		vbox.getChildren().addAll(setupMenu(), setupToolBar());
		root.setTop(vbox);

		root.setCenter(panel);
		Scene scene = new Scene(root, 500, 500);
		setKeyShortcuts(scene);

		primaryStage.setTitle("3D Refined Mesh Creator");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void setKeyShortcuts(Scene scene) {
		scene.setOnKeyTyped(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				switch (e.getCharacter()) {
				case "a":
					panel.moveSelected(Side.WEST());
					break;
				case "q":
					panel.moveSelected(Side.EAST());
					break;
				case "s":
					panel.moveSelected(Side.SOUTH());
					break;
				case "w":
					panel.moveSelected(Side.NORTH());
					break;
				case "d":
					panel.moveSelected(Side.BOTTOM());
					break;
				case "e":
					panel.moveSelected(Side.TOP());
					break;
				}
			}
		});
	}

	private MenuBar setupMenu() {
		Menu file_menu = new Menu("File");
		MenuBar menuBar = new MenuBar();
		Menu new_menu = new Menu("New");
		MenuItem new2d_item = new MenuItem("2D Mesh");
		new2d_item.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				Stage stage = new Stage();
				RMCreator2D amc = new RMCreator2D();
				amc.start(stage);
			}
		});
		MenuItem new3d_item = new MenuItem("3D Mesh");
		new3d_item.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				Stage stage = new Stage();
				RMCreator3D amc = new RMCreator3D();
				amc.start(stage);
			}
		});
		new_menu.getItems().addAll(new2d_item, new3d_item);
		MenuItem save_item = new MenuItem("Save");
		save_item.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				save();
			}
		});
		MenuItem open_item = new MenuItem("Open");
		open_item.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				open();
			}
		});

		file_menu.getItems().addAll(new_menu, save_item, open_item);
		menuBar.getMenus().add(file_menu);
		return menuBar;
	}

	private ToolBar setupToolBar() {
		Button refine_button = new Button("Refine");
		refine_button.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				panel.refineSelected();
			}
		});

		Button coarsen_button = new Button("Coarsen");
		coarsen_button.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				panel.coarsenSelcted();
			}
		});

		ToggleButton x_rot = new ToggleButton("X");
		x_rot.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				panel.toggleXRotate();
			}
		});
		ToggleButton y_rot = new ToggleButton("Y");
		y_rot.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				panel.toggleYRotate();
			}
		});
		return new ToolBar(refine_button, coarsen_button, x_rot, y_rot);
	}

	private void save() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Mesh to File");
		File out_file = fileChooser.showSaveDialog(primary_stage);
		if (out_file != null) {
			outputMeshGraph(out_file);
		}
	}

	private void open() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Load Mesh from File");
		File out_file = fileChooser.showOpenDialog(primary_stage);
		if (out_file != null) {
			readMeshGraph(out_file);
		}
	}

	private void outputMeshGraph(File out_file) {
		Gson gson = GsonAdapters.getNewGson();
		gson = GsonAdapters.getNewGson();

		Levels levels = new Levels(forest);

		try {
			FileWriter writer = new FileWriter(out_file);
			gson.toJson(levels, writer);
			writer.flush();
			writer.close();
		} catch (JsonIOException | IOException e) {
			e.printStackTrace();
		}
	}

	private void readMeshGraph(File out_file) {
	}
}
