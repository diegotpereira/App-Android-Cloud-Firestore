package br.java.app_android_cloud_firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import br.java.app_android_cloud_firestore.adapter.RelatorioRecyclerViewAdapter;
import br.java.app_android_cloud_firestore.model.Relatorio;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // instanciando o adaptador
    private RelatorioRecyclerViewAdapter mAdapter;

    // definindo firebase
    private FirebaseFirestore firestoreDB;
    private ListenerRegistration firestoreListener;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rvRelatorioLista);
        firestoreDB = FirebaseFirestore.getInstance();

        carregarRelarioLista();

        firestoreListener = firestoreDB.collection("relatorios")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Falha ao ouvir!", e);
                            return;
                        }

                        List<Relatorio> relatorioLista = new ArrayList<>();

                        for(DocumentSnapshot doc : documentSnapshots) {
                            Relatorio relatorio = doc.toObject(Relatorio.class);
                            relatorio.setId(doc.getId());
                            relatorioLista.add(relatorio);
                        }
                        mAdapter = new RelatorioRecyclerViewAdapter(relatorioLista, getApplicationContext(), firestoreDB);
                        recyclerView.setAdapter(mAdapter);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firestoreListener.remove();
    }

    private void carregarRelarioLista() {
        firestoreDB.collection("relatorios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Relatorio> relatorioLista = new ArrayList<>();

                            for (DocumentSnapshot doc : task.getResult()) {
                                Relatorio relatorio = doc.toObject(Relatorio.class);
                                relatorio.setId(doc.getId());
                                relatorioLista.add(relatorio);
                            }

                            mAdapter = new RelatorioRecyclerViewAdapter(relatorioLista, getApplicationContext(), firestoreDB);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);
                        } else {
                            Log.d(TAG, "Erro ao obter documentos: ", task.getException());
                        }
                    }
                });
    }
//    @Override
    public boolean onCreateOptionMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

//    @Override
    public boolean onOptionItemSelected(MenuItem item) {
        if (item != null) {
            if (item.getItemId() == R.id.addRelatorio) {
                Intent intent = new Intent(this, RelatorioActivity.class);
                startActivity(intent);
            }
        }
        return  super.onOptionsItemSelected(item);
    }

}