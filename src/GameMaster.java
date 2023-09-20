import java.awt.*;
import java.awt.event.*;

public class GameMaster extends Canvas implements KeyListener {
  // ■ フィールド変数
  Image        buf;   // 仮の画面としての buffer に使うオブジェクト(Image クラス)
  Graphics     buf_gc;// buffer のグラフィックスコンテキスト (gc) 用オブジェクト
  Dimension    d;     // ウィンドウの大きさを管理するオブジェクト
  private int  imgW, imgH; // キャンバスの大きさ

  private int enmyAnum  = 15; //金魚の数
  private int enmyBnum  =  6; //亀の数
  private int enmyCnum  =  3; //蟹の数
  private int enmyDnum  =  1; //エメラルドの数
  private int enDappScr = 40000; //エメラルドの出現スコア
  private int enmyEnum  = 1;  //タコの数
  private int enEappScr = 10000; //タコの出現スコア
  private int enmyFnum  = 2;  //イルカの数
  private int enFappScr = 5000; //イルカの出現スコア
  private int enmyGnum  = 20; //クラゲの最大数
  private int ftrBltNum = 10; //自機の弾の数
  private int eneBltNum = 30; //敵機の弾の数
  private int eBlappScr = 20000; //敵機の弾の出現スコア
  private int mode      = -3; // -1: タイトル画面, -2: ゲームオーバー, 1〜 ゲームステージ
  private int i, j;
  static int TotalScore;
  int BestScore = 0;
  int cnt = 0;
  int hP = 1;
  int BgMove = 0;
  int bomb = 0;
  int Bx,By;

  Fighter       ftr;  //自機
  FtrRightBlt  ftrRBlt[] = new FtrRightBlt[ftrBltNum];   //自機の右弾
  FtrLeftBlt   ftrLBlt[] = new FtrLeftBlt[ftrBltNum];    //自機の左弾
  EnmDownBlt    enDBlt[] = new EnmDownBlt[eneBltNum];    //敵機の下弾
  EnmUpBlt      enUBlt[] = new EnmUpBlt[eneBltNum];      //敵機の上弾
  EnmRightBlt   enRBlt[] = new EnmRightBlt[eneBltNum];   //敵機の右弾
  EnmLeftBlt    enLBlt[] = new EnmLeftBlt[eneBltNum];    //敵機の左弾
  GoldFish      enmyA[]  = new GoldFish[enmyAnum];       // 敵機A = 金魚
  Turtle        enmyB[]  = new Turtle[enmyBnum];         // 敵機B = 亀
  Club          enmyC[]  = new Club[enmyCnum];           // 敵機C = 蟹
  EmeFish       enmyD[]  = new EmeFish[enmyDnum];        // 敵機D = エメラルド
  Octopus       enmyE[]  = new Octopus[enmyEnum];        // 敵機E = タコ
  Dolphin       enmyF[]  = new Dolphin[enmyFnum];        // 敵機F = イルカ
  JellyFish     enmyG[]  = new JellyFish[enmyGnum];      // 敵機G = クラゲ

  Image imgBg, imgTitle, imgGaOv, imgLoad, imgLet, imgLife, imgFtr, imgBlt, imgEnmBlt, imgBomb;
  Image imgEnmA, imgEnmB, imgEnmC, imgEnmD, imgEnmE, imgEnmF, imgEnmG;

