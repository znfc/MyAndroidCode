package linlin.com.myrunintestview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.view.View.OnClickListener;

import java.util.List;
import java.util.Map;

/**
 * Created by zhaopenglin on 2017/4/13.
 */

public class LVItemAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener
,OnClickListener{
    private Context context;
    private List<ItemBean> list;
    Map<Integer, Boolean> isCheckMap ;
    private MCallback mCallback;

    public LVItemAdapter(Context context, List<ItemBean> list, Map<Integer, Boolean> isCheckMap) {
        this.context = context;
        this.list = list;
        this.isCheckMap = isCheckMap;
    }

    public void setmCallback(MCallback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context,R.layout.itemlayout,null);//这个最后一个参数传null不是传parent？
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.my_checkbox);
            viewHolder.button = (Button) convertView.findViewById(R.id.my_button);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.checkBox.setText(list.get(position).getItemName());
        viewHolder.button.setVisibility(list.get(position).isVisable()?View.VISIBLE:View.INVISIBLE);

        /**
         * 添加了如下代码后解决了缓冲造成的checkbox选中错乱的问题
         */
        viewHolder.checkBox.setTag(position);
        viewHolder.button.setTag(position);
        if(isCheckMap != null && isCheckMap.containsKey(position)){
            viewHolder.checkBox.setChecked(isCheckMap.get(position));
        }else{
            viewHolder.checkBox.setChecked(false);
        }

        viewHolder.checkBox.setOnCheckedChangeListener(this);
        viewHolder.button.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int secectedID = Integer.parseInt(buttonView.getTag().toString());
        if(isChecked) {
            //将选中的放入hashmap中
            isCheckMap.put(secectedID, isChecked);
            Toast.makeText(context,"选中了："+secectedID ,Toast.LENGTH_SHORT).show();
        } else {
            //取消选中的则剔除
            isCheckMap.remove(secectedID);
            isCheckMap.put(secectedID, false);
            Toast.makeText(context,"缓存触发了："+secectedID ,Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 自定义接口，用于回调按钮点击事件到Activity
     * @author Ivan Xu
     * 2014-11-26
     */
     public interface MCallback {
         void click(View v);
     }

    @Override
    public void onClick(View v) {
        mCallback.click(v);
    }

    private final class ViewHolder {
        CheckBox checkBox;
        Button button;
    }

    public void selectAll(){
        for (int i = 0; i < isCheckMap.size(); i++){
            isCheckMap.put(i,true);
        }
    }
    public void selectNone(){
        for (int i = 0; i < isCheckMap.size(); i++){
//            isCheckMap.remove(i);
            isCheckMap.put(i,false);
        }
    }
}
