package it.aletinti.aled;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ConsumerIrManager IR;
    Patterns patterns;
    MediaRecorder micrec = null;
    int miclvl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SeekBar lvlSeekBar = findViewById(R.id.lvlSeekBar);
        final TextView lvlTextView = findViewById(R.id.lvlTextView);
        IR = (ConsumerIrManager) getSystemService(Context.CONSUMER_IR_SERVICE);
        patterns = new Patterns();

        try{
            micrec = new MediaRecorder();
            micrec.setAudioSource(MediaRecorder.AudioSource.MIC);
            micrec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            micrec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            micrec.setOutputFile("/dev/null");
            micrec.prepare();
            micrec.start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true) {
                        miclvl /*+*/= micrec.getMaxAmplitude();
                        try {
                            Thread.sleep(0,001);
                        } catch (Exception e) {
                        }
                        miclvl /= lvlSeekBar.getProgress();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lvlTextView.setText(Integer.toString(miclvl));
                            }
                        });
                        if(miclvl < 300*1){ IR.transmit(38400, patterns.Green1); }
                        if(miclvl > 300*2 && miclvl < 300*3){IR.transmit(38400, patterns.Green2);}
                        if(miclvl > 300*3 && miclvl < 300*4){IR.transmit(38400, patterns.Green3);}
                        if(miclvl > 300*4 && miclvl < 300*5){IR.transmit(38400, patterns.Green4);}
                        if(miclvl > 300*5 && miclvl < 300*6){IR.transmit(38400, patterns.Green5);}
                        if(miclvl > 300*6 && miclvl < 300*7){IR.transmit(38400, patterns.Blue1);}
                        if(miclvl > 300*7 && miclvl < 300*8){IR.transmit(38400, patterns.Blue2);}
                        if(miclvl > 300*8 && miclvl < 300*9){IR.transmit(38400, patterns.Blue3);}
                        if(miclvl > 300*9 && miclvl < 300*10){IR.transmit(38400, patterns.Blue4);}
                        if(miclvl > 300*10 && miclvl < 300*11){IR.transmit(38400, patterns.Red5);}
                        if(miclvl > 300*11 && miclvl < 300*12){IR.transmit(38400, patterns.Red4);}
                        if(miclvl > 300*12 && miclvl < 300*13){IR.transmit(38400, patterns.Red3);}
                        if(miclvl > 300*13 && miclvl < 300*14){IR.transmit(38400, patterns.Red2);}
                        if(miclvl > 2250){IR.transmit(38400, patterns.Red1);}
                    }
                }
            }).start();
        } catch(Exception e){}
    }
}