  // ■ コンストラクタ
  GameMaster(int imgW, int imgH) {
    this.imgW = imgW;
    this.imgH = imgH;
    setSize(imgW, imgH);

    addKeyListener(this);

    ftr = new Fighter(imgW, imgH);
    for (i = 0; i < ftrBltNum; i++){
      ftrRBlt[i] = new FtrRightBlt();
      ftrLBlt[i] = new FtrLeftBlt();
    }
    for (i = 0; i < eneBltNum; i++){
      enDBlt[i] = new EnmDownBlt();
      enUBlt[i] = new EnmUpBlt();
      enRBlt[i] = new EnmRightBlt();
      enLBlt[i] = new EnmLeftBlt();
    }
    for (i = 0; i < enmyAnum; i++) enmyA[i] = new GoldFish(imgW, imgH);
    for (i = 0; i < enmyBnum; i++) enmyB[i] = new Turtle(imgW, imgH);
    for (i = 0; i < enmyCnum; i++) enmyC[i] = new Club(imgW, imgH);
    for (i = 0; i < enmyDnum; i++) enmyD[i] = new EmeFish(imgW, imgH);
    for (i = 0; i < enmyEnum; i++) enmyE[i] = new Octopus(imgW, imgH);
    for (i = 0; i < enmyFnum; i++) enmyF[i] = new Dolphin(imgW, imgH);
    for (i = 0; i < enmyGnum; i++) enmyG[i] = new JellyFish(imgW, imgH);

    imgBg   = getToolkit().getImage("../img/BackGround.png");
    imgTitle = getToolkit().getImage("../img/Title.png");
    imgGaOv = getToolkit().getImage("../img/GameOver.png");
    imgLoad = getToolkit().getImage("../img/java.png");
    imgLet  = getToolkit().getImage("../img/letter.png");
    imgLife = getToolkit().getImage("../img/life.png");
    imgFtr  = getToolkit().getImage("../img/ftr3.png");
    imgEnmA = getToolkit().getImage("../img/GoldFish.png");
    imgEnmB = getToolkit().getImage("../img/turtle.png");
    imgEnmC = getToolkit().getImage("../img/club.png");
    imgEnmD = getToolkit().getImage("../img/EmeFish.png");
    imgEnmE = getToolkit().getImage("../img/Octopus.png");
    imgEnmF = getToolkit().getImage("../img/dolphin.png");
    imgEnmG = getToolkit().getImage("../img/jellyfish.png");
    imgBlt  = null;
    imgEnmBlt = null;
    imgBomb = getToolkit().getImage("../img/bomb.png");

    ftr.setImage(imgFtr); // 読み込んだ画像 imgFtr を ftr に設定
  }

  // ■ メソッド
  // コンストラクタ内で createImage を行うと peer の関連で
  // nullpointer exception が返ってくる問題を回避するために必要
  public void addNotify(){
    super.addNotify();
    buf = createImage(imgW, imgH);
    buf_gc = buf.getGraphics();
  }

