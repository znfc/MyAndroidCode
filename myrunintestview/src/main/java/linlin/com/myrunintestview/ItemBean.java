package linlin.com.myrunintestview;

/**
 * Created by zhaopenglin on 2017/4/13.
 */

public class ItemBean {
    String itemName;
    boolean visable;

    public ItemBean(String itemName, boolean visable) {
        this.itemName = itemName;
        this.visable = visable;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setVisable(boolean visable) {
        this.visable = visable;
    }

    public String getItemName() {
        return itemName;
    }

    public boolean isVisable() {
        return visable;
    }
}
