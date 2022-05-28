package com.example.treballadorsapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.treballadorsapp.R;
import com.example.treballadorsapp.TasquesActivity;

import org.milaifontanals.model.Projecte;

import java.util.List;

public class ProjecteAdapter extends RecyclerView.Adapter<ProjecteAdapter.ViewHolder>{

    private List<Projecte> mProjecte;

    private int idxProjecte=-1;
    private TasquesActivity mTasquesActivity;

    public ProjecteAdapter(List<Projecte> projectes, TasquesActivity tasquesActivity){
        mProjecte = projectes;
        mTasquesActivity = tasquesActivity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.projecte;
        View filaView = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new ViewHolder(filaView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Projecte actual = mProjecte.get(position);

        holder.txvIdProjecte.setText(actual.getId()+"");
        holder.txvNomProjecte.setText(actual.getNom());
        holder.txvDescripcioProjecte.setText(actual.getDescripcio());
        if(actual.getCapProjecte().getCognom2()!=null){
            holder.txvCapProjecte.setText(actual.getCapProjecte().getNom()+" "+actual.getCapProjecte().getCognom1()+" "+actual.getCapProjecte().getCognom2());
        }else{
            holder.txvCapProjecte.setText(actual.getCapProjecte().getNom()+" "+actual.getCapProjecte().getCognom1());
        }


        boolean isSeleccionat = idxProjecte==position;
        if(isSeleccionat){
            holder.linProjecte.setBackgroundResource(R.drawable.border_selected);
        }else{
            holder.linProjecte.setBackgroundResource(R.drawable.border_no_selected);
        }



    }

    @Override
    public int getItemCount() {
        return mProjecte.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txvIdProjecte;
        TextView txvNomProjecte;
        TextView txvDescripcioProjecte;
        TextView txvCapProjecte;

        LinearLayout linProjecte;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txvIdProjecte = itemView.findViewById(R.id.txvIdProjecte);
            txvNomProjecte = itemView.findViewById(R.id.txvNomProjecte);
            txvDescripcioProjecte = itemView.findViewById(R.id.txvDescripcioProjecte);
            txvCapProjecte = itemView.findViewById(R.id.txvCapProjecte);
            linProjecte = itemView.findViewById(R.id.linProjecte);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mTasquesActivity!=null){
                        int anticIdxSeleccionat = idxProjecte;
                        idxProjecte = getAdapterPosition();
                        notifyItemChanged(anticIdxSeleccionat);
                        notifyItemChanged(idxProjecte);

                        mTasquesActivity.onProjecteSelected(mProjecte.get(idxProjecte));
                    }
                }
            });
        }
    }
}
