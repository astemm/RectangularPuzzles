package principal;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.awt.event.ActionEvent;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.JLabel;

public class MainWindow extends JFrame {

	private Validator validator;
	private Builder builder;
	private List<Image> fragments;
	private Icon bufferedIcon;
	private JPanel initPanel;
	private JPanel midPanel;
	JScrollPane scrollPane;
	int w,h;
	boolean isAutoAssembled;
	JLabel lblValid;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}
	
	public void setBufferedIcon(Icon icon) {
		this.bufferedIcon=icon;
	}
	
	public Icon getBufferedIcon() {
		return bufferedIcon;
	}
	
	
	private void initMosaics(List<Image> fragments) {
		BufferedImage im=(BufferedImage)fragments.get(0);
		w=im.getWidth(); h=im.getHeight();
		System.out.println(im.getWidth()+" "+im.getHeight());
		if (initPanel!=null) getContentPane().remove(initPanel);
		initPanel = new JPanel();
		initPanel.setName("initPane");
		initPanel.setBounds(32, 86, 3*w, 4*h);
		getContentPane().add(initPanel);
		initPanel.setLayout(new GridLayout(4, 3));
		for(Image image:fragments) {
			PuzButton btn = new PuzButton(image);
			initPanel.add(btn);
		}
		initPanel.validate();
		getContentPane().repaint();
	}
	
	private void createMainStage() {
		this.isAutoAssembled=false;
		Component c1=null;
		for (Component c:getContentPane().getComponents()) {
			if (c.getName()!=null) {if (c.getName().equals("buildPane")) c1=c;}
		}
		System.out.println("c1:"+c1);
		//if (c1!=null) getContentPane().remove(c1);
		if(midPanel!=null) {getContentPane().remove(midPanel);}
		Optional<Component> bPane=Stream.of(getContentPane().getComponents()).filter(e->e.getName()!=null&&e.getName().equals("buildPane")).findFirst();
		System.out.println(bPane);
		if (bPane.isPresent()) {getContentPane().remove(bPane.get());
		System.out.println("bbbbbbbbbbbbbbbb");
		}
		midPanel=new JPanel();
		midPanel.setName("midPane");
		midPanel.setBounds(52+3*w, 46, 5*w, 6*h);
		getContentPane().add(midPanel); 
		midPanel.setLayout(new GridLayout(6, 5));
		//midPanel.setBorder(BorderFactory.createLineBorder(Color.cyan));
		for(int i=0;i<30;i++) {PuzButton btn = new PuzButton();
		btn.setContentAreaFilled(false); 
		midPanel.add(btn);}
		midPanel.validate();
		
	}
	
	private JPanel creatSecondStage() {
		JPanel buildPanel=new JPanel();
		buildPanel.setName("buildPane");
		buildPanel.setBounds(52+3*w, 46, 5*w, 6*h);
		getContentPane().add(buildPanel);
		buildPanel.setLayout(new GridLayout(6, 5));
		//midPanel.setBorder(BorderFactory.createLineBorder(Color.cyan));
		for(int i=0;i<30;i++) {JButton btn = new JButton();
		btn.setContentAreaFilled(false);
		//btn.setBorder(null);
		buildPanel.add(btn);}
		buildPanel.validate();
		return buildPanel;
	}
	
	private PuzButton[] getButtons() {
		Component[] components=initPanel.getComponents();
		PuzButton[] buttons=new PuzButton[components.length];
		int i=0;
		for(Component c:components) {  //Convert Components to JButtons
			PuzButton button=(PuzButton)(JButton)(JComponent)c;
			buttons[i]=button;
			i++;
		}
		System.out.println(buttons.length);
		return buttons;
	}
	
	
	private JButton[] cloneButtons(JButton[] buttons) {
		JButton[] newButtons=new JButton[buttons.length];
		//new PuzButton[buttons.length];
		int l=0;
		for(JButton jb:buttons) {  ImageIcon icon=(ImageIcon)jb.getIcon();
			JButton b=new JButton(icon);
			b.setBorder(null);
			newButtons[l]=b; l++;
	}
		System.out.println("EQUAL:"+(buttons[0]==newButtons[0]));
		System.out.println("EQUAL2:"+(buttons[0].getIcon().equals(newButtons[0].getIcon())));
		System.out.println(l);
		return newButtons;
	}
	
	
	private boolean isRectangularLayout (JButton[] buttons) {
		int[] sequence={0,1,2,5,6,7,10,11,12,15,16,17}; //right sequence of jbutton indexes
		//if Correct Layout of Images Puzzles start in very upper left corner
		for(int d=0;d<sequence.length;d++) { //d- difference between 0-index and starting index of other puzzles tiles
		if (buttons[sequence[d]].getIcon()!=null) { //0,1,2,5,6,7,10,11,12,15,16,17,20,21,22
			for(int j=1;j<sequence.length;j++) {
				//System.out.println(sequence[j]+sequence[d]);
				if (buttons[sequence[j]+sequence[d]].getIcon()==null) return false;
			}
			break;
		}
		else continue;
		}
		return true;
	}
	
	private boolean validatePuzllesLayout() {
		boolean isCorrectLayout=false;
		
		Component[] components=midPanel.getComponents();
		JButton[] buttons=new JButton[components.length];
		int imageButtons=0;
		int i=0;
		for(Component c:components) {  //Convert Components to JButtons
			JButton button=(JButton)(JComponent)c;
			buttons[i]=button;
			i++;
		}
		
		imageButtons=(int)Stream.of(buttons).filter(e->e.getIcon()!=null).count();//count the manually pulled over tiles to second stage 
		System.out.println("IMMMBTTN:"+imageButtons);
		if (imageButtons<12) return false;
		
		int ind=0;
		int[] seqs={0,1,2,5,6,7,10,11,12,15,16,17};
		for(int d=0;d<seqs.length;d++) { //d- difference between 0-index and starting index of other puzzles tiles
			if (buttons[seqs[d]].getIcon()!=null) {ind=seqs[d];
		System.out.println("index: "+ind);break;}
		}
		
		isCorrectLayout=isRectangularLayout(buttons); //is final image of rectangular non-empty layout of 12 tiles
		System.out.println("CorrectLayout:"+isCorrectLayout);
		
		JButton[] fullButtons=new JButton[12]; //final non-empty jbutton array 3*4 manually composed
		for(int t=0; t<fullButtons.length; t++) {
			System.out.println("seqq:"+(ind+seqs[t]));
			fullButtons[t]=buttons[ind+seqs[t]];
		}
		boolean checked=false;
		if (isCorrectLayout) checked=checkEdges2(fullButtons,ind);
		return checked;
	}
	
	private boolean notValidHorizontalEdges(JButton[] buttons, int index) {
		boolean CheckL; boolean CheckR;
		System.out.println("CHECKKK::");
		if(index<8) {
		Edge leftSouth=validator.getEdge(buttons[index],"south"); 
		Edge leftNorth=validator.getEdge(buttons[index+3],"north");
		Edge isClosest=validator.findClosest(leftSouth);
		CheckL=isClosest!=null?(leftNorth.getId()==isClosest.getId()):false;
		System.out.println("CheckLL:"+CheckL+"-"+leftSouth.getId()+":"+leftNorth.getId());
		
		Edge rightSouth=validator.getEdge(buttons[index+1],"south"); 
		Edge rightNorth=validator.getEdge(buttons[index+4],"north");
		isClosest=validator.findClosest(rightSouth);
		CheckR=isClosest!=null?(rightNorth.getId()==isClosest.getId()):false;
		System.out.println("CheckRR:"+CheckR+"-"+rightSouth.getId()+":"+rightNorth.getId());}
		else {
		Edge leftNorth=validator.getEdge(buttons[index],"north"); 
		Edge leftSouth=validator.getEdge(buttons[index-3],"south");
		Edge isClosest=validator.findClosest(leftNorth);
		CheckL=isClosest!=null?(leftSouth.getId()==isClosest.getId()):false;
		System.out.println("CheckLL:"+CheckL+"-"+leftNorth.getId()+":"+leftSouth.getId());
		
		Edge rightNorth=validator.getEdge(buttons[index+1],"north"); 
		Edge rightSouth=validator.getEdge(buttons[index-2],"south");
		isClosest=validator.findClosest(rightNorth);
		CheckR=isClosest!=null?(rightSouth.getId()==isClosest.getId()):false;
		System.out.println("CheckRR:"+CheckR+"-"+rightNorth.getId()+":"+rightSouth.getId());}
		
		boolean result=!CheckL && !CheckR;
		System.out.println("ResultSouth:"+index+"-"+result);
		return result;
	}
	
	private boolean notValidVerticalEdges(JButton[] buttons, int index) {
		boolean CheckT; boolean CheckB;
		System.out.println("CHECKKK::");
		if((index+1)%3!=0) {
		Edge topEast=validator.getEdge(buttons[index],"east"); 
		Edge topWest=validator.getEdge(buttons[index+1],"west");
		Edge isClosest=validator.findClosest(topEast);
		CheckT=isClosest!=null?(topWest.getId()==isClosest.getId()):false;
		System.out.println("CheckTT:"+CheckT+"-"+topEast.getId()+":"+topWest.getId());
		
		Edge bottomEast=validator.getEdge(buttons[index+3],"east"); 
		Edge bottomWest=validator.getEdge(buttons[index+4],"west");
		isClosest=validator.findClosest(bottomEast);
		CheckB=isClosest!=null?(bottomWest.getId()==isClosest.getId()):false;
		System.out.println("CheckBB:"+CheckB+"-"+bottomEast.getId()+":"+bottomWest.getId()); }
		else {
			Edge topWest=validator.getEdge(buttons[index],"west"); 
			Edge topEast=validator.getEdge(buttons[index-1],"east");
			Edge isClosest=validator.findClosest(topWest);
			CheckT=isClosest!=null?(topEast.getId()==isClosest.getId()):false;
			System.out.println("CheckTT:"+CheckT+"-"+topWest.getId()+":"+topEast.getId());
			
			Edge bottomWest=validator.getEdge(buttons[index+3],"west"); 
			Edge bottomEast=validator.getEdge(buttons[index+2],"east");
			isClosest=validator.findClosest(bottomWest);
			CheckB=isClosest!=null?(bottomEast.getId()==isClosest.getId()):false;
			System.out.println("CheckBB:"+CheckB+"-"+bottomWest.getId()+":"+bottomEast.getId());
		}
		
		boolean result=!CheckT && !CheckB;
		System.out.println("ResultEast:"+index+"-"+result);
		return result;
	}
	
	private boolean checkEdges2(JButton[] buttons,int index) {
		validator=new Validator(buttons);
		validator.buildEdges();
		int vertErrors=0; int horizErrors=0;
		boolean isVertical=true;
		boolean isHorizontal=true;
		int [] verticals={0,1,3,4,6,7,9,10}; //index of puzzle cells in row except the last cell in it
		//int [] verticals2={0,1,5,6,10,11,15,16};
		for (int cell:verticals) { //for (int cell:verticals) {
			Edge left=validator.getEdge(buttons[cell],"east"); //(buttons[index+cell],"east");
			Edge right=validator.getEdge(buttons[1+cell],"west"); //(buttons[index+1+cell],"west");
			Edge isClosest=validator.findClosest(left);
			//System.out.println("EDGESclosest1:"+left.getId()+":"+right.getId()+"::"+isClosest.getId());
			System.out.println(right==isClosest);
			System.out.println(right.getId()+":"+isClosest);
			boolean Vcheck=isClosest!=null?(right.getId()==isClosest.getId()):false;
			if (!Vcheck) {if (notValidHorizontalEdges(buttons,cell)) vertErrors++;}//{isVertical=false; break;}
			System.out.println("VERTICAL:"+vertErrors);
		}
		
		int [] horizontals={0,1,2,3,4,5,6,7,8}; 
		//int [] horizontals={0,1,2,5,6,7,10,11,12}; //index of puzzle cells in row except last row
		for (int cell:horizontals) {
			Edge top=validator.getEdge(buttons[cell],"south"); //(buttons[index+cell],"south");
			Edge bottom=validator.getEdge(buttons[3+cell],"north"); //(buttons[index+5+cell],"north");
			Edge isClosest=validator.findClosest(top);
			//System.out.println("EDGESclosest2:"+top.getId()+":"+bottom.getId()+"::"+isClosest.getId());
			System.out.println(bottom==isClosest);
			boolean Hcheck=isClosest!=null?(bottom.getId()==isClosest.getId()):false;
			if (!Hcheck) {if (notValidVerticalEdges(buttons,cell)) horizErrors++;}//{isHorizontal=false; break;}
			System.out.println("HORIZONT:"+horizErrors);
		}
		System.out.println("Errors:"+vertErrors+"-"+horizErrors);
		isVertical=(vertErrors<1);
		isHorizontal=(horizErrors<1);
		System.out.println("CheckMANUALS:"+isVertical+"-"+isHorizontal);
		return (isVertical && isHorizontal);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setBounds(100, 100, 750, 650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		JButton btnCutUpImage = new JButton("CutUp New Image");
		btnCutUpImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Utility.disSectImage();
			}
		});
		btnCutUpImage.setBounds(147, 11, 137, 23);
		getContentPane().add(btnCutUpImage);
		
		JButton btnDisplayPuzzles = new JButton("Display Puzzles");
		btnDisplayPuzzles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PuzButton.setClicked(0);
				fragments=Utility.getPuzzles();
				System.out.println(fragments.size());
				initMosaics(fragments);
				createMainStage();
				lblValid.setText("");
				}
		});
		btnDisplayPuzzles.setBounds(147, 52, 137, 23);
		getContentPane().add(btnDisplayPuzzles);
		
		JButton btnValidatePuzzle = new JButton("Validate Puzzle");
		btnValidatePuzzle.setBounds(124, 496, 137, 23);
		btnValidatePuzzle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isAutoAssembled) return;////
				boolean isCorrect=validatePuzllesLayout();
				if (isCorrect) lblValid.setText("Puzzles assembled correctly -- "+PuzButton.getClicked()+"clicks");
				else lblValid.setText("Puzlles assembled incorrectly");
				System.out.println("IsCorrect: "+isCorrect);
				}
		});
		getContentPane().add(btnValidatePuzzle);
		
		JButton btnAssembleAutomatically = new JButton("Assemble Automatically");
		btnAssembleAutomatically.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(JButton jb:getButtons()) {if (jb.getIcon()==null) {
					lblValid.setText("Manual composition started. Redisplay image");
					return;
				   }
				}
				getContentPane().remove(midPanel);
				builder=new Builder(cloneButtons(getButtons()),creatSecondStage()); //midPanel
				builder.buildEdges();
				builder.traverseEdges();
				isAutoAssembled=true;
			}
		});
		btnAssembleAutomatically.setBounds(124, 555, 172, 23);
		getContentPane().add(btnAssembleAutomatically);
		lblValid = new JLabel("");
		lblValid.setBounds(124, 530, 283, 25);
		getContentPane().add(lblValid);
	}
}
