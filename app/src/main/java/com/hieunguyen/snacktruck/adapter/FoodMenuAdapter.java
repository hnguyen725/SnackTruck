package com.hieunguyen.snacktruck.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hieunguyen.snacktruck.R;
import com.hieunguyen.snacktruck.bean.FoodMenuItem;
import com.hieunguyen.snacktruck.bean.FoodType;
import com.hieunguyen.snacktruck.databinding.FoodMenuRowItemBinding;

import java.util.List;

/***
 * This is a food menu adapter class that will help connect the data and the UI together
 * by displaying each food menu item data onto a food menu view holder for a recycler view.
 */
public class FoodMenuAdapter extends RecyclerView.Adapter<FoodMenuAdapter.FoodMenuViewHolder> {
    private ItemCheckChangedListener checkChangedListener;
    private List<FoodMenuItem> menuItems;

    public FoodMenuAdapter(List<FoodMenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    /**
     * Create and return a new FoodMenuViewHolder.
     * @param parent The ViewGroup that the new view will be added to after bounding to an adapter
     *               position.
     * @param viewType The view of type of the new view.
     * @return a new FoodMenuViewHolder.
     */
    @NonNull
    @Override
    public FoodMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate view with the generated binding class for this view holder item layout.
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        FoodMenuRowItemBinding binding = FoodMenuRowItemBinding.inflate(inflater, parent, false);

        return new FoodMenuViewHolder(binding);
    }

    /**
     * Update the content of the FoodMenuViewHolder with data at the given position.
     * @param holder The FoodMenuViewHolder that should be updated to display the content of item
     *               at the given position.
     * @param position The position of the item from the adapter data set.
     */
    @Override
    public void onBindViewHolder(@NonNull FoodMenuViewHolder holder, int position) {
        FoodMenuItem menuItem = menuItems.get(position);
        holder.onBind(menuItem);
    }

    /**
     * @return The total number of items in the data set.
     */
    @Override
    public int getItemCount() {
        return menuItems != null ? menuItems.size() : 0;
    }

    /**
     * Set the adapter data set with the provided list of FoodMenuItem data.
     * @param menuItems A list of FoodMenuItem.
     */
    public void setData(List<FoodMenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    /**
     * Set the checked change listener callback for each of the the checked changed event
     * from the menu item selection checkbox.
     * @param checkChangedListener An ItemCheckChangedListener callback.
     */
    public void setCheckedChangeListener(ItemCheckChangedListener checkChangedListener) {
        this.checkChangedListener = checkChangedListener;
    }

    /**
     * This is a custom interface that a class will implement to listen to food menu item selection
     * change event.
     */
    public interface ItemCheckChangedListener {
        void onItemCheckChanged(int position, boolean isChecked);
    }

    /**
     * A class that reference to the views for a data item.
     */
    public class FoodMenuViewHolder extends RecyclerView.ViewHolder
            implements CompoundButton.OnCheckedChangeListener {
        private FoodMenuRowItemBinding binding;

        public FoodMenuViewHolder(FoodMenuRowItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        /**
         * Initialize view components with the provided FoodMenuItem data.
         * @param menuItem FoodMenuItem data to display to the views.
         */
        public void onBind(FoodMenuItem menuItem) {
            binding.checkboxMenuItemSelected.setText(menuItem.getName());
            binding.checkboxMenuItemSelected.setChecked(menuItem.isSelected());
            binding.checkboxMenuItemSelected.setOnCheckedChangeListener(this);

            Context context = binding.checkboxMenuItemSelected.getContext();

            // If menu item is veggie, then display green color.
            // Else display non veggie light red color.
            if (menuItem.getType() == FoodType.VEGGIE) {
                binding.checkboxMenuItemSelected
                        .setTextColor(ContextCompat.getColor(context, R.color.colorVeggie));
            } else {
                binding.checkboxMenuItemSelected
                        .setTextColor(ContextCompat.getColor(context, R.color.colorNonVeggie));
            }
        }

        /**
         * Checked change event listener that will listen to a checkbox selection change and notify
         * the callback if exists.
         * @param compoundButton The checkbox view for each menu item.
         * @param isChecked If the menu item is selected or not.
         */
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (checkChangedListener != null)
                checkChangedListener.onItemCheckChanged(getAdapterPosition(), isChecked);
        }
    }
}
