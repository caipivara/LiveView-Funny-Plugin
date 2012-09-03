package model;

public class Sound {

	private static final String FOLDER = "sounds/";
	
	private String file;
	private String name;
	
	public Sound(String name, String file){
		this.name = name;
		this.file = FOLDER + file;
	}

	public String getPath() {
		return file;
	}

	public void setFile(String path) {
		this.file = FOLDER + path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