  // ■ メソッド (Canvas)
  public void paint(Graphics g) {
    buf_gc.drawImage(imgBg,0,0,imgW,imgH,BgMove%(imgW*2),0,BgMove%(imgW*2)+imgW,imgH,this);
    BgMove ++;
    switch (mode) {
    case -3:
      buf_gc.setColor(Color.black);
      buf_gc.fillRect(0, 0, imgW, imgH);
      buf_gc.drawImage(imgLoad,imgW/2-225/2,imgH/2-225/2,this);
      buf_gc.setColor(Color.white);
      buf_gc.drawString("      Loading... ", imgW/2-60, imgH/2+160);
      buf_gc.drawString("Programed by Java", 10, imgH-10);
      if(cnt==80) mode = -1;
      else cnt++;
      break;
    case -2:
      buf_gc.drawImage(imgGaOv,0,0,this);
      DisplayScore(TotalScore,10,10);
      buf_gc.drawImage(imgLet,imgW/2+1,10+1,imgW/2+16,10+24,545,93,559,103,this);           //へ
      buf_gc.drawImage(imgLet,imgW/2+8,10+1,imgW/2+16,10+9,216,92,224,100,this);            //゛
      buf_gc.drawImage(imgLet,imgW/2+1+17,10+1,imgW/2+16+17,10+24,232,93,244,106,this);     //ス
      buf_gc.drawImage(imgLet,imgW/2+1+17*2,10+1,imgW/2+16+17*2,10+24,367,92,376,106,this); //ト
      if(TotalScore>BestScore) BestScore=TotalScore;
      DisplayScore(BestScore,50+imgW/2,10);
      break;
    case -1:
      buf_gc.drawImage(imgTitle,0,0,this);
      ftr.hp = 3;
      hP = 1;
      ftr.x = (int)(imgW*0.1);
      ftr.y = (int)(imgH/2);
      TotalScore = 0;
      for (i = 0; i < enmyAnum; i++) enmyA[i].revive(imgW, imgH);
      for (i = 0; i < enmyBnum; i++) enmyB[i].revive(imgW, imgH);
      for (i = 0; i < enmyCnum; i++) enmyC[i].revive(imgW, imgH);
      for (i = 0; i < enmyDnum; i++) enmyD[i].revive(imgW, imgH);
      for (i = 0; i < enmyEnum; i++) enmyE[i].revive(imgW, imgH);
      for (i = 0; i < enmyFnum; i++) enmyF[i].revive(imgW, imgH);
      for (i = 0; i < enmyGnum; i++) enmyG[i].revive(imgW, imgH);
      break;
    default:
      // *** ランダムに敵を生成 ***
      MakeEnmy(enmyA,enmyAnum);
      MakeEnmy(enmyB,enmyBnum);
      MakeEnmy(enmyC,enmyCnum);
      if(TotalScore>=enDappScr) MakeEnmy(enmyD,enmyDnum);
      if(TotalScore>=enEappScr&&bomb==0) MakeEnmy(enmyE,enmyEnum);
      if(TotalScore>=enFappScr) MakeEnmy(enmyF,enmyFnum);
      if(hP<enmyGnum) MakeEnmy(enmyG,hP);
      else MakeEnmy(enmyG,enmyGnum);
      // *** 自分の弾を発射 ***
      if (ftr.sflag == true && ftr.delaytime == 0) {
        for (i = 0; i < ftrBltNum; i++) {
          if(ftr.vector == 1 && ftrLBlt[i].hp == 0) {
            ftrLBlt[i].revive(ftr.x, ftr.y);
            ftr.delaytime = 5;
            break;
          }
          if (ftr.vector == 0 && ftrRBlt[i].hp == 0) {
            ftrRBlt[i].revive(ftr.x, ftr.y);
            ftr.delaytime = 5;
            break;
          }
        }
      } else if (ftr.delaytime > 0)
        ftr.delaytime--;

      // *** 敵が弾を発射 ***
      if (TotalScore>=eBlappScr)
      if (enmyE[0].delaytime == 0)
      for (i = 0; i < eneBltNum; i++) {
        if (enDBlt[i].hp == 0 && enUBlt[i].hp == 0 && enLBlt[i].hp == 0 && enRBlt[i].hp == 0 && enmyE[0].hp > 0) {
          enDBlt[i].revive(enmyE[0].x, enmyE[0].y);
          enUBlt[i].revive(enmyE[0].x, enmyE[0].y);
          enRBlt[i].revive(enmyE[0].x, enmyE[0].y);
          enLBlt[i].revive(enmyE[0].x, enmyE[0].y);
          enmyE[0].delaytime = (int)(Math.random()*30)+3;
          break;
        }
      }
      else if (enmyE[0].delaytime > 0) enmyE[0].delaytime--;

      // ***各オブジェクト間の衝突チェック ***/
      if(ftr.f3flag == false | ftr.f4flag == false){
        CollisionCheck(enmyA,enmyAnum,100);
        CollisionCheck(enmyB,enmyBnum,300);
        CollisionCheck(enmyC,enmyCnum,600);
        if(TotalScore>=enDappScr) CollisionCheck(enmyD,enmyDnum,20000);
        if(TotalScore>=enEappScr) {
          CollisionCheck(enmyE,enmyEnum,10000);
          if(bomb==0&&enmyE[0].hp==0) {
            bomb = 1;
            enmyE[0].hp --;
            Bx = enmyE[0].x;
            By = enmyE[0].y;
          }
          if(bomb>0){
            buf_gc.drawImage(imgBomb,Bx-40,By-40,Bx+40,By+40,20*((int)(bomb/4)-1),0,20*(int)(bomb/4),20,this);
            bomb += 1;
            if(bomb>64) bomb=0;
          }
        }
        if(TotalScore>=enFappScr) CollisionCheck(enmyF,enmyFnum,1000);
        if(hP<enmyGnum) CollisionCheck(enmyG,hP,3000);
        else CollisionCheck(enmyG,enmyGnum,3000);
        if(TotalScore>=eBlappScr)
        for (i=0; i < eneBltNum; i++){
          if (enDBlt[i].hp > 0) ftr.collisionCheck(enDBlt[i], 0);
          if (enUBlt[i].hp > 0) ftr.collisionCheck(enUBlt[i], 0);
          if (enLBlt[i].hp > 0) ftr.collisionCheck(enLBlt[i], 0);
          if (enRBlt[i].hp > 0) ftr.collisionCheck(enRBlt[i], 0);
        }
      }

      // *** 自機の生死を判断 ***
      if (ftr.hp < 1)
        mode = -2; // ゲーム終了

      // ***オブジェクトの描画＆移動 ***
      MoveDraw(enmyA,enmyAnum,imgEnmA);
      MoveDraw(enmyB,enmyBnum,imgEnmB);
      MoveDraw(enmyC,enmyCnum,imgEnmC);
      if(TotalScore>=enDappScr) MoveDraw(enmyD,enmyDnum,imgEnmD);
      MoveDraw(ftrRBlt,ftrBltNum,imgBlt);
      MoveDraw(ftrLBlt,ftrBltNum,imgBlt);
      if (TotalScore>=eBlappScr){
        MoveDraw(enDBlt,eneBltNum,imgEnmBlt);
        MoveDraw(enUBlt,eneBltNum,imgEnmBlt);
        MoveDraw(enRBlt,eneBltNum,imgEnmBlt);
        MoveDraw(enLBlt,eneBltNum,imgEnmBlt);
      }
      if(TotalScore>=enEappScr) MoveDraw(enmyE,enmyEnum,imgEnmE);
      if(TotalScore>=enFappScr) MoveDraw(enmyF,enmyFnum,imgEnmF);
      if(hP<enmyGnum) MoveDraw(enmyG,hP,imgEnmG);
      else MoveDraw(enmyG,enmyGnum,imgEnmG);
      ftr.move(imgW, imgH);
      ftr.draw(buf_gc, imgFtr);

      if(TotalScore/100000>=hP) {
        ftr.hp++;
        hP++;
      }
      DisplayScore(TotalScore,10,10);
      buf_gc.drawImage(imgLet,imgW/2+1,10+1,imgW/2+16,10+24,200,115,213,129,this);
      buf_gc.drawImage(imgLet,imgW/2+1+17,10+1,imgW/2+16+17,10+24,17,92,29,106,this);
      buf_gc.drawImage(imgLet,imgW/2+1+17*2,10+1,imgW/2+16+17*2,10+24,530,93,543,106,this);
      for(i=0;i<ftr.hp;i++) buf_gc.drawImage(imgLife,imgW/2+60+17*i,10+2,imgW/2+75+17*i,10+23,(i%7)*13,0,(i%7+1)*13,11,this);

      // 状態チェック
      for (i = 0; i < enmyAnum; i++){
        //System.out.print(enmyA[i].hp + " ");
      }

      // 隠しコマンド
      if(ftr.f1flag == true & ftr.f3flag == true) TotalScore += 1000;
      if(ftr.f1flag == true & ftr.f4flag == true) ftr.hp ++;
      if(ftr.f2flag == true & ftr.f3flag == true) TotalScore -= 1000;
      if(ftr.f2flag == true & ftr.f4flag == true) ftr.hp --;
      if(ftr.f5flag == true) mode = -1;

    }
    g.drawImage(buf, 0, 0, this);
  }

