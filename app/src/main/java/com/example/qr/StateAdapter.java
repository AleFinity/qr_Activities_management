package com.example.qr;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.ViewHolder>{

        private LayoutInflater inflater;
        private LinkedHashMap<String,String> characteristic;
        private Iterator iterator;
        private int characteristicSize;
        
        StateAdapter(Context context, LinkedHashMap<String,String> characteristic) {
            this.characteristic = characteristic;
            this.inflater = LayoutInflater.from(context);
            this.iterator = this.characteristic.keySet().iterator();
            this.characteristicSize = characteristic.size();
            Iterator iter = characteristic.keySet().iterator();
            while(iter.hasNext()) {
                if(characteristic.get(iter.next())==null)
                    this.characteristicSize--;
            }
        }
        @Override
        public StateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.list_info, parent, false);
            return new ViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(StateAdapter.ViewHolder holder, int position) {
            if(iterator.hasNext()){
                Object key  = iterator.next();
                while(characteristic.get(key)==null || characteristic.get(key)=="null"){
                    if(iterator.hasNext())
                        key  = iterator.next();
                    else
                        return;
                }
                holder.nameView.setText(key.toString()+"");
                holder.infoView.setText(characteristic.get(key));
            }
        }
        
        @Override
        public int getItemCount() {
            return characteristicSize;
        }
        
        public static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView nameView, infoView;
            ViewHolder(View view){
                super(view);
                nameView = (TextView) view.findViewById(R.id.title);
                infoView = (TextView) view.findViewById(R.id.information);
            }
        }
}