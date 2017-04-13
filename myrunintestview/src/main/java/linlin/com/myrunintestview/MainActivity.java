package linlin.com.myrunintestview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ItemBean> list;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDate();
        LVItemAdapter itemAdapter= new LVItemAdapter(this,list);
        listView = (ListView) findViewById(R.id.my_Listview);
        listView.setAdapter(itemAdapter);

    }

    /**
     * 初始化数据
     */
    private void initDate() {
        list = new ArrayList<ItemBean>();
        for (int i = 0 ;i <20;i++){
            list.add(new ItemBean("reboot",true));
        }
    }

    /**
     * 目前存在的问题checkbox选中一个时候上下滑动listview时候回造成下边的也被选中，这个是由于用了listview的缓存机制
     * 造成的，目前只发现了这个问题。20170413
     */
}
