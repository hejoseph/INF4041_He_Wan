package org.esiea.he_wan.mybasicapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

public class BieresAdapter extends RecyclerView.Adapter<BieresAdapter.BiereHolder>{
    private JSONArray bieres;
    public BieresAdapter(JSONArray b) {
        this.bieres = b;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public BiereHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BiereHolder holder, int position) {

    }

    public class BiereHolder extends RecyclerView.ViewHolder{
        public BiereHolder(View itemView) {
            super(itemView);
        }
    }
}
