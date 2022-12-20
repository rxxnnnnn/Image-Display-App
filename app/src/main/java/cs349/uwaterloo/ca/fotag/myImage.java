package cs349.uwaterloo.ca.fotag;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;


public class myImage {
    String myurl;
    int number;
    ImageView myimage;
    RatingBar myrating;
    boolean set;

    myImage(String url, final Context c, RequestQueue myRequestQueue){
        myurl = url;
        myrating= new RatingBar(c);
        myrating.setRating(0);
        myrating.setNumStars(5);
        myrating.setStepSize(1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.gravity= Gravity.CENTER_HORIZONTAL;
        myrating.setLayoutParams(lp2);

        myimage=new ImageView(c);
        ImageRequest imageRequest = new ImageRequest(myurl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        myimage.setImageBitmap(bitmap);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(c,"invalid url",Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });
        myRequestQueue.add(imageRequest);
        myimage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(10,10);
        lp1.gravity= Gravity.CENTER;
        myimage.setLayoutParams(lp1);
    }


    public  void  setNumber(int number){this.number=number;}

    public RatingBar getRating() {
        return myrating;
    }

    public float getRatingNumber(){return myrating.getRating();}

    public void setRating(float rate) {
        myrating.setRating(rate);
    }

    public ImageView getImage() {
        return myimage;
    }

    public String getULR() {
        return myurl;
    }

    public void set(){set=true;}

    public boolean ifset(){return this.set;}

}
