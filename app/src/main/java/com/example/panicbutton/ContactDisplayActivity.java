package com.example.panicbutton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactDisplayActivity extends AppCompatActivity {

//    RecyclerView myRecycleView;
    ListView myRecycleView;
    List<String> Name = new ArrayList<>();
    List<String> Number = new ArrayList<>();
    SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_display);

        getSupportActionBar().hide();
        myRecycleView = findViewById(R.id.recycler_view);

        List<Map<String, String>> contactData = new ArrayList<>();
//        for(int i=0; i<5; i++)
//        {
//            Map<String, String> Info = new HashMap<>();
//            Info.put("name", "Contact " + i);
//            Info.put("number", "1234-" + i);
//            contactData.add(Info);
//        }

        ParseQuery<ParseObject> contacts = new ParseQuery<ParseObject>("Contacts");
        contacts.whereEqualTo("User", ParseUser.getCurrentUser().getUsername());

        contacts.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null)
                {
                    for(ParseObject object : objects)
                    {
                        Map<String, String> Info = new HashMap<>();
                        Info.put("name", object.get("Name").toString());
                        Info.put("number", object.get("Number").toString());
                        contactData.add(Info);
                    }

                    simpleAdapter = new SimpleAdapter(ContactDisplayActivity.this, contactData, android.R.layout.simple_list_item_2, new String[] {"name","number"},new int[] {android.R.id.text1, android.R.id.text2}){
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);

                            TextView one = view.findViewById(android.R.id.text1);
                            TextView two = view.findViewById(android.R.id.text2);

                            one.setTypeface(null, Typeface.BOLD_ITALIC);

                            one.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                            two.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

                            return view;
                        }
                    };

                    myRecycleView.setAdapter(simpleAdapter);

                }
            }
        });
    }
}