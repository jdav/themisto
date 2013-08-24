package com.davenport.themisto.ocaml;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.davenport.themisto.Module;
import com.davenport.themisto.Project;
import com.davenport.themisto.Shell;

public class OCAMLModule extends Module {

	public OCAMLModule(Project project, String name) {
		super(project, name);
		this.sourceFile = new File(project.getProjectFile().getParentFile(), name + ".ml");
	}

	public void compile() {
		Shell shell = new Shell(project.getProjectFile().getParentFile(), project.getMessageFrame());
		List<String> cmds = new ArrayList<String>();
		cmds.add("/usr/bin/ocamlc");
		cmds.add("-c");
		cmds.add(sourceFile.getName());
		shell.execute(cmds);
	}
}
