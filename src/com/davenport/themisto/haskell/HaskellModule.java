package com.davenport.themisto.haskell;
import java.io.File;
import java.io.IOException;

import com.davenport.themisto.Module;
import com.davenport.themisto.Project;
import com.davenport.themisto.Shell;

public class HaskellModule extends Module {

	public HaskellModule(Project project, String name) {
		super(project, name);
		this.sourceFile = new File(project.getProjectFile().getParentFile(), name + ".hs");
	}

	public void compile() {
		Shell shell = new Shell(project.getProjectFile().getParentFile(), project.getMessageFrame());
		shell.execute("ghc --make -c " + sourceFile.getName());
	}
}
