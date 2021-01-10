package principal;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;

public class Utility {
	
	public static List<Image> getPuzzles() {
		List<Image> puzzles=new ArrayList<>();
		try{
		File folder=new File("./PicturesOfPuzzles");
		File[] listOfFiles=folder.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lcName=name.toLowerCase();
				if (lcName.endsWith(".jpg")) {
					return true;
				}
				else {
					return false;
				}
			}
		});
	    List<File> list=Arrays.asList(listOfFiles);
	    Collections.shuffle(list);
		for (File f:list) {
			BufferedImage image = ImageIO.read(f);	
			puzzles.add(image);
		}
		}
		catch(IOException ex) {};
		return puzzles;
	}
	
	public static BufferedImage resizeImage2(BufferedImage original, int targetWidth) {
	int width = original.getWidth();
	int height = original.getHeight();
	double ratio=(double)height/width;
	System.out.println(width+" "+height+" "+ratio);
	int targetHeight=(int)(targetWidth*ratio);
	System.out.println(targetWidth+" "+targetHeight);
	BufferedImage resized = new BufferedImage(targetWidth, targetHeight, original.getType());
	Graphics2D g2 = resized.createGraphics();
	g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	g2.drawImage(original, 0, 0, targetWidth, targetHeight, 0, 0, original.getWidth(),
	    original.getHeight(), null);
	g2.dispose();
	return resized;
	}
	
	public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth) {
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		double ratio=(double)height/width;
		System.out.println(width+" "+height+" "+ratio);
		int targetHeight=(int)(targetWidth*ratio);
		System.out.println(targetWidth+" "+targetHeight);
	    BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics2D = resizedImage.createGraphics();
	    graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
	    graphics2D.dispose();
	    return resizedImage;
	}
	
	private static File getRandomImage() {
		File folder=new File("./PicturesOfPuzzles/originals");
		File[] listOfFiles=folder.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lcName=name.toLowerCase();
				if (lcName.endsWith(".jpg")) {
					return true;
				}
				else {
					return false;
				}
			}
		});
		Random r=new Random();
		int index=r.nextInt(listOfFiles.length);
		System.out.println(listOfFiles[index].getName());
		return listOfFiles[index];
	}
	
	
	public static void disSectImage() {
		List<Entity> entities=new ArrayList<>();
		try {
		int row = 4;
		int col = 3;
		//BufferedImage initialImage = ImageIO.read(new File("C:/Pictures/originals/IMAGE.jpg"));
		BufferedImage initialImage = ImageIO.read(getRandomImage());
		BufferedImage image;
		image=resizeImage2(initialImage,250);
		int width = image.getWidth();
		int height = image.getHeight();
		System.out.println("Image Size: " + width + "x" + height);
		int dWidth = width / col;
		int dHeight = height / row;
		int x = 0;
		int y = 0;
		int count=1;
		for (int i = 0; i < row; i++) {//row
			y = 0;
			for (int j = 0; j < col; j++) {//col
				try {
					System.out.println("creating piece: "+i+" "+j);
					System.out.println(x+":"+y+"-"+dWidth+":"+dHeight);
					BufferedImage subImage = image.getSubimage(y, x, dWidth, dHeight); //y,x
					File outputfile = new File ("./PicturesOfPuzzles/IMAGE"+count+".jpg");
					count++;
					entities.add(new Entity(subImage,outputfile));
					y += dWidth;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			x += dHeight;
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
		try {
		for(Entity e:entities) {ImageIO.write((BufferedImage)e.im0, "jpg", e.f0);
		System.out.println("shuffled piece: "+e.f0.getName());} 
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
