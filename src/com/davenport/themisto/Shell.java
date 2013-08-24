package com.davenport.themisto;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Shell {
	
	private File baseDirectory = null;
	private EchoTarget echoTarget = null;

	public Shell() {
	}
	
	public Shell(File baseDirectory) {
		this();
		setBaseDirectory(baseDirectory);
	}
	
	public Shell(EchoTarget echoTarget) {
		this();
		setEchoTarget(echoTarget);
	}
	
	public Shell(File baseDirectory, EchoTarget echoTarget) {
		this();
		setBaseDirectory(baseDirectory);
		setEchoTarget(echoTarget);
	}

	public EchoTarget getEchoTarget() {
		return echoTarget;
	}

	public void setEchoTarget(EchoTarget echoTarget) {
		this.echoTarget = echoTarget;
	}
	
	public void setBaseDirectory(File baseDirectory) {
		this.baseDirectory = baseDirectory;
	}
	
	public void execute(String command) {
		List<String> commands = new ArrayList<String>();
		commands.add(command);
		execute(commands);
	}

	public void execute(List<String> commands) {
		try {
			ProcessBuilder pb = new ProcessBuilder();
			if (baseDirectory != null) {
				pb.directory(baseDirectory);
				if (echoTarget != null) {
					echoTarget.write("Working directory: " + baseDirectory.getAbsolutePath() + "\n");
				}
			}
			pb.command(commands);
			
			for (String command : commands) {
				if (echoTarget != null) {
					echoTarget.write(command + " ");
				}
			}
			
			if (echoTarget != null) {
				echoTarget.write("\n");
			}
			
			Process process = pb.start();
			
		    BufferedReader stdin = 
		    	new BufferedReader(new InputStreamReader(process.getInputStream()));
		    BufferedReader stderr = 
		    	new BufferedReader(new InputStreamReader(process.getErrorStream()));
		    
			String readValue = null;
			while ((readValue = stdin.readLine()) != null) {				
				if (echoTarget != null) {
					echoTarget.write(readValue + "\n");
				}
			}

			while ((readValue = stderr.readLine()) != null) {
				if (echoTarget != null) {
					echoTarget.write(readValue + "\n");
				}
			}
			
			process.destroy();
		} catch (IOException e) {
			if (echoTarget != null) {
				echoTarget.write(e.getMessage() + "\n");
				StackTraceElement[] stes = e.getStackTrace();
				for (StackTraceElement ste : stes) {
					echoTarget.write(ste.toString() + "\n");
				}
			}
			e.printStackTrace();
		}
	}
}
