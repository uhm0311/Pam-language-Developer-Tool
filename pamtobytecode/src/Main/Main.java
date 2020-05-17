package Main;

import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import bytecodeComponent.util.BytecodeMaker;

public class Main {

	public static void main(String[] args) throws Exception {
		File selectedFile = null;
		String FilePath = "";
		
		JFileChooser Opener = new JFileChooser();
		Opener.removeChoosableFileFilter(Opener.getFileFilter());
		Opener.setFileFilter(new FileNameExtensionFilter("TEXT", "txt"));
		Opener.setCurrentDirectory(new File(new File("C:\\").getCanonicalPath()));
		int result = Opener.showOpenDialog(null);
		
		if (result == JFileChooser.APPROVE_OPTION) 
		{
		    selectedFile = Opener.getSelectedFile();
		    FilePath = selectedFile.getAbsolutePath();
		}
		else return;
		
		BytecodeMaker byteCodeMaker = new BytecodeMaker(FilePath);
		byte[] ByteStream = byteCodeMaker.getByteStream();
		
		JFileChooser Saver = new JFileChooser();
		Saver.removeChoosableFileFilter(Saver.getFileFilter());
		Saver.setFileFilter(new FileNameExtensionFilter("class", "class"));
		Saver.setSelectedFile(new File(FilePath.substring(0, FilePath.length() - 4) + ".class"));
		
		result = Saver.showSaveDialog(null);
		
		if (result == JFileChooser.APPROVE_OPTION) 
		{
		    selectedFile = Saver.getSelectedFile();
		    FilePath = selectedFile.getAbsolutePath();
		    
		    if(FilePath.length() <= 6 || (!FilePath.substring(FilePath.length() - 6, FilePath.length()).equals(".class")))
	    		FilePath += ".class";
		    
	        FileOutputStream out = new FileOutputStream(FilePath);
	        out.write(ByteStream);
	        out.close();
		}
		else return;
	}

}
