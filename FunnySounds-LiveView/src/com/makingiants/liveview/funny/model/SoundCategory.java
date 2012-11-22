package com.makingiants.liveview.funny.model;

import java.util.ArrayList;

public class SoundCategory {
	
	private int actualSound;
	
	private ArrayList<Sound> sounds;
	private String name;
	
	public SoundCategory(String name) {
		this.sounds = new ArrayList<Sound>();
		this.actualSound = 0;
		this.name = name;
	}
	
	public void addSound(Sound sound) {
		sounds.add(sound);
	}
	
	public Sound getActualSound() {
		return sounds.get(actualSound);
	}
	
	public Sound moveToNextSound() {
		
		if (++actualSound >= sounds.size()) {
			actualSound = 0;
		}
		
		return sounds.get(actualSound);
	}
	
	public Sound moveToPreviousSound() {
		if (--actualSound < 0) {
			actualSound = sounds.size() - 1;
		}
		
		return sounds.get(actualSound);
	}
	
	public String getName() {
		return name;
	}
}
