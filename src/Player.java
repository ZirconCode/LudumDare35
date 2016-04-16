import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyEvent;


public class Player extends Element{

	public double x,y;
	
	public Polygon p;
	
	public GameState state;
	
	public Player(GameState s)
	{
		state = s;
		
		p = new Polygon();
		p.addPoint(0, 0);
		p.addPoint(50, 0);
		p.addPoint(50, 50);
		p.addPoint(0, 50);
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.pink);
		if(state.mouseDown) g.setColor(Color.blue);
		//g.fillRect((int)x, (int)y, 50, 50);
		
		g.fillPolygon(p);
		
		g.setColor(Color.white);
        g.drawString("V:"+area(p), (int)x, (int)y);
		
	}
	
	public void tick(GameState s)
	{
		p.translate(-(int)x, -(int)y);
		// working from origin
		
		// idea: find nearest point and move it closer to mouse
		
		if(state.mouseDown)
			p.xpoints[1] += 2;
		else 
			p.xpoints[1] = Math.max(Math.abs(p.xpoints[1] - 2), 50);
				
				
		if(state.keyDown[KeyEvent.VK_S])
			y += 1;
		if(state.keyDown[KeyEvent.VK_W])
			y += -1;
		if(state.keyDown[KeyEvent.VK_D])
			x += 1;
		if(state.keyDown[KeyEvent.VK_A])
			x += -1;

		p.translate((int)x, (int)y);
		// working beyond origin
		
		// TODO check bounds
		

			
		//p.xpoints[0] += 1;
	}
	
	// thanks math openref <3
	public double area(Polygon poly)
	{
		double result = 0;
		int j = poly.npoints-1;
		for(int i=0; i<poly.npoints; i++)
		{
			result = result +  (poly.xpoints[j]+poly.xpoints[i]) * (poly.ypoints[j]-poly.ypoints[i]); 
			j = i;  //j is previous vertex to i
		}
		return -1*result/2;
	}

	
}
