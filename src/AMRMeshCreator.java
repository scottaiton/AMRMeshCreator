
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AMRMeshCreator extends JFrame {

	public AMRMeshCreator(){
		// TODO Auto-generated constructor stub
        AMRPanel panel=new AMRPanel("root");  
        setBounds(40,80,400,400);    
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(panel, BorderLayout.CENTER);
		setVisible(true);//now frame will be visible, by default not visible  
	}

	public static void main(String args[]) {
		AMRMeshCreator f = new AMRMeshCreator();
	}

}
