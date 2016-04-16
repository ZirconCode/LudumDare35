
/*
 * 	Basic Game Applet Structure
 * 
 *  Version: 	1.0
 *  Author: 	Simon Gruening / ZirconCode
 */

import java.applet.*; 
import java.awt.*; 
import java.awt.event.*; 
  
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
		
		polyElement enem = new polyElement();
		Polygon poly = new Polygon();
		poly.addPoint(0, 24);
		poly.addPoint(50, 0);
		poly.addPoint(80, 50);	
		poly.addPoint(0, 70);
		poly.translate(200, 200);
		enem.p = poly;
		enem.clr = Color.green;
		state.elements.addElement(enem);
		// --
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