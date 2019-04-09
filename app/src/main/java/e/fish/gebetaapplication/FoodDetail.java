package e.fish.gebetaapplication;

import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import e.fish.gebetaapplication.Model.Food;

public class FoodDetail extends AppCompatActivity {

    TextView food_name, food_price, food_Description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String foodId="";

    FirebaseDatabase database;
    DatabaseReference foods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        // firebase init
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        //Init view

        numberButton = (ElegantNumberButton) findViewById(R.id.number_button_id);
        btnCart = (FloatingActionButton) findViewById(R.id.btnCart_id);

        food_Description = (TextView) findViewById(R.id.food_description_id);
        food_name = (TextView) findViewById(R.id.food_name_desc_id);
        food_price = (TextView) findViewById(R.id.food_price_id);
        food_image = (ImageView) findViewById(R.id.img_food_id);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_id);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppbar);

        //Get foodId form Intent

        if(getIntent() != null)
                foodId = getIntent().getStringExtra("FoodId");
        if(!foodId.isEmpty()){
            getDetailFood(foodId);
        }

     }

    private void getDetailFood(String foodId) {

        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Food food = dataSnapshot.getValue(Food.class);

                //set Image
                Glide.with(getBaseContext()).load(food.getImage()).into(food_image);

                collapsingToolbarLayout.setTitle(food.getName());

                food_price.setText(food.getPrice());
                food_name.setText(food.getName());
                food_Description.setText(food.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
