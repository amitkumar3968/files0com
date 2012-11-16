package ComWork;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;

public class FileManager {
	
	File f,f1;
	FileOutputStream out;
	
	FileManager(String name)
	{
		f1=new File(name);
		JFileChooser fileopen = new JFileChooser();
		fileopen.setSelectedFile(f1);
        fileopen.showSaveDialog(null);
        f1=fileopen.getSelectedFile();
		f=new File(f1.getAbsolutePath());
    	try {
			out=new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void add(byte[] data) throws IOException
	{
		for(int i=0;i<data.length;i++)
		{
			this.out.write(data[i]);
		}
	}
	
	public void close() throws IOException
	{
		out.close();
	}

}
