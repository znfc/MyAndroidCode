package linlin.com.aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import linlin.com.service.IMyAidlInterface;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText num1,num2;
    private TextView result,text;
    private Button measure;
    private IMyAidlInterface iMyAidl;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMyAidl = IMyAidlInterface.Stub.asInterface(service);
            Log.i("TAG", "bind service successful");
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            iMyAidl = null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        num1 = (EditText) findViewById(R.id.num1);
        num2 = (EditText) findViewById(R.id.num2);
        measure = (Button) findViewById(R.id.measure);
        result = (TextView) findViewById(R.id.result);
        text = (TextView) findViewById(R.id.text);
        measure.setOnClickListener(this);
        bindService();
    }


    @Override
    public void onClick(View v) {
        try {
            int i = iMyAidl.add(Integer.parseInt(num1.getText().toString()),Integer.parseInt(num2.getText().toString()));
            result.setText("" + i);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("linlin.com.aidlservice","linlin.com.aidlservice.IRemoteService"));
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}
