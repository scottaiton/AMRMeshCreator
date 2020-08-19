package meshCreator.twoDimensions;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
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
import meshCreator.Forest;
import meshCreator.Node;
import meshCreator.Orthant;
import meshCreator.Side;

public class RMCanvasPane extends Pane {

	protected Forest forest;
	private double cursor_x = 0;
	private double cursor_y = 0;
	private double scroll = 0;
	private int x_trans = 10;
	private int y_trans = 10;
	private double size = 300;
	private Mode mode;
	private Canvas canvas;

	public RMCanvasPane(Forest forest) {
		this.forest = forest;
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
			drawPotentialNbr(g, 0, (int) size, (int) size, getX(), getY());
		}
	}

	private void drawLeavesForNode(GraphicsContext g, Node node) {
		if (node.hasChildren()) {
			for (Orthant o : Orthant.getValuesForDimension(2)) {
				drawLeavesForNode(g, forest.getNode(node.getChildId(o)));
			}
		} else {
			int x_px = 0 + (int) (node.getStart(0) * size);
			int y_px = (int) size - (int) ((node.getStart(1) + node.getLength(1)) * size);
			int x_ln = (int) Math.ceil(size * node.getLength(0));
			int y_ln = (int) Math.ceil(size * node.getLength(1));
			g.setFill(Color.WHITE);
			g.setStroke(Color.RED);
			g.fillRect(x_px, y_px, x_ln, y_ln);
			g.strokeRect(x_px, y_px, x_ln, y_ln);
		}
	}

	private void drawLeafs(GraphicsContext g) {
		for (int id : forest.getRootIds()) {
			Node n = forest.getNode(id);
			drawLeavesForNode(g, n);
			int x_px = 0 + (int) (n.getStart(0) * size);
			int y_px = (int) size - (int) ((n.getStart(1) + n.getLength(1)) * size);
			g.setStroke(Color.BLACK);
			g.strokeRect(x_px, y_px, (int) size - 1, (int) size - 1);
			g.strokeRect(x_px - 1, y_px - 1, (int) size + 1, (int) size + 1);
		}
	}

	public void drawPotentialNbr(GraphicsContext g, int x_orig, int y_orig, int size, double x, double y) {
		double[] coord = new double[2];
		coord[0] = x;
		coord[1] = y;
		double[] starts = new double[2];
		double[] lengths = new double[2];
		if (forest.coordIsPotentialNbr(coord, starts, lengths)) {
			int x_px = 0 + (int) (starts[0] * size);
			int y_px = (int) size - (int) ((starts[1] + lengths[1]) * size);
			g.setFill(Color.LIGHTGRAY);
			g.setStroke(Color.BLACK);
			g.fillRect(x_px, y_px, size, size);
			g.strokeRect(x_px, y_px, (int) size - 1, (int) size - 1);
			g.strokeRect(x_px - 1, y_px - 1, (int) size + 1, (int) size + 1);
		}
	}

	public void addNbrAt(double x, double y) {
		double[] coord = new double[2];
		coord[0] = x;
		coord[1] = y;
		forest.addNbrAt(coord);
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
		double[] coord = new double[2];
		coord[0] = x;
		coord[1] = y;
		forest.refineAt(coord);
		paint();
	}

	public void coarsenAt(double x, double y) {
		double[] coord = new double[2];
		coord[0] = x;
		coord[1] = y;
		forest.coarsenAt(coord);
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
