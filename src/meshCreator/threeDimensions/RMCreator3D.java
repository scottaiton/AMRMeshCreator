package meshCreator.threeDimensions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import meshCreator.twoDimensions.RMCreator2D;

public class RMCreator3D extends Application {

	private RMScenePane panel;
	private ToggleGroup mode_group;
	private Mode mode;
	private OctTree root;
	Stage primary_stage;

	public void start(Stage primaryStage) {
		primary_stage = primaryStage;
		root = new OctTree();
		setupToolBar();
		panel = new RMScenePane(root);
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
					panel.moveSelected(Side.WEST);
					break;
				case "q":
					panel.moveSelected(Side.EAST);
					break;
				case "s":
					panel.moveSelected(Side.SOUTH);
					break;
				case "w":
					panel.moveSelected(Side.NORTH);
					break;
				case "d":
					panel.moveSelected(Side.BOTTOM);
					break;
				case "e":
					panel.moveSelected(Side.TOP);
					break;
				}
			}
		});
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				if (e.getCode().equals(KeyCode.ENTER)) {
					if (mode != null) {
						switch (mode) {
						case coarsen:
							panel.coarsenSelcted();
							break;
						case refine:
							panel.refineSelcted();
							break;
						default:
							break;

						}
					}
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
		ToggleButton refine_button = new ToggleButton("Refine");
		refine_button.setUserData(Mode.refine);

		ToggleButton coarsen_button = new ToggleButton("Coarsen");
		coarsen_button.setUserData(Mode.coarsen);

		mode_group = new ToggleGroup();
		refine_button.setToggleGroup(mode_group);
		coarsen_button.setToggleGroup(mode_group);

		mode_group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
				if (new_toggle == null)
					mode = null;
				else
					mode = (Mode) mode_group.getSelectedToggle().getUserData();
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
		int num_nodes = OctTree.indexNodes(root);
		try {
			FileOutputStream fos = new FileOutputStream(out_file);

			ByteBuffer h_buf = ByteBuffer.allocate(8);
			h_buf.order(ByteOrder.LITTLE_ENDIAN);
			h_buf.putInt(num_nodes);

			Queue<OctTree> q = new LinkedList<OctTree>();
			Set<OctTree> visited = new HashSet<OctTree>();
			{
				Queue<OctTree> q2 = new LinkedList<OctTree>();
				Set<OctTree> visited2 = new HashSet<OctTree>();
				q2.add(root);
				while (!q2.isEmpty()) {
					OctTree curr = q2.remove();
					visited2.add(curr);
					q.add(curr);
					for (Side s : Side.values()) {
						OctTree nbr = curr.nbr(s);
						if (nbr != null && !q2.contains(nbr) && !visited2.contains(nbr)) {
							q2.add(nbr);
						}
					}
				}
			}
			h_buf.putInt(q.size());
			fos.write(h_buf.array());
			while (!q.isEmpty()) {
				ByteBuffer n_buf = ByteBuffer.allocate(116);
				n_buf.order(ByteOrder.LITTLE_ENDIAN);
				OctTree curr = q.remove();
				visited.add(curr);
				n_buf.putInt(curr.id);
				n_buf.putInt(curr.level);
				if (curr.parent == null) {
					n_buf.putInt(-1);
				} else {
					n_buf.putInt(curr.parent.id);
				}
				n_buf.putDouble(curr.x_length);
				n_buf.putDouble(curr.y_length);
				n_buf.putDouble(curr.z_length);
				n_buf.putDouble(curr.x_start);
				n_buf.putDouble(curr.y_start);
				n_buf.putDouble(curr.z_start);
				// nbrs
				for (Side s : Side.values()) {
					if (curr.nbr(s) == null) {
						n_buf.putInt(-1);
					} else {
						n_buf.putInt(curr.nbr(s).id);
					}
				}
				if (curr.hasChildren()) {
					for (Oct o : Oct.values()) {
						OctTree nbr = curr.child(o);
						if (!q.contains(nbr) && !visited.contains(nbr)) {
							q.add(nbr);
						}
						n_buf.putInt(nbr.id);
					}
				} else {
					for (int i = 0; i < 8; i++) {
						n_buf.putInt(-1);
					}
				}
				fos.write(n_buf.array());
			}
			fos.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void readMeshGraph(File out_file) {
		try {
			FileInputStream fos = new FileInputStream(out_file);

			ByteBuffer h_buf = ByteBuffer.allocate(8);
			h_buf.order(ByteOrder.LITTLE_ENDIAN);
			fos.read(h_buf.array());
			int num_nodes = h_buf.getInt();
			h_buf.getInt();

			OctTree.cubes.getChildren().clear();
			Map<Integer, OctTree> map = new HashMap<Integer, OctTree>();
			for (int i = 0; i < num_nodes; i++) {
				ByteBuffer n_buf = ByteBuffer.allocate(116);
				n_buf.order(ByteOrder.LITTLE_ENDIAN);
				fos.read(n_buf.array());
				OctTree node;
				int id = n_buf.getInt();
				if (map.containsKey(id)) {
					node = map.get(id);
				} else {
					node = new OctTree(id);
					map.put(id, node);
				}
				node.finish_setup(n_buf, map);
				if (i == 0) {
					root = node;
					panel.setRoot(root);
				}
			}
			OctTree child = root;
			while (child.hasChildren()) {
				child = child.children[0];
			}
			child.toggleSelected();
			fos.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
