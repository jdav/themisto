package com.davenport.themisto;

public enum ShellType {
	
	BASH, CSH, KSH, SH;
	
	public String command() {
		switch (this) {
			case BASH: return "/bin/bash";
			case CSH: return "/bin/csh";
			case KSH: return "/bin/ksh";
			case SH: return "/bin/sh";
			default: return "ERROR";
		}
	}
}
