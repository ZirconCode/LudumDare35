
/*
 * 	Basic Game Applet Structure
 * 
 *  Version: 	1.0
 *  Author: 	Simon Gruening / ZirconCode
 */

import java.applet.*; 
import java.awt.*; 
import java.awt.event.*; 
import java.util.ArrayList;
import java.util.Random;
  
public class Main extends Applet implements MouseMotionListener, MouseListener, KeyListener, Runnable
{
  
	boolean running;
	Thread UpdaterThread;
	
	Graphics bufferg; 
	Image bufferi;
	Dimension bufferdim; 
	Font MyFont;
	
	int tickTime = 5;
	
	GameState state;
	
	Player p;
	
	// Setup
	
	public void init() 
    { 
   	    
		setSize(800, 600);
		this.resize(800,600);
		
        bufferdim = getSize(bufferdim);
	    bufferi = createImage(bufferdim.width,bufferdim.height); 
	    bufferg = bufferi.getGraphics();
	    
	    setBackground(Color.white);
	    MyFont = new Font("Arial",Font.BOLD,16);
	    addMouseListener(this);
	    addMouseMotionListener(this); 
	    addKeyListener(this);
		
	    state = new GameState();
	    state.height = bufferdim.height;
	    state.width = bufferdim.width;
   	 	gameSetup();
	    
		running = true;
		UpdaterThread = new Thread(this);
   	    UpdaterThread.start();
    }
	
	public void gameSetup()
	{
		// -- Setup Game
		p = new Player(state);
		state.elements.add(p);
		
		Random rand = new Random();
		rand.setSeed(2223221);
		
		
//		for(int i = 0; i<5; i++)
//		{
//			polyElement enem = new polyElement();
//			Polygon poly = new Polygon();
//			poly.addPoint(0, 5);
//			poly.addPoint(5, 0);
//			poly.addPoint(5, 5);
//			for(int j = 0; j<5+rand.nextInt(30); j++)
//			{
//				Point p = new Point(rand.nextInt(200),rand.nextInt(200));
//				while(poly.contains(p.x, p.y))
//				{ // TODO dont allow dead loop xD
//					p = new Point(rand.nextInt(200),rand.nextInt(200));
//				}
//				poly.addPoint(p.x, p.y); // TODO add point at the correct angle between already existing points...
//				// TODO so ugly to generate polygons....
//				// hmmmm
////				poly.
//			}
			
//			poly.addPoint(50, 0);
//			poly.addPoint(80, 50);	
//			poly.addPoint(0, 70);
//			poly.translate(rand.nextInt(state.width), rand.nextInt(state.height));
//			enem.p = poly;
////			Polygon poly = createPoly(rand.nextInt(state.width), rand.nextInt(state.height), 200, 1, 0.7, 20);
//			enem.p = poly;
//			enem.clr = Color.green;
//			state.elements.addElement(enem);
//		}
		for(int i = 0; i<6; i++)
		{
			polyElement enem = new polyElement();
			Polygon poly = new Polygon();
			poly.addPoint(0, 0);
			poly.addPoint(50, 0);
			poly.addPoint(50, 50);	
			poly.addPoint(0, 50);
			poly.translate(rand.nextInt(state.width), rand.nextInt(state.height));
			enem.p = poly;
			enem.clr = Color.green;
			enem.good = true;
			state.elements.addElement(enem);	
		}
		for(int i = 0; i<6; i++)
		{
			polyElement enem = new polyElement();
			Polygon poly = new Polygon();
			poly.addPoint(0, 24);
			poly.addPoint(50, 0);
			poly.addPoint(80, 50);	
			poly.addPoint(0, 70);
			poly.translate(rand.nextInt(state.width), rand.nextInt(state.height));
			enem.p = poly;
			enem.clr = Color.red;
			enem.good = false;
			state.elements.addElement(enem);
			
		}
		// --
	}
	
	
	// Thanks stackoverflow user Mike Ounsworth for original nice code
	// Horrible non-working matlab -> java port....
	// THIS NEEDS TO DIE...
//	public Polygon createPoly(double ctrX, double ctrY, double aveRadius, double irregularity, double spikeyness, double numVerts)
//	{
//	    irregularity = clip( irregularity, 0,1 ) * 2*Math.PI / numVerts;
//	    spikeyness = clip( spikeyness, 0,1 ) * aveRadius;
//
//	    ArrayList<Double> angleSteps = new ArrayList<Double>();
//	    	   double lower = (2*Math.PI / numVerts) - irregularity;
//	    			   double upper = (2*Math.PI / numVerts) + irregularity;
//	    					   double sum = 0;
//	    					   
//	    		for(int i = 0; i<numVerts; i++)
//	    		{
//	    			double tmp = lower+Math.random()*(upper-lower); // TODO
//	    			angleSteps.add(tmp);
//	    	        sum = sum + tmp;
//	    		}
//	    	   // for i in range(numVerts) :
//	    	      //  tmp = random.uniform(lower, upper)
//	    	        
//
//	    	   // # normalize the steps so that point 0 and point n+1 are the same
//	    	   double k = sum / (2*Math.PI);
//	    			   for(int i = 0; i<numVerts; i++)
//	    			   {
//	    				   angleSteps.set(i, angleSteps.get(i)/k);
//	    			   }
//	    	   // for i in range(numVerts) :
//	    	      //  angleSteps[i] = angleSteps[i] / k
//
//	    	    //# now generate the points
//	    			   ArrayList<Point> points = new ArrayList<Point>();
//	    	    double angle = Math.random() * 2*Math.PI;
//	    	    for(int i = 0; i<numVerts; i++)
//	    	    {
//	    	    	//double r_i = clip(aveRadius+Math.random()*spikeyness , 0, 2*aveRadius ); // random.gauss(aveRadius, spikeyness)
//	    	    	double r_i = clip(aveRadius+(Math.random())*spikeyness , 0, 2*aveRadius );
//	    	    			double  x = ctrX + r_i*Math.cos(Math.toRadians(angle));
//	    	    					double   y = ctrY + r_i*Math.sin(Math.toRadians(angle));
//	    	    	        points.add( new Point((int)x,(int)y) );
//
//	    	    	        angle = angle + angleSteps.get(i);
//	    	    }
//	    	    
//	    	  //  for i in range(numVerts) :
//	    	        
//
//	    	  // return points;
//	    	    // construct poly from points
//	    	    Polygon result = new Polygon();
//	    	    for(Point p: points)
//	    	    {
//	    	    	result.addPoint(p.x, p.y);
//	    	    }
//	    	    return result;
//	}
	
