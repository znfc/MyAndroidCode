package linlin.com.myfragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jay on 2015/9/6 0006.
 */
public class MyAdapter extends BaseAdapter{

    private List<Data> mData;
    private Context mContext;

    public MyAdapter(List<Data> mData, Context mContext) {
//        Log.i(Tools.ZHAO11,this.getClass().getSimpleName()+" MyAdapter "+mData);
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        Log.i(Tools.ZHAO11,this.getClass().getSimpleName()+" getCount");
        if (mData == null) return 0;
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
            viewHolder.txt_item_title = (TextView) convertView.findViewById(R.id.txt_item_title);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txt_item_title.setText(mData.get(position).getNew_title());
        return convertView;
    }

    private class ViewHolder{
        TextView txt_item_title;
    }

}
