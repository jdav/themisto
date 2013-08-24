package com.davenport.themisto;

import java.awt.Container;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileFilter;

public abstract class Project {

	protected Menu menu = null;
	protected File projectFile = null;
	protected Collection<Module> modules = new ArrayList<Module>();
	protected File executable = null;
	protected JFrame frame = null;
	protected JTabbedPane tabs = null;
	protected MessageFrame messageFrame = null;

	public Project(JFrame frame, File projectFile) {
		this.frame = frame;
		this.projectFile = projectFile;
		File directory = projectFile.getParentFile();
		System.out.println("DIRECTORY = " + directory.getAbsolutePath());
		this.executable = new File(directory, directory.getName());
		this.tabs = new JTabbedPane();
		tabs.setVisible(true);

		MenuItem create = new MenuItem();
		create.setLabel("Create Module...");
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createModule();
			}
		});

		MenuItem view = new MenuItem();
		view.setLabel("View");
		view.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				view();
			}
		});

		MenuItem link = new MenuItem();
		link.setLabel("Link");
		link.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				link();
			}
		});

		MenuItem run = new MenuItem();
		run.setLabel("Run");
		run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				run();
			}
		});

		MenuItem close = new MenuItem();
		close.setLabel("Close");
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				close();
			}
		});

		menu = new Menu();
		menu.setLabel(directory.getName());
		menu.add(create);
		menu.add(view);
		menu.add(link);
		menu.add(run);
		menu.add(close);

		loadModules();
		view();
	}

	// Getters...
	public JFrame getFrame() {
		return frame;
	}

	public Collection<Module> getModules() {
		return modules;
	}

	public File getExecutable() {
		return executable;
	}

	public Menu getMenu() {
		return menu;
	}

	public MessageFrame getMessageFrame() {
		return messageFrame;
	}

	public File getProjectFile() {
		return projectFile;
	}

	// Actions...
	public abstract void loadModules();

	private void createModule() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(this.projectFile.getParentFile());
		fileChooser.setFileFilter(new MyFileFilter());
		int response = fileChooser.showSaveDialog(frame);

		if (response == JFileChooser.APPROVE_OPTION) {
			File projectFile = fileChooser.getSelectedFile();
			if (!projectFile.getAbsolutePath().endsWith(".ml")) {
				projectFile = new File(projectFile.getAbsolutePath() + ".ml");
			}
			try {
				projectFile.createNewFile();
			} catch (IOException e) {
				messageFrame
						.write(e.getMessage() + "\n" + e.fillInStackTrace());
			}
		}
	}

	private void view() {
		while (tabs.getTabCount() > 0) {
			tabs.removeTabAt(0);
		}

		for (final Module module : modules) {
			tabs.addTab(module.getName(), new ModuleFrame(module));
		}

		messageFrame = new MessageFrame();
		tabs.addTab("Messages", messageFrame);

		Container container = getFrame().getContentPane();
		container.removeAll();
		container.add(tabs);

		container.repaint();
	}

	public abstract void link();

	public void run() {
		Shell shell = new Shell(messageFrame);
		shell.execute(executable.getAbsolutePath());
	}

	private void close() {
		Container container = getFrame().getContentPane();
		container.removeAll();
		container.repaint();
		frame.getMenuBar().remove(menu);
		frame.repaint();
	}

	protected void sortModules() {
		Collections.sort((List<Module>) modules, new Comparer());
	}

	private final class Comparer implements Comparator<Module> {
		@Override
		public int compare(Module first, Module second) {
			if (first.hasDependency(second)) {
				return 1;
			}

			if (second.hasDependency(first)) {
				return -1;
			}

			return 0;
		}
	}

	private class MyFileFilter extends FileFilter {
		@Override
		public boolean accept(File pathname) {
			return pathname.isDirectory() || pathname.getName().endsWith(".ml");
		}

		@Override
		public String getDescription() {
			return "ML Source Files (*.ml)";
		}
	}

}
