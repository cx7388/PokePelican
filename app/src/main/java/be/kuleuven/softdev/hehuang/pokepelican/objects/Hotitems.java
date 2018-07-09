package be.kuleuven.softdev.hehuang.pokepelican.objects;

/**
 * Created by Administrator on 18/12/2016.
 */

public class Hotitems {

    private int pic_resoureces;
    private String item_name;
    private String item_rating;

    public Hotitems(int pic_resoureces, String item_name, String item_rating) {
        this.pic_resoureces = pic_resoureces;
        this.item_name = item_name;
        this.item_rating = item_rating;
    }

    public int getPic_resoureces() {
        return pic_resoureces;
    }

    public void setPic_resoureces(int pic_resoureces) {
        this.pic_resoureces = pic_resoureces;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_rating() {
        return item_rating;
    }

    public void setItem_rating(String item_rating) {
        this.item_rating = item_rating;
    }
}
