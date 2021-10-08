package br.java.app_android_cloud_firestore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import br.java.app_android_cloud_firestore.R;
import br.java.app_android_cloud_firestore.RelatorioActivity;
import br.java.app_android_cloud_firestore.model.Relatorio;

public class RelatorioRecyclerViewAdapter extends RecyclerView.Adapter<RelatorioRecyclerViewAdapter.ViewHolder> {

    private List<Relatorio> relatorioLista;
    private Context context;
    private FirebaseFirestore firestoreDB;

    public RelatorioRecyclerViewAdapter(List<Relatorio> relatorioLista, Context context, FirebaseFirestore firestoreDB) {
        this.relatorioLista = relatorioLista;
        this.context = context;
        this.firestoreDB = firestoreDB;
    }

    @Override
    public RelatorioRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_relatorio, parent, false);

        return new RelatorioRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RelatorioRecyclerViewAdapter.ViewHolder holder, int position) {
        final int itemPosicao = position;
        final Relatorio relatorio = relatorioLista.get(position);

        holder.titulo.setText(relatorio.getTitulo());
        holder.conteudo.setText(relatorio.getConteudo());

        // Editar Relatório
        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarRelatorio(relatorio);
            }
        });

        // Deletar Relatório
        holder.deletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletarRelatorio(relatorio.getId(), itemPosicao);
            }
        });
    }
    // buscar numero de itens
    @Override
    public int getItemCount() {
        return relatorioLista.size();
    }

    // classe ViewHOlder
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titulo;
        TextView conteudo;
        ImageView editar;
        ImageView deletar;

        ViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tvTitulo);
            conteudo = itemView.findViewById(R.id.tvConteudo);

            editar = itemView.findViewById(R.id.ivEdit);
            deletar = itemView.findViewById(R.id.ivDelete);
        }
    }

    // Edição de Relatório
    public  void atualizarRelatorio(Relatorio relatorio) {
        Intent intent = new Intent(context, RelatorioActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("AtualizarRelatorioId", relatorio.getId());
        intent.putExtra("AtualizarRelatorioTitulo", relatorio.getTitulo());
        intent.putExtra("AtualizarRelatorioConteudo", relatorio.getConteudo());
        context.startActivity(intent);
    }
    
    // Deletar Relatório
    public void deletarRelatorio(String id, final int position) {
        firestoreDB.collection("relatorios")
                .document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        relatorioLista.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, relatorioLista.size());
                        Toast.makeText(context, "O relatório foi excluída!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
