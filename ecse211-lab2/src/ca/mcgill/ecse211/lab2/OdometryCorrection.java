package ca.mcgill.ecse211.lab2;

import static ca.mcgill.ecse211.lab2.Resources.*;
import lejos.hardware.Sound;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class OdometryCorrection implements Runnable {
  private static final long CORRECTION_PERIOD = 10;
private Odometer odometer = Odometer.getOdometer();

//Activate color sensor
private static EV3ColorSensor colorSensor = Resources.colorSensor;
// retrieve color samples
private static SampleProvider colorSample;
//The color samples will be stored in the colorValue array
private float[] colorValue;

  /*
   * Here is where the odometer correction code should be run.
   */
  public void run() {
    long correctionStart, correctionEnd;

    colorSample = colorSensor.getRedMode();
    colorValue = new float[colorSensor.sampleSize()];
    Sound.setVolume(40000);
  
    while (true) {
      correctionStart = System.currentTimeMillis();
      
      // TODO Trigger correction (When do I have information to correct?)
     
      // TODO Calculate new (accurate) robot position

      // TODO Update odometer with new calculated (and more accurate) values, eg:
      //odometer.setXYT(0.3, 19.23, 5.0);
      
    //Retrieve a color sample and store it at position 0 in the colorValue array
      colorSample.fetchSample(colorValue, 0);
      
      if(colorValue[0] <= 0.5){//If a black line is crossed
        if(odometer.getXYT()[2] < 1){ //going straight
            Sound.beep();
            //Update y position when sensor crosses a black line while going straight
//            odometer.setY();
        }
        else if(odometer.getXYT()[2]>0 && odometer.getXYT()[2]<Math.PI/2){ //going right
            Sound.beep();
            //Update x position when sensor crosses a black line while going right
//            odometer.setX();
        }
        else if(odometer.getXYT()[2]>Math.PI/2 && odometer.getXYT()[2]<Math.PI){//going backwards
            Sound.beep();
            //Update x position when sensor crosses a black line while going backwards
//            odometer.setY();
        }
        else if(odometer.getXYT()[2]-3*Math.PI/2<0){//going left
            Sound.beep();
            //Update x position when sensor crosses a black line while going left
//            odometer.setX();
        }
    }

      // this ensures the odometry correction occurs only once every period
      correctionEnd = System.currentTimeMillis();
      if (correctionEnd - correctionStart < CORRECTION_PERIOD) {
        Main.sleepFor(CORRECTION_PERIOD - (correctionEnd - correctionStart));
      }
    }
  }
  
}
