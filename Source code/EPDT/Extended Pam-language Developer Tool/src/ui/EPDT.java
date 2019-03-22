package ui;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.Bytecode.util.BytecodeMaker;
import com.Bytecode.util.util;


public class EPDT extends JFrame {

	private static final long serialVersionUID = 2626410227904887283L;
	
	private JTextArea txtInput = new JTextArea("");
	private JLabel labSourceFile = new JLabel(" ");
	
	private String SourcePath = "";
	private String SourceName = "";
	private final String SourceExtension = ".pam";
	private final String CompiledExtension = ".class";
	private final String RunnerExtension = ".bat";
	
	private boolean hasOpend = false;
	private boolean hasSaved = true;
	
	private String LastPath = ""; //������ ������ ���� ������ �۾��ϴ� ���
	private String LastName = ""; //���̵� �ҽ��� �̸�
	private String CurrentSourceContent = ""; //������ �� ������ �ҽ� ����
	
	public EPDT()
	{
		super("Extended Pam-language Developer Tool");
		
		try
		{
			File pamPath = new File("pamPath");
			
			if(pamPath.exists() == false)
				pamPath.createNewFile();
			
			else
			{
				FileInputStream in = new FileInputStream(pamPath);
		        byte[] bytes = new byte[in.available()];
		        in.read(bytes);
		        in.close();
		        
		        String hex = new String(bytes);
		        String pamPathContent = new String(util.hexToByteArray(hex));
		        
		        Scanner scan = new Scanner(pamPathContent);
		        String line = null;
		        
		        if((line = scan.nextLine()) != null)
		        	LastPath = line;
		        
		        if((line = scan.nextLine()) != null)
		        	LastName = line;
		        
		        scan.close();
			}
		}
		catch(Exception e1)
		{
			JOptionPane.showMessageDialog(null, e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
		
		super.setLayout(new BorderLayout()); //���̾ƿ� ����
		
		super.setSize(700, 800); //ũ�� ����

		//�޴��� ���� ����
		JMenuBar menuBar = new JMenuBar(); //�޴��� ��ü
		
		//�޴� ���� ����
		List<JMenu> m = new ArrayList<JMenu>();
		//���� �޴� ���� ����
		JMenu mFile = new JMenu("File"); //���� �޴� ��ü
		mFile.setMnemonic(KeyEvent.VK_F);
		
		//���� �޴��� �� �޴� ������ ���� ����
		List<JMenuItem> miFile = new ArrayList<JMenuItem>();
		
		JMenuItem miFileNew = new JMenuItem("New"); //���� �����
		miFileNew.addActionListener(new miFileNewListener());
		miFile.add(miFileNew);
		
		JMenuItem miFileOpen = new JMenuItem("Open"); //�ҷ�����
		miFileOpen.addActionListener(new miFileOpenListener());
		miFile.add(miFileOpen);
		
		JMenuItem miFileClose = new JMenuItem("Close"); //�ҽ� ����
		miFileClose.addActionListener(new miFileCloseListener());
		miFile.add(miFileClose);
		
		JMenuItem miFileSave = new JMenuItem("Save"); //����
		miFileSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
		miFileSave.addActionListener(new miFileSaveListener());
		miFile.add(miFileSave);
		
		JMenuItem miFileSaveAs = new JMenuItem("Save As"); //���� ����
		miFileSaveAs.addActionListener(new miFileSaveAsListener());
		miFile.add(miFileSaveAs);
		
		final JMenuItem miFileExit = new JMenuItem("Exit"); //���α׷� ����
		miFileExit.addActionListener(new miFileExitListener());
		miFile.add(miFileExit);
		//���� �޴��� �� �޴� ������ ���� ��
		
		//���� �޴��� ������ ���� ����
		for(int i = 0; i < miFile.size(); i++)
			mFile.add(miFile.get(i));
		//���� �޴��� ������ ���� ��
		
		m.add(mFile);
		//���� �޴� ���� ��
		
		//���� �޴� ���� ����
		JMenu mRun = new JMenu("Run"); //���� �޴� ��ü
		mRun.setMnemonic(KeyEvent.VK_R);
		
		//���� �޴��� �� �޴� ������ ���� ����
		List<JMenuItem> miRun = new ArrayList<JMenuItem>();
		
		JMenuItem miRunRun = new JMenuItem("Run"); //����
		miRunRun.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, Event.SHIFT_MASK));
		miRunRun.addActionListener(new mRunListener());
		miRun.add(miRunRun);
		//���� �޴��� �� �޴� ������ ���� ��
		
		//���� �޴��� ������ ���� ����
		for(int i = 0; i < miRun.size(); i++)
			mRun.add(miRun.get(i));
		//���� �޴��� ������ ���� ��
		
		m.add(mRun);
		//���� �޴� ���� ��
		//�޴� ���� ��
		
		//�޴��ٿ� ���� ����
		for(int i = 0; i < m.size(); i++)
			menuBar.add(m.get(i));
		//�޴��ٿ� ���� ��
		//�޴��� ���� ��
		
		txtInput.addKeyListener(new txtInputKeyListener());
		txtInput.setEnabled(false);
		
		JPanel EditingArea = new JPanel(); //�ҽ� �����ϴ� �κ� �г�
		EditingArea.setLayout(new BorderLayout());
		EditingArea.add(labSourceFile, BorderLayout.NORTH);
		EditingArea.add(txtInput, BorderLayout.CENTER);
		
		super.add(menuBar, BorderLayout.NORTH);
		super.add(EditingArea, BorderLayout.CENTER);
		
		setVisible(true);

		super.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent evt) { new miFileExitListener().actionPerformed(new ActionEvent(miFileExit, ActionEvent.ACTION_PERFORMED, "Exit")); } });
	}
	
	public static void main(String[] args) 
	{
		new EPDT();
	}
	
	private class miFileNewListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			try
			{
				if(hasSaved == false)
				{
					int result = JOptionPane.showConfirmDialog(null, "Will You Save Current Source File?", "New", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					//OK = 0, NO = 1, Cancel = 2
					
					if(result == 2)
						return;
					
					if(result == 0)
						new miFileSaveListener().actionPerformed(e);
				}
				
				File selectedFile = null;
				
				JFileChooser Creater = new JFileChooser();
				Creater.removeChoosableFileFilter(Creater.getFileFilter());
				Creater.setFileFilter(new FileNameExtensionFilter(SourceExtension.substring(1) + "-language Source File", SourceExtension.substring(1)));
				if((LastPath + LastName).equals("") == false)
					Creater.setSelectedFile(new File(LastPath + LastName + SourceExtension));
				else Creater.setSelectedFile(new File("C:\\NewPamSource" + SourceExtension));
				
				int result = Creater.showDialog(null, "����");
				
				if(result == JFileChooser.APPROVE_OPTION)
				{
					selectedFile = Creater.getSelectedFile();
					
					if(selectedFile.exists() == false)
						selectedFile.createNewFile();
					
					SourcePath = selectedFile.getParentFile().getAbsolutePath() + File.separator;
					SourceName = selectedFile.getName();
					
					String fullPath = SourcePath + SourceName;
						    
				    if(fullPath.length() <= SourceExtension.length() || (!fullPath.substring(fullPath.length() - SourceExtension.length(), fullPath.length()).toLowerCase().equals(SourceExtension)))
				    	fullPath += SourceExtension;
				    else SourceName = SourceName.substring(0, SourceName.lastIndexOf("."));
				    
				    FileOutputStream out = new FileOutputStream(fullPath);
			        out.write("".getBytes());
			        out.close();
			        
			        labSourceFile.setText(SourceName + SourceExtension);
			        
			        hasOpend = true;
			        
			        txtInput.setEnabled(true);
				}
			}
			catch(Exception e1)
			{
				JOptionPane.showMessageDialog(null, e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
		}
	}
	
	private class miFileOpenListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			try
			{
				if(hasSaved == false)
				{
					int result = JOptionPane.showConfirmDialog(null, "Will You Save Current Source File?", "New", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					//OK = 0, NO = 1, Cancel = 2
					
					if(result == 2)
						return;
					
					if(result == 0)
						new miFileSaveListener().actionPerformed(e);
				}
				
				File selectedFile = null;
				
				JFileChooser Opener = new JFileChooser();
				Opener.removeChoosableFileFilter(Opener.getFileFilter());
				Opener.setFileFilter(new FileNameExtensionFilter(SourceExtension.substring(1) + "-language Source File", SourceExtension.substring(1)));
				if((LastPath + LastName).equals("") == false)
					Opener.setSelectedFile(new File(LastPath + LastName + SourceExtension));
				else Opener.setSelectedFile(new File("C:\\NewPamSource" + SourceExtension));
				
				int result = Opener.showOpenDialog(null);
				
				if(result == JFileChooser.APPROVE_OPTION)
				{
					selectedFile = Opener.getSelectedFile();
					
					if(selectedFile.exists() == false)
					{
						JOptionPane.showMessageDialog(null, "Your Selected File Does Not Exists.", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					SourcePath = selectedFile.getParentFile().getAbsolutePath() + File.separator;
					SourceName = selectedFile.getName();
					
					String fullPath = SourcePath + SourceName;
						    
				    if(fullPath.length() <= SourceExtension.length() || (!fullPath.substring(fullPath.length() - SourceExtension.length(), fullPath.length()).toLowerCase().equals(SourceExtension)))
				    	fullPath += SourceExtension;
				    else SourceName = SourceName.substring(0, SourceName.lastIndexOf("."));
				    
				    FileInputStream in = new FileInputStream(fullPath);
			        byte[] bytes = new byte[in.available()];
			        in.read(bytes);
			        in.close();
			        
			        CurrentSourceContent = new String(bytes);
			        txtInput.setText(CurrentSourceContent);
			        
			        labSourceFile.setText(SourceName + SourceExtension);
			        
			        hasOpend = true;
			        
			        txtInput.setEnabled(true);
				}
			}
			catch(Exception e1)
			{
				JOptionPane.showMessageDialog(null, e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
		}
	}
	
	private class miFileCloseListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			if(hasSaved == false)
			{
				int result = JOptionPane.showConfirmDialog(null, "Will You Save Current Source File?", "New", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				//OK = 0, NO = 1, Cancel = 2
				
				if(result == 2)
					return;
				
				if(result == 0)
					new miFileSaveListener().actionPerformed(e);
			}
			
			txtInput.setText("");
			txtInput.setEnabled(false);
			labSourceFile.setText(" ");
			
			CurrentSourceContent = "";
			SourceName = "";
			SourcePath = "";
		}
	}
	
	private class miFileSaveListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			if(hasOpend == true)
			{
				try
				{
					String fullPath = SourcePath + SourceName + SourceExtension;
					File currentFile = new File(fullPath);
					
					if(currentFile.exists() == false)
						currentFile.createNewFile();
					
					CurrentSourceContent = txtInput.getText();
					
					FileOutputStream out = new FileOutputStream(fullPath);
			        out.write(CurrentSourceContent.getBytes());
			        out.close();
			        
			        hasSaved = true;
			        
			        labSourceFile.setText(SourceName + SourceExtension);
				}
				catch (Exception e1)
				{
					JOptionPane.showMessageDialog(null, e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
			else JOptionPane.showMessageDialog(null, "You Must Create or Open Source File before Save.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private class miFileSaveAsListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			if(hasOpend == true)
			{
				try
				{
					File selectedFile = null;
					
					JFileChooser Saver = new JFileChooser();
					Saver.removeChoosableFileFilter(Saver.getFileFilter());
					Saver.setFileFilter(new FileNameExtensionFilter(SourceExtension.substring(1) + "-language Source File", SourceExtension.substring(1)));
					Saver.setSelectedFile(new File(SourcePath + SourceName + SourceExtension));
					
					int result = Saver.showSaveDialog(null);
					
					if (result == JFileChooser.APPROVE_OPTION) 
					{
					    selectedFile = Saver.getSelectedFile();
					    
					    if(selectedFile.exists() == false)
							selectedFile.createNewFile();
					    
					    SourcePath = selectedFile.getParentFile().getAbsolutePath() + File.separator;
					    SourceName = selectedFile.getName();
					    
					    String fullPath = SourcePath + SourceName;
					    
					    if(fullPath.length() <= SourceExtension.length() || (!fullPath.substring(fullPath.length() - SourceExtension.length(), fullPath.length()).toLowerCase().equals(SourceExtension)))
					    	fullPath += SourceExtension;
					    else SourceName = SourceName.substring(0, SourceName.length() - SourceName.lastIndexOf("."));
					    
					    CurrentSourceContent = txtInput.getText();
					    
				        FileOutputStream out = new FileOutputStream(fullPath);
				        out.write(CurrentSourceContent.getBytes());
				        out.close();
				        
				        hasSaved = true;
				        
				        labSourceFile.setText(SourceName + SourceExtension);
					}
				}
				catch(Exception e1)
				{
					JOptionPane.showMessageDialog(null, e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
			else JOptionPane.showMessageDialog(null, "You Must Create or Open Source File before Save.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private class miFileExitListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			if(hasOpend == true)
			{
				if(hasSaved == false) //���� ���� ������ ���� ���� ���
				{
					int result = JOptionPane.showConfirmDialog(null, "Will You Save Current Source File?", "Exit", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					//OK = 0, NO = 1, Cancel = 2
					
					if(result == 2)
						return;
					
					if(result == 0)
						new miFileSaveListener().actionPerformed(e);
				}
				try
				{
					FileOutputStream out = new FileOutputStream("pamPath");
					
			        out.write(util.strToHex((SourcePath + "\r\n" + SourceName)).getBytes());
			        out.close();
				}
				catch (Exception e1)
				{
					JOptionPane.showMessageDialog(null, e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
			dispose();
		}
	}
	
	private class mRunListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			if(hasOpend == true)
			{
				try
				{
					if(hasSaved == false)
						new miFileSaveListener().actionPerformed(e);
					
					String FilePath = SourcePath + SourceName + SourceExtension;
					
					BytecodeMaker byteCodeMaker = new BytecodeMaker(FilePath);
					byte[] ByteStream = byteCodeMaker.getByteStream();
					
					File CompiledFile = new File(SourcePath + SourceName + CompiledExtension);
					if(CompiledFile.exists() == false)
						CompiledFile.createNewFile();
					
					FileOutputStream out = new FileOutputStream(CompiledFile);
			        out.write(ByteStream);
			        out.close();
			        
			        File runner = new File(SourcePath + SourceName + RunnerExtension);
			        if(runner.exists() == false)
			        	runner.createNewFile();
			        
			        out = new FileOutputStream(runner);
			        out.write(("java -cp " + SourcePath + " " + SourceName + " && " + "pause" + " && " + "exit").getBytes());
			        out.close();
			        
			        Runtime.getRuntime().exec("cmd /C start " + runner.getAbsolutePath());
				}
				catch (Exception e1)
				{
					JOptionPane.showMessageDialog(null, e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		}
	}
	
	private class txtInputKeyListener implements KeyListener
	{

		@Override
		public void keyPressed(KeyEvent arg0) 
		{
			if(CurrentSourceContent.equals(txtInput.getText()) == false)
			{
				labSourceFile.setText("*" + SourceName + SourceExtension);
				hasSaved = false;
			}
		}

		public void keyReleased(KeyEvent arg0) { }
		public void keyTyped(KeyEvent arg0) { }
	}
}
