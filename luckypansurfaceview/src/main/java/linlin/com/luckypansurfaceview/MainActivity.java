package linlin.com.luckypansurfaceview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private LuckyPanView mLuckyPanView;
    private ImageView mStartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surfacev);
//        setContentView(R.layout.activity_main);
//
//        mLuckyPanView = (LuckyPanView) findViewById(R.id.id_luckypan);
//        mStartBtn =(ImageView) findViewById(R.id.id_start_btn);
//
//        mStartBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!mLuckyPanView.isStart()){
//                    mStartBtn.setImageResource(R.drawable.node);
//                    mLuckyPanView.luckyStart(1);
//                }else {
//                    if(!mLuckyPanView.isShouldEnd()){
//                        mStartBtn.setImageResource(R.drawable.combat);
//                        mLuckyPanView.luckyEnd();
//                    }
//                }
//            }
//        });
    }
}