  // ■ メソッド (Canvas)
  public void update(Graphics gc) {
    paint(gc);
  }

  // ■ メソッド (KeyListener)
  public void keyTyped(KeyEvent ke) {
  }

  public void keyPressed(KeyEvent ke) {
    int cd = ke.getKeyCode();
    switch (cd) {
    case KeyEvent.VK_LEFT: // [←]キーが押されたら
      ftr.lflag = true; // フラグを立てる
      break;
    case KeyEvent.VK_RIGHT: // [→]キーが押されたら
      ftr.rflag = true; // フラグを立てる
      break;
    case KeyEvent.VK_UP: // [↑]キーが押されたら
      ftr.uflag = true; // フラグを立てる
      break;
    case KeyEvent.VK_DOWN: // [↓]キーが押されたら
      ftr.dflag = true; // フラグを立てる
      break;
    case KeyEvent.VK_SPACE: // スペースキーが押されたら
      ftr.sflag = true; // フラグを立てる
      if (this.mode != 1 && this.mode != -3){
        this.mode++;
      }
      break;
    case KeyEvent.VK_F1: // [F1]キーが押されたら
      ftr.f1flag = true; // フラグを立てる
      break;
    case KeyEvent.VK_F2: // [F2]キーが押されたら
      ftr.f2flag = true; // フラグを立てる
      break;
    case KeyEvent.VK_F3: // [F3]キーが押されたら
      ftr.f3flag = true; // フラグを立てる
      break;
    case KeyEvent.VK_F4: // [F4]キーが押されたら
      ftr.f4flag = true; // フラグを立てる
      break;
    case KeyEvent.VK_F5: // [F5]キーが押されたら
      ftr.f5flag = true; // フラグを立てる
      break;
    }
  }

