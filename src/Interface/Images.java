package Interface;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Images {
	private static File MAP_IMAGE_PATH = new File("res\\images\\map1.png");
	public static Image MAP_IMAGE;
	
	private static File AIR_PLANE_IMAGE_PATH = new File("res\\images\\airplane.png");
	public static Image AIR_PLANE_IMAGE;
	
	
	
	public static void loadImages() {
		try {
			MAP_IMAGE = ImageIO.read(MAP_IMAGE_PATH);
			AIR_PLANE_IMAGE = ImageIO.read(AIR_PLANE_IMAGE_PATH);
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}
}
