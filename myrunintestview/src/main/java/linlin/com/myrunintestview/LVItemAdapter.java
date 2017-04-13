package linlin.com.myrunintestview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.List;

/**
 * Created by zhaopenglin on 2017/4/13.
 */

public class LVItemAdapter extends BaseAdapter {
    private Context context;
    private List<ItemBean> list;

    public LVItemAdapter(Context context, List<ItemBean> list) {
        this.context = context;
        this.list = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
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
        return convertView;
    }

    private final class ViewHolder {
        CheckBox checkBox;
        Button button;
    }
}
