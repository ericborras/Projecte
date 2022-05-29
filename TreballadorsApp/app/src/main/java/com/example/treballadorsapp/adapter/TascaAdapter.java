package com.example.treballadorsapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.treballadorsapp.R;
import com.example.treballadorsapp.TasquesActivity;

import org.milaifontanals.model.Tasca;

import java.text.SimpleDateFormat;
import java.util.List;

public class TascaAdapter extends RecyclerView.Adapter<TascaAdapter.ViewHolder>{

    private List<Tasca> mTasca;
    private int idxTasca=0;
    private TasquesActivity mTasquesActivity;

    public TascaAdapter(List<Tasca> tasques, TasquesActivity tasquesActivity){
        mTasca = tasques;
        mTasquesActivity = tasquesActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.tasca;
        View filaView = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new ViewHolder(filaView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tasca actual = mTasca.get(position);

        holder.txvIdTasca.setText(actual.getId()+"");
        holder.txvNomTasca.setText(actual.getNom());
        holder.txvDescripcioTasca.setText(actual.getDescripcio());
        if(actual.getResponsable()!=null) {

            if (actual.getResponsable().getCognom2() != null) {
                holder.txvResponsableTasca.setText(actual.getResponsable().getNom() + " " + actual.getResponsable().getCognom1() + " " + actual.getResponsable().getCognom2());
            } else {
                holder.txvResponsableTasca.setText(actual.getResponsable().getNom() + " " + actual.getResponsable().getCognom1());
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        holder.txvDataLimitTasca.setText(sdf.format(actual.getDataCreacio()));

        try{
            holder.txvEstatTasca.setText(actual.getEstat().toString());
        }catch(Exception ex){

        }






    }

    @Override
    public int getItemCount() {
        return mTasca.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txvIdTasca;
        TextView txvNomTasca;
        TextView txvDescripcioTasca;
        TextView txvResponsableTasca;
        TextView txvDataLimitTasca;
        TextView txvEstatTasca;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txvIdTasca = itemView.findViewById(R.id.txvIdTasca);
            txvNomTasca = itemView.findViewById(R.id.txvNomTasca);
            txvDescripcioTasca = itemView.findViewById(R.id.txvDescripcioTasca);
            txvResponsableTasca = itemView.findViewById(R.id.txvResponsableTasca);
            txvDataLimitTasca = itemView.findViewById(R.id.txvDataLimitTasca);
            txvEstatTasca = itemView.findViewById(R.id.txvEstatTasca);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mTasquesActivity!=null){

                        int anticIdxSeleccionat = idxTasca;
                        idxTasca = getAdapterPosition();
                        notifyItemChanged(anticIdxSeleccionat);
                        notifyItemChanged(idxTasca);

                        mTasquesActivity.onTascaSelected(mTasca.get(idxTasca));
                    }
                }
            });
        }
    }
}
