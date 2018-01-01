package twoDimensions;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class AMRPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -1375940126525035468L;

	protected QuadTree root;
	protected double scale = 300;
	protected int x_orig = 10;
	protected int y_orig = 10;
	protected double cursor_x = 0;
	protected double cursor_y = 0;
	MoveAdapter move_adapter;
	AddAdapter add_adapter;
	RefineAdapter refine_adapter;
	Mode mode;

	public AMRPanel() {
		root = new QuadTree();
		/*
		 * move_adapter=new MoveAdapter(this); addMouseListener(move_adapter);
		 * addMouseMotionListener(move_adapter);
		 */
		move_adapter = new MoveAdapter(this);
		add_adapter = new AddAdapter(this);
		refine_adapter = new RefineAdapter(this);
		addMouseListener(move_adapter);
		addMouseMotionListener(move_adapter);
		addMouseWheelListener(move_adapter);
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void paint(Graphics g) {
		super.paint(g);
		drawLeafs(g);
		if (mode == Mode.add) {
			Pair<QuadTree, Side> nbr = getNbrOf(cursor_x, cursor_y);
			if (nbr != null) {
				nbr.getLeft().drawPotentialNbr(g, x_orig, y_orig + (int) scale,
						(int) scale, cursor_x, cursor_y);
			}
		}
	}

	public void drawLeafs(Graphics g) {
		Set<QuadTree> enqueued = new HashSet<QuadTree>();
		Queue<QuadTree> queue = new LinkedList<QuadTree>();
		queue.add(root);
		enqueued.add(root);
		while (!queue.isEmpty()) {
			QuadTree t = queue.remove();
			t.drawLeafs(g, x_orig, y_orig + (int) scale, (int) scale);
			for (Side s : Side.values()) {
				if (t.nbr(s) != null && !enqueued.contains(t.nbr(s))) {
					queue.add(t.nbr(s));
					enqueued.add(t.nbr(s));
				}
			}
		}
	}

	public Set<Pair<QuadTree, Side>> getNbrsOf(double x, double y) {
		Set<Pair<QuadTree, Side>> nbrs = new HashSet<Pair<QuadTree, Side>>();
		Set<QuadTree> enqueued = new HashSet<QuadTree>();
		Queue<QuadTree> queue = new LinkedList<QuadTree>();
		queue.add(root);
		while (!queue.isEmpty()) {
			QuadTree t = queue.remove();
			if (t.isPotentialNbr(x, y)) {
				nbrs.add(new Pair<QuadTree, Side>(t, t.getSide(x, y)));
			}

			for (Side s : Side.values()) {
				if (t.nbr(s) != null && !enqueued.contains(t.nbr(s))) {
					queue.add(t.nbr(s));
					enqueued.add(t.nbr(s));
				}
			}
		}
		return nbrs;
	}

	public Pair<QuadTree, Side> getNbrOf(double x, double y) {
		Pair<QuadTree, Side> nbr = null;
		Set<QuadTree> enqueued = new HashSet<QuadTree>();
		Queue<QuadTree> queue = new LinkedList<QuadTree>();
		queue.add(root);
		while (!queue.isEmpty()) {
			QuadTree t = queue.remove();
			if (t.isPotentialNbr(x, y)) {
				nbr = new Pair<QuadTree, Side>(t, t.getSide(x, y));
				break;
			}
			for (Side s : Side.values()) {
				if (t.nbr(s) != null && !enqueued.contains(t.nbr(s))) {
					queue.add(t.nbr(s));
					enqueued.add(t.nbr(s));
				}
			}
		}
		return nbr;
	}

	public void addNbrAt(double x, double y) {
		Set<Pair<QuadTree, Side>> pairs = getNbrsOf(x, y);
		if (!pairs.isEmpty()) {
			QuadTree t = new QuadTree();
			Side first = null;
			for (Pair<QuadTree, Side> p : pairs) {
				QuadTree nbr = p.getLeft();
				Side s = p.getRight();
				if (first == null) {
					first = s.opposite();
				}
				nbr.setNbr(s, t);
				t.setNbr(s.opposite(), nbr);
			}
			t.setRelativeTo(first);
			t.reconcile();
		}
	}

	public void setMode(Mode mode) {
		if (this.mode != null) {
			switch (this.mode) {
			case add:
				removeMouseListener(add_adapter);
				removeMouseMotionListener(add_adapter);
				break;
			case coarsen:
			case refine:
				removeMouseListener(refine_adapter);
				removeMouseMotionListener(refine_adapter);
				break;
			default:
				break;
			}
		}
		if (mode != null) {
			switch (mode) {
			case add:
				addMouseListener(add_adapter);
				addMouseMotionListener(add_adapter);
				break;
			case refine:
			case coarsen:
				refine_adapter.setCoarsen(mode == Mode.coarsen);
				addMouseListener(refine_adapter);
				addMouseMotionListener(refine_adapter);
				break;
			default:
				break;

			}
		}
		this.mode = mode;
		repaint();
	}

	public void refineAt(double x, double y) {
		Set<QuadTree> enqueued = new HashSet<QuadTree>();
		Queue<QuadTree> queue = new LinkedList<QuadTree>();
		queue.add(root);
		while (!queue.isEmpty()) {
			QuadTree t = queue.remove();
			t.refineAt(x, y);
			for (Side s : Side.values()) {
				if (t.nbr(s) != null && !enqueued.contains(t.nbr(s))) {
					queue.add(t.nbr(s));
					enqueued.add(t.nbr(s));
				}
			}
		}
	}

	public void coarsenAt(double x, double y) {
		Set<QuadTree> enqueued = new HashSet<QuadTree>();
		Queue<QuadTree> queue = new LinkedList<QuadTree>();
		queue.add(root);
		while (!queue.isEmpty()) {
			QuadTree t = queue.remove();
			t.coarsenAt(x, y);
			for (Side s : Side.values()) {
				if (t.nbr(s) != null && !enqueued.contains(t.nbr(s))) {
					queue.add(t.nbr(s));
					enqueued.add(t.nbr(s));
				}
			}
		}
	}
}
