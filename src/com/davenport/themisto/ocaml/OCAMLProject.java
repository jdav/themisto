package com.davenport.themisto.ocaml;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;

import com.davenport.themisto.Module;
import com.davenport.themisto.Project;
import com.davenport.themisto.Shell;

public class OCAMLProject extends Project {

	public OCAMLProject(JFrame frame, File projectFile) {
		super(frame, projectFile);
	}
	
	public void loadModules() {
		File[] files = projectFile.getParentFile().listFiles();
		for (File file : files) {
			if (!file.getName().startsWith(".") && file.getName().endsWith(".ml")) {
				String moduleName = file.getName().replace(".ml", "");
				modules.add(new OCAMLModule(this, moduleName));
			}
		}
	}

	@Override
	public void link() {
		Shell shell = new Shell(getProjectFile().getParentFile(), messageFrame);
		List<String> cmds = new ArrayList<String>();
		cmds.add("/usr/bin/ocamlc");
		cmds.add("-o");
		cmds.add(executable.getName());
		for (Module module : modules) {
			cmds.add(module.getName() + ".ml");
		}
		shell.execute(cmds);
	}

}
