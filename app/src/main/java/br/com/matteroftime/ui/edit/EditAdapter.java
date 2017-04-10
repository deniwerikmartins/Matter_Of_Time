package br.com.matteroftime.ui.edit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.matteroftime.R;
import br.com.matteroftime.core.listeners.OnMusicSelectedListener;
import br.com.matteroftime.models.Musica;
import butterknife.ButterKnife;

/**
 * Created by RedBlood on 30/03/2017.
 */

public class EditAdapter extends RecyclerView.Adapter<EditAdapter.ViewHolder>{

    private List<Musica> musicas;
    private Context context;
    private final OnMusicSelectedListener listener;

    public EditAdapter(List<Musica> musicas, Context context, OnMusicSelectedListener listener) {
        this.musicas = musicas;
        this.context = context;
        this.listener = listener;
    }


    public void replaceData(List<Musica> musicas) {
        this.musicas = musicas;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //criar a row
        /*View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;*/
        return null; //
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //bindar view
    }

    @Override
    public int getItemCount() {
        if (musicas != null){
            return  musicas.size();
        } else {
            return  0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{


        //bindar os itens da view


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Musica musicaSelecionada = musicas.get(getLayoutPosition());
            listener.onSelectMusic(musicaSelecionada);

        }

        @Override
        public boolean onLongClick(View v) {
            Musica musicaSelecionada = musicas.get(getLayoutPosition());
            listener.onLongClickMusic(musicaSelecionada);
            return true;
        }
    }
}
