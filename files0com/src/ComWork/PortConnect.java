package ComWork;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.util.Enumeration;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import gnu.io.*; 

public class PortConnect {
	
	Enumeration portList;
	CommPortIdentifier portId;
	SerialPort serialPort;
	PortConnect pc;
	String portNumber;
	int speed;
	int databits;
	int stopbits;
	int parity;
	
	 public PortConnect() {
		 speed=57600;
		 databits=SerialPort.DATABITS_8;
		 stopbits=SerialPort.STOPBITS_1;
		 parity=SerialPort.PARITY_NONE;
		 portNumber="COM1";
	 }
	 
	 public void portConnect(String portNumber)
	 {
		 portList = CommPortIdentifier.getPortIdentifiers();
			while (portList.hasMoreElements()) {
				portId = (CommPortIdentifier) portList.nextElement();
				if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					if (portId.getName().equals(portNumber)) {
						try {
							serialPort = (SerialPort) portId.open("files0com", 2000);
						    } catch (PortInUseException e) {
						    	 
						    	 JOptionPane optionPane = new JOptionPane();
						         optionPane.setMessage("Порт недоступен");
						         optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
						         JDialog dialog = optionPane.createDialog(portNumber);
						         dialog.show();
						    }
						
						//тестовое
							//serialPort.notifyOnOutputEmpty(true);
						    //serialPort.notifyOnBreakInterrupt(true);
						    //serialPort.notifyOnCarrierDetect(true);
						    //serialPort.notifyOnCTS(true);
						    serialPort.notifyOnDSR(true);
						    //serialPort.notifyOnOverrunError(true);
						    //serialPort.notifyOnParityError(true);
						    //serialPort.notifyOnFramingError(true);
						
						 try {
						      // устанавливаем параметры порта
						      serialPort.setSerialPortParams(this.speed,this.databits,this.stopbits,this.parity);

						    } catch (UnsupportedCommOperationException e) {}
					}
		 //serialPort.notifyOnDataAvailable(true);
				}
			}
	 }
	 
	 public void portDisconnect()
	 {
		 this.serialPort.close();
	 }
	 
	 public void setPortParam(int p1,int p2,int p3,int p4,String p5)
	 {
		 this.speed=p1;
		 this.databits=p2;
		 this.stopbits=p3;
		 this.parity=p4;
		 this.portNumber=p5;
	 }
	 
	 public String getPortNumber()
	 {
		 return this.portNumber;
	 }
	 
	 public int getSpeed()
	 {
		 return this.speed;
	 }
	 
	 public int getDatabits()
	 {
		 return this.databits;
	 }
	 
	 public int getParity()
	 {
		 return this.parity;
	 }
	 
	 public int getStopBits()
	 {
		 return this.stopbits;
	 }
	
}
