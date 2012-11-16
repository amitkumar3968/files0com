package ComWork;

import face.MainForm;
import gnu.io.*; 

import java.awt.GridBagLayout;
import java.io.*;
import java.util.*;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Input implements Runnable, SerialPortEventListener {
	
	InputStream inputStream;
	Output output;
	MainForm form;
	Thread readThread;
	PortConnect prtcnt;
	public int timeOfLink=0;
	int whatToDo=0;
	int flag=0;
	int size=0;
	File f;
	FileOutputStream out;
	Frame fr = new Frame();
	private Thread m_MyThread = null;
	Boolean flagTrueFalse;
	//= new FileManager();
	//SerialPort serialPort;
	
	public Input(PortConnect connect, Output ou,MainForm frm) {
		try {
			  this.prtcnt=connect;
			  this.form=frm;
			  this.output=ou;
		      inputStream = connect.serialPort.getInputStream();
		    } catch (IOException e) {}
		try {
		      connect.serialPort.addEventListener(this);
		    } catch (TooManyListenersException e) {}
		connect.serialPort.notifyOnDataAvailable(true);
	}
	

	@Override
	public void run() {
		if(whatToDo==0)
		{
			 Frame frL;
			 frL = new Frame('L');
			 try {
					output.out.write(frL.specFrToByte());
				} catch (IOException e) {e.printStackTrace();}
		}
		if(whatToDo==1)
		{
		if(flagTrueFalse==true)
		{
			Frame frA = new Frame('A');
			try {
				output.out.write(frA.specFrToByte());
			} catch (IOException e) {e.printStackTrace();}
		} else
		{
			Frame frR = new Frame('R');
			try {
				output.out.write(frR.specFrToByte());
			} catch (IOException e) {e.printStackTrace();}
		}
		}
		
	}
	
	 public void start(int i)
	 {
		 whatToDo=i;
	  if (m_MyThread == null)
	  {
	   m_MyThread = new Thread(this);
	   m_MyThread.start();
	  }
	 }
	 @SuppressWarnings("deprecation")
	public void suspend()
	 {
	   m_MyThread.suspend();
	 }
	 @SuppressWarnings("deprecation")
	public void resume()
	 {
	   m_MyThread.resume();
	 }

	 public void stop()
	 {
	   m_MyThread.interrupt();
	   m_MyThread = null;
	 }


	@Override
	public void serialEvent(SerialPortEvent event) {
		if(event.getNewValue())
		  {
			//  System.out.println("!!!new value!!!");
		  }
	    switch(event.getEventType()) {
	      case SerialPortEvent.BI:
	    	  System.out.println("BI");
	      case SerialPortEvent.OE:
	    	  System.out.println("OE");
	      case SerialPortEvent.FE:
	    	  System.out.println("FE");
	      case SerialPortEvent.PE:
	    	  System.out.println("PE");
	      case SerialPortEvent.CD:
	    	  System.out.println("CD");
	      case SerialPortEvent.CTS:
	    	  System.out.println("CTS");
	      case SerialPortEvent.DSR:
	    	  System.out.println("DSR");
	    	  if(!this.prtcnt.serialPort.isDSR())
	    	  {
//	    		  final JDialog jd1 = new JDialog();
//					GridBagLayout gbl=new GridBagLayout();
//					jd1.setLayout(gbl);
//			        JLabel label6 = new JLabel("Соединение потеряно");
//			        jd1.add(label6);
//			        jd1.setBounds(100, 100, 180, 120);
//			        jd1.show();
	    		  JOptionPane optionPane = new JOptionPane();
			         optionPane.setMessage("Соединение потеряно");
			         optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
			         JDialog dialog = optionPane.createDialog("Cообщение");
			         dialog.show();

			        form.fr.setTitle("files0com - Разорвано");
					  form.label1.setText("Разорвано");
					  form.b4.setEnabled(false);
					  form.b1.setVisible(true);
					  form.b1.setEnabled(false);
					  form.b2.setEnabled(false);
					  try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {e.printStackTrace();}
					  this.prtcnt.portDisconnect();
				  
			        
	    	  }
	      case SerialPortEvent.RI:
	    	  System.out.println("RI");
	      case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
	      break;
	      case SerialPortEvent.DATA_AVAILABLE:
	    	  
	    	  
	    	  byte[] readBuffer = new byte[1];
	    	  byte[] buff = new byte[2048*2];
	    	  int len = 0;
		    	
				try {
					len = inputStream.read(readBuffer);
				} catch (IOException e1) {e1.printStackTrace();}
		    	int j=0;
		  
					  do{
						buff[j]=readBuffer[0];
						j++;
						try {
							len=inputStream.read(readBuffer);
						} catch (IOException e) {e.printStackTrace();}
					  } while(len>0);
					  
					  
					  if(fr.byteToFr(buff,j-3*2)==true)
					  {
						  try {
							  if(fr.type==(byte) Integer.parseInt("00000001", 2) && timeOfLink==0)
							  {
								  fr.frameManager(output);
								  start(0);
								  timeOfLink=1;
								  stop();
								  form.fr.setTitle("files0com - Соединено");
								  form.label1.setText("Соединено");
								  form.b3.setEnabled(false);
								  form.b4.setEnabled(true);
								  form.b1.setEnabled(true);
								  form.b2.setEnabled(true);
							  }
							  
							  if(fr.type==(byte) Integer.parseInt("00000010", 2) && timeOfLink!=0)
							  {
								  fr.frameManager(output);
								  output.start(2);
								  timeOfLink=0;
								  output.stop();
								  form.fr.setTitle("files0com - Разорвано");
								  form.label1.setText("Разорвано");
								  form.b4.setEnabled(false);
								  form.b1.setEnabled(false);
								  form.b2.setEnabled(false);
								  try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {e.printStackTrace();}
								  this.prtcnt.portDisconnect();
							  }
							  
							  
							  if(fr.type==(byte) Integer.parseInt("00000000", 2) || fr.type==(byte) Integer.parseInt("00000101", 2))
							  {
								  if(fr.type==(byte) Integer.parseInt("00000000", 2))
								  {
									  size=size+((j-3*2)/2);
									  form.progressBar.setValue(size);
									  output.setValueOfFile(size);
									  System.out.println(size);
								  }
								  if(fr.type==(byte) Integer.parseInt("00000101", 2))
								  {
									  if(form.b1.isVisible())
									  {
										  form.b1.setVisible(false);
									  } else  form.b1.setVisible(true);
									  if(form.b4.isEnabled())
									  {
										  form.b4.setEnabled(false);
									  } else form.b4.setEnabled(true);
									  if(form.b2.isEnabled())
									  {
										  form.b2.setEnabled(false);
									  } else
										  {
										  	form.b2.setEnabled(true);
										  	size=0;
										  }
								  }
								  flagTrueFalse=true;
								  fr.frameManager(output);
								  start(1);
								  stop();
							  }
							  
							  if(fr.type==(byte) Integer.parseInt("000000011", 2) || fr.type==(byte) Integer.parseInt("00000100", 2))
							  {
								  flagTrueFalse=true;
								  fr.frameManager(output);
							  }
							  
							  
						  	} catch (IOException e) {e.printStackTrace();}
					  } else
					  {
							  flagTrueFalse=false;
							  if(fr.type==(byte) Integer.parseInt("00000000", 2) || fr.type==(byte) Integer.parseInt("00000101", 2))
							  {
								  start(1);
								  stop();
							  }
					  }
					  

	    }
		
	}

}
