package br.java.app_android_cloud_firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import br.java.app_android_cloud_firestore.model.Relatorio;

public class RelatorioActivity extends AppCompatActivity {

    private static final String TAG = "AddRelatorioActivity";

    TextView edtTitulo;
    TextView edtConteudo;
    Button btAdd;

    private FirebaseFirestore firestoreDB;
    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

        edtTitulo = findViewById(R.id.edtTitulo);
        edtConteudo = findViewById(R.id.edtConteudo);
        btAdd = findViewById(R.id.btAdd);

        firestoreDB = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("AtualizarRelatorioId");

            edtTitulo.setText(bundle.getString("AtualizarRelatorioTitulo"));
            edtConteudo.setText(bundle.getString("AtualizarRelatorioConteudo"));
        }

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titulo = edtTitulo.getText().toString();
                String conteudo = edtConteudo.getText().toString();

                if (titulo.length() > 0) {
                    if (id.length() > 0) {
                        atualizarRelatorio(id, titulo, conteudo);
                    } else {
                        addRelatorio(titulo, conteudo);
                    }
                }

                finish();
            }
        });
    }

    private void atualizarRelatorio(String id, String titulo, String conteudo) {
        Map<String, Object> relatorio = (new Relatorio(id, titulo, conteudo)).toMap();
        
        firestoreDB.collection("relatorios")
                .document(id)
                .set(relatorio)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(TAG, "Relatório atualizado com sucesso!");
                        Toast.makeText(getApplicationContext(), "Relatório foi atualizado!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Erro ao adicionar relatório", e);
                Toast.makeText(getApplicationContext(), "Relatório não pôde ser atualizada!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addRelatorio(String titulo, String conteudo) {
        Map<String, Object> relatorio = new Relatorio(titulo, conteudo).toMap();

        firestoreDB.collection("relatorios")
                .add(relatorio)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.e(TAG, "DocumentSnapshot escrito com ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(), "O relatório foi adicionado", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Erro ao adicionar relatório", e);
                Toast.makeText(getApplicationContext(), "relatório não pôde ser adicionado!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}