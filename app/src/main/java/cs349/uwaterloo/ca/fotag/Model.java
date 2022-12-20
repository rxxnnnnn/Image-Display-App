package cs349.uwaterloo.ca.fotag;


import android.content.Context;
import android.widget.LinearLayout;
import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


class Model extends Observable {  //some codes from MVC2 example
    private static Model ourInstance;
    private ArrayList<myImage> images;

    static Model getInstance()
    {
        if (ourInstance == null) {
            ourInstance = new Model();
        }
        return ourInstance;
    }

    Model(){
        images= new ArrayList<>();
    }

    //addimage
    public void addimage(String url, Context c, RequestQueue rq){
        myImage newimage = new myImage(url,c,rq);
        images.add(newimage);
        setChanged();
        notifyObservers();
    }




    public ArrayList<myImage> imagesList(){return images; }

    public void changeimagerate(){
        setChanged();
        notifyObservers();
    }
    //clear images
    public void clear(){
        images.clear();
        setChanged();
        notifyObservers();
    }

    public void changeRate(int number, float rate){
        images.get(number).setRating(rate);
    }

    public void initObservers()
    {
        setChanged();
        notifyObservers();
    }

    @Override
    public synchronized void addObserver(Observer o)
    {

        super.addObserver(o);
    }

    @Override
    public void deleteObserver(Observer o)
    {

        super.deleteObserver(o);
    }

    @Override
    public void notifyObservers()
    {
        super.notifyObservers();
    }

}
