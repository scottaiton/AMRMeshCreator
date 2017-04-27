import java.awt.Color;

import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class AMRButton extends JButton {

	private static final long serialVersionUID = -6542252242144657539L;

	public AMRButton() {
		super();
		  setForeground(Color.BLACK);
		  setBackground(Color.WHITE);
		  Border line = new LineBorder(Color.BLACK);
		  Border margin = new EmptyBorder(5, 15, 5, 15);
		  Border compound = new CompoundBorder(line, margin);
		  setBorder(compound);
	}
}