	public double clip(double x, double min, double max)
	{
		if(min>max)return x;
		else if (x<min) return min;
		else if (x > max) return max;
		return x;
	} 
	
	// Render
	
    public void update(Graphics g) 
    { 
    	paint(g); 
    }
	
	public void paint(Graphics g) 
    { 
		bufferg.setColor(Color.black);
        bufferg.fillRect(0,0,bufferdim.width,bufferdim.height);
        
        bufferg.setColor(Color.red);
        bufferg.setFont(MyFont);
        bufferg.drawString("hello world", 100, 100);
        bufferg.drawString("w a s d, click mouse to morhp, hold f to freeze", 100, 150);
        
        renderGame(g);
        
        
        
		g.drawImage(bufferi,0,0,this); 
    }
	
	public void renderGame(Graphics g)
	{
		// -- Render Game
		
		for(int i = 0; i<state.elements.size(); i++)
			state.elements.get(i).render(bufferg);
        
        // --
	}
	
	// Game Loop
	
	public void run() 
    { 
         while (running) 
         { 
        	 long nextTick = (System.currentTimeMillis() + tickTime);
        	 
        	 updateGameLogic();
        	
        	 while(nextTick > System.currentTimeMillis()) { /* blergh */ }
        	 repaint();
         }
    }
    
	public void updateGameLogic()
	{
		// -- Update Game State
   	 
   	 	for(int i = 0; i<state.elements.size(); i++)
   	 		state.elements.get(i).tick(state);
   	 
   	 	// --
	}
	
    // Teardown
    
	public void stop() 
    { 
		
    }
    
	public void destroy() 
    { 
		stop();
		running = false; 
		UpdaterThread = null; 
    }
	

	// Input
	
    public void mouseMoved(MouseEvent me)  
    {  
    	state.mouseCoordX = me.getX();
    	state.mouseCoordY = me.getY();
    }
    
    public void mouseDragged(MouseEvent me)  
    { 
    	// TODO!!!
    	state.mouseCoordX = me.getX();
    	state.mouseCoordY = me.getY();
    } 

    public void mouseClicked (MouseEvent me) 
    {

    } 
    
    public void mouseEntered (MouseEvent me) 
    {

    } 
    
    public void mousePressed (MouseEvent me) 
    {
    	state.mouseDown = true;	
    } 
    
    public void mouseReleased (MouseEvent me) 
    {
    	state.mouseDown = false;
    }  
    
    public void mouseExited (MouseEvent me) 
    {

    }

	public void keyPressed(KeyEvent e) {
		state.keyDown[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		state.keyDown[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent arg0) {
		
	}  	
	
}