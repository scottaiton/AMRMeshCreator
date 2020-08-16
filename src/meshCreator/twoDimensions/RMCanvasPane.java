package meshCreator.twoDimensions;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;
import meshCreator.Orthant;
import meshCreator.Side;

public class RMCanvasPane extends Pane {

	protected QuadTree root;
	private double cursor_x = 0;
	private double cursor_y = 0;
	private double scroll = 0;
	private int x_trans = 10;
	private int y_trans = 10;
	private double size = 300;
	private Mode mode;
	private Canvas canvas;

	public RMCanvasPane(QuadTree root) {
		this.root = root;
		/*
		 * move_adapter=new MoveAdapter(this); addMouseListener(move_adapter);
		 * addMouseMotionListener(move_adapter);
		 */
		canvas = new Canvas();
		getChildren().add(canvas);
		canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				if (mode == Mode.add) {
					cursor_x = me.getX();
					cursor_y = me.getY();
					paint();
				}
			}
		});
		canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				if (!me.isStillSincePress()) {
					x_trans += (me.getX() - cursor_x);
					y_trans += (me.getY() - cursor_y);
					cursor_x = me.getX();
					cursor_y = me.getY();
					paint();
				}
			}
		});
		canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				cursor_x = me.getX();
				cursor_y = me.getY();
			}
		});
		canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				if (me.isStillSincePress()) {
					int size = (int) (300 * Math.pow(1.1, scroll));
					double x = (me.getX() - x_trans) / (double) size;
					double y = (size - (me.getY() - y_trans)) / (double) size;
					if (mode != null) {
						switch (mode) {
						case add:
							addNbrAt(x, y);
							break;
						case coarsen:
							coarsenAt(x, y);
							break;
						case refine:
							refineAt(x, y);
							break;
						}
					}
				}
			}
		});
		canvas.setOnScroll(new EventHandler<ScrollEvent>() {
			public void handle(ScrollEvent e) {
				double delta = e.getDeltaY() / e.getMultiplierY();
				scroll += delta;
				size = (int) (300 * Math.pow(1.1, scroll));
				x_trans = (int) ((x_trans - e.getX()) * Math.pow(1.1, delta) + e.getX());
				y_trans = (int) ((y_trans - e.getY()) * Math.pow(1.1, delta) + e.getY());
				paint();
			}
		});
		paint();
	}

	private double getX() {
		return (cursor_x - x_trans) / (double) size;
	}

	private double getY() {
		return -(cursor_y - y_trans) / (double) size + 1;
	}

	public void paint() {
		GraphicsContext g = canvas.getGraphicsContext2D();
		Translate r = new Translate(x_trans, y_trans);
		g.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
		g.clearRect(-x_trans, -y_trans, canvas.getWidth(), canvas.getHeight());
		drawLeafs(g);
		if (mode == Mode.add) {
			Pair<QuadTree, Side> nbr = getNbrOf(getX(), getY());
			if (nbr != null) {
				drawPotentialNbr(g, nbr.getLeft(), 0, (int) size, (int) size, getX(), getY());
			}
		}
	}

	private void drawLeafs(GraphicsContext g) {
		Set<QuadTree> enqueued = new HashSet<QuadTree>();
		Queue<QuadTree> queue = new LinkedList<QuadTree>();
		queue.add(root);
		enqueued.add(root);
		while (!queue.isEmpty()) {
			QuadTree t = queue.remove();
			drawLeafs(g, t, 0, (int) size, (int) size);
			for (Side s : Side.getValuesForDimension(2)) {
				if (t.nbr(s) != null && !enqueued.contains(t.nbr(s))) {
					queue.add(t.nbr(s));
					enqueued.add(t.nbr(s));
				}
			}
		}
	}

	public void drawLeafs(GraphicsContext g, QuadTree node, int x_orig, int y_orig, double size) {
		if (node.hasChildren()) {
			for (Orthant q : Orthant.getValuesForDimension(2)) {
				drawLeafs(g, node.getChild(q), x_orig, y_orig, size);
			}
		} else {
			int x_px = x_orig + (int) (node.starts[0] * size);
			int y_px = y_orig - (int) ((node.starts[1] + node.lengths[1]) * size);
			int x_ln = (int) Math.ceil(size * node.lengths[0]);
			int y_ln = (int) Math.ceil(size * node.lengths[1]);
			g.setFill(Color.WHITE);
			g.setStroke(Color.RED);
			g.fillRect(x_px, y_px, x_ln, y_ln);
			g.strokeRect(x_px, y_px, x_ln, y_ln);
		}
		if (node.level == 1) {
			int x_px = x_orig + (int) (node.starts[0] * size);
			int y_px = y_orig - (int) ((node.starts[1] + node.lengths[1]) * size);
			g.setStroke(Color.BLACK);
			g.strokeRect(x_px, y_px, (int) size - 1, (int) size - 1);
			g.strokeRect(x_px - 1, y_px - 1, (int) size + 1, (int) size + 1);
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

			for (Side s : Side.getValuesForDimension(2)) {
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
			for (Side s : Side.getValuesForDimension(2)) {
				if (t.nbr(s) != null && !enqueued.contains(t.nbr(s))) {
					queue.add(t.nbr(s));
					enqueued.add(t.nbr(s));
				}
			}
		}
		return nbr;
	}

	public void drawPotentialNbr(GraphicsContext g, QuadTree root, int x_orig, int y_orig, int size, double x,
			double y) {
		int x_px = 0;
		int y_px = 0;
		if (root.isPotentialNbr(Side.WEST(), x, y)) {
			x_px = x_orig + (int) (root.starts[0] - root.lengths[0]) * size;
			y_px = y_orig - (int) (root.starts[1] + root.lengths[1]) * size;
		} else if (root.isPotentialNbr(Side.EAST(), x, y)) {
			x_px = x_orig + (int) (root.starts[0] + root.lengths[0]) * size;
			y_px = y_orig - (int) (root.starts[1] + root.lengths[1]) * size;
		} else if (root.isPotentialNbr(Side.SOUTH(), x, y)) {
			x_px = x_orig + (int) (root.starts[0]) * size;
			y_px = y_orig - (int) (root.starts[1]) * size;
		} else if (root.isPotentialNbr(Side.NORTH(), x, y)) {
			x_px = x_orig + (int) (root.starts[0]) * size;
			y_px = y_orig - (int) (root.starts[1] + 2 * root.lengths[1]) * size;
		}
		g.setFill(Color.LIGHTGRAY);
		g.setStroke(Color.BLACK);
		g.fillRect(x_px, y_px, size, size);
		g.strokeRect(x_px, y_px, (int) size - 1, (int) size - 1);
		g.strokeRect(x_px - 1, y_px - 1, (int) size + 1, (int) size + 1);
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
					first = s.getOpposite();
				}
				nbr.setNbr(s, t);
				t.setNbr(s.getOpposite(), nbr);
			}
			t.setRelativeTo(first);
			t.reconcile();
		}
		paint();
	}

	public void setMode(Mode mode) {
		Mode prev = this.mode;
		this.mode = mode;
		if (prev == Mode.add) {
			paint();
		}
	}

	public void refineAt(double x, double y) {
		Set<QuadTree> enqueued = new HashSet<QuadTree>();
		Queue<QuadTree> queue = new LinkedList<QuadTree>();
		queue.add(root);
		while (!queue.isEmpty()) {
			QuadTree t = queue.remove();
			t.refineAt(x, y);
			for (Side s : Side.getValuesForDimension(2)) {
				if (t.nbr(s) != null && !enqueued.contains(t.nbr(s))) {
					queue.add(t.nbr(s));
					enqueued.add(t.nbr(s));
				}
			}
		}
		paint();
	}

	public void coarsenAt(double x, double y) {
		Set<QuadTree> enqueued = new HashSet<QuadTree>();
		Queue<QuadTree> queue = new LinkedList<QuadTree>();
		queue.add(root);
		while (!queue.isEmpty()) {
			QuadTree t = queue.remove();
			t.coarsenAt(x, y);
			for (Side s : Side.getValuesForDimension(2)) {
				if (t.nbr(s) != null && !enqueued.contains(t.nbr(s))) {
					queue.add(t.nbr(s));
					enqueued.add(t.nbr(s));
				}
			}
		}
		paint();
	}

	@Override
	protected void layoutChildren() {
		super.layoutChildren();
		final double x = snappedLeftInset();
		final double y = snappedTopInset();
		final double w = snapSize(getWidth()) - x - snappedRightInset();
		final double h = snapSize(getHeight()) - y - snappedBottomInset();
		canvas.setLayoutX(x);
		canvas.setLayoutY(y);
		canvas.setWidth(w);
		canvas.setHeight(h);
		paint();
	}
}
