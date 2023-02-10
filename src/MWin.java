import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import java.util.*;

class autoclick extends Thread{

    MWin window;

    autoclick(MWin win){
        window = win;
    }

    public void run(){

        System.out.println("started");

        try {

            while(true){
            sleep(1000);
            window.jb2.doClick();

            }
        } catch (InterruptedException e) {
            

            e.printStackTrace();
        }
            
        
    }
}

public class MWin extends JFrame implements ActionListener{
    

    JLabel top;
    JButton jb1,jb2,jb3;

    JPanel maintext;
    
    Panel p;
    Container bottomContainer,c2;
    JTextField jt1,jt2;

    JLabel jl1,jl2;

    DbConn obj;

    JPanel test;
    GridBagConstraints c = new GridBagConstraints();

    Encrip encrip;

    MWin(DbConn obj){
        
        
        this.obj = obj;
        
        setLayout(new BorderLayout());
        
        top = new JLabel("Welcome " + obj.current_user);
        top.setBounds(150,10,300,100);
        top.setHorizontalAlignment(SwingConstants.CENTER);
        top.setVerticalAlignment(SwingConstants.CENTER);
        top.setPreferredSize(new Dimension(400,40));
        top.setFont(new Font("TimesRoman", Font.BOLD, 20));
        
        jb1 = new JButton("Logout");
        jb1.setAlignmentX(CENTER_ALIGNMENT);
        jb2 = new JButton("Refresh");
        jb2.setAlignmentX(CENTER_ALIGNMENT);
        jb3 = new JButton("Send");
        
        setTitle("*_*");
        setTitle("Logged in as " + obj.current_user);

        jb1.addActionListener(this);
        jb2.addActionListener(this);
        jb3.addActionListener(this);


        maintext = new JPanel();
        maintext.setOpaque(true);
        maintext.setBackground(Color.GRAY);
        //maintext.setForeground(Color.RED);
        maintext.setLayout(new BoxLayout(maintext, BoxLayout.Y_AXIS));
        //maintext.setLayout(new MigLayout());
        //maintext.setLayout(new FlowLayout);

        maintext.setSize(new Dimension(500,500));

        //
        test = new JPanel();
        test.setOpaque(true);
        test.setBackground(Color.BLACK);
        test.setLayout(new GridBagLayout());
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 100;
        //
        jt1 = new JTextField();
        jt2 = new JTextField();

        jl1 = new JLabel("TO:");
        jl1.setFont(new Font("TimesRoman",Font.BOLD,20));

        jl2 = new JLabel("MESSAGE:");
        jl2.setFont(new Font("TimesRoman",Font.BOLD,20));

        c2 = new JPanel();

        c2.add(jl1);
        c2.add(jt1);
        c2.add(jl2);
        c2.add(jt2);
        c2.add(jb3);
        c2.setLayout(new BoxLayout(c2,BoxLayout.X_AXIS));
        
        bottomContainer = new JPanel();
        
        bottomContainer.add(c2);
        //bottomContainer.add(jb2);
        bottomContainer.add(jb1);
        bottomContainer.setLayout(new BoxLayout(bottomContainer, BoxLayout.Y_AXIS));
        
        add(top,BorderLayout.NORTH);
        
        add(bottomContainer,BorderLayout.SOUTH);

        //add(maintext,BorderLayout.CENTER);
        add(test,BorderLayout.CENTER);

        setSize(500, 500);
        //setBounds(360, 180, 360, 400);

        setVisible(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        autoclick auto = new autoclick(this);

        auto.start();
        encrip = new Encrip();

    }

    public static void main(String[] args){
        DbConn obj2 = new DbConn();
        new MWin(obj2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        

        if (e.getSource() == jb2){
            
            ArrayList<ArrayList<String>> arr = obj.getMessages();

            Iterator<ArrayList<String>> iter = arr.iterator();

            int len = arr.size();

            JLabel msgs[] = new JLabel[len];

            System.out.println(len);

            int i= 0;
            while(iter.hasNext()){
                ArrayList<String> row = iter.next();

                msgs[i] = new JLabel();
                msgs[i].setOpaque(true);
                msgs[i].setBackground(Color.BLACK);

                msgs[i].setPreferredSize(new Dimension(300,70));
                
                
                
                String truemsg = encrip.dec(row.get(2));

                if (row.get(0).equals(obj.current_user)){

                    
                    msgs[i].setFont(new Font("TimesRoman",Font.PLAIN,20));
                    msgs[i].setForeground(Color.GREEN);

                    //String str = "TO: " + row.get(1) + " - " + row.get(2);
                    String str = "TO: " + row.get(1) + " - " + truemsg;
                    msgs[i].setHorizontalAlignment(JLabel.RIGHT);

                    msgs[i].setText(str);
                }
                else{
                    

                    msgs[i].setFont(new Font("TimesRoman",Font.PLAIN,20));
                    msgs[i].setForeground(Color.RED);

                    //String str = "FROM: " + row.get(0) + " - " + row.get(2);
                    String str = "FROM: " + row.get(0) + " - " + truemsg;
                    
                    msgs[i].setHorizontalAlignment(JLabel.LEFT);
                   

                    msgs[i].setText(str);
                }
                i++;

            }

            maintext.removeAll();


            for(int j=0;j<len;j++){
                System.out.println(msgs[j].getText());
                maintext.add(msgs[j]);
            }

            //
            c.gridx=0;
            c.gridy=0;

            for(int j=0;j<len;j++){
                //msgs[j].setHorizontalAlignment(JLabel.RIGHT);
                test.add(msgs[j],c);
                c.gridy++;
            }
            //

            validate();
            repaint();

        }
        
        
        if (e.getSource() == jb3){

            String to = jt1.getText();
            String message = jt2.getText();

            if (to == "" || message == ""){
                return;
            }else{

                String msgencripted = encrip.enc(message);


                if (!obj.sendMsg(to, msgencripted)){
                    System.out.println("Error occured");
                }

                jt1.setText("");
                jt2.setText("");
            }
        }

        if (e.getSource() == jb1){
            obj.logout();

            new AllUi(obj);
            dispose();
        }
        
    }
}
