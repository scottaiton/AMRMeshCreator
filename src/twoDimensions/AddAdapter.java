package twoDimensions;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class AddAdapter extends MouseAdapter {
	AMRPanel panel;
	AddAdapter(AMRPanel panel){
		this.panel=panel;
	}
	public void mouseClicked(MouseEvent e) {
		double x = (e.getX() - panel.x_orig) / (double) panel.scale;
		double y = (panel.scale - (e.getY() - panel.y_orig)) / (double) panel.scale;
		panel.addNbrAt(x,y);
		panel.repaint();
	}
	public void mouseMoved(MouseEvent e) {
		double x = (e.getX() - panel.x_orig) / (double) panel.scale;
		double y = (panel.scale - (e.getY() - panel.y_orig)) / (double) panel.scale;
		panel.cursor_x=x;
		panel.cursor_y=y;
		panel.repaint();
	}
}
