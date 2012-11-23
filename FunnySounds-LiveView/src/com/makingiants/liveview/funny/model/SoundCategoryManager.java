package com.makingiants.liveview.funny.model;

import java.util.ArrayList;

import android.content.Context;

import com.makingiants.liveview.funny.model.dao.CategoryDAO;

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
	
	// ****************************************************************
	// Constructor
	// ****************************************************************
	
	public SoundCategoryManager(final Context context) {
		
		this.categories = CategoryDAO.getCategories(context);
		this.actualCategory = 0;
		
	}
	
	// ****************************************************************
	// Accessor Methods
	// ****************************************************************
	
	public Sound getActualSound() {
		SoundCategory category = categories.get(actualCategory);
		return category.getActualSound();
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
	
}
