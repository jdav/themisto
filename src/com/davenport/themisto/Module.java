package com.davenport.themisto;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


public abstract class Module {

	private static final long serialVersionUID = 1L;

	private File objectFile = null;
	protected File sourceFile = null;
	private String name = null;
	protected Project project = null;
	private Collection<Module> dependencies = new ArrayList<Module>();

	public Module(Project project, String name) {
		this.project = project;
		this.name = name;		
		this.objectFile = new File(project.getProjectFile().getParentFile(), name + ".o");
	}

	public String getName() {
		return name;
	}

	public File getObjectFile() {
		return objectFile;
	}

	public File getSourceFile() {
		return sourceFile;
	}

	public void save(String newContents) {
		try {
			FileOutputStream fos = new FileOutputStream(sourceFile);
			fos.write(newContents.getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addDependency(Module module) {
		dependencies.add(module);
	}
	
	public void removeDependency(Module module) {
		dependencies.remove(module);
	}
	
	public boolean hasDependency(Module module) {
		return dependencies.contains(module);
	}

	public abstract void compile();
}
