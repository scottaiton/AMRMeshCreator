
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class AMRMeshCreator extends JFrame {

	private AMRPanel root;

	public AMRMeshCreator() {
		// TODO Auto-generated constructor stub
		// menubar
		JMenuBar menubar = new JMenuBar();
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				outputMesh();
			}
		});
		menubar.add(save);
		setJMenuBar(menubar);
		root = new AMRPanel();
		setBounds(40, 80, 400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(root, BorderLayout.CENTER);
		setVisible(true);// now frame will be visible, by default not visible
	}

	private void outputMesh() {
		indexButtons();
		PrintStream out = System.out;
		Queue<AMRPanel> q = new LinkedList<AMRPanel>();
		Set<AMRPanel> visited = new HashSet<AMRPanel>();
		q.add(root.getSWLeaf());
		while (!q.isEmpty()) {
			AMRPanel curr = q.remove();
			visited.add(curr);
			out.print(curr.id);
			out.print("\t");
			out.print(curr.level);
			out.print("\t");
			out.print(curr.nbrWestLeft());
			out.print("\t");
			out.print(curr.nbrWestRight());
			out.print("\t");
			out.print(curr.nbrEastRight());
			out.print("\t");
			out.print(curr.nbrEastLeft());
			out.print("\t");
			out.print(curr.nbrSouthRight());
			out.print("\t");
			out.print(curr.nbrSouthLeft());
			out.print("\t");
			out.print(curr.nbrNorthLeft());
			out.print("\t");
			out.print(curr.nbrNorthRight());
			out.print("\n");
			enqueue(curr, q, visited);
		}
		out.println("DONE");

	}

	private void indexButtons() {
		int curr_i = 0;
		Queue<AMRPanel> q = new LinkedList<AMRPanel>();
		Set<AMRPanel> visited = new HashSet<AMRPanel>();
		q.add(root.getSWLeaf());
		while (!q.isEmpty()) {
			AMRPanel curr = q.remove();
			visited.add(curr);
			curr.id = curr_i;
			curr_i++;
			enqueue(curr, q, visited);
		}

	}

	private static void enqueue(AMRPanel curr, Queue<AMRPanel> q, Set<AMRPanel> visited) {
		try_enqueue(curr.nbr_north,q,visited);
		try_enqueue(curr.nbr_north_right,q,visited);
		try_enqueue(curr.nbr_east,q,visited);
		try_enqueue(curr.nbr_east_right,q,visited);
		try_enqueue(curr.nbr_south,q,visited);
		try_enqueue(curr.nbr_south_right,q,visited);
		try_enqueue(curr.nbr_west,q,visited);
		try_enqueue(curr.nbr_west_right,q,visited);
	}

	private static void try_enqueue(AMRPanel next, Queue<AMRPanel> q, Set<AMRPanel> visited) {
		if (next != null && !q.contains(next) && !visited.contains(next)) {
			q.add(next);
		}
	}

	public static void main(String args[]) {
		AMRMeshCreator f = new AMRMeshCreator();
	}

}
