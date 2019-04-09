package e.fish.gebetaapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import e.fish.gebetaapplication.Interface.ItemClickListener;
import e.fish.gebetaapplication.Model.Food;
import e.fish.gebetaapplication.ViewHolder.FoodViewHolder;

public class FoodList extends AppCompatActivity {
    private static final String TAG = "FoodList";

    RecyclerView recyclerView_foodList;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;

    String categoryId = "";

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        Log.d(TAG, "onCreate: started.");

        // Firebase init
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");

        recyclerView_foodList = (RecyclerView) findViewById(R.id.recycler_foodList_id);
        recyclerView_foodList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView_foodList.setLayoutManager(layoutManager);

        //get intent here

        if(getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if(!categoryId. isEmpty() ){

            loadListFood(categoryId);
        }
    }

    private void loadListFood(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item, FoodViewHolder.class,
                foodList.orderByChild("MenuId").equalTo(categoryId)) //like : select from Foods where MenuId = categoryId
        {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {

                viewHolder.food_Name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_image);

                //final Food clickItem = model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                            //Toast.makeText(FoodList.this, clickItem.getName(), Toast.LENGTH_SHORT).show();

                                                //get food id and send to new Activity
                        Intent foodDetail = new Intent(FoodList.this, FoodDetail.class);
                        foodDetail.putExtra("FoodId", adapter.getRef(position).getKey()); //send food id to new activity
                        startActivity(foodDetail);
                    }

                });
            }
        };
        //set Adapter
        Log.d(TAG, ""+adapter.getItemCount());
        recyclerView_foodList.setAdapter(adapter);

    }
}
