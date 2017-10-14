package linlin.com.luckypansurfaceview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private LuckyPanView mLuckyPanView;
    private ImageView mStartBtn;
    private Button mDrawPanBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLuckyPanView = (LuckyPanView) findViewById(R.id.id_luckypan);
        mStartBtn =(ImageView) findViewById(R.id.id_start_btn);
        mDrawPanBtn = (Button) findViewById(R.id.drawpanbtn);

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mLuckyPanView.isStart()){
                    mStartBtn.setImageResource(R.drawable.node);
                    mLuckyPanView.luckyStart(1);
                }else {
                    if(!mLuckyPanView.isShouldEnd()){
                        mStartBtn.setImageResource(R.drawable.combat);
                        mLuckyPanView.luckyEnd();
                    }
                }
            }
        });
        mDrawPanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,DrawPan.class));
            }
        });
    }
}
