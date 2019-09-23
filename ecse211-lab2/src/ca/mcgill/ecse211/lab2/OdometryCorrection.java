package ca.mcgill.ecse211.lab2;

import static ca.mcgill.ecse211.lab2.Resources.*;
import lejos.hardware.Sound;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class OdometryCorrection implements Runnable {
  
private static final long CORRECTION_PERIOD = 10;
private Odometer odometer = Odometer.getOdometer();
private static EV3ColorSensor colorSensor = Resources.colorSensor;  //Activate color sensor
private static SampleProvider colorSample;  // retrieve color samples
private float[] colorValue; //The color samples will be stored in the colorValue array
public int counterX = 0;    
public int counterY = 0;
public double updateX = 0;
public double updateY = 0;
private static double NORTH = 1.5;  // threshold of theta value to determine robot is heading north
private static double EAST = 91;    // threshold of theta value to determine robot is heading east
private static double SOUTH = 181;  // threshold of theta value to determine robot is heading south
private static double WEST = 271;   // threshold of theta value to determine robot is heading west

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
      
      colorSample.fetchSample(colorValue, 0); //Retrieve a color sample and store it at position 0 in the colorValue array
      if(colorValue[0] <= 0.2){   //If a black line is crossed
       
       if(odometer.getXYT()[2] < NORTH){ //going straight
            counterY++; //increase counter for y when robot crosses a line
            Sound.beep();
             updateY = TILE_SIZE * counterY;    //calculate the vertical distance traveled with respect to origin
              odometer.setY(updateY);   //Update y position when sensor crosses a black line while going straight
        }
        else if(odometer.getXYT()[2]>NORTH && odometer.getXYT()[2]<EAST){ //going right
            counterX++; //increase counter for x when robot crosses a line
            Sound.beep();
            updateX = TILE_SIZE * counterX; //calculate the horizontal distance traveled with respect to origin
            odometer.setX(updateX); //Update x position when sensor crosses a black line while going right
        }
        else if(odometer.getXYT()[2]<SOUTH){ //going backwards
            Sound.beep();
            updateY = TILE_SIZE * counterY;
            odometer.setY(updateY); //Update y position when sensor crosses a black line while going backwards
            counterY--; 
        }
        else if(odometer.getXYT()[2]<WEST){ //going left
            Sound.beep();
            updateX = TILE_SIZE * counterX;
            odometer.setX(updateX); //Update x position when sensor crosses a black line while going left
            counterX--;
            
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
