package meshCreator.threeDimensions;

import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class RMScenePane extends Pane {

	protected OctTree tree;
	private double cursor_x = 0;
	private double cursor_y = 0;
	private SubScene scene;
	private PerspectiveCamera camera;
	private Group group;
	private Group subgroup;
	private Point3D pivot = new Point3D(.5, .5, .5);
	double rotate = 0;
	double rotate2 = 0;
	private boolean x_rotate = false;
	private boolean y_rotate = false;

	public RMScenePane(OctTree root) {
		this.tree = root;
		createContent();
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				if (!me.isStillSincePress()) {
					double x_delta = (me.getSceneX() - cursor_x);
					double y_delta = (me.getSceneY() - cursor_y);
					cursor_x = me.getSceneX();
					cursor_y = me.getSceneY();
					Point3D camera_pivot = subgroup.getLocalToSceneTransform().transform(pivot);
					camera_pivot = camera.sceneToLocal(camera_pivot);
					double x = camera_pivot.getX();
					double y = camera_pivot.getY();
					double z = camera_pivot.getZ();
					if (x_rotate) {
						camera.getTransforms().addAll(new Rotate(x_delta / 2, x, y, z, Rotate.Y_AXIS));
					}
					if (y_rotate) {
						camera.getTransforms().addAll(new Rotate(-y_delta / 2, x, y, z, Rotate.X_AXIS));
					}
				}
			}
		});
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				cursor_x = me.getSceneX();
				cursor_y = me.getSceneY();
			}
		});
		scene.setOnScroll(new EventHandler<ScrollEvent>() {
			public void handle(ScrollEvent e) {
				double delta = e.getDeltaY() / e.getMultiplierY();
					Point3D camera_pivot = subgroup.getLocalToSceneTransform().transform(pivot);
					camera_pivot = camera.sceneToLocal(camera_pivot);
					double scale = (1-Math.pow(1.1,delta));
					double x_trans = camera_pivot.getX()*scale;
					double y_trans = camera_pivot.getY()*scale;
					double z_trans = camera_pivot.getZ()*scale;
					camera.getTransforms().add(new Translate(x_trans,y_trans,z_trans));
			}
		});
		scene.setOnKeyTyped(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				String c = e.getCharacter();
				if (c.equals("q")) {
					tree.moveSelected(Side.EAST);
				} else if (c.equals("a")) {
					tree.moveSelected(Side.WEST);
				} else if (c.equals("s")) {
					tree.moveSelected(Side.SOUTH);
				} else if (c.equals("w")) {
					tree.moveSelected(Side.NORTH);
				} else if (c.equals("d")) {
					tree.moveSelected(Side.BOTTOM);
				} else if (c.equals("e")) {
					tree.moveSelected(Side.TOP);
				}
			}
		});
	}

	public void createContent() {
		// Create and position camera
		camera = new PerspectiveCamera(true);
		camera.getTransforms().addAll(new Translate(.5, -.5, -2.5));

		// Build the Scene Graph
		group = new Group();
		subgroup = new Group(OctTree.cubes);
		subgroup.getTransforms().addAll(new Scale(1, 1, 1), new Rotate(90, Rotate.X_AXIS));
		group.getChildren().add(camera);
		group.getChildren().add(subgroup);

		// Use a SubScene
		SubScene subScene = new SubScene(group, 300, 300, true, SceneAntialiasing.DISABLED);
		subScene.setFill(Color.ALICEBLUE);
		subScene.setCamera(camera);
		getChildren().remove(scene);
		scene = subScene;
		getChildren().add(scene);
	}

	@Override
	protected void layoutChildren() {
		super.layoutChildren();
		final double x = snappedLeftInset();
		final double y = snappedTopInset();
		double w = snapSize(getWidth()) - x - snappedRightInset();
		double h = snapSize(getHeight()) - y - snappedBottomInset();
		scene.setLayoutX(x);
		scene.setLayoutY(y);
		scene.setWidth(w);
		scene.setHeight(h);
	}

	public void moveSelected(Side s) {
		tree.moveSelected(s);
	}

	public void refineSelcted() {
		tree.refineSelected();
	}

	public void coarsenSelcted() {
		tree.coarsenSelected();
	}

	public void toggleXRotate() {
		x_rotate = !x_rotate;
	}

	public void toggleYRotate() {
		y_rotate = !y_rotate;
	}

	public void setRoot(OctTree root) {
		this.tree=root;
	}
}
