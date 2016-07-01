package souleima.qcm;

/**
 * Created by pc on 19/06/2016.
 */
public class Dataprovider {

    private int img_res;
    private String f_name;
    private  int color1;
    private int color2;

    public  Dataprovider(int img_res , String f_name, int color1, int color2){
        this.setF_name(f_name);
        this.setImg_res(img_res);
        this.color1=color1;
        this.color2=color2;

    }


    public int getColor2() {
        return color2;
    }

    public void setColor2(int color2) {
        this.color2 = color2;
    }



    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public int getImg_res() {
        return img_res;
    }

    public void setImg_res(int img_res) {
        this.img_res = img_res;
    }
    public int getColor1() {
        return color1;
    }

    public void setColor1(int color) {
        this.color1 = color;
    }
}
