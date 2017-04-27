import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class AMRPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -1375940126525035468L;

	public AMRPanel nbr_north;
	public AMRPanel nbr_east;
	public AMRPanel nbr_south;
	public AMRPanel nbr_west;
	public AMRPanel nbr_north_right;
	public AMRPanel nbr_east_right;
	public AMRPanel nbr_south_right;
	public AMRPanel nbr_west_right;
	public int level;
	public int id;

	private AMRPanel child_nw;
	private AMRPanel child_ne;
	private AMRPanel child_sw;
	private AMRPanel child_se;

	public AMRPanel() {
		level = 1;
		AMRButton b = new AMRButton();
		b.addActionListener(this);
		setLayout(new BorderLayout(0, 0));
		setVisible(true);
		add(b);
	}

	public AMRPanel(AMRPanel parent) {
		level = parent.level + 1;
		AMRButton b = new AMRButton();
		b.addActionListener(this);
		setLayout(new BorderLayout(0, 0));
		setVisible(true);
		add(b);
	}

	public void refine() {
		removeAll();
		setLayout(new GridLayout(2, 2));
		setVisible(true);
		child_nw = new AMRPanel(this);
		child_ne = new AMRPanel(this);
		child_sw = new AMRPanel(this);
		child_se = new AMRPanel(this);
		// set new neighbors
		// nw
		child_nw.nbr_east = child_ne;
		child_nw.nbr_south = child_sw;
		// ne
		child_ne.nbr_west = child_nw;
		child_ne.nbr_south = child_se;
		// sw
		child_sw.nbr_east = child_se;
		child_sw.nbr_north = child_nw;
		// se
		child_se.nbr_west = child_sw;
		child_se.nbr_north = child_ne;

		// refine neighbors if needed
		if (nbr_north != null && nbr_north.level < level) {
			nbr_north.refine();
		}
		if (nbr_east != null && nbr_east.level < level) {
			nbr_east.refine();
		}
		if (nbr_south != null && nbr_south.level < level) {
			nbr_south.refine();
		}
		if (nbr_west != null && nbr_west.level < level) {
			nbr_west.refine();
		}
		// set outer neighbors
		if (nbr_north != null) {
			if (nbr_north_right != null) {
				child_nw.nbr_north = nbr_north;
				child_ne.nbr_north = nbr_north_right;
				nbr_north.nbr_south = child_nw;
				nbr_north_right.nbr_south = child_ne;
			} else {
				child_nw.nbr_north = nbr_north;
				child_ne.nbr_north = nbr_north;
				nbr_north.nbr_south_right = child_nw;
				nbr_north.nbr_south = child_ne;
			}
		}
		if (nbr_east != null) {
			if (nbr_east_right != null) {
				child_ne.nbr_east = nbr_east;
				child_se.nbr_east = nbr_east_right;
				nbr_east.nbr_west = child_ne;
				nbr_east_right.nbr_west = child_se;
			} else {
				child_ne.nbr_east = nbr_east;
				child_se.nbr_east = nbr_east;
				nbr_east.nbr_west_right = child_ne;
				nbr_east.nbr_west = child_se;
			}
		}
		if (nbr_south != null) {
			if (nbr_south_right != null) {
				child_se.nbr_south = nbr_south;
				child_sw.nbr_south = nbr_south_right;
				nbr_south.nbr_north = child_se;
				nbr_south_right.nbr_north = child_sw;
			} else {
				child_sw.nbr_south = nbr_south;
				child_se.nbr_south = nbr_south;
				nbr_south.nbr_north = child_sw;
				nbr_south.nbr_north_right = child_se;
			}
		}
		if (nbr_west != null) {
			if (nbr_west_right != null) {
				child_sw.nbr_west = nbr_west;
				child_nw.nbr_west = nbr_west_right;
				nbr_west.nbr_east = child_sw;
				nbr_west_right.nbr_east = child_nw;
			} else {
				child_nw.nbr_west = nbr_west;
				child_sw.nbr_west = nbr_west;
				nbr_west.nbr_east = child_nw;
				nbr_west.nbr_east_right = child_sw;
			}
		}
		add(child_nw);
		add(child_ne);
		add(child_sw);
		add(child_se);
		revalidate();

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		refine();
	}

	public int nbrNorthLeft() {
		if (nbr_north == null) {
			return -1;
		} else {
			return nbr_north.id;
		}
	}

	public int nbrNorthRight() {
		if (nbr_north_right == null) {
			return nbrNorthLeft();
		} else {
			return nbr_north_right.id;
		}
	}

	public int nbrEastLeft() {
		if (nbr_east == null) {
			return -1;
		} else {
			return nbr_east.id;
		}
	}

	public int nbrEastRight() {
		if (nbr_east_right == null) {
			return nbrEastLeft();
		} else {
			return nbr_east_right.id;
		}
	}

	public int nbrSouthLeft() {
		if (nbr_south == null) {
			return -1;
		} else {
			return nbr_south.id;
		}
	}

	public int nbrSouthRight() {
		if (nbr_south_right == null) {
			return nbrSouthLeft();
		} else {
			return nbr_south_right.id;
		}
	}

	public int nbrWestLeft() {
		if (nbr_west == null) {
			return -1;
		} else {
			return nbr_west.id;
		}
	}

	public int nbrWestRight() {
		if (nbr_west_right == null) {
			return nbrWestLeft();
		} else {
			return nbr_west_right.id;
		}
	}

	public AMRPanel getSWLeaf() {
		if (child_sw != null) {
			return child_sw.getSWLeaf();
		} else {
			return this;
		}
	}

}