  // ■ メソッド (KeyListener)
  public void keyReleased(KeyEvent ke) {
    int cd = ke.getKeyCode();
    switch (cd) {
    case KeyEvent.VK_LEFT: // [←]キーが離されたら
      ftr.lflag = false; // フラグを降ろす
      break;
    case KeyEvent.VK_RIGHT: // [→]キーが離されたら
      ftr.rflag = false; // フラグを降ろす
      break;
    case KeyEvent.VK_UP: // [↑]キーが離されたら
      ftr.uflag =false; // フラグを降ろす
      break;
    case KeyEvent.VK_DOWN: // [↓]キーが離されたら
      ftr.dflag = false; // フラグを降ろす
      break;
    case KeyEvent.VK_SPACE: // スペースキーが離されたら
      ftr.sflag = false; // フラグを降ろす
      break;
    case KeyEvent.VK_F1: // [F1]キーが離されたら
      ftr.f1flag = false; // フラグを降ろす
      break;
    case KeyEvent.VK_F2: // [F2]キーが離されたら
      ftr.f2flag = false; // フラグを立てる
      break;
    case KeyEvent.VK_F3: // [F3]キーが離されたら
      ftr.f3flag = false; // フラグを立てる
      break;
    case KeyEvent.VK_F4: // [F4]キーが離されたら
      ftr.f4flag = false; // フラグを立てる
      break;
    case KeyEvent.VK_F5: // [F5]キーが離されたら
      ftr.f5flag = false; // フラグを立てる
      break;
    }
  }

  public void CollisionCheck(Enemy enmy[], int enmynum, int score) {
    for (i=0; i < enmynum; i++)
      if (enmy[i].hp > 0) {
        ftr.collisionCheck(enmy[i], 0);
        for (j = 0; j < ftrBltNum; j++){
          if (ftrRBlt[j].hp > 0)
            ftrRBlt[j].collisionCheck(enmy[i], score);
          if (ftrLBlt[j].hp > 0)
            ftrLBlt[j].collisionCheck(enmy[i], score);
        }
      }
  }

  public void MakeEnmy(Enemy enmy[], int enmynum) {
    makeEnmy: if (Math.random() < 0.1) for (i = 0; i < enmynum; i++)
      if (enmy[i].hp <= 0) {
        enmy[i].revive(imgW, imgH);
        break makeEnmy;
      }
  }

  public void MoveDraw(MovingObject move[], int num, Image img) {
    for (i = 0; i < num; i++){
      move[i].move(imgW, imgH);
      move[i].draw(buf_gc, img);
    }
  }

