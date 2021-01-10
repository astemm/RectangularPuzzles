package principal;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

public class PuzButton extends JButton {
	
	static int clicked;

	public PuzButton() {
        super();
        init();
    }
	
	public PuzButton(Image image) {
        super(new ImageIcon(image));
        init();
    }
	
	public static int getClicked() {
		return clicked;
	}

	public static void setClicked(int clicked) {
		PuzButton.clicked = clicked;
	}
	
	 private void init() {
		 BorderFactory.createLineBorder(Color.gray);
		 addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseEntered(MouseEvent e) {
	                setBorder(BorderFactory.createLineBorder(Color.yellow));
	            }
	            
	            @Override
	            public void mouseClicked(MouseEvent e) {
	            	if (((MainWindow)getParent().getParent().getParent().getParent().getParent()).isAutoAssembled)
	            	{((MainWindow)getParent().getParent().getParent().getParent().getParent()).lblValid.setText("The Puzzles is set automatically"); return;}
	            	
	            	if (getIcon()==null && getParent().getName().equals("midPane")) {setIcon(((MainWindow)getParent().getParent().getParent().getParent().getParent()).getBufferedIcon());
	            	((MainWindow)getParent().getParent().getParent().getParent().getParent()).setBufferedIcon(null);
	            	System.out.println("true"); JButton button=(JButton)((JComponent)getParent().getParent()).getClientProperty("current");
	            	button.setIcon(null); button.setContentAreaFilled(false); clicked++; }
	                else if (getIcon()!=null) { System.out.println("true");
	                	((MainWindow)getParent().getParent().getParent().getParent().getParent()).setBufferedIcon(getIcon());
	                	((JComponent)getParent().getParent()).putClientProperty("current",e.getComponent());
	                }
	            }
	            
	            @Override
	            public void mouseExited(MouseEvent e) {
	            	System.out.println(getParent().getName());
	            	System.out.println(((MainWindow)getParent().getParent().getParent().getParent().getParent()).getBufferedIcon());
	            	setBorder(null);
	            }
	        });
	    }

}
