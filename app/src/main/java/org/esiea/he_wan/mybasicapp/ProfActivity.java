package org.esiea.he_wan.mybasicapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class ProfActivity extends AppCompatActivity {

    private static final String TAG = "org.esiea.ProfActivity";

    private RecyclerView rv;

    public RecyclerView getRV(){
        return rv;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof);

        rv = (RecyclerView) findViewById(R.id.rv_biere);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
        else{
            rv.setLayoutManager(new GridLayoutManager(this.getApplicationContext(), 2));
        }
        BieresAdapter bieresAdapter = new BieresAdapter(getBiersFromFile());

        rv.setAdapter(bieresAdapter);
        IntentFilter intentFilter = new IntentFilter(BIERS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BierUpdate(), intentFilter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    public static final String BIERS_UPDATE = "com.esiea.biers_update";
    public class BierUpdate extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("hejoseph allo", getIntent().getAction());
//            Toast.makeText(BierUpdate.this, "inside update biere class", Toast.LENGTH_SHORT).show();
            ((BieresAdapter)getRV().getAdapter()).setNewBiere();
        }
    }

    private class BieresAdapter extends RecyclerView.Adapter<BieresAdapter.BiereHolder>{
        private JSONArray bieres;
        private final int[] image_ids = {R.drawable.desp, R.drawable.hein, R.drawable.guiness};
        public BieresAdapter(JSONArray b) {
            this.bieres = b;
        }

        public void setNewBiere(){
            bieres = getBiersFromFile();
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return bieres.length();
        }

        @Override
        public BiereHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_biere_element, parent, false);
            BiereHolder biereHolder = new BiereHolder(itemLayoutView);
            Log.d(TAG, "onCreateViewHolder");
            return biereHolder;
        }

        @Override
        public void onBindViewHolder(BiereHolder holder, int position) {
            try {
                holder.name.setText(bieres.getJSONObject(position).getString("name"));
                int nb = position;
                nb = nb%3;
                holder.image.setImageResource(image_ids[nb]);

                Log.d(TAG, "onBindViewHolder");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        class BiereHolder extends RecyclerView.ViewHolder{
            public TextView name;
            public ImageView image;

            public BiereHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.rv_biere_element_name);
                image = (ImageView) itemView.findViewById(R.id.biere_image);
            }
        }
    }

    public JSONArray getBiersFromFile(){
        try{
            InputStream is = new FileInputStream(getCacheDir() + "/"  + "bieres.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, "UTF-8"));
        }catch(IOException e){
            e.printStackTrace();
            return new JSONArray();
        }catch(JSONException e){
            e.printStackTrace();
            return new JSONArray();
        }
    }

}
