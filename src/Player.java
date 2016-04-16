import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyEvent;


public class Player extends Element{

	public double x,y;
	
	public int mini;
	
	public Polygon p;
	public Polygon origin;
	
	public GameState state;
	
	public Player(GameState s)
	{
		state = s;
		
		p = new Polygon();
		// square
//		p.addPoint(0, 0);
//		p.addPoint(50, 0);
//		p.addPoint(50, 50);
//		p.addPoint(0, 50);
		// hexagon
		
		origin = new Polygon();
		for(int i = 0; i<p.npoints; i++)
			origin.addPoint(p.xpoints[i], p.ypoints[i]);
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.pink);
		if(state.mouseDown) g.setColor(Color.blue);
		//g.fillRect((int)x, (int)y, 50, 50);
		
		g.fillPolygon(p);
		
		g.setColor(Color.white);
        g.drawString("V:"+area(p), (int)x, (int)y);
		
        g.setColor(Color.RED);
        g.drawLine(p.xpoints[mini], p.ypoints[mini], state.mouseCoordX, state.mouseCoordY);
        
        // TODO lighten and make look cooler....
        g.setColor(Color.gray);
        drawCosshair(state.mouseCoordX, state.mouseCoordY, g);
        g.setColor(Color.ORANGE);
        drawCosshair(p.xpoints[mini], p.ypoints[mini], g);
        
	}
	
	public void drawCosshair(int x, int y, Graphics g)
	{
        g.drawLine(0, y, x, y);
        g.drawLine(x, 0, x, y);
        g.drawLine(x, y, state.width, y);
        g.drawLine(x, y, x, state.height);
	}
	
	public void tick(GameState s)
	{
		p.translate(-(int)x, -(int)y);
		double xMo = (state.mouseCoordX-x);
		double yMo = (state.mouseCoordY-y);
		// working from origin
		
		// idea: find nearest point and move it closer to mouse
		
		// find nearest
		mini = 0;
		double min = dist(p.xpoints[mini],p.ypoints[mini],xMo,yMo);
		for(int i = 0; i<p.npoints; i++)
		{
			double curr = dist(p.xpoints[i],p.ypoints[i],xMo,yMo);
			if(min > curr)
			{
				mini = i;
				min = curr;
			}
		}
		//System.out.println("min:"+mini);
		
		// TODO only if mouse outside poly
		double morphSpeed = 2;
		if(state.mouseDown)
		{
			
			// warp nearest towards mouse
			double diff = dist(xMo,yMo,p.xpoints[mini],p.ypoints[mini]);
			if(diff < morphSpeed)
			{
				p.xpoints[mini] = (int)xMo;
				p.ypoints[mini] = (int)yMo;
			}
			else
			{
				// PROBLEM: Java uses ints in polygon..... why.....
				double x = morphSpeed* (p.xpoints[mini]-xMo)/diff;
				double y = morphSpeed* (p.ypoints[mini]-yMo)/diff;
			
				p.xpoints[mini] += -x;
				p.ypoints[mini] += -y;
				//System.out.println("diff:"+diff+"x"+x+"y"+y);
			}
			//System.out.println("diff:"+diff);
			
		}
		else 
		{
			// reflow to original poly
			for(int i = 0; i<p.npoints; i++)
			{
				double diff = dist(origin.xpoints[i],origin.ypoints[i],p.xpoints[i],p.ypoints[i]);
				//Math.signum()
				if(diff < morphSpeed)
				{
					p.xpoints[i] = origin.xpoints[i];
					p.ypoints[i] = origin.ypoints[i];
				}
				else
				{
					// normalize direction vector
					double x = morphSpeed*(origin.xpoints[i]-p.xpoints[i])/diff;
					double y = morphSpeed*(origin.ypoints[i]-p.ypoints[i])/diff;
					p.xpoints[i] += x;
					p.ypoints[i] += y;
				}
					
			}
		}
			
			
		
		// have an original we slowly change back to
				
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
	
	public double dist(double x, double y, double x2, double y2)
	{
		return Math.hypot(x-x2, y-y2);
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
