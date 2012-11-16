package ComWork;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.BitSet;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Frame {
	
	byte start;
	byte type;
	byte[] data;
	byte stop;
	
	FileManager fl=null;
	int proff=1;
	
	
	Frame() {
		start = (byte) Integer.parseInt("10101010", 2);
		stop = (byte)  Integer.parseInt("01010101", 2);
		type = (byte) Integer.parseInt("00000000", 2);
	}
	
	Frame(char tp,byte[] dt) {
		start = (byte) Integer.parseInt("10101010", 2);
		stop = (byte)  Integer.parseInt("01010101", 2);
		switch(tp) {
			case 'I':
				type = (byte) Integer.parseInt("00000000", 2);
				break;
			case 'L':
				type = (byte) Integer.parseInt("00000001", 2);
				break;
			case 'U':
				type = (byte) Integer.parseInt("00000010", 2);
				break;
			case 'A':
				type = (byte) Integer.parseInt("00000011", 2);
				break;
			case 'R':
				type = (byte) Integer.parseInt("00000100", 2);
				break;
			case 'H':
				type = (byte) Integer.parseInt("00000101", 2);
				break;
		}
	  data=dt;
	}
	
	public Frame(char c) {
		start = (byte) Integer.parseInt("10101010", 2);
		stop = (byte)  Integer.parseInt("01010101", 2);
		switch(c) {
			case 'I':
				type = (byte) Integer.parseInt("00000000", 2);
				break;
			case 'L':
				type = (byte) Integer.parseInt("00000001", 2);
				break;
			case 'U':
				type = (byte) Integer.parseInt("00000010", 2);
				break;
			case 'A':
				type = (byte) Integer.parseInt("00000011", 2);
				break;
			case 'R':
				type = (byte) Integer.parseInt("00000100", 2);
				break;
			case 'H':
				type = (byte) Integer.parseInt("00000101", 2);
				break;
		}
		data=new byte[1];

	}
	
	public Frame(char c,String name,long len) {
		start = (byte) Integer.parseInt("10101010", 2);
		stop = (byte)  Integer.parseInt("01010101", 2);
		switch(c) {
			case 'I':
				type = (byte) Integer.parseInt("00000000", 2);
				break;
			case 'L':
				type = (byte) Integer.parseInt("00000001", 2);
				break;
			case 'U':
				type = (byte) Integer.parseInt("00000010", 2);
				break;
			case 'A':
				type = (byte) Integer.parseInt("00000011", 2);
				break;
			case 'R':
				type = (byte) Integer.parseInt("00000100", 2);
				break;
			case 'H':
				type = (byte) Integer.parseInt("00000101", 2);
				break;
		}
		
		String len1=Long.toString(len);
		data=new byte[name.length()+len1.length()+1];
		int i = 0,j=0;
		while(i<name.length())
		{
			data[i]=name.getBytes()[i];
			i++;
			j++;
		}
		j++;
		data[j]=(byte)0;
		i=0;
		while(i<len1.length())
		{
			data[j]=len1.getBytes()[i];
			j++;
			i++;
		}
	}
	
	
	public byte[] frToByte(int len)
	{
		byte rt[] = new byte[len+3];
		rt[0]=this.start;
		rt[1]=this.type;
		int i=0;
		while(i<len)
		{
			rt[i+2]=this.data[i];
			i++;
		}
		rt[i+2]=this.stop;
		
		return encodingFrame(rt);	
	}
	
	public byte[] specFrToByte()
	{
		byte rt[] = new byte[4];
		rt[0]=this.start;
		rt[1]=this.type;
		rt[3]=this.stop;
		return encodingFrame(rt);	
		
	}
	
	public int frameSize()
	{
		int size=this.data.length+3;
		return size;
		
	}
	
	public byte[] encodingFrame(byte rt[])
	{
		 byte buf[]=new byte[1];
	     buf[0]= (byte) Integer.parseInt("1011", 2);
	     BitSet p_polinom = new BitSet();
	     
	     BitSet vector = new BitSet();
	     BitSet inf_v = new BitSet();
	     BitSet ost = new BitSet();
	     BitSet q;
	     BitSet kod74 = new BitSet();

	     p_polinom = fromByte(buf[0]);
		
	     byte[] ef = new byte[rt.length*2];
		int j=0;
		for(int i=0;i<rt.length;i++)
		{
			inf_v.clear();
			ost.clear();
			vector = fromByte(rt[i]);

			
			inf_v=vector.get(0, 4);				//первые 4
	
			
		
	        int iter=0;

	        ost.xor(inf_v);
	        if(ost.length()!=0)
			{
	        do
	        {
	            while(ost.length()<4 && iter!=3)
	            {
	            	iter++;
	            	q = new BitSet();
	            for(int i1=0;i1<3;i1++)
	            {
	            	if(ost.get(i1)==true)
	            	{
	            		q.set(i1+1);
	            	}
	            }
	            ost=q;
	
	            }
	            
	            if(ost.length()>3)
	            {
	            ost.xor(p_polinom);
	            }
	   

	        } while(iter<3);
	        
	
	        
	       
	        kod74=inf_v;

	        int j1=kod74.length();
	        
	        while(kod74.length()<j1+3)
	        {
	        	//iter++;
	        	q = new BitSet();
	        for(int i1=0;i1<kod74.length();i1++)
	        {
	        	if(kod74.get(i1)==true)
	        	{
	        		q.set(i1+1);
	        	}
	        }
	        kod74=q;
	        }
	        
	        kod74.or(ost);
			
	
	        
			ef[j]= fromBits(kod74);
			}
	        
			j++;
			kod74.clear();
			inf_v.clear();
			iter=0;
			ost.clear();
			
			
			
			inf_v=vector.get(4, 8);				//вторые 4
		
			
			ost.xor(inf_v);
			if(ost.length()!=0)
			{
	        do
	        {
	            while(ost.length()<4 && iter!=3)
	            {
	            	iter++;
	            	q = new BitSet();
	            for(int i1=0;i1<3;i1++)
	            {
	            	if(ost.get(i1)==true)
	            	{
	            		q.set(i1+1);
	            	}
	            }
	            ost=q;
	  
	            }
	            
	            if(ost.length()>3)
	            {
	            ost.xor(p_polinom);
	            }
	

	        } while(iter<3);

	        
	       
	        kod74=inf_v;
	
	        int j11=kod74.length();
	        
	        while(kod74.length()<j11+3)
	        {
	        	//iter++;
	        	q = new BitSet();
	        for(int i1=0;i1<kod74.length();i1++)
	        {
	        	if(kod74.get(i1)==true)
	        	{
	        		q.set(i1+1);
	        	}
	        }
	        kod74=q;
	        }
	        
	        kod74.or(ost);
			
	     
			
			ef[j]=fromBits(kod74);
			}
			j++;
	
		}
	
		return ef;
	}
	
	public boolean byteToFr(byte[] buf1,int size)
	{
			byte buf[] = decodingFrame(buf1);
			if(buf.length==1)
			{
				return false;
			} else
			{

				this.type=buf[1];
				this.data=new byte[size/2];
				int i=2;
				while(i!=size/2+2)
					{
						this.data[i-2]=buf[i];
						i++;
					}
				return true;
			}
	}
	
	public byte[] decodingFrame(byte buf1[])
	{
			BitSet q = new BitSet();
			BitSet qq = new BitSet();
			BitSet kod74 = new BitSet();
			BitSet e = new BitSet();
			BitSet p_polinom = new BitSet();
			byte bufrr[]=new byte[1];

			
			byte bufrrr[]=new byte[1];
	        bufrrr[0]= (byte) Integer.parseInt("1011", 2);

			byte[] buf = new byte[buf1.length/2]; 
			int n=0;
			for(int i=0;i<buf1.length;i++)
			{
		        q = fromByte(bufrr[0]);
		        kod74=fromByte(buf1[i]);
		        
		        if(kod74.length()!=0)		//первая половина
		        {
		       
		        	e=kod74.get(kod74.length()-(kod74.length()-3), kod74.length());
		        	
		        	while(kod74.length()!=0 && kod74.length()>3) //декодируем
		            {
		            	p_polinom = fromByte(bufrrr[0]);
		            while(p_polinom.length()<kod74.length())
		            {
		            	qq = new BitSet();
		            for(int i1=0;i1<p_polinom.length();i1++)
		            {
		            	if(p_polinom.get(i1)==true)
		            	{
		            		qq.set(i1+1);
		            	}
		            }
		            p_polinom=qq;
		            }
		            
		            kod74.xor(p_polinom);

		            }
		        	
		        	
		        	if(kod74.length()==0)
		        	{
		        		for(int j=0;j<4;j++)
				        {
				        	if(e.get(j)==true)
				        	{
				        		q.set(j);
				        	}
				        	
				        }
		        	} else break;
		        	
		        }
		        
		        kod74.clear();
		        e.clear();
		        i++;
		        kod74=fromByte(buf1[i]);
		        
		        
	
		        if(kod74.length()!=0)				//вторая половина
		        {
//		        //имитируем ошибку
//		        	if(proff==763)
//		        	{
//					byte err[]=new byte[1];
//			        err[0]= (byte) Integer.parseInt("1000000", 2);
//			        kod74.xor(fromByte(err[0]));
//			       
//		        	}
//		        	 proff++;
//		        //----------------
		        	
		        e=kod74.get(kod74.length()-(kod74.length()-3), kod74.length());
		        
		        while(kod74.length()!=0 && kod74.length()>3) //декодируем
	            {
	            	p_polinom = fromByte(bufrrr[0]);
	            while(p_polinom.length()<kod74.length())
	            {
	            	qq = new BitSet();
	            for(int i1=0;i1<p_polinom.length();i1++)
	            {
	            	if(p_polinom.get(i1)==true)
	            	{
	            		qq.set(i1+1);
	            	}
	            }
	            p_polinom=qq;
	            }
	            
	            kod74.xor(p_polinom);

	            }
	        	
	        	
	        	
	        	if(kod74.length()==0)
	        	{
	        		 for(int k=4;k<8;k++)
	 		        {
	 		        	if(e.get(k-4)==true)
	 		        	{
	 		        		q.set(k);
	 		        	}
	 		        }
	        	} else break;

		    }
		        buf[n]=fromBits(q);
		        
		        n++;
				
			}
			
			//если приходит единичный буфер,а должен всегда 512 то значит была ошибка
			if(n==2048)
			{
				return buf;
			} else
			{
				 bufrrr[0]= (byte) Integer.parseInt("0", 2);
				 return bufrr;
			}
	}
	
	@SuppressWarnings("unused")
	public void frameManager(Output ou) throws IOException
	{	
		if(this.type==(byte) Integer.parseInt("00000001", 2))
		{
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		    int locationX = (screenSize.width - 100) / 2;
		    int locationY = (screenSize.height - 100) / 2;
		    
//			final JDialog jd1 = new JDialog();
//			GridBagLayout gbl=new GridBagLayout();
//			jd1.setLayout(gbl);
//	        JLabel label6 = new JLabel("подключение выполнено");
//	        jd1.add(label6);
//	        jd1.setBounds(locationX, locationY, 180, 120);
//	        jd1.show();
		    JOptionPane optionPane = new JOptionPane();
	         optionPane.setMessage("Связь установлена");
	         optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
	         JDialog dialog = optionPane.createDialog("Cообщение");
	         dialog.show();
		}
		
		if(this.type==(byte) Integer.parseInt("00000010", 2))
		{
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		    int locationX = (screenSize.width - 100) / 2;
		    int locationY = (screenSize.height - 100) / 2;
		    
		    JOptionPane optionPane = new JOptionPane();
	         optionPane.setMessage("Связь разорвана");
	         optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
	         JDialog dialog = optionPane.createDialog("Cообщение");
	         dialog.show();
		    
//			final JDialog jd2 = new JDialog();
//			GridBagLayout gbl=new GridBagLayout();
//			jd2.setLayout(gbl);
//	        JLabel label6 = new JLabel("подключение разорвано");
//	        jd2.add(label6);
//	        jd2.setBounds(locationX, locationY, 180, 120);
//	        jd2.show();
		}
		if(this.type==(byte) Integer.parseInt("00000101", 2)) //пришел заголовочный кадр
		{
			
			if(this.fl==null)
			{
				int i=0;
				String name;
				Long size;
				byte buf[]=new byte[20];
				byte buf1[]=new byte[20];
				while(this.data[i]!=0)
				{
					buf[i]=data[i];
					i++;
				}
				i++;
				name=new String(buf);
				name=new String(name.trim());
				int j=0;
				while(i<data.length)
				{
					buf1[j]=data[i];
					i++;
					j++;
				}
				size = Long.parseLong(new String(buf1).trim());
				
				ou.setSizeOfBar(size);
				
				System.out.println(size);
				this.fl = new FileManager(name);
				
				System.out.println("пришел Н");
			} else
			{
				this.fl.close();
				fl=null;
				System.out.println("пришел Н2");
			}
		}
		if(this.type==(byte) Integer.parseInt("00000000", 2)) //пришел информационный кадр
		{
			System.out.println("пришел I");
			this.fl.add(this.data);
		}
		if(this.type==(byte) Integer.parseInt("00000011", 2))
		{
			System.out.println("пришел A");
			ou.resume(true);
		}
		if(this.type==(byte) Integer.parseInt("00000100", 2))
		{
			System.out.println("пришел R"); 
			ou.resume(false);
		}
	}
	
	 public static BitSet fromByte(byte b)  
	    {  
	        BitSet bits = new BitSet(8);  
	        for (int i = 0; i < 8; i++)  
	        {  
	            bits.set(i, (b & 1) == 1);  
	            b >>= 1;  
	        }  
	        return bits;  
	    }
	    
	    public static byte fromBits(BitSet b)
	    {
	    	int ib = 0;
	    	byte bb;
	    	
	    	for (int i = 0;i<b.length();i++)
	    	{
	    		if(b.get(i)==true)
	    		{
	    			ib=(int) (ib+Math.pow(2, i));
	    		}
	    	}
	    	bb=(byte)ib;
			return bb;
	    }

}
