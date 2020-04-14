package edu.neu.foodiefriendfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.Objects;

import edu.neu.foodiefriendfinder.models.User;

public class FoodiesAdapter extends RecyclerView.Adapter<FoodiesAdapter.ViewHolder> {
    private int foodieLayout;
    private Context context;
    private List<User> foodieList;

    private OnItemClickListener mListener;
    private OnDineWithButtonItemClickListener dineWithButtonItemClickListener;

    /**
     * This interface can be used to implement a generic listener for the row of the recycler view.
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnDineWithButtonItemClickListener {
        void onDineWithIsClick(View button, int position);
    }
    
    void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    void setDineWithButtonListener(OnDineWithButtonItemClickListener dineWithButtonListener) {
        this.dineWithButtonItemClickListener = dineWithButtonListener;
    }

    FoodiesAdapter(int foodieLayout, Context context) {
        this.foodieLayout = foodieLayout;
        this.context = context;
    }

    void setFoodies(List<User> foodies) {
        foodieList = foodies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(foodieLayout, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User foodie = foodieList.get(position);
        holder.bind(foodie, this.context);

        if (dineWithButtonItemClickListener != null) {
            Objects.requireNonNull(holder).getDineWithButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dineWithButtonItemClickListener.onDineWithIsClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return foodieList == null ? 0 : foodieList.size();
    }

    List<User> getFoodieList() {
        return this.foodieList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fName;
        TextView fAge;
        TextView fLanguages;
        TextView fCuisines;
        TextView fRestaurantsPicked;
        ImageView userAvatar;
        Button dineWithButton;

        ViewHolder(View view, final OnItemClickListener listener) {
            super(view);
            fName = view.findViewById(R.id.fullNameProfile);
            fAge = view.findViewById(R.id.ageProfile);
            fLanguages = view.findViewById(R.id.languageProfile);
            fCuisines = view.findViewById(R.id.cuisineProfile);
            fRestaurantsPicked = view.findViewById(R.id.restaurantsProfile);
            userAvatar = view.findViewById(R.id.userAvatar);
            dineWithButton = view.findViewById(R.id.dineWithButton);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

        Button getDineWithButton() {
            return dineWithButton;
        }

        void bind(User foodie, Context context) {
            StringBuilder fullName = new StringBuilder(foodie.getFirstName() + " " + foodie.getLastName());
            fName.setText(fullName);
            fAge.setText(foodie.getAge());

            StringBuilder languages = languagesStringPrep(foodie);
            fLanguages.setText(languages);

            StringBuilder cuisines = cuisinesStringPrep(foodie);
            fCuisines.setText(cuisines);

            StringBuilder restaurants = restaurantsStringPrep(foodie);
            fRestaurantsPicked.setText(restaurants);

            Glide.with(context).load(userAvatar).apply(new RequestOptions().transform(
                    new CenterCrop(), new RoundedCorners(20))).into(userAvatar);
        }

        // https://stackoverflow.com/questions/599161/best-way-to-convert-an-arraylist-to-a-string
        StringBuilder languagesStringPrep(User foodie) {
            StringBuilder languages = new StringBuilder();
            for (String language : foodie.getLanguages()) {
                languages.append(language);
                languages.append(", ");
            }
            return languages;
        }

        StringBuilder cuisinesStringPrep(User foodie) {
            StringBuilder cuisines = new StringBuilder();
            for (String cuisine : foodie.getCuisine()) {
                cuisines.append(cuisine);
                cuisines.append(", ");
            }
            return cuisines;
        }

        StringBuilder restaurantsStringPrep(User foodie) {
            StringBuilder restaurants = new StringBuilder();
            for (String restaurant : foodie.getInterestedRestaurants()) {
                restaurants.append(restaurant);
                restaurants.append(", ");
            }
            return restaurants;
        }
    }
}
