package meshCreator.threeDimensions;

import com.javafx.experiments.shape3d.PolygonMesh;
import com.javafx.experiments.shape3d.PolygonMeshView;

public class Cube extends PolygonMeshView {

	public final double x;
	public final double y;
	public final double z;
	public final double length;
	public Cube(double dx, double dy, double dz, double dl) {
		super();
		x=dx;
		y=dy;
		z=dz;
		length = dl;
		float x = (float)dx;
		float y = (float)dy;
		float z = (float)dz;
		float l = (float)dl;
		float[] points = { x, y, z, // 0
				x + l, y, z, // 1
				x, y + l, z, // 2
				x + l, y + l, z, // 3
				x, y, z + l, // 4
				x + l, y, z + l, // 5
				x, y + l, z + l, // 6
				x + l, y + l, z + l // 7
		};
		float[] tex_coords = { 0, 0, 0, 0, 0, 0, 0, 0 };
		int[] smooth = {1,2,4,8,16,32};
		int[][] faces = {{0,0,2,3,3,2,1,1},
				{6,0,4,3,5,2,7,1},
				{4,0,0,3,1,2,5,1},
				{7,0,3,3,2,2,6,1},
				{6,0,2,3,0,2,4,1},
				{5,0,1,3,3,2,7,1}
		};
		PolygonMesh mesh = new PolygonMesh(points,tex_coords,faces);
		mesh.getFaceSmoothingGroups().addAll(smooth);
		
		setMesh(mesh);
	}
}
