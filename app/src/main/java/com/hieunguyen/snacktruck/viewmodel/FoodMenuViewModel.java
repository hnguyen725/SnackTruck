package com.hieunguyen.snacktruck.viewmodel;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hieunguyen.snacktruck.bean.FoodMenuItem;
import com.hieunguyen.snacktruck.bean.FoodType;
import com.hieunguyen.snacktruck.service.FoodMenuDemoService;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * A view model class for the food menu that will help with handling the data logic for the UI.
 */
public class FoodMenuViewModel extends ViewModel {
    private List<FoodMenuItem> allMenuItems;
    private List<FoodMenuItem> filteredMenuItems;
    private MutableLiveData<List<FoodMenuItem>> menuItemsLiveData;
    private EnumSet<FoodType> filterTypes;

    /**
     * Initialize all list of menu items and fetch all menu items from the service.
     */
    public void init() {
        allMenuItems = new ArrayList<>();
        filteredMenuItems = new ArrayList<>();
        menuItemsLiveData = new MutableLiveData<>(filteredMenuItems);

        fetchAllMenuItems();
    }

    /**
     * @return The MutableLiveData object for a list of FoodMenuItem, this will be used so that the
     * View classes can observe any changes to this data.
     */
    public MutableLiveData<List<FoodMenuItem>> getMenuItemsLiveData() {
        if (menuItemsLiveData == null)
            menuItemsLiveData = new MutableLiveData<>();

        return menuItemsLiveData;
    }

    /**
     * Call the demo service to fetch all menu item data.
     */
    public void fetchAllMenuItems() {
        if (menuItemsLiveData == null)
            menuItemsLiveData = new MutableLiveData<>();

        // Call service to perform demo API call and get a list of all menu items
        allMenuItems = FoodMenuDemoService.getMenuItems();

        // Call filter method after service call, using both food type as default.
        filterMenuItems(EnumSet.of(FoodType.VEGGIE, FoodType.NON_VEGGIE));

        menuItemsLiveData.postValue(filteredMenuItems);
    }

    /**
     * Get a list of selected food menu items and call the service to submit a new order.
     * @return true if the order submission is successfully, false otherwise.
     */
    public boolean submitOrder() {
        List<FoodMenuItem> selectedItems = getSelectedItems();

        if (selectedItems != null && selectedItems.size() > 0)
            return FoodMenuDemoService.submitOrder(selectedItems);

        return false;
    }

    /**
     * Reset all menu items selection to false.
     */
    public void resetMenuItems() {
        if (allMenuItems == null)
            return;

        for (FoodMenuItem menuItem : allMenuItems) {
            menuItem.setSelected(false);
        }

        filteredMenuItems = new ArrayList<>(allMenuItems);
        menuItemsLiveData.postValue(filteredMenuItems);
    }

    /**
     * Handle food menu item selection change.
     * @param position the position of the food menu item in the current filtered list.
     * @param isSelected whether the item at the provided position is selected.
     */
    public void itemSelectionChanged(int position, boolean isSelected) {
        if (allMenuItems == null || filteredMenuItems == null)
            return;

        // Get the FoodMenuItem data from the filtered list.
        FoodMenuItem selectedItem = filteredMenuItems.get(position);
        selectedItem.setSelected(isSelected);

        // Find the data in the all menu item list, and set that data to selection to false also.
        // This will help the FoodMenuItem to have the correct selection state when a new filter
        // is applied.
        for (FoodMenuItem item : allMenuItems) {
            if (item.getName().equals(selectedItem.getName())) {
                item.setSelected(isSelected);
                break;
            }
        }
    }

    /**
     * @return a list of selected food menu items base on the filtered menu list.
     */
    public List<FoodMenuItem> getSelectedItems() {
        List<FoodMenuItem> selectedItems = new ArrayList<>();

        if (filteredMenuItems == null)
            return selectedItems;

        for (FoodMenuItem menuItem : filteredMenuItems) {
            if (menuItem.isSelected())
                selectedItems.add(menuItem);
        }

        return selectedItems;
    }

    /**
     * @return A list of selected food menu item names base on the filtered menu list.
     */
    public List<String> getSelectedItemNames() {
        List<String> selectedItems = new ArrayList<>();

        if (filteredMenuItems == null)
            return selectedItems;

        for (FoodMenuItem menuItem : filteredMenuItems) {
            if (menuItem.isSelected())
                selectedItems.add(menuItem.getName());
        }

        return selectedItems;
    }

    /**
     * Filter all food menu items base on the provided food type filters.
     * @param filterTypes An EnumSet of FoodType filters.
     */
    public void filterMenuItems(EnumSet<FoodType> filterTypes) {
        if (allMenuItems == null || filteredMenuItems == null)
            return;

        filteredMenuItems.clear();

        // Check each food menu item to see if its food type belongs a type in the filter types set
        // and add the item to the filtered list.
        for (FoodMenuItem menuItem : allMenuItems) {
            if (filterTypes.contains(menuItem.getType())) {
                filteredMenuItems.add(menuItem);
            }
        }

        menuItemsLiveData.postValue(filteredMenuItems);

        this.filterTypes = filterTypes;
    }

    /**
     * Call the service to add a new menu item to the list.
     * @param menuItem The FoodMenuItem data to be added.
     * @return true if the service call to add a menu item is successful, false otherwise.
     */
    public boolean addMenuItem(FoodMenuItem menuItem) {
        boolean isSuccess = FoodMenuDemoService.addMenuItem(menuItem);

        if (isSuccess) {
            allMenuItems.add(menuItem);

            // Call filter method to add the new menu item to the filtered list if the new item's food type
            // matches one of the filter types.
            filterMenuItems(filterTypes);
        }

        return isSuccess;
    }
}
