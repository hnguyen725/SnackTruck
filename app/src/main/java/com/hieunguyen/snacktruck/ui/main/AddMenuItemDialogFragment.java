package com.hieunguyen.snacktruck.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.hieunguyen.snacktruck.R;
import com.hieunguyen.snacktruck.bean.FoodMenuItem;
import com.hieunguyen.snacktruck.bean.FoodType;
import com.hieunguyen.snacktruck.databinding.AddMenuItemDialogFragmentBinding;
import com.hieunguyen.snacktruck.viewmodel.FoodMenuViewModel;

/**
 * This is a dialog fragment class that will represents a simple dialog for a user to add a new
 * food menu item.
 */
public class AddMenuItemDialogFragment extends DialogFragment implements View.OnClickListener {
    private AddMenuItemDialogFragmentBinding binding;
    private FoodMenuViewModel viewModel;

    public static AddMenuItemDialogFragment newInstance() {
        return new AddMenuItemDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view with the generated binding class for this fragment layout.
        binding = AddMenuItemDialogFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the FoodMenuViewModel with the hosted activity as the ViewModelStoreOwner.
        viewModel = new ViewModelProvider(requireActivity()).get(FoodMenuViewModel.class);

        binding.btnSave.setOnClickListener(this);
        binding.btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_save:
                saveMenuItem();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    /**
     * Use the view model to save the new menu item to the menu list.
     */
    private void saveMenuItem() {
        if (viewModel == null)
            return;

        // Get provided name and food type selection from the UI
        String name = binding.etFoodName.getText().toString();
        FoodType foodType = binding.radioVeggie.isChecked() ? FoodType.VEGGIE : FoodType.NON_VEGGIE;

        // Create a new FoodMenuItem and use the view model to add a new menu item
        boolean isSuccess = viewModel.addMenuItem(new FoodMenuItem(name, foodType));

        if (isSuccess)
            dismiss();
    }
}
