import java.awt.Graphics;
import java.awt.*;

abstract class MovingObject { // 抽象クラス
  // フィールド変数
  int x, y;
  int dx, dy;
  int w, h;
  int hp;
  int imgx, imgy;

  Image img;

  // コンストラクタ1 引数が無い場合は初期設定しない（フィールド変数は初期値0をもつ）
  MovingObject () {}
  // コンストラクタ2 描画領域の大きさを引数に、出現の初期値をランダムに設定
  MovingObject (int width, int height) {
    x  = (int) (Math.random()*width);
    y  = (int) (Math.random()*height);
    dx = (int) (Math.random()*6-3);
    dy = (int) (Math.random()*6-3);
    w = 2;
    h = 2;
    hp = 3;
  }

  abstract void move(int apWidth, int apHeight);
  // オブジェクトを動かし、位置を更新する抽象メソッド
  // MovingObject を継承する全てのクラスに同名での実装を強制
  // move では 1.まず描いてから 2. vx,vy だけ移動させる
  // move を呼び出す前に衝突判定し、合格したものだけ描く

  abstract void draw(Graphics buf, Image img);

  abstract void revive(int w,int h);
  // オブジェクトを生き返らす抽象メソッド（MovingObject を継承する全てのクラスに同名での実装を強制）
  // 引数は、通常はアプレットの縦横の大きさ。弾の場合は、オブジェクトの位置

  abstract void setImage(Image img);

  boolean collisionCheck(MovingObject obj,int Score) {
    // 引数は相手のオブジェクト
    // 衝突判定の共通メソッド
    if (Math.abs(this.x-obj.x) <= (this.w+obj.w) && Math.abs(this.y-obj.y) <= (this.h+obj.h)) {
      this.hp--;
      obj.hp--;
      if(obj.hp==0) GameMaster.TotalScore += Score;
      return true;
    } else
      return false;
  }
}
