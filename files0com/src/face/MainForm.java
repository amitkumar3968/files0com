package face;

import gnu.io.CommPortIdentifier;

import javax.swing.*;

import ComWork.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

public class MainForm implements ActionListener {
	
	public JFrame fr;
	public JLabel label;
	public JLabel label1;
	MainForm form=this;
	
	private GridBagLayout gbl;
    private GridBagConstraints gbc;
    
    public JButton b1,b2,b3,b4;
    public JProgressBar progressBar;
    
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static int sizeWidth = 350;
    static int sizeHeight = 190;
    int locationX = (screenSize.width - sizeWidth) / 2;
    int locationY = (screenSize.height - sizeHeight) / 2;
	
    Output ou;
    Input in;
    Thread t;
    File file;
    PortConnect con;
    String port;
    int speed;
	int databits;
	int stopbits;
	int parity;
    
    
	MainForm()
	{
		//создаем основной фрейм
        fr=new JFrame("files0com");
        gbl=new GridBagLayout();
        gbc=new GridBagConstraints();
        //размер и выход
        fr.setBounds(locationX, locationY, sizeWidth, sizeHeight);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        fr.setLayout(gbl);
	
        
        con = new PortConnect();
        
        
		port=con.getPortNumber();
		speed=con.getSpeed();
		databits =con.getDatabits();
		stopbits=con.getStopBits();
		parity=con.getParity();
        
        
        b1 = new JButton("Послать");
        b1.setEnabled(false);
        b2 = new JButton("Выбрать");
        b2.setEnabled(false);
        b3 = new JButton("Соединить");
        b3.setEnabled(false);
        b4 = new JButton("Разорвать");
        b4.setEnabled(false);
        
        label = new JLabel("Ничего не выбрано");
        label1 = new JLabel("Нет соединения");
        
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setMinimum(0);
        //progressBar.setMaximum(100);
        progressBar.setValue(0);
        //progressBar.setVisible(false);
        
        gbc.insets = new Insets(15,0,0,0);
        gbc.gridy=1;
        gbc.gridwidth=5;
        gbc.gridx=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(label, gbc);
        
        gbc.insets = new Insets(15,10,0,0);
        gbc.gridy=1;
        gbc.gridwidth =1;
        gbc.gridx=6;
        gbl.setConstraints(b2, gbc);
        
        gbc.insets = new Insets(15,0,0,0);
        gbc.gridwidth =1;
        gbc.gridy=2;
        gbc.gridx=1;
        gbl.setConstraints(b1, gbc);
        
        gbc.gridwidth =5;
        gbc.gridy=2;
        gbc.gridx=2;
        gbc.insets = new Insets(15,10,0,0);
        gbl.setConstraints(progressBar, gbc);
        
        gbc.gridwidth =3;
        gbc.gridy=3;
        gbc.gridx=1;

        gbc.insets = new Insets(15,0,15,0);
        gbl.setConstraints(b3, gbc);
        
        gbc.gridwidth =2;
        gbc.gridy=3;
        gbc.gridx=4;
        gbc.ipadx=0;
        gbc.insets = new Insets(15,10,15,0);
        gbl.setConstraints(b4, gbc);
        
        gbc.gridwidth =1;
        gbc.gridy=3;
        gbc.gridx=6;
        gbc.ipadx=0;
        gbc.insets = new Insets(15,10,15,0);
        gbl.setConstraints(label1, gbc);
        
        //-------------------
        JMenuBar menuBar = new JMenuBar();
        JMenu Menu = new JMenu("Меню");
        menuBar.add(Menu);
        JMenuItem properties = new JMenuItem("Настройки");
        Menu.add(properties);
        JMenuItem about = new JMenuItem("О программе");
        Menu.add(about);
        Menu.addSeparator();
        JMenuItem exit = new JMenuItem("Выход");
        Menu.add(exit);
        //-------------------

        b2.addActionListener(this);
        b1.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);
        exit.addActionListener(this);
        properties.addActionListener(this);
        about.addActionListener(this);
        fr.setJMenuBar(menuBar);
        fr.add(b1);
        fr.add(label);
        fr.add(label1);
        fr.add(b2);
        fr.add(b3);
        fr.add(b4);
        fr.add(progressBar);
        

