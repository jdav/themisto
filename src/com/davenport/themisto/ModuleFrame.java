package com.davenport.themisto;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;


public class ModuleFrame extends JInternalFrame {

	private static final long serialVersionUID = 6903762122495346523L;

	public ModuleFrame(final Module module) {
		String fileContents = "";

		try {
			// Retrieve module contents...
			FileInputStream fis = new FileInputStream(module.getSourceFile());

			while (fis.available() > 0) {
				fileContents += (char) fis.read();
			}
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		final JTextPane textPane = new JTextPane();
		textPane.setFont(Font.decode("ARIAL-PLAIN-18"));
		textPane.setText(fileContents);
		textPane.setVisible(true);
		
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setAutoscrolls(true);
		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.setVisible(true);
		
		this.getContentPane().add(scrollPane);

		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				module.save(textPane.getText());
			}
		});

		JMenuItem compile = new JMenuItem("Compile");
		compile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				module.compile();
			}
		});
		
		JMenu setDependencies = new JMenu("Set Dependencies");
		Collection<Module> modules = module.project.modules;
		for (final Module mod : modules) {
			if (mod != module) {
				final JMenuItem modMenuItem = new JMenuItem();
				if (module.hasDependency(mod)) {
					modMenuItem.setText("dependent on " + mod.getName());
				} else {
					modMenuItem.setText(mod.getName());
				}
				modMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if (module.hasDependency(mod)) {
							module.removeDependency(mod);
							modMenuItem.setText(mod.getName());
						} else {
							module.addDependency(mod);
							modMenuItem.setText("dependent on " + mod.getName());
						}
						module.project.sortModules();
					}
				});
				setDependencies.add(modMenuItem);
			}
		}
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(save);
		menuBar.add(compile);
		menuBar.add(setDependencies);

		this.setJMenuBar(menuBar);
		
		this.repaint();
	}
}
