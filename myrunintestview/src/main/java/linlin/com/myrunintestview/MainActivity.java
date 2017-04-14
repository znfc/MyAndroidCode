package linlin.com.myrunintestview;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements LVItemAdapter.MCallback,
        View.OnClickListener{

    private List<ItemBean> list;

    Map<Integer, Boolean> isCheckMap ;
    LVItemAdapter itemAdapter;
    private ListView listView;

    private Button selectAllBtn;
    private Button selectNoneBtn;
    private Button startTextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectAllBtn = (Button) findViewById(R.id.selectall_btn);
        selectNoneBtn = (Button) findViewById(R.id.selectnone_btn);
        startTextBtn = (Button) findViewById(R.id.startBtn);
        initDate();
        itemAdapter= new LVItemAdapter(this,list,isCheckMap);
        itemAdapter.setmCallback(this);
        listView = (ListView) findViewById(R.id.my_Listview);
        listView.setAdapter(itemAdapter);

        selectAllBtn.setOnClickListener(this);
        selectNoneBtn.setOnClickListener(this);
        startTextBtn.setOnClickListener(this);

    }

    /**
     * 初始化数据
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initDate() {
        isCheckMap =  new HashMap<Integer, Boolean>();
        list = new ArrayList<ItemBean>();
//        for (int i = 0 ;i <20;i++){
//            list.add(new ItemBean("reboot",true));
////            isCheckMap.put(i,false);
//        }
        readXMLToDisplay(this);
    }

    @Override
    public void click(View v) {
        Toast.makeText(
                MainActivity.this,
                "listview的内部的按钮被点击了！，位置是-->" + v.getTag()
                , Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private  void readXMLToDisplay(Context context) {
        try (XmlResourceParser xrp = context.getResources().getXml(R.xml.runin_test_list)) {
            while (xrp.next() != XmlResourceParser.START_TAG) {
                continue;
            }
            xrp.next();
            while (xrp.getEventType() != XmlResourceParser.END_TAG) {
                while (xrp.getEventType() != XmlResourceParser.START_TAG) {
                    xrp.next();
                }
                if (xrp.getName().equals("boolean")) {
                    String text = xrp.getAttributeValue(0);
                    String isshow = xrp.getAttributeValue(1);

                    list.add(new ItemBean(text,Boolean.parseBoolean(isshow)));
                    xrp.next();
                }
                while (xrp.getEventType() != XmlResourceParser.END_TAG) {
                    xrp.next();
                }
                xrp.next();
            }
        } catch (XmlPullParserException xppe) {
        } catch (java.io.IOException ioe) {
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.selectall_btn :
                itemAdapter.selectAll();
                itemAdapter.notifyDataSetChanged();
                break;
            case R.id.selectnone_btn :
                itemAdapter.selectNone();
                itemAdapter.notifyDataSetChanged();
                break;
            case R.id.startBtn :
                startTest();
                break;
        }
    }

    private void startTest() {
        Log.i("zhao11","isCheckMap.size():"+isCheckMap.size());
        for (int i = 0; i < isCheckMap.size(); i++ ){
            Log.i("zhao11",i+"是："+isCheckMap.get(i));
            if (isCheckMap.get(i) != null && isCheckMap.get(i)){
                Toast.makeText(this,i+"被选中了！",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 目前存在的问题checkbox选中一个时候上下滑动listview时候回造成下边的也被选中，这个是由于用了listview的缓存机制
     * 造成的，目前只发现了这个问题。20170413
     * 已经在LVItemAdapter里解决了.20170413
     */
}
