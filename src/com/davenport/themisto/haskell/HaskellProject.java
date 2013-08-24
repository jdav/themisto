package com.davenport.themisto.haskell;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import com.davenport.themisto.Project;
import com.davenport.themisto.Shell;

public class HaskellProject extends Project {

	public HaskellProject(JFrame frame, File projectFile) {
		super(frame, projectFile);
	}

	public void link() {
		Shell shell = new Shell(getProjectFile().getParentFile(), messageFrame);
		shell.execute("ghc --make -o " + executable.getName() + " Main.hs");
	}

	@Override
	public void loadModules() {
		File[] files = projectFile.getParentFile().listFiles();
		for (File file : files) {
			if (file.getName().endsWith(".hs")) {
				String moduleName = file.getName().replace(".hs", "");
				modules.add(new HaskellModule(this, moduleName));
			}
		}
	}
}