		fr.setVisible(true);
	}
	
	 
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            		
                    new MainForm();
                
            }
        });
    }

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getActionCommand().equals("Выбрать"))
        {
			JFileChooser fileopen = new JFileChooser();
	        int ret = fileopen.showDialog(null, "Открыть файл");
	        file = fileopen.getSelectedFile();
	        label.setText(file.getName());

        }
		if(ae.getActionCommand().equals("Послать"))
		{
	        ou.setWayOfFile(file.getAbsolutePath());
	        ou.setNameOfFile(file.getName());
	        ou.setSizeOfFile(file.length());
	        progressBar.setMaximum((int) file.length());
	        b1.setVisible(false);
	        b4.setEnabled(false);
	        b2.setEnabled(false);
			ou.start(1);

		}
		if(ae.getActionCommand().equals("Соединить"))
		{
			ou.start(0);
			ou.stop();						
		}
		if(ae.getActionCommand().equals("Разорвать"))
		{
			ou.start(2);
			ou.stop();
		}
		if(ae.getActionCommand().equals("Настройки"))
		{
			
			final JDialog jd = new JDialog(fr,"Настройки",true);
			
			JLabel label1 = new JLabel("COM порт");
			JLabel label2 = new JLabel("Скорость");
			JLabel label3 = new JLabel("Биты данных");
			JLabel label4 = new JLabel("Стоп биты");
			JLabel label5 = new JLabel("Четность");
			
			
			ArrayList<String> list = new ArrayList<String>();
			 
		    jd.setLayout(gbl);
		   
		    Enumeration portList = CommPortIdentifier.getPortIdentifiers();
			while (portList.hasMoreElements()) {
				CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
				list.add(portId.getName());

			}
			System.out.println(stopbits);
			final String[] speed1 = {"9600","14400","19200","38400","57600","115200"};
			final String[] databits1 = {"5","6","7","8"};
			final String[] stopbits1 = {"1","1.5","2"};
			String[] parity1 = {"нет","нечет","чет","'1'","'0'"};
			final JComboBox comboBox1 = new JComboBox(list.toArray());
			Iterator<String> it = list.iterator ();
			int elem=0;
			
			port=con.getPortNumber();
			speed=con.getSpeed();
			databits =con.getDatabits();
			stopbits=con.getStopBits();
			parity=con.getParity();
			
			while(it.hasNext ()) {
				if(it.next().equals(port))
				{	
					
					comboBox1.setSelectedIndex(elem);
					
				}
				elem++;
			}
			final JComboBox comboBox2 = new JComboBox(speed1);
			for(int i=0;i<speed1.length;i++)
			{
				if(speed1[i].equals(Integer.toString(speed)))
				{
					comboBox2.setSelectedIndex(i);
				}
			}
			
			final JComboBox comboBox3 = new JComboBox(databits1);
			for(int i=0;i<databits1.length;i++)
			{
				if(databits1[i].equals(Integer.toString(databits)))
				{
					comboBox3.setSelectedIndex(i);
				}
			}
			
			final JComboBox comboBox4 = new JComboBox(stopbits1);
			for(int i=0;i<stopbits1.length;i++)
			{
				if(stopbits==1)
				{
					comboBox4.setSelectedIndex(0);
				}
				if(stopbits==3)
				{
					comboBox4.setSelectedIndex(1);
				}
				if(stopbits==2)
				{
					comboBox4.setSelectedIndex(2);
				}
			}
			final JComboBox comboBox5 = new JComboBox(parity1);
			for(int i=0;i<5;i++)
			{
				if(parity==i)
				{
					comboBox5.setSelectedIndex(i);
				}
			}
			
				
			JButton	but = new JButton("OK");
			JButton	but1 = new JButton("Подключиться к порту");
			
			locationX = (screenSize.width - 270) / 2;
	        locationY = (screenSize.height - 300) / 2;
			jd.setBounds(locationX, locationY, 300, 270);
			
			
			
			ActionListener actionListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(e.getActionCommand().equals("comboBoxChanged"))
					{
						if((JComboBox)e.getSource()==comboBox1)
						{
							JComboBox box = (JComboBox)e.getSource();
							port = (String)box.getSelectedItem();
						}
						if((JComboBox)e.getSource()==comboBox2)
						{
							JComboBox box = (JComboBox)e.getSource();
							speed = Integer.parseInt((String) box.getSelectedItem());

						}
						if((JComboBox)e.getSource()==comboBox3)
						{
							JComboBox box = (JComboBox)e.getSource();
							databits = Integer.parseInt((String) box.getSelectedItem());

						}
						if((JComboBox)e.getSource()==comboBox4)
						{
							JComboBox box = (JComboBox)e.getSource();
							if((String) box.getSelectedItem()=="1")
							{
								stopbits = 1;
							}
							if((String) box.getSelectedItem()=="2")
							{
								stopbits = 2;
							}
							if((String) box.getSelectedItem()=="1.5")
							{
								stopbits = 3;
							}

						}
						if((JComboBox)e.getSource()==comboBox5)
						{
							String setter;
							JComboBox box = (JComboBox)e.getSource();
							if((String) box.getSelectedItem()=="нет")
							{
								parity=0;
							}
							if((String) box.getSelectedItem()=="нечет")
							{
								parity=1;
							}
							if((String) box.getSelectedItem()=="чет")
							{
								parity=2;
							}
							if((String) box.getSelectedItem()=="'1'")
							{
								parity=3;
							}
							if((String) box.getSelectedItem()=="'0'")
							{
								parity=4;
							}
						}
						
					}
					if(e.getActionCommand().equals("OK"))
					{
						con.setPortParam(speed, databits, stopbits, parity, port);
						jd.setVisible(false);
					}
					
					if(e.getActionCommand().equals("Подключиться к порту"))
					{
						con.setPortParam(speed, databits, stopbits, parity, port);
						con.portConnect(port);
						ou = new Output(con, form);
				        in = new Input(con,ou,form);
//				        final JDialog jd1 = new JDialog(jd,"Сообщение",true);
//				        GridBagLayout gbl=new GridBagLayout();
//						jd1.setLayout(gbl);
//				        JLabel label6 = new JLabel("Подключение выполнено");
//				        jd1.add(label6);
//				        locationX = (screenSize.width - 100) / 2;
//				        locationY = (screenSize.height - 100) / 2;
//				        jd1.setBounds(locationX, locationY, 180, 120);
//				        jd1.show();
				        JOptionPane optionPane = new JOptionPane();
				         optionPane.setMessage("Подключение выполнено");
				         optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
				         JDialog dialog = optionPane.createDialog("Cообщение");
				         dialog.show();
				        b3.setEnabled(true);
				        jd.setVisible(false);
				        
				        
					}
				}
			};
			
			comboBox1.addActionListener(actionListener);
			comboBox2.addActionListener(actionListener);
			comboBox3.addActionListener(actionListener);
			comboBox4.addActionListener(actionListener);
			comboBox5.addActionListener(actionListener);
			but.addActionListener(actionListener);
			but1.addActionListener(actionListener);
			
			gbc.insets = new Insets(10,10,0,0);
	        gbc.gridy=1;
	        gbc.gridwidth=1;
	        gbc.gridx=1;
	        gbc.fill=GridBagConstraints.HORIZONTAL;
	        gbl.setConstraints(label1, gbc);
	        
	        gbc.gridy=2;
	        gbl.setConstraints(label2, gbc);
	        
	        gbc.gridy=3;
	        gbl.setConstraints(label3, gbc);
	        
	        gbc.gridy=4;
	        gbl.setConstraints(label4, gbc);
	        
	        gbc.gridy=5;
	        gbl.setConstraints(label5, gbc);
	        
	        gbc.insets = new Insets(10,20,0,0);
	        gbc.gridy=1;
	        gbc.gridwidth=1;
	        gbc.gridx=2;
	        gbc.fill=GridBagConstraints.HORIZONTAL;
	        gbl.setConstraints(comboBox1, gbc);
	        
	        gbc.gridy=2;
	        gbl.setConstraints(comboBox2, gbc);
	        
	        gbc.gridy=3;
	        gbl.setConstraints(comboBox3, gbc);
	        
	        gbc.gridy=4;
	        gbl.setConstraints(comboBox4, gbc);
	        
	        gbc.gridy=5;
	        gbl.setConstraints(comboBox5, gbc);
	        
	        gbc.insets = new Insets(10,10,10,0);
	        gbc.gridy=6;
	        gbc.gridwidth=1;
	        gbc.gridx=2;
	        gbl.setConstraints(but, gbc);
	        
	        gbc.insets = new Insets(10,10,10,0);
	        gbc.gridx=1;
	        gbl.setConstraints(but1, gbc);
	        
	        jd.add(label1);
	        jd.add(label2);
	        jd.add(label3);
	        jd.add(label4);
	        jd.add(label5);
	        jd.add(comboBox1);
	        jd.add(comboBox2);
	        jd.add(comboBox3);
	        jd.add(comboBox4);
	        jd.add(comboBox5);
	        jd.add(but);
	        jd.add(but1);
			jd.show();
		}
		if(ae.getActionCommand().equals("О программе"))
		{
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		    int locationX = (screenSize.width - 100) / 2;
		    int locationY = (screenSize.height - 100) / 2;
			final JDialog jd2 = new JDialog();
			GridBagLayout gbl=new GridBagLayout();
			jd2.setLayout(gbl);
	        JLabel label6 = new JLabel("Курсовая работа по курсу 'Сетевые технологии'");
	        gbc.fill=GridBagConstraints.CENTER;
	        gbc.insets = new Insets(0,0,0,0);
	        gbc.gridy=1;
	        gbc.gridx=1;
	        gbl.setConstraints(label6, gbc);
	        jd2.add(label6);

	        JLabel label7 = new JLabel("Шевченко П.А.(ИУ5-74) и Федоров Д.Б.(ИУ5-79)");
	        gbc.gridy=2;
	        gbc.gridx=1;
	        gbl.setConstraints(label7, gbc);
	        jd2.add(label7);
	        
	        JLabel label8 = new JLabel("2011 г.");
	        gbc.gridy=3;
	        gbc.gridx=1;
	        gbl.setConstraints(label8, gbc);
	        jd2.add(label8);
	        jd2.setBounds(locationX, locationY, 500, 120);
	        jd2.show();
		}
		if(ae.getActionCommand().equals("Выход"))
		{
			 System.exit(0);
		}		
	}
	
	

}
