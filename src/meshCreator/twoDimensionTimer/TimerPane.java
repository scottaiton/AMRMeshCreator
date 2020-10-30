package meshCreator.twoDimensionTimer;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

import org.hsqldb.util.FontDialogSwing;

import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Translate;
import meshCreator.Patch;

public class TimerPane extends Pane {

	private double cursor_x = 0;
	private double cursor_y = 0;
	private double scroll = 0;
	private int x_trans = 10;
	private int y_trans = 10;
	private double size = 300;
	private Canvas canvas;
	private Map<Integer, Color> rank_color_map;
	private ArrayList<Patch> patches;
	private Map<Integer, String> patch_text;

	public TimerPane(ArrayList<Patch> patches, Map<Integer, Color> rank_color_map,
			Consumer<Patch> patch_click_callback) {
		this.patches = patches;
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
					if (patch_click_callback != null) {
						int size = (int) (300 * Math.pow(1.1, scroll));
						double[] coord = new double[2];
						coord[0] = (me.getX() - x_trans) / (double) size;
						coord[1] = (size - (me.getY() - y_trans)) / (double) size;
						Patch patch = getPatchAt(coord);
						if (patch != null) {
							patch_click_callback.accept(patch);
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

	public void paint() {
		GraphicsContext g = canvas.getGraphicsContext2D();
		Translate r = new Translate(x_trans, y_trans);
		g.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
		g.clearRect(-x_trans, -y_trans, canvas.getWidth(), canvas.getHeight());
		drawPatches(g);
	}

	private boolean isInBounds(Font font, String text, int x_ln, int y_ln) {
		Text helper = new Text();
		helper.setFont(font);
		helper.setText(text);
		double textWidth = Math.ceil(helper.getLayoutBounds().getWidth());
		double textHeight = Math.ceil(helper.getLayoutBounds().getHeight());
		return x_ln > textWidth && y_ln > textHeight;
	}

	private void drawPatches(GraphicsContext g) {
		g.setTextAlign(TextAlignment.CENTER);
		g.setTextBaseline(VPos.CENTER);
		for (Patch p : patches) {
			int x_px = 0 + (int) (p.starts[0] * size);
			int y_px = (int) size - (int) ((p.starts[1] + p.lengths[1]) * size);
			int x_ln = (int) Math.ceil(size * p.lengths[0]);
			int y_ln = (int) Math.ceil(size * p.lengths[1]);
			g.setFill(rank_color_map.get(p.rank));
			g.setStroke(Color.RED);
			g.fillRect(x_px, y_px, x_ln, y_ln);
			g.strokeRect(x_px, y_px, x_ln, y_ln);
			g.setFill(Color.BLACK);
			if (patch_text != null && isInBounds(g.getFont(), patch_text.get(p.id), x_ln, y_ln)) {
				g.fillText(patch_text.get(p.id), x_px + x_ln / 2, y_px + y_ln / 2);
			}
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

	private Patch getPatchAt(double[] coord) {
		for (Patch patch : patches) {
			if (patch.coordIsInside(coord)) {
				return patch;
			}
		}
		return null;
	}

	public void setPatches(ArrayList<Patch> patches) {
		this.patches = patches;
		paint();
	}

	public void setPatchText(Map<Integer, String> patch_text) {
		this.patch_text = patch_text;
		paint();
	}
}
