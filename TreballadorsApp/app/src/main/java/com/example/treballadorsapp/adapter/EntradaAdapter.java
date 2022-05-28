package com.example.treballadorsapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.treballadorsapp.EntradesActivity;
import com.example.treballadorsapp.R;

import org.milaifontanals.model.Entrada;

import java.text.SimpleDateFormat;
import java.util.List;

public class EntradaAdapter extends RecyclerView.Adapter<EntradaAdapter.ViewHolder>{

    private List<Entrada> mEntrades;
    private EntradesActivity mEntradesActivity;

    public EntradaAdapter(List<Entrada> entrades, EntradesActivity entradesActivity){
        mEntrades = entrades;
        mEntradesActivity = entradesActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.entrada;
        View filaView = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new ViewHolder(filaView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Entrada actual = mEntrades.get(position);

        holder.txvIdEntrada.setText(actual.getNumero()+"");
        holder.txvEntrada.setText(actual.getEntrada());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        holder.txvDataEntrada.setText(sdf.format(actual.getDataEntrada()));

        if(actual.getNovaAssignacio()!=null){

            if(actual.getNovaAssignacio().getCognom2()!=null){
                holder.txvNovaAssignacioEntrada.setText(actual.getNovaAssignacio().getNom()+" "+actual.getNovaAssignacio().getCognom1()+" "+actual.getNovaAssignacio().getCognom2());
            }else{
                holder.txvNovaAssignacioEntrada.setText(actual.getNovaAssignacio().getNom()+" "+actual.getNovaAssignacio().getCognom1());
            }
        }

        if(actual.getNouEstat()!=null){
            holder.txvNouEstatEntrada.setText(actual.getNouEstat().toString());
        }


    }

    @Override
    public int getItemCount() {
        return mEntrades.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txvIdEntrada;
        TextView txvEntrada;
        TextView txvDataEntrada;
        TextView txvNovaAssignacioEntrada;
        TextView txvEscriptorEntrada;
        TextView txvNouEstatEntrada;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            txvIdEntrada = itemView.findViewById(R.id.txvIdEntrada);
            txvEntrada = itemView.findViewById(R.id.txvEntrada);
            txvDataEntrada = itemView.findViewById(R.id.txvDataEntrada);
            txvNovaAssignacioEntrada = itemView.findViewById(R.id.txvNovaAssignacioEntrada);
            txvEscriptorEntrada = itemView.findViewById(R.id.txvEscriptorEntrada);
            txvNouEstatEntrada = itemView.findViewById(R.id.txvNouEstatEntrada);

        }
    }
}
