package com.davenport.themisto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFrame;

import com.davenport.themisto.haskell.HaskellProject;
import com.davenport.themisto.ocaml.OCAMLProject;


public class ProjectFactory {
	
	private JFrame frame = null;
	
	public ProjectFactory(JFrame frame) {
		this.frame = frame;
	}
	
	public Project create(File projectFile) throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(projectFile));
		ProjectType type = ProjectType.valueOf(properties.getProperty("PROJECT_TYPE"));
		
		switch (type) {
		case HASKELL: return new HaskellProject(frame, projectFile);
		case OCAML: return new OCAMLProject(frame, projectFile);
		default: throw new IOException();
		}
	}
}
