package app.com.ezata.model;

import app.com.ezata.R;

public enum ColorsEnum {
    COLOR1(R.color.color1),
    COLOR2(R.color.color2),
    COLOR3(R.color.color3),
    COLOR4(R.color.color4),
    COLOR5(R.color.color5);

    ColorsEnum(int s) {
        this.color = s;
    }

    private int color;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
