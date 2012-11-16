package ComWork;

import java.io.*;
import java.text.Normalizer.Form;

import face.MainForm;

public class Output implements Runnable {
	
	OutputStream out;
	int whatToDo=0;
	private Thread m_MyThread = null;
	long size;
	MainForm f;
	Boolean status;
	String nameOfFile;
	String wayOfFile;
	
	public Output(PortConnect connect,MainForm form) {
		 try {
			 	this.f=form;
				out = connect.serialPort.getOutputStream();
			    } catch (IOException e) {}
		 
		 try {
		    	connect.serialPort.notifyOnOutputEmpty(true);
		    } catch (Exception e) {
			System.out.println("Error setting event notification");
			System.out.println(e.toString());
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
	public void resume(Boolean bool)
	 {
		 if(bool==true)
		 {
			 this.status=true;
			 m_MyThread.resume();
		 } else
		 {
			 this.status=false;
			 m_MyThread.resume();
		 }
	 }

	 public void stop()
	 {
	   m_MyThread.interrupt();
	   m_MyThread = null;
	 }
	
	@Override
	public void run() {
		if(whatToDo==1)
		{
			send();
		}
		if(whatToDo==0)
		{
			connect();
		}
		if(whatToDo==2)
		{
			disconnect();
		}
	}
	
	public void setNameOfFile(String name)
	{
		this.nameOfFile=name;
	}
	
	public void setWayOfFile(String way)
	{
		this.wayOfFile=way;
	}
	
	public void setSizeOfFile(long siz)
	{
		size=siz;
	}
	public void setSizeOfBar(long siz)
	{
		f.progressBar.setMaximum((int) siz);
	}
	
	public void setValueOfFile(int n)
	{
		f.progressBar.setValue(n);
	}
	
	

	
	public void send()
	{
		 try {
			    InputStream inputStream= new FileInputStream(wayOfFile);
			    
			    byte buf[]=new byte[2045];
			    int len = inputStream.read(buf);
			    Frame fr,fr1,fr2;
			    do
			    {
			    fr1 = new Frame('H',nameOfFile,size);
			    this.out.write(fr1.frToByte(fr1.frameSize()-3),0,fr1.frameSize()*2);
			    suspend();
			    } while(status==false);
			    int len1=0;
			    do
			    {
			    	fr = new Frame('I',buf);
			    	len1=len1+len;
			    	f.progressBar.setValue(len1);
			    	this.out.write(fr.frToByte(len),0,(len+3)*2);
			    	suspend();
			    	if(this.status==true)
			    	{
			    		len=inputStream.read(buf);
			    	}
			    } while(len>0);
			    
			    do
			    {
			    fr2 = new Frame('H');
			    this.out.write(fr2.specFrToByte());
			    suspend();
			    } while(status==false);
			    stop();

			    } catch (IOException e) {}
		 f.b1.setVisible(true);
		 f.b4.setEnabled(true);
		 f.b2.setEnabled(true);
	}
	
	public void connect()
	{
		 Frame fr;
		 fr = new Frame('L');
		 System.out.println(fr.type);
		 try {
			this.out.write(fr.specFrToByte());
		} catch (IOException e) {e.printStackTrace();}
		 
	}
	
	public void disconnect()
	{
		 Frame fr1;
		 fr1 = new Frame('U');
		 System.out.println(fr1.type);
		 try {
			this.out.write(fr1.specFrToByte());
		} catch (IOException e) {e.printStackTrace();}
		 
	}

}
