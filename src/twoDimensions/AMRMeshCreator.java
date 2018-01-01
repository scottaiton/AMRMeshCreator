package twoDimensions;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class AMRMeshCreator extends Application {

	private AMRPanel panel;
	//private JFileChooser fc;
	private ToolBar toolbar;
	//private JMenuItem save;
	//private JMenuItem reset;
	private ToggleGroup mode;
	private ToggleButton refine_button;
	private ToggleButton coarsen_button;
	private ToggleButton add_button;


    public void start(Stage primaryStage) {
		// TODO Auto-generated constructor stub
		// menubar
		//JMenuBar menubar = new JMenuBar();
		//save = new JMenuItem("Save");
		//reset = new JMenuItem("Reset");
		//save.addActionListener(this);
		//reset.addActionListener(this);
		//menubar.add(save);
		//menubar.add(reset);
		refine_button = new ToggleButton("Refine");
		refine_button.setUserData(Mode.refine);
		//refine_button.addActionListener(this);
		coarsen_button = new ToggleButton("Coarsen");
		coarsen_button.setUserData(Mode.coarsen);
		//coarsen_button.addActionListener(this);
		add_button = new ToggleButton("Add");
		add_button.setUserData(Mode.add);
		mode = new ToggleGroup();
		refine_button.setToggleGroup(mode);
		coarsen_button.setToggleGroup(mode);
		add_button.setToggleGroup(mode);
		mode.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov,
		        Toggle toggle, Toggle new_toggle) {
		            if (new_toggle == null)
		                panel.setMode(null);
		            else
		                panel.setMode((Mode) mode.getSelectedToggle().getUserData());
		         }
		});
		//add_button.addActionListener(this);
		toolbar = new ToolBar(
		refine_button,
		coarsen_button,
		add_button);
		panel = new AMRPanel ();
		SwingNode sn=new SwingNode();
		sn.setContent(panel);
		 BorderPane root = new BorderPane();
		 root.setTop(toolbar);
		 root.setCenter(sn);
		//fc = new JFileChooser();
		//File workingDirectory = new File(System.getProperty("user.dir"));
		//fc.setCurrentDirectory(workingDirectory);
		//setBounds(40, 80, 400, 400);
		Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
	}

	private void outputMesh(File out_file) {
		/*
		 * indexButtons(); PrintStream out = null; try { out = new
		 * PrintStream(out_file); } catch (FileNotFoundException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } Queue<AMRPanel> q =
		 * new LinkedList<AMRPanel>(); Set<AMRPanel> visited = new
		 * HashSet<AMRPanel>(); q.add(root.getSWLeaf()); while (!q.isEmpty()) {
		 * AMRPanel curr = q.remove(); visited.add(curr); out.print(curr.id);
		 * out.print("\t"); out.print(curr.level); out.print("\t");
		 * out.print(curr.nbrWestLeft()); out.print("\t");
		 * out.print(curr.nbrWestRight()); out.print("\t");
		 * out.print(curr.nbrEastRight()); out.print("\t");
		 * out.print(curr.nbrEastLeft()); out.print("\t");
		 * out.print(curr.nbrSouthRight()); out.print("\t");
		 * out.print(curr.nbrSouthLeft()); out.print("\t");
		 * out.print(curr.nbrNorthLeft()); out.print("\t");
		 * out.print(curr.nbrNorthRight()); out.print("\n"); enqueue(curr, q,
		 * visited); } out.close();
		 */
	}

	private void indexButtons() {
		/*
		 * int curr_i = 0; Queue<AMRPanel> q = new LinkedList<AMRPanel>();
		 * Set<AMRPanel> visited = new HashSet<AMRPanel>();
		 * q.add(root.getSWLeaf()); while (!q.isEmpty()) { AMRPanel curr =
		 * q.remove(); visited.add(curr); curr.id = curr_i; curr_i++;
		 * enqueue(curr, q, visited); }
		 */
	}

	private static void enqueue(AMRPanel curr, Queue<AMRPanel> q,
			Set<AMRPanel> visited) {
		/*
		 * try_enqueue(curr.nbr_north, q, visited);
		 * try_enqueue(curr.nbr_north_right, q, visited);
		 * try_enqueue(curr.nbr_east, q, visited);
		 * try_enqueue(curr.nbr_east_right, q, visited);
		 * try_enqueue(curr.nbr_south, q, visited);
		 * try_enqueue(curr.nbr_south_right, q, visited);
		 * try_enqueue(curr.nbr_west, q, visited);
		 * try_enqueue(curr.nbr_west_right, q, visited);
		 */
	}

	private static void try_enqueue(AMRPanel next, Queue<AMRPanel> q,
			Set<AMRPanel> visited) {
		/*
		 * if (next != null && !q.contains(next) && !visited.contains(next)) {
		 * q.add(next); }
		 */
	}

	public static void main(String args[]) {
        launch(args);
	}


}
