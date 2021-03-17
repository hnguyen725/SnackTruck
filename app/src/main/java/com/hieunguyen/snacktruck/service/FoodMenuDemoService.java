package com.hieunguyen.snacktruck.service;

import com.hieunguyen.snacktruck.bean.FoodMenuItem;
import com.hieunguyen.snacktruck.bean.FoodType;

import java.util.ArrayList;
import java.util.List;

/**
 * A demo food menu service class that will act as a mock API calling service for working with
 * the menu item data.
 */
public class FoodMenuDemoService {

    /**
     * @return A list of demo food menu items data.
     */
    public static List<FoodMenuItem> getMenuItems() {
        List<FoodMenuItem> menuItems = new ArrayList<>();

        menuItems.add(new FoodMenuItem("French fries", FoodType.VEGGIE));
        menuItems.add(new FoodMenuItem("Veggieburger", FoodType.VEGGIE));
        menuItems.add(new FoodMenuItem("Carrots", FoodType.VEGGIE));
        menuItems.add(new FoodMenuItem("Apple", FoodType.VEGGIE));
        menuItems.add(new FoodMenuItem("Banana", FoodType.VEGGIE));
        menuItems.add(new FoodMenuItem("Milkshake", FoodType.VEGGIE));
        menuItems.add(new FoodMenuItem("Cheeseburger", FoodType.NON_VEGGIE));
        menuItems.add(new FoodMenuItem("Hamburger", FoodType.NON_VEGGIE));
        menuItems.add(new FoodMenuItem("Hot dog", FoodType.NON_VEGGIE));

        return menuItems;
    }

    /**
     * A demo service method to submit order for the provided user's menu item selections.
     * @param foodMenuItems A list of selected FoodMenuItem.
     * @return true to mock a success API call response after submitting the order.
     */
    public static boolean submitOrder(List<FoodMenuItem> foodMenuItems) {
        return true;
    }

    /**
     * A demo service method to add a new menu item to the list.
     * @param foodMenuItem The new FoodMenuItem data to be added.
     * @return true to mock a success API call response after adding a menu item.
     */
    public static boolean addMenuItem(FoodMenuItem foodMenuItem) {
        return true;
    }
}
