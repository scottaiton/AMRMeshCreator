package twoDimensions;

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
import javafx.scene.transform.Translate;

public class AmrCanvasPane extends Pane {

	protected QuadTree root;
	private double cursor_x = 0;
	private double cursor_y = 0;
	private double scroll = 0;
	private int x_trans = 10;
	private int y_trans = 10;
	private double size = 300;
	private Mode mode;
	private Canvas canvas;

	public AmrCanvasPane(QuadTree root) {
		this.root=root;
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
				double delta = e.getDeltaY()/e.getMultiplierY();
				scroll += delta;
				size = (int) (300 * Math.pow(1.1, scroll));
				x_trans=(int)((x_trans-e.getX())*Math.pow(1.1, delta)+e.getX());
				y_trans=(int)((y_trans-e.getY())*Math.pow(1.1, delta)+e.getY());
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
		g.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(),
				r.getTx(), r.getTy());
		g.clearRect(-x_trans, -y_trans, canvas.getWidth(), canvas.getHeight());
		drawLeafs(g);
		if (mode == Mode.add) {
			Pair<QuadTree, Side> nbr = getNbrOf(getX(),getY());
			if (nbr != null) {
				nbr.getLeft().drawPotentialNbr(g, 0, (int) size, (int) size,
						getX(), getY());
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
			t.drawLeafs(g, 0, (int) size, (int) size);
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
			for (Side s : Side.values()) {
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
			for (Side s : Side.values()) {
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
