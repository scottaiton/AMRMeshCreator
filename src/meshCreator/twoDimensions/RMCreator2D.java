package meshCreator.twoDimensions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import meshCreator.threeDimensions.RMCreator3D;

public class RMCreator2D extends Application {

	private RMCanvasPane panel;
	// private JFileChooser fc;
	private ToolBar toolbar;
	private ToggleGroup mode;
	private ToggleButton refine_button;
	private ToggleButton coarsen_button;
	private ToggleButton add_button;
	private Button balance_button;
	private QuadTree root;
	Stage primary_stage;

	public void start(Stage primaryStage) {
		primary_stage = primaryStage;
		root = new QuadTree();
		refine_button = new ToggleButton("Refine");
		refine_button.setUserData(Mode.refine);
		// refine_button.addActionListener(this);
		coarsen_button = new ToggleButton("Coarsen");
		coarsen_button.setUserData(Mode.coarsen);
		// coarsen_button.addActionListener(this);
		add_button = new ToggleButton("Add");
		add_button.setUserData(Mode.add);
		mode = new ToggleGroup();
		refine_button.setToggleGroup(mode);
		coarsen_button.setToggleGroup(mode);
		add_button.setToggleGroup(mode);
		mode.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
				if (new_toggle == null)
					panel.setMode(null);
				else
					panel.setMode((Mode) mode.getSelectedToggle().getUserData());
			}
		});
		balance_button = new Button("Balance");
		balance_button.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				Stage stage = new Stage();
				RMBalance2D amc = new RMBalance2D(root);
				amc.start(stage);
				((Node) (t.getSource())).getScene().getWindow().hide();
			}
		});
		// add_button.addActionListener(this);
		toolbar = new ToolBar(refine_button, coarsen_button, add_button, balance_button);
		panel = new RMCanvasPane(root);
		BorderPane root = new BorderPane();
		root.setCenter(panel);
		VBox vbox = new VBox();
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
		file_menu.getItems().addAll(new_menu, save_item);
		menuBar.getMenus().add(file_menu);
		vbox.getChildren().addAll(menuBar, toolbar);
		root.setTop(vbox);

		Scene scene = new Scene(root, 500, 500);
		primaryStage.setTitle("2D Refined Mesh Creator");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void save() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		File out_file = fileChooser.showSaveDialog(primary_stage);
		if (out_file != null) {
			outputMeshGraphJson(out_file);
		}
	}

	private void outputMeshGraphJson(File out_file) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		Levels levels = new Levels(root);

		try {
			FileWriter writer = new FileWriter(out_file);
			gson.toJson(levels, writer);
			writer.flush();
			writer.close();
		} catch (JsonIOException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void outputMeshGraph(File out_file) {
		int num_nodes = QuadTree.indexNodes(root);
		try {
			FileOutputStream fos = new FileOutputStream(out_file);

			ByteBuffer h_buf = ByteBuffer.allocate(8);
			h_buf.order(ByteOrder.LITTLE_ENDIAN);
			h_buf.putInt(num_nodes);

			Queue<QuadTree> q = new LinkedList<QuadTree>();
			Set<QuadTree> visited = new HashSet<QuadTree>();
			{
				Queue<QuadTree> q2 = new LinkedList<QuadTree>();
				Set<QuadTree> visited2 = new HashSet<QuadTree>();
				q2.add(root);
				while (!q2.isEmpty()) {
					QuadTree curr = q2.remove();
					visited2.add(curr);
					q.add(curr);
					for (Side s : Side.values()) {
						QuadTree nbr = curr.nbr(s);
						if (nbr != null && !q2.contains(nbr) && !visited2.contains(nbr)) {
							q2.add(nbr);
						}
					}
				}
			}
			h_buf.putInt(q.size());
			fos.write(h_buf.array());
			while (!q.isEmpty()) {
				ByteBuffer n_buf = ByteBuffer.allocate(116 - 8 * 2 - 4 * 4 - 2 * 4);
				n_buf.order(ByteOrder.LITTLE_ENDIAN);
				QuadTree curr = q.remove();
				visited.add(curr);
				n_buf.putInt(curr.id);
				n_buf.putInt(curr.level);
				if (curr.hasParent()) {
					n_buf.putInt(curr.getParent().id);
				} else {
					n_buf.putInt(-1);
				}
				n_buf.putDouble(curr.lengths[0]);
				n_buf.putDouble(curr.lengths[1]);
				n_buf.putDouble(curr.starts[0]);
				n_buf.putDouble(curr.starts[1]);
				// nbrs
				for (Side s : Side.values()) {
					if (curr.nbr(s) == null) {
						n_buf.putInt(-1);
					} else {
						n_buf.putInt(curr.nbr(s).id);
					}
				}
				if (curr.hasChildren()) {
					for (Quad o : Quad.values()) {
						QuadTree nbr = curr.getChild(o);
						if (!q.contains(nbr) && !visited.contains(nbr)) {
							q.add(nbr);
						}
						n_buf.putInt(nbr.id);
					}
				} else {
					for (int i = 0; i < 4; i++) {
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

}
