package br.com.matteroftime.ui.play;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import br.com.matteroftime.R;
import br.com.matteroftime.core.listeners.OnMusicSelectedListener;
import br.com.matteroftime.models.Musica;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

/**
 * Created by RedBlood on 30/03/2017.
 */

public class PlayAdapter extends RecyclerView.Adapter<PlayAdapter.ViewHolder>{

    private List<Musica> musicas;
    private Context context;
    private boolean shouldHighlightSelectedRow = false;
    private boolean selecao;
    private int selectedPosition = 0;
    private final OnMusicSelectedListener listener;

    public PlayAdapter(List<Musica> musicas, Context context, OnMusicSelectedListener listener) {
        this.musicas = musicas;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_musicas_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Musica musica = musicas.get(position);
        if (musicas != null  && musicas.get(position).getCompassos().size() > 0){
            try {
                holder.numeroMusica.setText(String.valueOf(musica.getOrdem() + 1));
                holder.nomeMusica.setText(musica.getNome());
                holder.bpm.setText(String.valueOf(musica.getCompassos().get(0).getBpm()));
                holder.tempos.setText(String.valueOf(musica.getCompassos().get(0).getTempos()));
                holder.nota.setText(String.valueOf(musica.getCompassos().get(0).getNota()));
                holder.totalCompassos.setText(String.valueOf(musica.getCompassos().size()));
                if (shouldHighlightSelectedRow){
                    if (selectedPosition == position){
                        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
                    } else {
                        holder.itemView.setBackgroundColor(Color.TRANSPARENT);
                    }
                } else {
                    holder.itemView.setBackgroundColor(Color.TRANSPARENT);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        } else if (musicas != null && musicas.get(position).getCompassos().size() == 0){
            try {
                holder.numeroMusica.setText(String.valueOf(musica.getOrdem() + 1));
                holder.nomeMusica.setText(musica.getNome());
                holder.bpm.setText("0");
                holder.tempos.setText("0");
                holder.nota.setText("0");
                holder.totalCompassos.setText("0");
                if (shouldHighlightSelectedRow){
                    if (selectedPosition == position){
                        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
                    } else {
                        holder.itemView.setBackgroundColor(Color.TRANSPARENT);
                    }
                } else {
                    holder.itemView.setBackgroundColor(Color.TRANSPARENT);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        if (musicas != null){
            return musicas.size();
        } else {
            return 0;
        }
    }

    public void replaceData(List<Musica> musicas) {
        this.musicas = musicas;
        shouldHighlightSelectedRow = false;
        notifyDataSetChanged();
    }

    public void checaSelecao(boolean selecao) {
        this.selecao = selecao;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.txt_numero_musica) TextView numeroMusica;
        @BindView(R.id.txt_nome_musica) TextView nomeMusica;
        @BindView(R.id.txt_BPM) TextView bpm;
        @BindView(R.id.txt_tempos) TextView tempos;
        @BindView(R.id.txt_nota) TextView nota;
        @BindView(R.id.txtTotalCompassos) TextView totalCompassos;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

            //nomeMusica.setOnClickListener(this);
            //itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //shouldHighlightSelectedRow = true;
            selectedPosition = getLayoutPosition();
            Musica musicaSelecionada = musicas.get(getLayoutPosition());
            listener.onSelectMusic(musicaSelecionada);
            notifyDataSetChanged();
            if (selecao == false){
                shouldHighlightSelectedRow = false;
            } else {
                shouldHighlightSelectedRow = true;
            }

        }

    }
}
