package meshCreator.threeDimensions;

import java.util.HashMap;
import java.util.Map;

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
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import meshCreator.Forest;
import meshCreator.Node;
import meshCreator.Orthant;
import meshCreator.Side;

public class RMScenePane extends Pane {

	protected Forest forest;
	private double cursor_x = 0;
	private double cursor_y = 0;
	private SubScene scene;
	private PerspectiveCamera camera;
	private Group group;
	private Group subgroup;
	private Point3D pivot = new Point3D(.5, .5, .5);
	double rotate = 0;
	double rotate2 = 0;
	private int selected_node = 0;
	private boolean x_rotate = false;
	private boolean y_rotate = false;
	private Map<Integer, Cube> cube_map;
	private PhongMaterial selected_mat;
	private PhongMaterial wire_mat;
	private PhongMaterial trans_mat;

	public RMScenePane(Forest forest) {
		selected_mat = new PhongMaterial();
		selected_mat.setDiffuseColor(Color.RED);
		selected_mat.setSpecularColor(Color.RED);
		wire_mat = new PhongMaterial();
		wire_mat.setDiffuseColor(Color.BLACK);
		wire_mat.setSpecularColor(Color.BLACK);
		trans_mat = new PhongMaterial();
		trans_mat.setDiffuseColor(Color.TRANSPARENT);
		trans_mat.setSpecularColor(Color.TRANSPARENT);

		this.forest = forest;
		cube_map = new HashMap<Integer, Cube>();
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
				double scale = (1 - Math.pow(1.1, delta));
				double x_trans = camera_pivot.getX() * scale;
				double y_trans = camera_pivot.getY() * scale;
				double z_trans = camera_pivot.getZ() * scale;
				camera.getTransforms().add(new Translate(x_trans, y_trans, z_trans));
			}
		});
		scene.setOnKeyTyped(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				String c = e.getCharacter();
				if (c.equals("q")) {
					moveSelected(Side.EAST());
				} else if (c.equals("a")) {
					moveSelected(Side.WEST());
				} else if (c.equals("s")) {
					moveSelected(Side.SOUTH());
				} else if (c.equals("w")) {
					moveSelected(Side.NORTH());
				} else if (c.equals("d")) {
					moveSelected(Side.BOTTOM());
				} else if (c.equals("e")) {
					moveSelected(Side.TOP());
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
		subgroup = new Group();

		subgroup.getTransforms().addAll(new Scale(1, 1, 1), new Rotate(90, Rotate.X_AXIS));
		resetCubes();
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

	/**
	 * Remove all cubes from the scene and add them again
	 */
	private void resetCubes() {
		subgroup.getChildren().clear();
		for (int root_id : forest.getRootIds()) {
			Node root = forest.getNode(root_id);
			addCubes(root);
		}
	}

	/**
	 * Add the leaf cubes to the scene
	 * 
	 * @param node the node from which to add leaf nodes
	 */
	private void addCubes(Node node) {
		if (node.hasChildren()) {
			for (Orthant o : Orthant.getValuesForDimension(3)) {
				Node child = forest.getNode(node.getChildId(o));
				addCubes(child);
			}
		} else {
			Cube cube = new Cube(node);
			subgroup.getChildren().add(cube);
			if (node.getId() == selected_node) {
				cube.setMaterial(selected_mat);
				cube.setDrawMode(DrawMode.FILL);
			} else {
				cube.setMaterial(wire_mat);
				cube.setDrawMode(DrawMode.LINE);
			}
		}
	}

	@Override
	protected void layoutChildren() {
		super.layoutChildren();
		final double x = snappedLeftInset();
		final double y = snappedTopInset();
		double w = snapSizeX(getWidth()) - x - snappedRightInset();
		double h = snapSizeY(getHeight()) - y - snappedBottomInset();
		scene.setLayoutX(x);
		scene.setLayoutY(y);
		scene.setWidth(w);
		scene.setHeight(h);
	}

	public void moveSelected(Side s) {
		Node node = forest.getNode(selected_node);
		if (node.hasNbr(s)) {
			Node nbr = forest.getNode(node.getNbrId(s));
			if (nbr.hasChildren()) {
				selected_node = nbr.getChildId(Orthant.GetValuesOnSide(3, s.getOpposite())[0]);
			} else {
				selected_node = nbr.getId();
			}
			resetCubes();
		} else if (node.hasParent()) {
			Node parent = forest.getNode(node.getParentId());
			if (parent.hasNbr(s)) {
				selected_node = parent.getNbrId(s);
				resetCubes();
			}
		}

	}

	public void refineSelected() {
		forest.refineNode(selected_node);
		selected_node = forest.getNode(selected_node).getChildId(Orthant.BSW());
		resetCubes();
	}

	public void coarsenSelcted() {
		int old_selected_node = selected_node;
		Node node = forest.getNode(old_selected_node);
		if (node.hasParent()) {
			selected_node = node.getParentId();
			forest.coarsenNode(old_selected_node);
			resetCubes();
		}
	}

	public void toggleXRotate() {
		x_rotate = !x_rotate;
	}

	public void toggleYRotate() {
		y_rotate = !y_rotate;
	}

	public void setForest(Forest forest) {
		this.forest = forest;
	}
}
