package com.hieunguyen.snacktruck.ui.main;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.hieunguyen.snacktruck.R;
import com.hieunguyen.snacktruck.adapter.FoodMenuAdapter;
import com.hieunguyen.snacktruck.bean.FoodMenuItem;
import com.hieunguyen.snacktruck.bean.FoodType;
import com.hieunguyen.snacktruck.databinding.FoodMenuFragmentBinding;
import com.hieunguyen.snacktruck.viewmodel.FoodMenuViewModel;

import java.util.EnumSet;
import java.util.List;

/**
 * This is a fragment to display the UI for the main menu list page. This page contains the views
 * for the user to select, add, filter menu items, and submit a new order.
 */
public class FoodMenuFragment extends Fragment implements CompoundButton.OnCheckedChangeListener,
        View.OnClickListener {
    private FoodMenuFragmentBinding binding;
    private FoodMenuViewModel viewModel;
    private FoodMenuAdapter foodMenuAdapter;

    public static FoodMenuFragment newInstance() {
        return new FoodMenuFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set has options menu to handle 'Add' new menu item event in this fragment
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate view with the generated binding class for this fragment layout.
        binding = FoodMenuFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the FoodMenuViewModel with the hosted activity as the ViewModelStoreOwner.
        viewModel = new ViewModelProvider(requireActivity()).get(FoodMenuViewModel.class);

        // Only call the view model init method if the the app is not resuming or after screen rotation
        if (savedInstanceState == null)
            viewModel.init();

        initViewModelObservers();

        binding.checkboxNonVeggie.setOnCheckedChangeListener(this);
        binding.checkboxVeggie.setOnCheckedChangeListener(this);
        binding.btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.mi_Add:
                addMenuItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_submit:
                submitOrder();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        int id = compoundButton.getId();

        switch (id) {
            case R.id.checkbox_veggie:
            case R.id.checkbox_nonVeggie:
                filterItems();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        viewModel.getMenuItemsLiveData().removeObservers(requireActivity());
    }

    /**
     * Initialize the observers for view model data changes.
     */
    private void initViewModelObservers() {
        // Observe on change event when the list of FoodMenuItem from the view model changes.
        viewModel.getMenuItemsLiveData().observe(requireActivity(), new Observer<List<FoodMenuItem>>() {
            @Override
            public void onChanged(List<FoodMenuItem> foodMenuItems) {
                setFoodMenuAdapter(foodMenuItems);
            }
        });
    }

    /**
     * With the provided list of FoodMenuItem data, create or update the recycler view adapter.
     * @param foodMenuItems A list of FoodMenuItem to display on the recycler view.
     */
    private void setFoodMenuAdapter(List<FoodMenuItem> foodMenuItems) {
        // If adapter is null then create a new adapter, otherwise set the new data and notify the change.
        if (foodMenuAdapter == null) {
            foodMenuAdapter = new FoodMenuAdapter(foodMenuItems);
            foodMenuAdapter.setCheckedChangeListener(new FoodMenuAdapter.ItemCheckChangedListener() {
                @Override
                public void onItemCheckChanged(int position, boolean isChecked) {
                    if (viewModel != null)
                        viewModel.itemSelectionChanged(position, isChecked);
                }
            });

            binding.rvMenu.setAdapter(foodMenuAdapter);
            binding.rvMenu.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            foodMenuAdapter.setData(foodMenuItems);
            foodMenuAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Use the view model to submit a new order for the new user and display a summary dialog if the
     * order is submitted successfully.
     */
    private void submitOrder() {
        if (viewModel == null)
            return;

        boolean isSuccess = viewModel.submitOrder();

        if (isSuccess)
            showOrderSummaryDialog();
    }

    /**
     * Display a summary dialog of the list of food menu items that were selected for the user's order.
     */
    private void showOrderSummaryDialog() {
        List<String> menuItemNames = viewModel.getSelectedItemNames();
        final CharSequence[] items = menuItemNames.toArray(new CharSequence[menuItemNames.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Order Summary");
        builder.setItems(items, null);
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                // Reset the menu item list and filters when dialog is dismissed.
                resetMenu();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Reset the main menu list to the a full list of food menu items with both veggie and non-veggie
     * filters selected.
     */
    private void resetMenu() {
        binding.checkboxVeggie.setChecked(true);
        binding.checkboxNonVeggie.setChecked(true);
        viewModel.resetMenuItems();
    }

    /**
     * Using the view model to filter the list of food menu items base on the current food type
     * filter selections.
     */
    private void filterItems() {
        if (viewModel == null)
            return;

        // Start with no filter as default.
        EnumSet<FoodType> filterTypes = EnumSet.noneOf(FoodType.class);

        // Add filter type base on whether each filter is selected.
        if (binding.checkboxVeggie.isChecked())
            filterTypes.add(FoodType.VEGGIE);

        if (binding.checkboxNonVeggie.isChecked())
            filterTypes.add(FoodType.NON_VEGGIE);

        viewModel.filterMenuItems(filterTypes);
    }

    /**
     * Open a new simple dialog for the user to add a food menu item.
     */
    private void addMenuItem() {
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        AddMenuItemDialogFragment fragment = AddMenuItemDialogFragment.newInstance();
        fragment.show(fm, "fragment_add_menu_item");
    }
}
