import java.awt.*;
class Fighter extends MovingObject {
  boolean lflag;
  boolean rflag;
  boolean uflag;
  boolean dflag;
  boolean sflag;
  boolean f1flag;
  boolean f2flag;
  boolean f3flag;
  boolean f4flag;
  boolean f5flag;
  int delaytime;
  int vector=0;
  Image img;

  // コンストラクタ
  Fighter(int apWidth, int apHeight) {
    // super(); //省略可
    x = (int)(apWidth*0.1);
    y = (int)(apHeight/2);
    dx = 5;
    dy = 5;
    w = 20;
    h = 20;
    lflag = false;
    rflag = false;
    uflag = false;
    dflag = false;
    sflag = false;
    f1flag= false;
    f2flag= false;
    f3flag= false;
    f4flag= false;
    f5flag= false;
    delaytime = 5;
  }
  void revive(int apWidth, int apHeight) {}
  void move(int apWidth, int apHeight) {
    if (lflag && !rflag && x >  w){
      x = x - dx;
      vector = 1;
    }
    if (rflag && !lflag && x < apWidth - w){
      x = x + dx;
      vector = 0;
    }
    if (uflag && !dflag && y > h){
      y = y - dy;
    }
    if (dflag && !uflag && y < apHeight - h){
      y = y + dy;
    }
  }
  void draw(Graphics buf, Image img) {
    if(vector == 1) imgx = 0;
    else imgx = w*2;
    this.img = img;
    buf.drawImage(img, x-w, y-h, x+w, y+h, imgx, 0, imgx+w*2, h*2, null); // buf に img を貼り付け
  }
  void setImage(Image img){
    this.img = img;
  }
}
