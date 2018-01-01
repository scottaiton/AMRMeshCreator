package twoDimensions;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MoveAdapter extends MouseAdapter {
	AMRPanel panel;
	int x_prev;
	int y_prev;
	int x_pos;
	int y_pos;

	MoveAdapter(AMRPanel panel) {
		this.panel = panel;
	}

	public void mousePressed(MouseEvent e) {
		x_prev = e.getX();
		y_prev = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		x_pos = e.getX();
		y_pos = e.getY();
	}
	public void mouseDragged(MouseEvent e) {
		int x_shift = e.getX() - x_prev;
		int y_shift = e.getY() - y_prev;
		x_prev = e.getX();
		y_prev = e.getY();
		panel.x_orig += x_shift;
		panel.y_orig += y_shift;
		panel.repaint();
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		double diff = e.getPreciseWheelRotation();
		double trans = Math.pow(1.1,-diff);
		double scale = panel.scale*trans;
		if (scale > 1) {
			panel.scale =scale;
			panel.x_orig=(int)((panel.x_orig-x_pos)*trans+x_pos);
			panel.y_orig=(int)((panel.y_orig-y_pos)*trans+y_pos);
			panel.repaint();
		}
	}
}
