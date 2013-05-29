import processing.core.*; 
import processing.xml.*; 

import ddf.minim.*; 
import ddf.minim.signals.*; 
import ddf.minim.effects.*; 
import java.io.File; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class phono_divinoro extends PApplet {






float div1000;
byte frate;
short buffer_size;
Minim minim;

AudioInput in;
AudioPlayer groove;
Waveline input_sketcher;

int img_buffer_size;
int img_loaded;

int smp_range;
int trigger_thresh;

boolean keypressed;
boolean clear;
boolean play_demo;
String rec_name;
byte play_i;

PFont afont;
PGraphics acanvas;
PImage bcanvas;

char keypush;

Boolean playing[];

public void setup() {
  size(800, 480);
  frate = 15;
  div1000 = (1000/frate);
  buffer_size = 256;
  img_buffer_size = 32;
  img_loaded = 1;
  smp_range = 0;
  trigger_thresh = 72;
  frameRate(frate*2);
  background(0, 0, 0);
  
  afont = createFont("Times", 12);
  textFont(afont);
  
  minim = new Minim(this);
  in = minim.getLineIn(Minim.STEREO, buffer_size);
  groove = minim.loadFile("mp3/phono_demo.mp3", buffer_size*8);
  
  input_sketcher = new Waveline(23, 200, 255, img_buffer_size, buffer_size);
  //input_sketcher = new Waveline(loadImage(dataPath("img")+"/"+images[0]), img_buffer_size);
  if (img_buffer_size > 0) {
    getImages("img");
  }
  
  playMode("demo");
  
  play_i = 0;
  
  keypush = PApplet.parseChar(0);
  
  keypressed = false;
  
  clear = true;
  
  play_demo = true;
}

public void getImages(String folder_name) {
  
  String[] images = new String[img_buffer_size];
  File folder = null;
  InputStream f;
  
  try {
    println("Importing pictures from "+ dataPath(folder_name));  
    folder = new File(dataPath(folder_name));
    if (folder != null) {
      File[] listOfFiles = folder.listFiles();

      for (int i = 0, id = 0; (i < 1000); i++) {
        if (listOfFiles[i].isFile()) {
          String file_num = listOfFiles[i].getName().substring(
                                listOfFiles[i].getName().length() - 6);
          //println(file_num);
          id = -1 + ((Character.getNumericValue(file_num.charAt(0))*10)+Character.getNumericValue(file_num.charAt(1)));
          String temp_ext = listOfFiles[i].getName().substring(
                                listOfFiles[i].getName().length() - 4);
          if ( (temp_ext.equals(".jpg") || temp_ext.equals(".png")) && (id < img_buffer_size) && (id > 0)) {       
            images[id] = dataPath(folder_name)+"/"+listOfFiles[i].getName();
            img_loaded += 1;
            id += 1;
          }
        } else if (listOfFiles[i].isDirectory()) {
          System.out.println("Directory " + listOfFiles[i].getName());
        }
      }
    }
  } catch (Exception e) {}
  
  //Back up pics
  int id = 0;
  String[] lista = { "img/fire01.jpg", 
                      "img/fire02.jpg", 
                      "img/fire03.jpg",  
                      "img/fire04.jpg",
                      "img/fire05.jpg", 
                      "img/fire06.jpg",  
                      "img/fire07.jpg", 
                      "img/fire08.jpg", 
                      "img/fire09.jpg",  
                      "img/fire10.jpg",  
                      "img/fire11.jpg", 
                      "img/fire12.jpg", 
                      "img/fire13.jpg", 
                      "img/fire14.jpg",
                      "img/fire15.jpg", 
                      "img/fire16.jpg", 
                      "img/fire17.jpg", 
                      "img/fire18.jpg",
                      "img/fire19.jpg", 
                      "img/fire20.jpg", 
                      "img/fire21.jpg",  
                      "img/fire22.jpg", 
                      "img/fire23.jpg",  
                      "img/fire24.jpg",
                      "img/fire25.jpg", 
                      "img/fire26.jpg",  
                      "img/fire27.jpg", 
                      "img/fire28.jpg", 
                      "img/fire29.jpg",  
                      "img/fire30.jpg",  
                      "img/fire31.jpg", 
                      "img/fire32.jpg", 
                      "img/fire33.jpg", 
                      "img/fire34.jpg",
                      "img/fire35.jpg", 
                      "img/fire36.jpg", 
                      "img/fire37.jpg", 
                      "img/fire38.jpg",
                      "img/fire39.jpg", 
                      "img/fire40.jpg", 
                      "img/fire41.jpg", 
                      "img/fire42.jpg", 
                      "img/fire43.jpg",   
                      "img/fire44.jpg",
                      "img/fire45.jpg", 
                      "img/fire46.jpg",  
                      "img/fire47.jpg", 
                      "img/fire48.jpg", 
                      "img/fire49.jpg",  
                      "img/fire50.jpg",  
                      "img/fire51.jpg", 
                      "img/fire52.jpg", 
                      "img/fire53.jpg", 
                      "img/fire54.jpg",
                      "img/fire55.jpg", 
                      "img/fire56.jpg", 
                      "img/fire57.jpg", 
                      "img/fire58.jpg",
                      "img/fire59.jpg", 
                      "img/fire60.jpg", 
                      "img/fire61.jpg", 
                      "img/fire62.jpg", 
                      "img/fire63.jpg",   
                      "img/fire64.jpg",
                      "img/fire65.jpg", 
                      "img/fire66.jpg", 
                      "img/fire67.jpg", 
                      "img/fire68.jpg",
                      "img/fire69.jpg", 
                      "img/fire70.jpg", 
                      "img/fire71.jpg", 
                      "img/fire72.jpg", 
                      "img/fire73.jpg",   
                      "img/fire74.jpg",
                      "img/fire75.jpg", 
                      "img/fire76.jpg",  
                      "img/fire77.jpg", 
                      "img/fire78.jpg", 
                      "img/fire79.jpg",  
                      "img/fire80.jpg",  
                      "img/fire81.jpg", 
                      "img/fire82.jpg", 
                      "img/fire83.jpg", 
                      "img/fire84.jpg",
                      "img/fire85.jpg", 
                      "img/fire86.jpg", 
                      "img/fire87.jpg", 
                      "img/fire88.jpg",
                      "img/fire89.jpg", 
                      "img/fire90.jpg", 
                      "img/fire91.jpg", 
                      "img/fire92.jpg" };
  for (final String name : lista){
    if ((images[id] == null) && (id < img_buffer_size)) {
      images[id] = name;
      img_loaded += 1;
      id += 1;
    }  
  }

  if (img_loaded >= img_buffer_size) {
    for (short i = 0; i < img_buffer_size; i++) {
      input_sketcher.pauseVid(true);
      try {
        load_images(i, images[i]);
        println(images[i]);
        text(images[i], 0, i * 16 + 16);
      } catch (Exception e) {
        input_sketcher.pauseVid(true);
        background(0);
        text(images[i] + "DID NOT LOAD", 0, i * 16);
        img_loaded = 0;
      }
    }
  }
}

public void draw() {
  short time = (short)(millis() % 1000);
  
  if((div1000/2) >= (time % (int)div1000)) { 
    
    if(!mousePressed) {
      play_img(trigger_thresh);
    } 
    
    if (null != acanvas) { 
      acanvas = input_sketcher.draw(0, 0, clear);
      acanvas.tint(255, 207); 
      pushMatrix();
      translate(width,0);
      scale(-1.0f,1.0f);
      image(acanvas, 0, acanvas.height/2);
      //tint(255, 63); 
      //image(bcanvas, 0, -12, bcanvas.width, height+24);
      noTint();
      popMatrix();
      image(acanvas, 0, acanvas.height/2);
      tint(255, 207); 
      image(bcanvas, -1, -7, width+1, height+12);
      noTint();
      this.loadPixels();
      bcanvas.loadPixels();
      for(int j = 0, y = bcanvas.height; j < y; j++) {
        for(int i = 0, x = bcanvas.width; i < x; i++) {
          bcanvas.pixels[(j*x)+i] = this.pixels[(4*j*x)+(i*2)];
        }
      }
      bcanvas.updatePixels();
    } else {
      acanvas = input_sketcher.draw(0, 0, clear);
      bcanvas = createImage(width/2, height/2, RGB);
      bcanvas.loadPixels();
      for(int i = 0, z = bcanvas.pixels.length; i < z; i++) {
        bcanvas.pixels[i] = 0xffffffff;
      }
      bcanvas.updatePixels();
      background(0, 0, 0);
    } 
    
    if (keypush != PApplet.parseChar(0)) {
      text(keypush, 0, height - 8);
    }
    clear = false;
  }
  
  if(mousePressed) {
    byte sketch_mode = input_sketcher.get_mode();
    switch (sketch_mode) {
      case 0:
      short vr = (short)map(mouseX*2-width, 0, width, 255, 32);
      short vg = (short)map(mouseY*2-height, 0, height, 255, 32);
      short vb = (short)map(width-mouseX*2, 0, width, 255, 32);
      input_sketcher.change_clr(vr, vg, vb);
      break;
      case 1:
      short img_num = (short)map(width-mouseX, 0, width, img_buffer_size, 0);
      if ((img_num <= (img_buffer_size-1)) && (img_num >= 0)) {
        input_sketcher.change_img(img_num);
        clear = true;
      }
      break;
    }
  }
  
  if(play_demo) {
    smooth();
    text("Hit L to switch back to Line-In visualizing", width-300, 16);
  }
}

public void keyPressed() {
  if ((key != 'r')||(key != 'R')) {
    keypressed = true;
    if ((key == ' ')||(key == '.'))
      input_sketcher.pauseVid(true);
    if ((key == 'v')||(key == 'V')||(key == '0'))
      clear = true;
    if ((key == 's')||(key == 'S')||(key == '1'))
      input_sketcher.change_mdB((byte)2);
    if ((key == 'd')||(key == 'D')||(key == '2'))
      input_sketcher.change_mdB((byte)1);
    if ((key == 'f')||(key == 'F')||(key == '3'))
      input_sketcher.change_mdB((byte)0);
    if ((key == 'l')||(key == 'L'))
      playMode("line");
    if ((key == 'p')||(key == 'P'))
      playMode("demo");
  }
  keypush = key;
}

public void keyReleased()
{
  //if ( (key == 'r')||(key == 'R') ) 
  //{
  //  rec_name = "phono" + str(year() + month() + day() + hour() + minute());
  //  saveFrame("data/rec/"+rec_name+"####.jpg");
  //}
  
  if ( ((key == 'a')||(key == 'A')||(key == ENTER)) && (img_loaded >= img_buffer_size)) {
    byte chk_mode = input_sketcher.get_mode();
    switch (chk_mode) {
      case 0:
      short img_num = (short)map(width-mouseX, 0, width, img_buffer_size, 0);
      input_sketcher.change_img(img_num);
      clear = false; 
      break;
      case 1:
      short vr = (short)map(mouseX*2-width, 0, width, 255, 32);
      short vg = (short)map(mouseY*2-height, 0, height, 255, 32);
      short vb = (short)map(width-mouseX*2, 0, width, 255, 32);
      input_sketcher.change_clr(vr, vg, vb); 
      clear = false;
      break;
    }
  }
  
  if (key == ' ')
    input_sketcher.pauseVid(false);
    
  keypressed = false;
}

public void mouseMoved()
{
}

public void load_images(short img_index, String img_cur) {
  /*String name_index = "0000";
  if (img_index < 10) {
    name_index = "0000" + str(img_index);
  } else if (img_index < 100) {
    name_index = "000" + str(img_index);
  } else if (img_index < 1000) {
    name_index = "00" + str(img_index);
  } else if (img_index < 10000) {
    name_index = "0" + str(img_index);
  }
  String img_now = "img/img" + name_index + ".jpg";*/
  PImage chg_img = loadImage(img_cur);
  if (chg_img != null) input_sketcher.load_img(chg_img, img_index);
}

public void play_img(int pb_dec) {
  float sample_min = .01f;
  float sample_max = .01f;
  for (int i = 0; i < (img_buffer_size - 1); i++) {
    float samp_diff = input_sketcher.left[i+1] - input_sketcher.left[i];
    if ( samp_diff <= sample_min) 
      sample_min = samp_diff;
    if (samp_diff >= sample_max) 
      sample_max = samp_diff;
  }
  int curr_range = (int)(1000*sample_max - 1000*sample_min);
  
  if (curr_range > pb_dec ) {
    smp_range = curr_range;
    if (play_i > (img_buffer_size-1)) {
      play_i = 0;
    } else if (play_i < (img_buffer_size-1)) {
      input_sketcher.change_img(play_i);
    }
    play_i++;
    clear = true;
  }
  
  fill(0);
  rect(width - 32, height - 24, 32, 32);
  fill(255);
  text(smp_range, width - 32, height - 8); 
}

public void playMode(String mode) {
  if(mode == "demo") {
    groove.play();
    in.removeListener(input_sketcher);
    groove.addListener(input_sketcher);
    play_demo = true;
  } else {
    groove.removeListener(input_sketcher);
    in.addListener(input_sketcher);
    groove.pause();
    play_demo = false;
  }
}
    

public void stop() {
  in.close();
  groove.close();
  minim.stop();
  super.stop();  
}
class Waveline implements AudioListener
{
  private float[] left;
  private float[] right;
  short r, g, b;
  PImage[] img;
  short img_i;
  byte modeA;
  byte modeB;
  
  int wavew;
  int wavew_2;
  int waveh;
  PGraphics vscreen;
  
  boolean pause;
  
  Waveline(int vred, int vgreen, int vblue, int ibuff, int abuff)
  {
    left = new float[abuff];
    right = new float[abuff];
    for (int i=0, end=abuff; i < end; i++) {
      left[i]= 1.0f; 
      right[i] = 1.0f;
    }
    r = (short) vred;
    g = (short) vgreen;
    b = (short) vblue;
    if (ibuff > 0)
      img = new PImage[ibuff];
    img_i = 0;
    modeA = 0;
    modeB = 2;
    
    wavew = width/2;
    wavew_2 = wavew/2;
    waveh = height/2;
    
    vscreen = createGraphics(wavew,waveh, JAVA2D);
    vscreen.beginDraw();
    vscreen.background(0);
    vscreen.text("Hit the SPACE-BAR to begin visualizing...", 1, vscreen.height-8);
    vscreen.endDraw();
    
    background(0);
    pause = false;
  }
  
  Waveline(PImage img1, int ibuff, int abuff)
  {
    left = new float[abuff];
    right = new float[abuff];
    for (int i=0, end=abuff; i < end; i++) {
      left[i]= 1.0f; 
      right[i] = 1.0f;
    }
    if (ibuff > 0)
      img = new PImage[ibuff];
    img_i = 0;
    PGraphics temp_img = createGraphics(wavew,waveh,JAVA2D);
    temp_img.beginDraw();
    temp_img.image(img1, 0, 0, wavew, waveh);
    temp_img.endDraw();
    img[img_i] = temp_img;
    modeA = 1;
    modeB = 0;
     
    wavew = width;
    wavew_2 = wavew/2;
    waveh = height;
    
    vscreen = createGraphics(wavew_2,waveh, JAVA2D);
    vscreen.beginDraw();
    vscreen.background(0);
    vscreen.endDraw();
    
    pause = false;
    
  }
  
  public synchronized void samples(float[] samp)
  {
    left = samp;
  }
  
  public synchronized void samples(float[] sampL, float[] sampR)
  {
    left = sampL;
    right = sampR;
  }
  
  public void change_clr(short vred, short vgreen, short vblue) {
    r = vred;
    g = vgreen;
    b = vblue;
    modeA = 0;
  }
  
  public void load_img(PImage img1, short img_index) {
    img_i = img_index;
    PGraphics temp_img = createGraphics(wavew,waveh,JAVA2D);
    temp_img.beginDraw();
    temp_img.image(img1, 0, 0, wavew, waveh);
    temp_img.endDraw();
    img[img_i] = temp_img;
  }
  
  public void change_img(short img_index) {
    img_i = img_index;
    modeA = 1;
  }
  
  public byte get_mode() {
    return modeA;
  }
  
  public void change_mdB(byte m) {
    modeB = m;
    if (modeB > 2) modeB = 0;
  } 

  public void pauseVid(boolean p) {
    if (p) {
      pause = true;
    } else {
      pause = false;
    }
  }  
  
  public synchronized PGraphics draw(int vx, int vy, boolean clear)
  {

    if (!pause) {
      smooth();
      boolean clr_scrn = clear;
      
      // we've got a stereo signal if right or left are not null
      if ( left != null && right != null )
      {
        vscreen.beginDraw();
        vscreen.smooth();
        vscreen.strokeWeight(2);
      
        short y1;

        switch (modeA) {
          case 0:
          if(clr_scrn == true) vscreen.background(0,239);
          // draw the input waveforms
          y1 = (short)(waveh/2);
          for(byte i = 0; i < (wavew/16);i++)
          {
            short x1 = (short)(i * 8);
            short x2 = (short)((i + 1) * 8);
            vscreen.noFill();
            int pal = color(random(i*(r/32)),random(i*(g/32)),random(i*(b/32)));
            if(modeB == 2) vscreen.fill(pal, 27);
            vscreen.stroke(pal);
            draw_shape(x1, y1 + left[x1]*(wavew), x2, y1 + left[x2]*(wavew));
            if (modeB == 0)
              draw_shape(x1, y1 - right[x1]*(wavew), x2, y1 - right[x2]*(wavew));
          }
          break;
        
          case 1:
          tint(255, 239);
          //if(clr_scrn == true) vscreen.image(img[img_i],0,0);
          // draw the input waveforms
          y1 = (short)(waveh/2);
          for(byte i = 0; i < (wavew/16);i++)
          {
            short x1 = (short)(i * 8);
            short x2 = (short)((i + 1) * 8);
            vscreen.noFill();
            int pix = img[img_i].get(x1, (int)(y1 + left[x1]*(wavew_2)));
            if(modeB == 2) vscreen.fill(pix, 27);
            vscreen.stroke(pix);
            draw_shape(x1, y1 + left[x1]*(wavew), x2, y1 + left[x2]*(wavew));
            if (modeB == 0) {
              pix = img[img_i].get(x1, (int)(y1 - right[x1]*(wavew_2)));
              vscreen.stroke(pix);
              draw_shape(x1, y1 - right[x1]*(wavew), x2, y1 - right[x2]*(wavew));
            }
          }  
          break;
        }
        vscreen.endDraw(); 
      
        //image(vscreen, vx, vy, width, height);
      }
      else if ( left != null )
      {
        vscreen.beginDraw();
        vscreen.background(0);
        vscreen.strokeWeight(2);
 
        short y1 = (short)(waveh/2);
 
        switch (modeA) {
          case 0:
          // draw the input waveforms
          for(byte i = 0; i < (wavew);i++)
          {
            short x1 = (short)(i * 8);
            short x2 = (short)((i + 1) * 8);
            int pal = color(random(i*(r/32)),random(i*(g/32)),random(i*(b/32)));
            if(modeB == 2) vscreen.fill(pal, 27);
            vscreen.stroke(pal);
            draw_shape(x1, y1 + left[x1]*(wavew), x2, y1 + left[x2]*(wavew));
            if (modeB == 0)
              draw_shape(x1, y1 - left[x1]*(wavew), x2, y1 - left[x2]*(wavew));
          }
          break;
        
          case 1:
          tint(255, 239);
          //if(clr_scrn == true) vscreen.image(img[img_i],0,0);
          // draw the input waveforms
          for(byte i = 0; i < (wavew/16);i++)
          {
            short x1 = (short)(i * 8);
            short x2 = (short)((i + 1) * 8);
            int pix = img[img_i].get(x1, (int)(y1 + left[x1]*(wavew_2)));
            if(modeB == 2) vscreen.fill(pix, 27);
            vscreen.stroke(pix);
            draw_shape(x1, y1 + left[x1]*(wavew), x2, y1 + left[x2]*(wavew));
            if(modeB == 0) {
              pix = img[img_i].get(x1, (int)(y1 - left[x1]*(wavew_2)));
              vscreen.stroke(pix);
              draw_shape(x1, y1 - left[x1]*(wavew), x2, y1 - left[x2]*(wavew));
            }
          }
          break;
        }
        vscreen.endDraw(); 
      
        //image(vscreen, vx, vy, width, height);
      }
    }
    return vscreen;
  }
  
  public void draw_shape(short ax, float ay, short bx, float by) {
    int mode = (int)modeB;
    switch (mode) {
      case 0:
      vscreen.line(ax*2, ay*1.5f, bx*2, by*1.5f); 
      break;
      case 1:
      vscreen.rect(ax, ay*2-(by/2), bx, by); 
      break;
      case 2:
      vscreen.ellipse(ax*2, ay*2, bx, by); 
      break;
    }
  }
  
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--present", "--bgcolor=#666666", "--stop-color=#cccccc", "phono_divinoro" });
  }
}
