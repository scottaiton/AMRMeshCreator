package twoDimensions;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class RefineAdapter extends MouseAdapter {
	AMRPanel panel;
	boolean coarsen=false;
	RefineAdapter(AMRPanel panel){
		this.panel=panel;
	}
	public void mouseClicked(MouseEvent e) {
		double x = (e.getX() - panel.x_orig) / (double) panel.scale;
		double y = (panel.scale - (e.getY() - panel.y_orig)) / (double) panel.scale;
		if(coarsen){
		panel.coarsenAt(x,y);
		}else{
		panel.refineAt(x,y);
		}
		panel.repaint();
	}
	public void setCoarsen(boolean b) {
		coarsen=b;
	}
}
