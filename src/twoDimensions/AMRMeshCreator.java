package twoDimensions;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class AMRMeshCreator extends JFrame implements ActionListener {

	private static final long serialVersionUID = 3178253784369479075L;
	private AMRPanel panel;
	private JFileChooser fc;
	private JToolBar toolbar;
	private JMenuItem save;
	private JMenuItem reset;
	private JToggleButton refine_button;
	private JToggleButton coarsen_button;
	private JToggleButton add_button;
	private Mode mode = Mode.add;

	public AMRMeshCreator() {
		// TODO Auto-generated constructor stub
		// menubar
		JMenuBar menubar = new JMenuBar();
		save = new JMenuItem("Save");
		reset = new JMenuItem("Reset");
		save.addActionListener(this);
		reset.addActionListener(this);
		menubar.add(save);
		menubar.add(reset);
		setJMenuBar(menubar);
		refine_button = new JToggleButton("Refine");
		refine_button.addActionListener(this);
		coarsen_button = new JToggleButton("Coarsen");
		coarsen_button.addActionListener(this);
		add_button = new JToggleButton("Add", true);
		add_button.addActionListener(this);
		toolbar = new JToolBar();
		toolbar.add(refine_button);
		toolbar.add(coarsen_button);
		toolbar.add(add_button);
		getContentPane().add(toolbar, BorderLayout.NORTH);
		panel = new AMRPanel();
		panel.setMode(mode);
		fc = new JFileChooser();
		File workingDirectory = new File(System.getProperty("user.dir"));
		fc.setCurrentDirectory(workingDirectory);
		setBounds(40, 80, 400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(panel, BorderLayout.CENTER);
		setVisible(true);// now frame will be visible, by default not visible
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

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == add_button) {
			if (mode == Mode.add) {
				add_button.setSelected(true);
			} else {
				mode = Mode.add;
				panel.setMode(mode);
				add_button.setSelected(true);
				refine_button.setSelected(false);
				coarsen_button.setSelected(false);
			}
		} else if (e.getSource() == refine_button) {
			if (mode == Mode.refine) {
				refine_button.setSelected(true);
			} else {
				mode = Mode.refine;
				panel.setMode(mode);
				add_button.setSelected(false);
				refine_button.setSelected(true);
				coarsen_button.setSelected(false);
			}
		} else if (e.getSource() == coarsen_button) {
			if (mode == Mode.coarsen) {
				coarsen_button.setSelected(true);
			} else {
				mode = Mode.coarsen;
				panel.setMode(mode);
				add_button.setSelected(false);
				refine_button.setSelected(false);
				coarsen_button.setSelected(true);
			}
		} else if (e.getSource() == reset) {
			//TODO create method for this
			panel.x_orig=10;
			panel.y_orig=10;
			panel.root = new QuadTree();
			repaint();
		}
		/*
		 * if (e.getSource() == save) { int returnVal = fc.showSaveDialog(this);
		 * if (returnVal == JFileChooser.APPROVE_OPTION) { File file =
		 * fc.getSelectedFile(); // This is where a real application would save
		 * the file. System.out.println("Saving: " + file.getName() + ".");
		 * outputMesh(file); } else {
		 * System.out.println("Save command cancelled by user."); } } else if
		 * (e.getSource() == reset) { remove(root); root = new AMRPanel();
		 * getContentPane().add(root, BorderLayout.CENTER); revalidate();
		 * System.out.println("reset"); }
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
		new AMRMeshCreator();
	}

}
