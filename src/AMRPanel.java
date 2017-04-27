import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class AMRPanel extends JPanel implements ActionListener {

	public AMRPanel(String button_text) {
		// TODO Auto-generated constructor stub
		JButton b=new JButton(button_text);  
		b.addActionListener(this);
		setLayout(new BorderLayout(0, 0));
		setVisible(true);
		add(b);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		removeAll();
		setLayout(new GridLayout(2, 2));
		setVisible(true);
		add(new AMRPanel("nw"));
		add(new AMRPanel("ne"));
		add(new AMRPanel("sw"));
		add(new AMRPanel("se"));
		revalidate();
		
	}

}
