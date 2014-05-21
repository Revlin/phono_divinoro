import ddf.minim.*;
import ddf.minim.signals.*;
import ddf.minim.effects.*;
import java.io.File;

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

void setup() {
  size(800, 480);
  frate = 15;
  div1000 = (1000/frate);
  buffer_size = 256;
  img_buffer_size = 99;
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
  
  keypush = char(0);
  
  keypressed = false;
  
  clear = true;
  
  play_demo = true;
}

void getImages(String folder_name) {
  
  String[] images = new String[img_buffer_size];
  File folder = null;
  InputStream f;
  
  try {
    println("Importing pictures from "+ dataPath(folder_name));  
    folder = new File(dataPath(folder_name));
    if (folder != null) {
      File[] listOfFiles = folder.listFiles();

      for (int i = 0, id = 0; (i < 100); i++) {
        if (listOfFiles[i].isFile()) {
          println( listOfFiles[i].getName() );
          String file_num = listOfFiles[i].getName().substring(
                                listOfFiles[i].getName().length() - 7);
          println(file_num);
          //println(id);
          id = -1 + ((Character.getNumericValue(file_num.charAt(0))*10)+Character.getNumericValue(file_num.charAt(1)));
          //println(id);
          String temp_ext = listOfFiles[i].getName().substring(
                                listOfFiles[i].getName().length() - 4);
          if ( (temp_ext.equals(".JPG") || temp_ext.equals(".jpg") || temp_ext.equals(".png")) && (id < img_buffer_size) /*&& (id > 0)*/ ) {       
            images[id] = dataPath(folder_name)+"/"+listOfFiles[i].getName();
            println(id);
            println(images[id]);
            img_loaded += 1;
            id += 1;
          }
        } else if (listOfFiles[i].isDirectory()) {
          System.out.println("Directory " + listOfFiles[i].getName());
        }
      }
    }
  } catch (Exception e) {
    e.printStackTrace();
    
    /* Back up pics
    int id = 0;
    String[] lista = { "img/IMG_0192.JPG", 
                      "img/IMG_0193.JPG", 
                      "img/IMG_0194.JPG",  
                      "img/IMG_0195.JPG",
                      "img/IMG_0196.JPG", 
                      "img/IMG_0197.JPG",  
                      "img/IMG_0198.JPG", 
                      "img/IMG_0202.JPG", 
                      "img/IMG_0203.JPG",  
                      "img/IMG_0204.JPG",  
                      "img/IMG_0205.JPG", 
                      "img/IMG_0206.JPG", 
                      "img/IMG_0207.JPG", 
                      "img/IMG_0210.JPG",
                      "img/IMG_0216.JPG", 
                      "img/IMG_0217.JPG", 
                      "img/IMG_0218.JPG", 
                      "img/IMG_0220.JPG",
                      "img/IMG_0227.JPG", 
                      "img/IMG_0228.JPG", 
                      "img/IMG_0229.JPG",  
                      "img/IMG_0248.JPG" };
    for (final String name : lista){
      //println (id);
      if (!  (id < img_buffer_size) ) break;
      if ( (id < img_buffer_size)  && (images[id] == null)) {
        images[id] = name;
        img_loaded += 1;
        id += 1;
      }  
    }
    */
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

void draw() {
  short time = (short)(millis() % 1000);
  
  if((div1000/2) >= (time % (int)div1000)) { 
    
    if(!mousePressed) {
      play_img(trigger_thresh);
      if ( (key == 'c')||(key == 'C')||(key == '0') ) {
        clear = true;
      } else if ( (key == 'v')||(key == 'V') ) {
        clear = true;
      } else {
        clear = false;
      }
    } 
    
    if (null != acanvas) { 
      acanvas = input_sketcher.draw(0, 0, clear);
      acanvas.tint(255, 207); 
      pushMatrix();
      translate(width,0);
      scale(-1.0,1.0);
      image(acanvas, 0, acanvas.height/2);
      //tint(255, 63); 
      //image(bcanvas, 0, -12, bcanvas.width, height+24);
      noTint();
      popMatrix();
      image(acanvas, 0, acanvas.height/2);
      tint(255, 207); 
      image(bcanvas, -3, -7, width+6, height+12);
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
    
    if (keypush != char(0)) {
      //text(keypush, 0, height - 8);
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
      clear = true;
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

void keyPressed() {
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

void keyReleased()
{
  if ( (key == 'r')||(key == 'R') ) 
  {
    rec_name = "phono" + str(year() + month() + day() + hour() + minute());
    saveFrame("data/rec/"+rec_name+"####.jpg");
  }
  
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

void mouseMoved()
{
}

void load_images(short img_index, String img_cur) {
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

void play_img(int pb_dec) {
  float sample_min = .01;
  float sample_max = .01;
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
    //clear = true;
  }
  
  //fill(0);
  //rect(width - 32, height - 24, 32, 32);
  //fill(255);
  //text(smp_range, width - 32, height - 8); 
}

void playMode(String mode) {
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
    

void stop() {
  in.close();
  groove.close();
  minim.stop();
  super.stop();  
}