  public void DisplayScore(int score, int x, int y) { //スコアを表示
    int num = 0;
    int length = (int)Math.log10(score);
    buf_gc.drawImage(imgLet,x+1,y+1,x+16,y+24,232,93,244,106,this);        //ス
    buf_gc.drawImage(imgLet,x+1+17,y+1,x+16+17,y+24,167,93,178,104,this);  //コ
    buf_gc.drawImage(imgLet,x+1+17*2,y+1,x+16+17*2,y+24,2,93,15,106,this); //ア
    if(score==0) buf_gc.drawImage(imgLet,x+60,y,x+18+60,y+26,238+num*9,0,238+(1+num)*9,13,this); //0
    i = 0;
    while(score>0){
      num = score % 10;
      buf_gc.drawImage(imgLet,(length-i)*17+x+60,y,(length-i)*17+x+18+60,y+26,238+num*9,0,238+(1+num)*9,13,this);
      score = score/10;
      i++;
    }
  }
}

class Bullet extends MovingObject {
  /** コンストラクタ **/
  Bullet() {
    hp = 0;
  }
  /** メソッド **/
  void move(int apWidth, int apHeight) {
    if (hp>0) {
      if (y > 0 && y < apHeight && x > 0 && x < apWidth){
        y = y + dy;
        x = x + dx;
      }
      else
        hp = 0;
    }
  }
  void draw(Graphics buf, Image img){}
  void revive(int x, int y) {
    this.x = x;
    this.y = y;
    hp = 1;
  }
  void setImage(Image img){
    this.img = img;
  }
}

class FighterBullet extends Bullet {
  FighterBullet() {
    w = h = 6;
  }
  void draw(Graphics buf, Image img) {
    if (hp>0) {
      buf.setColor(Color.white);
      buf.drawOval(x-w, y-h, 2*w, 2*h);
    }
  }
}

class EnemyBullet extends Bullet {
  EnemyBullet() {
    w = h = 10;
  }
  void draw(Graphics buf, Image img) {
    if (hp>0) {
      buf.setColor(Color.black);
      buf.fillOval(x-w/2, y-h/2, w, h);
    }
  }
}

class EnmDownBlt extends EnemyBullet {
  EnmDownBlt() {
    dx = -1; dy = 6;
  }
}

class EnmUpBlt extends EnemyBullet {
  EnmUpBlt() {
    dx = -1; dy = -6;
  }
}

class EnmRightBlt extends EnemyBullet {
  EnmRightBlt() {
    dx = 6; dy = 0;
  }
}

class EnmLeftBlt extends EnemyBullet {
  EnmLeftBlt() {
    dx = -6; dy = 0;
  }
}

class FtrRightBlt extends FighterBullet {
  FtrRightBlt() {
    dx = 6; dy = 0;
  }
}

class FtrLeftBlt extends FighterBullet {
  FtrLeftBlt() {
    dx = -6; dy = 0;
  }
}

class Enemy extends MovingObject {
  int delaytime;

  // コンストラクタ(初期値設定)
  Enemy(int apWidth, int apHeight) {
    hp = 0;
  }
  // ○を描き更新するメソッド
  void move(int apWidth,int apHeight) {
    if (hp>0) {
      x = x + dx;
      y = y + dy;
      if (x<-2*w|apWidth+2*w<x|y<-30-h|y>apHeight+30+h) hp=0;
      if (y<0) dy = 1+(int)(Math.random()*(2));
    }
  }
  void draw(Graphics buf, Image img) {}
  void revive(int apWidth, int apHeight) {
    x = apWidth+w*2;
    y = (int)(Math.random()*(apHeight));
    dx = -(int)(Math.random()*2)-1;
    if (y<apHeight/2)
      dy = (int)(Math.random()*2);
    else
      dy = -(int)(Math.random()*2);
  }
  void setImage(Image img){
    this.img = img;
  }
}

class Octopus extends Enemy {
  Octopus(int apWidth, int apHeight) {
    super(apWidth, apHeight);
    w = h = 60;
    delaytime = 5;
  }
  void draw(Graphics buf, Image img) {
    if (hp>0) {
      this.img = img;
      buf.drawImage(img, x-w, y-h, null); // buf に img を貼り付け
    }
  }
  void revive(int apWidth, int apHeight) {
    super.revive(apWidth,apHeight);
    hp = 10;
  }
}

