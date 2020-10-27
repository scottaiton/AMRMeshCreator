package meshCreator.twoDimensions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Translate;
import meshCreator.Forest;
import meshCreator.Levels;
import meshCreator.Node;
import meshCreator.Orthant;
import meshCreator.Patch;
import meshCreator.Side;

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
	private int curr_rank = 0;
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
					double[] coord = new double[2];
					coord[0] = (me.getX() - x_trans) / (double) size;
					coord[1] = (size - (me.getY() - y_trans)) / (double) size;
					levels.changeRankAt(coord, curr_level, curr_rank);
					paint();
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
		drawPatches(g);
	}

	private void drawPatches(GraphicsContext g) {
		g.setTextAlign(TextAlignment.CENTER);
		g.setTextBaseline(VPos.CENTER);
		for (Patch p : levels.getLevel(curr_level)) {
			int x_px = 0 + (int) (p.starts[0] * size);
			int y_px = (int) size - (int) ((p.starts[1] + p.lengths[1]) * size);
			int x_ln = (int) Math.ceil(size * p.lengths[0]);
			int y_ln = (int) Math.ceil(size * p.lengths[1]);
			g.setFill(rank_color_map.get(p.rank));
			g.setStroke(Color.RED);
			g.fillRect(x_px, y_px, x_ln, y_ln);
			g.strokeRect(x_px, y_px, x_ln, y_ln);
			g.setFill(Color.BLACK);
			g.fillText(Integer.toString(p.id), x_px + x_ln / 2, y_px + y_ln / 2);
		}
		for (int id : levels.getForest().getRootIds()) {
			Node n = levels.getForest().getNode(id);
			int x_px = 0 + (int) (n.getStart(0) * size);
			int y_px = (int) size - (int) ((n.getStart(1) + n.getLength(1)) * size);
			g.setStroke(Color.BLACK);
			g.strokeRect(x_px, y_px, (int) size - 1, (int) size - 1);
			g.strokeRect(x_px - 1, y_px - 1, (int) size + 1, (int) size + 1);
		}
	}

	@Override
	protected void layoutChildren() {
		super.layoutChildren();
		final double x = snappedLeftInset();
		final double y = snappedTopInset();
		final double w = snapSizeX(getWidth()) - x - snappedRightInset();
		final double h = snapSizeY(getHeight()) - y - snappedBottomInset();
		canvas.setLayoutX(x);
		canvas.setLayoutY(y);
		canvas.setWidth(w);
		canvas.setHeight(h);
		paint();
	}

	public void setRank(int i) {
		curr_rank = i;
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
