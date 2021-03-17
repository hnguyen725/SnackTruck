package com.hieunguyen.snacktruck.bean;

/**
 * This is a class that represents a FoodMenuItem. For simple demo purpose, this class will contain
 * only a name of the menu item, a food type (veggie or non-veggie) of the menu item, and a boolean
 * value to determine if the menu item is selected.
 */
public class FoodMenuItem {
    private String name;
    private FoodType type;
    private boolean isSelected;

    public FoodMenuItem(String name, FoodType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FoodType getType() {
        return type;
    }

    public void setType(FoodType type) {
        this.type = type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}


