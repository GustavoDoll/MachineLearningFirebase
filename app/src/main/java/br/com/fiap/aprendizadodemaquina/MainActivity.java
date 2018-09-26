package br.com.fiap.aprendizadodemaquina;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

public class MainActivity extends AppCompatActivity {

    private Button tirarFoto;
    private Button extrairTexto;
    private ImageView imageView;
    private TextView texto;
    private Bitmap foto;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tirarFoto = findViewById(R.id.tirarFoto);
        extrairTexto = findViewById(R.id.extrairTexto);
        imageView = findViewById(R.id.imageView);
        texto = findViewById(R.id.texto);
        tirarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
            }
            }
        });

        extrairTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            processarImagem();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            texto.setText("");
            foto = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(foto);
        }
    }

    public void processarImagem() {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(foto);
        FirebaseVisionTextDetector detector =  FirebaseVision.getInstance().getVisionTextDetector();

        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                if (firebaseVisionText.getBlocks().size() == 0){
                    Toast.makeText(MainActivity.this, "Não há textos nessa imagem", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (FirebaseVisionText.Block bloco :firebaseVisionText.getBlocks()){
                    bloco.getText();
                    String textoExtraido = bloco.getText();
                    String textoAtual = texto.getText().toString();
                    texto.setText(textoAtual + "-" + textoExtraido);
                }
            }
        });
    }

    public void processarTexto() {

    }
}
