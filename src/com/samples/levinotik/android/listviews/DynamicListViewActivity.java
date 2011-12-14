package com.samples.levinotik.android.listviews;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Levi Notik
 * Date: 12/14/11
 */


/**
 * This Activity answers the frequently asked question
 * of how to change items on the fly in a ListView.
 *
 * In my own project, some of the elements (inner classes, etc)
 * might be extracted into separate classes, but for clarity
 * purposes, I'm doing everything inline.
 *
 * The example here is very, very basic. But if you understand
 * the concept, it can scale to anything. You have complex
 * views bound to complex data wit complex conditions.
 * You could model a facebook user and update the ListView
 * based on changes to that user's data that's represented in
 * your model.
 */
public class DynamicListViewActivity extends Activity {

    MyCustomAdapter mAdapter;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        ListView listView = new ListView(this);
        setContentView(listView);

        /**
         * Obviously, this will typically some from somewhere else,
         * as opposed to be creating manually, one by one.
         */

        ArrayList<MyObject> myListOfObjects = new ArrayList<MyObject>();

        MyObject object1 = new MyObject("I love Android", "ListViews are cool");
        myListOfObjects.add(object1);
        MyObject object2 = new MyObject("Broccoli is healthy", "Pizza tastes good");
        myListOfObjects.add(object2);
        MyObject object3 = new MyObject("bla bla bla", "random string");
        myListOfObjects.add(object3);

        //Instantiate your custom adapter and hand it your listOfObjects
        mAdapter = new MyCustomAdapter(this, myListOfObjects);
        listView.setAdapter(mAdapter);

        /**
         * Now you are free to do whatever the hell you want to your ListView.
         * You can add to the List, change an object in it, whatever.
         * Just let your Adapter know that that the data has changed so it
         * can refresh itself and the Views in the ListView.
         */

        /**Here's an example. Set object2's condition to true.
        If everyting worked right, then the background color
        of that row will change to blue
         Obviously you would do this based on some later event.
         */
        object2.setSomeCondition(true);
        mAdapter.notifyDataSetChanged();



    }


    /**
     *
     *  An Adapter is bridge between your data
     *  and the views that make up the ListView.
     *  You provide some data and the adapter
     *  helps to place them into the rows
     *  of the ListView.
     *
     *  Subclassing BaseAdapter gives you the most
     *  flexibility. You'll have to override some
     *  methods to get it working.
     */
    class MyCustomAdapter extends BaseAdapter {

        private List<MyObject> mObjects;
        private Context mContext;

        /**
         * Create a constructor that takes a List
         * of some Objects to use as the Adapter's
         * data
         */
        public MyCustomAdapter(Context context, List<MyObject> objects) {
            mObjects = objects;
            mContext = context;
        }

        /**
         * Tell the Adapter how many items are in your data.
         * Here, we can just return the size of mObjects!
         */
        @Override
        public int getCount() {
            return mObjects.size();
        }

        /**
         * Tell your the Adapter how to get an
         * item as the specified position in the list.
         */
        @Override
        public Object getItem(int position) {
            return mObjects.get(position);
        }

        /**
         * If you want the id of the item
         * to be something else, do something fancy here.
         * Rarely any need for that.
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * Here's where the real work takes place.
         * Here you tell the Adapter what View to show
         * for the rows in the ListView.
         *
         * ListViews try to recycle views, so the "convertView"
         * is provided for you to reuse, but you need to check if
         * it's null before trying to reuse it.
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyView view;
            if(convertView == null){
                view = new MyView(mContext);
            } else {
                view = (MyView) convertView;
            }
            /**Here's where we utilize the method we exposed
             in order to change the view based on the data
             So right before you return the View for the ListView
             to use, you just call that method.
             */
            view.configure(mObjects.get(position));

            return view;
        }
    }


    /**
     * Very simple layout to use in the ListView.
     * Just shows some text in the center of the View
     */
    public class MyView extends RelativeLayout {

        private TextView someText;

        public MyView(Context context) {
            super(context);

            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.addRule(CENTER_IN_PARENT);
            someText = new TextView(context);
            someText.setTextSize(20);
            someText.setTextColor(Color.BLACK);
            someText.setLayoutParams(params);
            addView(someText);
        }

        /**
         * Remember, your View is an regular object like any other.
         * You can add whatever methods you want and expose it to the world!
         * We have the method take a "MyObject" and do things to the View
         * based on it.
         */

        public void configure(MyObject object) {

            someText.setText(object.bar);
            //Check if the condition is true, if it is, set background of view to Blue.
            if(object.isSomeCondition()) {
                this.setBackgroundColor(Color.BLUE);
            } else {  //You probably need this else, because when views are recycled, it may just use Blue even when the condition isn't true.
                this.setBackgroundColor(Color.WHITE);
            }
        }
    }

    /**
     * This can be anything you want. Usually,
     * it's some object that makes sense according 
     * to your business logic/domain.
     *
     * I'm purposely keeping this class as simple
     * as possible to demonstrate the point. 
     */
    class MyObject {
        private String foo;
        private String bar;
        private boolean someCondition;


        public boolean isSomeCondition() {
            return someCondition;
        }


        MyObject(String foo, String bar) {
            this.foo = foo;
            this.bar = bar;
        }

        public void setSomeCondition(boolean b) {
            someCondition = b;
        }
    }

}
