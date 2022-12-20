package cs349.uwaterloo.ca.fotag;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Network;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.util.Vector;

public class MainActivity extends AppCompatActivity implements Observer {
    Model mModel;
    LinearLayout mylayout;
    LinearLayout mylayout1;
    LinearLayout mylayout2;
    final Context context = this;
    private RequestQueue myRequestQueue;
    androidx.appcompat.widget.Toolbar tools;
    RatingBar filter;
    ImageView load;
    ImageView loadset;
    ImageView clear;
    int current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myRequestQueue = Volley.newRequestQueue(this);
        //set view
        setContentView(R.layout.activity_main);
        if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            current=1;
            mylayout1=findViewById(R.id.layout1);
            mylayout2=findViewById(R.id.layout2);
        } else {
            mylayout = findViewById(R.id.mylayout);
        }
        filter = findViewById(R.id.ratingBar);
        filter.setRating(getIntent().getFloatExtra("filter",0));
        //set model
        mModel = Model.getInstance();
        mModel.addObserver(this);
        mModel.initObservers();

        //settoolbar
        tools = findViewById(R.id.toolbar);
        setSupportActionBar(tools);

        load = findViewById(R.id.load);
        loadset = findViewById(R.id.loadimages);
        clear = findViewById(R.id.clear);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get url
                LayoutInflater layout = LayoutInflater.from(context);
                View promptsView = layout.inflate(R.layout.enter_url, null);
                AlertDialog.Builder DialogBuilder = new AlertDialog.Builder(context);
                DialogBuilder.setView(promptsView);
                //edit text to input url
                final EditText userInput = promptsView.findViewById(R.id.urlInput);

                DialogBuilder.setCancelable(false).setPositiveButton("Enter",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String URL = String.valueOf(userInput.getText());
                                if(URL.length()>3&&(URL.substring(URL.length() - 3).equalsIgnoreCase("jpg"))||(URL.substring(URL.length() - 3).equalsIgnoreCase("png"))) {
                                    mModel.addimage(URL, context, myRequestQueue);
                                } else {
                                    Toast.makeText(context,"invalid url",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = DialogBuilder.create();
                alertDialog.show();
            }
        });
        loadset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModel.addimage("https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/bunny.jpg",context,myRequestQueue);
                mModel.addimage("https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/chinchilla.jpg",context,myRequestQueue);
                mModel.addimage("https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/deer.jpg	",context,myRequestQueue);
                mModel.addimage("https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/doggo.jpg",context,myRequestQueue);
                mModel.addimage("https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/ducks.jpg",context,myRequestQueue);
                mModel.addimage("https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/fox.jpg	",context,myRequestQueue);
                mModel.addimage("https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/hamster.jpg",context,myRequestQueue);
                mModel.addimage("https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/hedgehog.jpg",context,myRequestQueue);
                mModel.addimage("https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/husky.jpg",context,myRequestQueue);
                mModel.addimage("https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/kitten.png",context,myRequestQueue);
                mModel.addimage("https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/loris.jpg",context,myRequestQueue);
                mModel.addimage("https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/puppy.jpg",context,myRequestQueue);
                mModel.addimage("https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/running.jpg",context,myRequestQueue);
                mModel.addimage("https://www.student.cs.uwaterloo.ca/~cs349/w20/assignments/images/sleepy.png",context,myRequestQueue);
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModel.clear();
            }
        });
        filter.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                setgrid();
            }
        });
        filter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setgrid();
                return filter.onTouchEvent(event);
            }
        });
   //     float rate = filter.getRating();
        setgrid();
    }



    @Override
    protected void onStop()
    {
        super.onStop();
        // Remove observer when activity is destroyed.
       if(context.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
            mylayout1.removeAllViews();
            mylayout2.removeAllViews();
            current=1;
        } else {
            mylayout.removeAllViews();
        }
        mModel.deleteObserver(this);

    }

    public void setgrid(){
        //clear grid
            if(context.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
                mylayout1.removeAllViews();
                mylayout2.removeAllViews();
                current=1;
            } else {
                mylayout.removeAllViews();
            }
            final ArrayList<myImage> myimages = mModel.imagesList();
            if(myimages!=null) {
                for (int i = 0; i < myimages.size(); i++) {
                    final myImage myimage = myimages.get(i);
                    ImageView imagev = myimage.getImage();
                    RatingBar myrating = myimage.getRating();
                    //set layout and listener
                    if (myimage != null && !myimage.ifset()) {
                        //image
                        myimage.setNumber(mModel.imagesList().size() - 1);
                        LinearLayout.LayoutParams lp1;
                        lp1 = new LinearLayout.LayoutParams(720, 720);
                        lp1.gravity = Gravity.CENTER_HORIZONTAL;
                        imagev.setLayoutParams(lp1);

                        myrating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                            @Override
                            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                mModel.changeimagerate();
                            }
                        });
                        myimage.set();
                    }
                    //if satisfy filter show
                    if (myrating.getRating() >= filter.getRating()) {
                        imagev.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, PhotoDetails.class);
                             //   intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                Bundle extras = new Bundle();
                                extras.putInt("number", myimage.number);
                                extras.putFloat("filter", filter.getRating());
                                intent.putExtras(extras);
                              //  onPause();
                                startActivity(intent);
                                finish();
                              //  onResume();
                            }
                        });
                        LinearLayout parent = (LinearLayout) imagev.getParent();
                        if (parent != null) {
                            parent.removeView(imagev);
                        }
                        //rating
                        LinearLayout parent2 = (LinearLayout) myrating.getParent();
                        if (parent2 != null) {
                            parent2.removeView(myrating);
                        }

                        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            if (current == 1) {
                                mylayout1.addView(imagev);
                            } else {
                                mylayout2.addView(imagev);
                            }
                        } else {
                            mylayout.addView(imagev);
                        }
                        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            if (current == 1) {
                                mylayout1.addView(myrating);
                                current = 2;
                            } else {
                                mylayout2.addView(myrating);
                                current = 1;
                            }
                        } else {
                            mylayout.addView(myrating);
                        }
                    }


                }
            }
    }

    @Override
    public void update(Observable o, Object arg) {
        setgrid();
    }

}
