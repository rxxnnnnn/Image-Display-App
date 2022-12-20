package cs349.uwaterloo.ca.fotag;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;

public class PhotoDetails extends AppCompatActivity implements Observer {
    Model mModel;
    LinearLayout mylayout;
    private RequestQueue myRequestQueue;
    ImageView newimage;
    RatingBar newratingbar;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set view
        setContentView(R.layout.details_images);
        mylayout = findViewById(R.id.mylayout2);
        myRequestQueue = Volley.newRequestQueue(this);
        //set model
        mModel = Model.getInstance();
        mModel.addObserver(this);
        mModel.initObservers();

        Intent intent1 = getIntent();
        final int number = intent1.getExtras().getInt("number");
        final float myrate = intent1.getExtras().getFloat("filter");
        //init rating
        newratingbar = new RatingBar(context);
        newratingbar.setRating(0);
        newratingbar.setNumStars(5);
        newratingbar.setStepSize(1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.gravity= Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM;
        newratingbar.setLayoutParams(lp2);

        newimage = new ImageView(context);
        newimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(context, MainActivity.class);
                intent2.putExtra("filter",myrate);
                startActivity(intent2);
             //   finish();
            }
        });
        LinearLayout.LayoutParams lp1;
        if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,1080-250);
        } else {
            lp1 = new LinearLayout.LayoutParams(1080, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        lp1.gravity= Gravity.CENTER_HORIZONTAL|Gravity.TOP;
        ArrayList<myImage> myimage = mModel.imagesList();

        if(myimage.size()>number) {
            String url = myimage.get(number).getULR();
            newimage.setLayoutParams(lp1);
            if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
                newimage.setMaxHeight(2160-250);
            }
            newimage.setAdjustViewBounds(true);
            newimage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageRequest imageRequest = new ImageRequest(url,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            newimage.setImageBitmap(bitmap);
                        }
                    }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context,"invalid url",Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    });
            myRequestQueue.add(imageRequest);
            if(newimage.getParent()==null)mylayout.addView(newimage);
            //rating
            float rate = myimage.get(number).getRatingNumber();
            newratingbar.setRating(rate);
            newratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {


                    mModel.changeRate(number,rating);
                }
            });
            if(newratingbar.getParent()==null)mylayout.addView(newratingbar);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        // Remove observer when activity is destroyed.
        mModel.deleteObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
