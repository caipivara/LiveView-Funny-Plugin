package com.makingiants.liveview.funny.model;

import java.io.IOException;
import java.util.ArrayList;

import com.makingiants.liveview.funny.model.dao.CategoryDAO;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

public class SoundCategoryManager {
	// ****************************************************************
	// Attributes
	// ****************************************************************
	
	public enum ACTUAL_CATEGORY_STATE {
		TOP, BOTTOM, OTHER
	};
	
	// Category 
	private ArrayList<SoundCategory> categories;
	private int actualCategory;
	
	// Player variables
	private final Context context;
	private MediaPlayer player;
	
	// ****************************************************************
	// Constructor
	// ****************************************************************
	
	public SoundCategoryManager(final Context context) {
		
		this.context = context;
		
		categories = CategoryDAO.getCategories(context);
		
		actualCategory = 0;
		
	}
	
	// ****************************************************************
	// Accessor Methods
	// ****************************************************************
	
	public String getActualSound() {
		SoundCategory category = categories.get(actualCategory);
		return category.getActualSound().getName();
	}
	
	public String getActualCategory() {
		return categories.get(actualCategory).getName();
	}
	
	public ACTUAL_CATEGORY_STATE getActualCategoryState() {
		if (actualCategory == 0) {
			return ACTUAL_CATEGORY_STATE.TOP;
		} else if (actualCategory == categories.size() - 1) {
			return ACTUAL_CATEGORY_STATE.BOTTOM;
		} else {
			return ACTUAL_CATEGORY_STATE.OTHER;
		}
	}
	
	// ****************************************************************
	// Jump Methods
	// ****************************************************************
	
	public String jumpNextSound() {
		return categories.get(actualCategory).moveToNextSound().getName();
	}
	
	public String jumpPastSound() {
		return categories.get(actualCategory).moveToPreviousSound().getName();
	}
	
	public String jumpNextCategory() {
		
		if (actualCategory + 1 < categories.size()) {
			actualCategory++;
		}
		return categories.get(actualCategory).getName();
		
	}
	
	public String jumpPastCategory() {
		
		if (actualCategory - 1 > -1) {
			actualCategory--;
		}
		return categories.get(actualCategory).getName();
		
	}
	
	// ****************************************************************
	// Action Methods
	// ****************************************************************
	
	public void playSound() {
		// Play the sound
		
		new Thread(new Runnable() {
			public void run() {
				
				final Sound sound = categories.get(actualCategory).getActualSound();
				try {
					if (player != null && player.isPlaying()) {
						player.stop();
					} else {
						
						final AssetFileDescriptor afd = context.getAssets().openFd(sound.getFile());
						player = new MediaPlayer();
						player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
						        afd.getLength());
						player.prepare();
						player.start();
					}
				} catch (final IllegalArgumentException e) {
					Log.e("IllegalArgumentException", "SoundManager 1", e);
				} catch (final IllegalStateException e) {
					Log.e("IllegalStateException", "SoundManager 2", e);
				} catch (final IOException e) {
					Log.e("IOException", "SoundManager 3", e);
				}
				
			}
		}).start();
	}
	
}
