package com.makingiants.liveview.funny.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.util.Log;

import com.makingiants.nielda.music.SoundPlayer;

public class SoundManager {
	// ****************************************************************
	// Attributes
	// ****************************************************************
	
	private final static String SOUNDS_FILE = "sounds.xml";
	private HashMap<String, ArrayList<Sound>> sounds;
	private final Context context;
	private int actualSound;
	private String actualCategory;
	
	// ****************************************************************
	// Constructor
	// ****************************************************************
	
	public SoundManager(final Context context) {
		
		this.context = context;
		
		loadSounds();
		
		actualSound = 0;
		actualCategory = sounds.keySet().iterator().next();
	}
	
	// ****************************************************************
	// Accessor Methods
	// ****************************************************************
	
	public String getActualSound() {
		return sounds.get(actualCategory).get(actualSound).getName();
	}
	
	public String getActualCategory() {
		return actualCategory;
	}
	
	// ****************************************************************
	// Jump Methods
	// ****************************************************************
	
	public String jumpNextSound() {
		
		if (++actualSound >= sounds.get(actualCategory).size()) {
			actualSound = 0;
		}
		
		return sounds.get(actualCategory).get(actualSound).getName();
	}
	
	public String jumpPastSound() {
		
		if (--actualSound < 0) {
			actualSound = sounds.get(actualCategory).size() - 1;
		}
		
		return sounds.get(actualCategory).get(actualSound).getName();
		
	}
	
	public String jumpNextCategory() {
		
		String nextCategory = "";
		Iterator<String> iterator = sounds.keySet().iterator();
		
		Log.d("eee actual", actualCategory);
		while (iterator.hasNext()) {
			
			nextCategory = iterator.next();
			Log.d("eee next", nextCategory);
			if (nextCategory.equals(actualCategory)) {
				if (iterator.hasNext()) {
					nextCategory = iterator.next();
				} else {
					nextCategory = actualCategory;
				}
				break;
			}
			
		}
		
		actualSound = 0;
		actualCategory = nextCategory;
		
		return actualCategory;
		
	}
	
	public String jumpPastCategory() {
		
		String nextCategory = "";
		String pastCategory = "";
		Iterator<String> iterator = sounds.keySet().iterator();
		
		while (iterator.hasNext()) {
			
			nextCategory = iterator.next();
			
			if (nextCategory.equals(actualCategory)) {
				break;
			}
			
			pastCategory = nextCategory;
		}
		
		if (pastCategory.equals("")) {
			actualCategory = nextCategory;
		} else {
			actualCategory = pastCategory;
		}
		
		actualSound = 0;
		return actualCategory;
		
	}
	
	// ****************************************************************
	// Action Methods
	// ****************************************************************
	
	public void playSound() {
		// Play the sound
		// final Message msg = Message.obtain(mHandler, this);
		// mHandler.sendMessage(msg);
		new Thread(new Runnable() {
			public void run() {
				
				final Sound sound = sounds.get(actualCategory).get(actualSound);
				try {
					SoundPlayer.getInstance(context).play(sound.getPath());
				} catch (IOException e) {
					Log.e("IOException", "SoundManager 1", e);
				} catch (InterruptedException e) {
					Log.e("InterruptedException", "SoundManager 2", e);
				} catch (Exception e) {
					Log.e("Exception", "SoundManager 3", e);
				}
				
			}
		}).start();
	}
	
	// ****************************************************************
	// XML Setup load
	// ****************************************************************
	
	public void loadSounds() {
		try {
			
			final boolean done = false;
			
			final XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			final XmlPullParser parser = factory.newPullParser();
			
			final InputSource source = new InputSource(context.getAssets().open(SOUNDS_FILE));
			parser.setInput(source.getByteStream(), source.getEncoding());
			
			int eventType = parser.getEventType();
			String name = "";
			
			while (eventType != XmlPullParser.END_DOCUMENT && !done) {
				
				switch (eventType) {
				
					case XmlPullParser.START_TAG:
						
						name = parser.getName();
						
						if (name.equals("sounds")) {
							sounds = new HashMap<String, ArrayList<Sound>>();
							
						} else if (name.equals("category")) {
							actualCategory = parser.getAttributeValue(0);
						} else if (name.equals("sound")) {
							
							ArrayList<Sound> soundsTemp = sounds.get(actualCategory);
							if (soundsTemp == null) {
								soundsTemp = new ArrayList<Sound>();
							}
							soundsTemp.add(new Sound(parser.getAttributeValue(0), parser
							        .getAttributeValue(1)));
							
							sounds.put(actualCategory, soundsTemp);
						}
						
						break;
				}
				eventType = parser.next();
			}
		} catch (final FileNotFoundException e) {
			Log.e("FileNotFoundException", e.getMessage());
		} catch (final IOException e) {
			Log.e("IOException en Favorito", e.getMessage());
		} catch (final Exception e) {
			Log.e("Exception en Favorito", e.getMessage());
		}
	}
}
