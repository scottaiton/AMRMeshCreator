package meshCreator.twoDimensions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;

public class RMBalancePane extends Pane {

	private double cursor_x = 0;
	private double cursor_y = 0;
	private double scroll = 0;
	private int x_trans = 10;
	private int y_trans = 10;
	private double size = 300;
	private Canvas canvas;
	private Map<Integer, Color> rank_color_map;
	private Levels levels;
	private int current_rank = 0;
	private int curr_level = 0;

	public RMBalancePane(Levels levels, Map<Integer, Color> rank_color_map) {
		this.levels = levels;
		this.rank_color_map = rank_color_map;

		/*
		 * move_adapter=new MoveAdapter(this); addMouseListener(move_adapter);
		 * addMouseMotionListener(move_adapter);
		 */
		canvas = new Canvas();
		getChildren().add(canvas);
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
					changeRankAt(x, y);
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
		drawLeafs();
	}

	private void drawLeafs() {
		GraphicsContext g = canvas.getGraphicsContext2D();
		Set<QuadTree> enqueued = new HashSet<QuadTree>();
		Queue<QuadTree> queue = new LinkedList<QuadTree>();
		queue.add(levels.trees.get(curr_level));
		enqueued.add(levels.trees.get(curr_level));
		while (!queue.isEmpty()) {
			QuadTree t = queue.remove();
			drawLeafs(t, 0, (int) size, (int) size);
			for (Side s : Side.values()) {
				if (t.nbr(s) != null && !enqueued.contains(t.nbr(s))) {
					queue.add(t.nbr(s));
					enqueued.add(t.nbr(s));
				}
			}
		}
	}

	public void drawLeafs(QuadTree node, int x_orig, int y_orig, double size) {
		GraphicsContext g = canvas.getGraphicsContext2D();
		if (node.hasChildren()) {
			for (Quad q : Quad.values()) {
				drawLeafs(node.getChild(q), x_orig, y_orig, size);
			}
		} else {
			int x_px = x_orig + (int) (node.starts[0] * size);
			int y_px = y_orig - (int) ((node.starts[1] + node.lengths[1]) * size);
			int x_ln = (int) Math.ceil(size * node.lengths[0]);
			int y_ln = (int) Math.ceil(size * node.lengths[1]);
			g.setFill(rank_color_map.get(levels.patch_maps.get(curr_level).get(node.id).rank));
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
		queue.add(levels.trees.get(curr_level));
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
		queue.add(levels.trees.get(curr_level));
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

	public void changeRankAt(double x, double y) {
		Set<QuadTree> enqueued = new HashSet<QuadTree>();
		Queue<QuadTree> queue = new LinkedList<QuadTree>();
		queue.add(levels.trees.get(curr_level));
		QuadTree leaf = null;
		while (!queue.isEmpty()) {
			QuadTree t = queue.remove();
			leaf = t.getLeafAt(x, y);
			if (leaf == null) {
				for (Side s : Side.values()) {
					if (t.nbr(s) != null && !enqueued.contains(t.nbr(s))) {
						queue.add(t.nbr(s));
						enqueued.add(t.nbr(s));
					}
				}
			}
		}
		if (leaf != null) {
			Patch p = levels.patch_maps.get(curr_level).get(leaf.id);
			p.rank = current_rank;
			for (Neighbor nbr : p.nbrs) {
				for (int id : nbr.ids) {
					levels.patch_maps.get(curr_level).get(id).updateNbrRankFor(leaf.id, current_rank);
				}
			}
			if (curr_level < levels.trees.size() - 1 && p.hasParent()) {
				levels.patch_maps.get(curr_level + 1).get(p.parent_id).updateChildRankFor(leaf.id, current_rank);
			}
			if (curr_level > 0 && p.hasChildren()) {
				for (int i = 0; i < 4; i++) {
					if (p.child_ids[i] != -1) {
						levels.patch_maps.get(curr_level-1).get(p.child_ids[i]).updateParentRank(current_rank);
					}
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

	public void setRank(int i) {
		current_rank = i;
	}

	public void setLevel(boolean finer) {
		// TODO Auto-generated method stub
		if (finer) {
			curr_level = 0;
		} else {
			curr_level = 1;
		}
		paint();
	}
}
