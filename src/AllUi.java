
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.JTextField;

class AllUi implements ActionListener{
	
	JTextField jt1,jt2;
	JButton jb1,jb2,jb3;
	Panel p;
	JFrame f;
	JLabel jl1,jl2,jl3;
	Container pane;
	Container p1,p2,p3,p4;
	
	DbConn DBobj;
	
	int login;

	AllUi(DbConn obj){
		
		DBobj = obj;
		
		jl1 = new JLabel("Username :");
		jl2 = new JLabel("Password :");
		jl3 = new JLabel("",JLabel.CENTER);
		
		jl3.setForeground(Color.red);
		
		
		
		jb1 = new JButton("Confirm");
		jb2 = new JButton("Reset");
		
		jb3 = new JButton("Create user");
		
		jt1 = new JTextField(25);
		//jt1.setBounds(100, 100, 50, 200);
		
		jt2 = new JTextField(25);
		//jt2.setBounds(10, 10, 50, 200);
		
		f = new JFrame("Login");
		
		p1 = new JPanel();
		p2 = new JPanel();
		p3 = new JPanel();
		p4 = new JPanel();
		
		
		p1.add(jl1);
		p1.add(jt1);
		
		p2.add(jl2);
		p2.add(jt2);
		
		
		
		p3.add(jb1);
		p3.add(jb2);
		p3.add(jb3);
		
		p4.add(jl3);
		
		pane = f.getContentPane();
		
		
		pane.add(p1);
		pane.add(p2);
		pane.add(p4);
		pane.add(p3);
		
		
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		jb3.addActionListener(this);
		
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		
		f.setSize(300, 300);
		pane.setBounds(360, 180, 360, 1020);
		
		f.setVisible(true);
		login = 0;
		
		
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		
		// RESET
		if (e.getSource() == jb2) {
			jt1.setText("");
			jt2.setText("");
		}
		
		//CONFIRM
		if (e.getSource() == jb1) {
			
			if (!DBobj.check_login(jt1.getText(), jt2.getText())) {
				jl3.setText("Try Again");
				
				
				System.out.println("Try Again");
			}else {
				loggedin();
			}
		}
		
		//CREATE
		if (e.getSource() == jb3) {
			
			String name = jt1.getText();
			String pass = jt2.getText();

			DBobj.add_user(1, name, pass);

			if (DBobj.check_login(name, pass)){
				loggedin();
			}else{
				jl3.setText("ERROR occurred");
			}
			//if (jl3)
		}
	
	}

	void loggedin(){
		f.dispose();
		new MWin(DBobj);
	}
}