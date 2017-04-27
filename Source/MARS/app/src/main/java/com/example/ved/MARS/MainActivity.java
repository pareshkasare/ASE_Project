package com.example.ved.MARS;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    ListView listView;
    private int largeNum=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<String> medList = new ArrayList<String>();
        ArrayList<String> displayList = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.list_view);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);



        String[] files = getApplicationContext().fileList();
        System.out.println("File list: ");
        int i=0;
        while(files.length > 0 && i< files.length) {
            System.out.println(files[i]);
            i++;
        }

        //Get all file names and create a list.
        for (String medfile : files) {
            System.out.println(medfile);
            if(medfile.startsWith("Med")) {
                medList.add(medfile);
                int num=Integer.parseInt(medfile.replaceAll("[^-?0-9]+", ""));
                if (num > largeNum) {
                    largeNum = num;
                }
                File med = new File(getApplicationContext().getFilesDir(),medfile);
                try {
                    BufferedReader br = new BufferedReader(new FileReader(med));
                    String line;

                    while ((line = br.readLine()) != null) {
                        String[] pieces = line.split(":");
                        displayList.add(pieces[0]);
                    }
                    br.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent redirect = new Intent(MainActivity.this, AddActivity.class);
                redirect.putExtra("lastfilenum",largeNum);
                startActivity(redirect);
                overridePendingTransition(R.anim.anim1, R.anim.anim2);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.customlist,R.id.Itemname,
                                                        displayList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                int itemPosition     = position;

                String  itemValue    = (String) listView.getItemAtPosition(position);
                Intent redirect = new Intent(MainActivity.this, UpdateActivity.class);
                redirect.putExtra("Extra_filename",medList.get(position));
                startActivity(redirect);
                overridePendingTransition(R.anim.anim1, R.anim.anim2);
            }

        });
    }
    private void create_new_file(String data, File user_file){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(user_file, true));
            bw.write(data);
            bw.flush();
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        new AlertDialog.Builder(this)
                .setTitle("Exiting application")
                .setMessage("Are you sure you want to exit? You will have to login again!!")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent redirect = new Intent(MainActivity.this, LoginActivity.class);
                        redirect.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(redirect);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_logout:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent redirect = new Intent(MainActivity.this, LoginActivity.class);
                redirect.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(redirect);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
