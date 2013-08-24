package com.davenport.themisto;


import java.awt.Font;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Properties;

import javax.swing.filechooser.FileFilter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class IDE extends JFrame {

	private static final long serialVersionUID = 5142388990039117949L;

	private ProjectFactory projectFactory = null;
	private Properties props = new Properties();
	private File propFile = new File("themisto.properties");
	private Menu openProjectMenu = new Menu();
	
	public static void main(String[] args) {
		IDE window = new IDE();
		Font currentFont = window.getFont();
		System.out.println(currentFont);
		window.setFont(Font.decode("ARIAL-PLAIN-18"));
		window.setVisible(true);
		window.repaint();
	}

	public IDE() {
		setSize(800, 600);
		setTitle("Themisto");
		addWindowListener(new WindowListener() {
			@Override
			public void windowActivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
			}
		});

		openProjectMenu.setLabel("Open Project");

		MenuItem register = new MenuItem();
		register.setLabel("Register Project...");
		register.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				registerProject();
				repaint();
			}
		});


		MenuItem create = new MenuItem();
		create.setLabel("Create Project...");
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createProject();
				repaint();
			}
		});

		MenuItem exit = new MenuItem();
		exit.setLabel("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					props.save(new FileOutputStream(propFile), "");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		});

		Menu fileMenu = new Menu();
		fileMenu.setLabel("File");
		fileMenu.add(create);
		fileMenu.add(register);
		fileMenu.add(openProjectMenu);
		fileMenu.add(exit);

		MenuBar menuBar = new MenuBar();
		menuBar.add(fileMenu);

		this.setMenuBar(menuBar);
		
		if (!propFile.exists()) {
			try {
				propFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			props.load(new FileInputStream(propFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		buildOpenProjectMenu();
		projectFactory = new ProjectFactory(this);
	}
	
	private void buildOpenProjectMenu() {
		openProjectMenu.removeAll();
		//props.setProperty("PROJECTS", "NONE");
		String[] projects = props.getProperty("PROJECTS", "NONE").split(";");
		System.out.println("Number of projects = " + projects.length);
		for (String project : projects) {
			System.out.println("Project="+project);
			if (!project.equals("NONE")) {
				MenuItem menuItem = new MenuItem();
				menuItem.setLabel(project);
				final File projectFile = new File(props.getProperty(project));
				menuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						openProject(projectFile);
						repaint();
					}
				});
				openProjectMenu.add(menuItem);
			}
		}
	}
	
	private void createProject() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new MyFileFilter());
		fileChooser.setDialogTitle("Create Project");
		int response = fileChooser.showSaveDialog(this);

		if (response == JFileChooser.APPROVE_OPTION) {
			File projectDir = fileChooser.getSelectedFile();
			projectDir.mkdir();
			String projectName = projectDir.getName();
			File projectFile = new File(projectDir, projectName + ".project");

			Properties projectProps = new Properties();
			projectProps.put("PROJECT_TYPE", "OCAML");
			projectProps.put("PROJECT_NAME", projectName);
			try {
				projectFile.createNewFile();
				projectProps.save(new FileOutputStream(projectFile), "");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			props.put("PROJECTS", props.getProperty("PROJECTS") + ";" + projectName);
			props.put(projectName, projectFile.getAbsolutePath());
			buildOpenProjectMenu();
			openProject(projectFile);
		}
		
		repaint();
	}
	
	private void registerProject() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new MyFileFilter());
		int response = fileChooser.showOpenDialog(this);

		if (response == JFileChooser.APPROVE_OPTION) {
			File projectFile = fileChooser.getSelectedFile();
			String projectName = projectFile.getName().replace(".project", "");
			props.put("PROJECTS", props.getProperty("PROJECTS") + ";" + projectName);
			props.put(projectName, projectFile.getAbsolutePath());
			buildOpenProjectMenu();
		}
		
		repaint();
	}

	private void openProject(File projectFile) {

		try {
			Project proj = projectFactory.create(projectFile);
			this.getMenuBar().add(proj.getMenu());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		repaint();
	}

	public Properties getProperties() {
		return props;
	}
	
	private class MyFileFilter extends FileFilter {
		@Override
		public boolean accept(File pathname) {
			return pathname.isDirectory();
		}

		@Override
		public String getDescription() {
			return "Project Directories";
		}
	}

}
