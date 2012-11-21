package com.makingiants.liveview.funny.model;

public class Sound {
	
	private static final String FOLDER = "sounds/";
	
	private String file;
	private String name;
	
	public Sound(final String name, final String file) {
		this.name = name;
		this.file = FOLDER + file;
	}
	
	public String getFile() {
		return file;
	}
	
	public void setFile(final String path) {
		this.file = FOLDER + path;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
}
