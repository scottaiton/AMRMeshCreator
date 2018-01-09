package meshCreator.twoDimensions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.LinkedList;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
			public void changed(ObservableValue<? extends Toggle> ov,
					Toggle toggle, Toggle new_toggle) {
				if (new_toggle == null)
					panel.setMode(null);
				else
					panel.setMode((Mode) mode.getSelectedToggle().getUserData());
			}
		});
		// add_button.addActionListener(this);
		toolbar = new ToolBar(refine_button, coarsen_button, add_button);
		panel = new RMCanvasPane(root);
		BorderPane root = new BorderPane();
		root.setCenter(panel);
		VBox vbox = new VBox();
		Menu file_menu = new Menu("File");
		MenuBar menuBar = new MenuBar();
		Menu new_menu= new Menu("New");
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
		new_menu.getItems().addAll(new2d_item,new3d_item);

		MenuItem save_item = new MenuItem("Save");
		save_item.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				save();
			}
		});
		file_menu.getItems().addAll(new_menu,save_item);
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
			outputMeshGraph(out_file);
		}
	}

	private void outputMeshGraph(File out_file) {
		QuadTree.indexNodes(root);
		PrintStream out = null;
		try {
			out = new PrintStream(out_file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Queue<QuadTree> q = new LinkedList<QuadTree>();
		Set<QuadTree> visited = new HashSet<QuadTree>();
		{
			QuadTree t = root;
			while (t.hasChildren()) {
				t = t.child(Quad.SW);
			}
			q.add(t);
		}
		while (!q.isEmpty()) {
			QuadTree curr = q.remove();
			visited.add(curr);
			out.print(curr.id);
			out.print("\t");
			out.print(curr.level);
			out.print("\t");
			for (Side s : Side.values()) {
				int lower = -1;
				int upper = -1;
				if (curr.nbr(s) == null && curr.parent != null
						&& curr.parent.nbr(s) != null) {
					// coarse case
					QuadTree nbr = curr.parent.nbr(s);
					lower = nbr.id;
					upper = nbr.id;
					if (!q.contains(nbr) && !visited.contains(nbr)) {
						q.add(nbr);
					}
				} else if (curr.nbr(s) != null && curr.nbr(s).hasChildren()) {
					// refined case
					QuadTree[] nbr_children = curr.nbr(s)
							.children(s.opposite());
					lower = nbr_children[0].id;
					upper = nbr_children[1].id;
					for (QuadTree nbr : nbr_children) {
						if (!q.contains(nbr) && !visited.contains(nbr)) {
							q.add(nbr);
						}
					}
				} else if (curr.nbr(s) != null) {
					// normal case
					QuadTree nbr = curr.nbr(s);
					lower = nbr.id;
					upper = nbr.id;
					if (!q.contains(nbr) && !visited.contains(nbr)) {
						q.add(nbr);
					}
				}
				out.print(lower);
				out.print("\t");
				out.print(upper);
				out.print("\t");

			}
			out.print("\n");
		}
		out.close();
	}

}