class GoldFish extends Enemy {
  GoldFish(int apWidth, int apHeight) {
    super(apWidth, apHeight);
    w = 32;
    h = 15;
  }
  void draw(Graphics buf, Image img) {
    if (hp>0) {
      if(dx>0) imgx = w*2;
      else imgx = 0;
      this.img = img;
      buf.drawImage(img, x-w, y-h, x+w, y+h, imgx, 0, imgx+w*2, h*2, null); // buf に img を貼り付け
    }
  }
  void revive(int apWidth, int apHeight) {
    super.revive(apWidth,apHeight);
    hp = 1;
  }
}

class Turtle extends Enemy {
  Turtle(int apWidth, int apHeight) {
    super(apWidth, apHeight);
    w = 32;
    h = 16;
  }
  void draw(Graphics buf, Image img) {
    if (hp>0) {
      if(dx>0) imgx = w*2;
      else imgx = 0;
      this.img = img;
      buf.drawImage(img, x-w, y-h, x+w, y+h, imgx, 0, imgx+w*2, h*2, null); // buf に img を貼り付け
    }
  }
  void revive(int apWidth, int apHeight) {
    super.revive(apWidth,apHeight);
    hp = 2;
  }
}

class Club extends Enemy {
  Club(int apWidth, int apHeight) {
    super(apWidth, apHeight);
    w = 24;
    h = 15;
  }
  void move(int apWidth,int apHeight) {
    if (hp>0) {
      if(y>apHeight-h) dy = -(int)(Math.random()*(2));
      if(y<apHeight*0.8) dy = (int)(Math.random()*(2));
      super.move(apWidth,apHeight);
    }
  }
  void draw(Graphics buf, Image img) {
    if (hp>0) {
      this.img = img;
      buf.drawImage(img, x-w, y-h, null); // buf に img を貼り付け
    }
  }
  void revive(int apWidth, int apHeight) {
    super.revive(apWidth,apHeight);
    x = apWidth+w*2;
    y = (int)(Math.random()*(apHeight/4))+3*apHeight/4;
    hp = 2;
  }
}

class EmeFish extends Enemy {
  EmeFish(int apWidth, int apHeight) {
    super(apWidth, apHeight);
    w = 32;
    h = 15;
  }
  void draw(Graphics buf, Image img) {
    if (hp>0) {
      /*
      buf.setColor(new Color(255,105,180));
      super.draw(buf,img);
      */
      if(dx>0) imgx = w*2;
      else imgx = 0;
      this.img = img;
      buf.drawImage(img, x-w, y-h, x+w, y+h, imgx, 0, imgx+w*2, h*2, null); // buf に img を貼り付け
    }
  }
  void revive(int apWidth, int apHeight) {
    super.revive(apWidth,apHeight);
    dx = -2-(int)(Math.random()*3);
    dy = 2-(int)(Math.random()*5);
    hp = 1;
  }
}

class Dolphin extends Enemy {
  Dolphin(int apWidth, int apHeight) {
    super(apWidth, apHeight);
    w = 32;
    h = 16;
  }
  void draw(Graphics buf, Image img) {
    if (hp>0) {
      /*
      buf.setColor(new Color(255,105,180));
      super.draw(buf,img);
      */
      if(dx>0) imgx = w*2;
      else imgx = 0;
      this.img = img;
      buf.drawImage(img, x-w, y-h, x+w, y+h, imgx, 0, imgx+w*2, h*2, null); // buf に img を貼り付け
    }
  }
  void revive(int apWidth, int apHeight) {
    super.revive(apWidth,apHeight);
    dx = -4-(int)(Math.random()*3);
    if(Math.random() < 0.5) {
      x = -w*2;
      dx = 2+(int)(Math.random()*3);
    }
    hp = 1;
  }
}

class JellyFish extends Enemy {
  JellyFish(int apWidth, int apHeight) {
    super(apWidth, apHeight);
    w = 24;
    h = 15;
  }
  void draw(Graphics buf, Image img) {
    if (hp>0) {
      this.img = img;
      buf.drawImage(img, x-w, y-h, null); // buf に img を貼り付け
    }
  }
  void revive(int apWidth, int apHeight) {
    super.revive(apWidth,apHeight);
    hp = 1;
  }
}
