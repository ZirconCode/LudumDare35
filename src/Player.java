import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;


public class Player extends Element{

	public double x,y;
	
	public int mini;
	
	public Polygon p;
	public Polygon origin;
	
	public GameState state;
	
	public int collBad = 0;
	public int collGood = 0;
	
	public boolean frozenFlag;
	public boolean frozen;
	
	public Player(GameState s)
	{
		state = s;
		
		p = new Polygon();
		// square
//		p.addPoint(0, 0);
//		p.addPoint(50, 0);
//		p.addPoint(50, 50);
//		p.addPoint(0, 50);
		
		x = 100;
		y = 100; // TODO gamestate.height...
		
		// hexagon
		p.addPoint(25,-43);
				p.addPoint(-25,-43);
						p.addPoint(-50,0);
								p.addPoint(-25,43);
										p.addPoint(25,43);
												p.addPoint(50,0);
		
		// octagon
//		p.addPoint(38,-92);
//		p.addPoint(-38,-92);
//		p.addPoint(-92,-38);
//		p.addPoint(-92,38);
//		p.addPoint(-38,92);
//		p.addPoint(38,92);
//		p.addPoint(92,38);
//		p.addPoint(92,-38);
		
		origin = new Polygon();
		for(int i = 0; i<p.npoints; i++)
			origin.addPoint(p.xpoints[i], p.ypoints[i]);
	}
	
	public void render(Graphics g)
	{
		
		
		
		
        g.setColor(Color.RED);
        g.drawLine(p.xpoints[mini], p.ypoints[mini], state.mouseCoordX, state.mouseCoordY);
        
        //
        g.setColor(Color.CYAN);
        for(int i = 0; i<p.npoints; i++)
        {
        	// looks cool as hell but not really useful, but gives idea
        	// correlate distance of point with larger circle -> gameplay?
        		//g.drawOval(p.xpoints[i]-5, p.ypoints[i]-5, p.xpoints[i]+10, p.xpoints[i]+10);
        	
        	// also totally cool
        	//        	int diff = (int)(dist(origin.xpoints[i],origin.ypoints[i],p.xpoints[i],p.ypoints[i]))/2;
        	//        	g.drawOval(p.xpoints[i]-diff, p.ypoints[i]-diff, 2*diff, 2*diff);
        	int diff = 50;
        	g.drawOval(p.xpoints[i]-diff, p.ypoints[i]-diff, 2*diff, 2*diff);
        }
        
       
        g.setColor(Color.pink);
		if(state.mouseDown) g.setColor(Color.blue);
		//g.fillRect((int)x, (int)y, 50, 50);
		g.fillPolygon(p);
		
		g.setColor(Color.white);
        g.drawString("Vol:"+(int)area(p)+" Dist: "+(int)distMorphed(), (int)x, (int)y);
        
        // TODO lighten and make look cooler....
        g.setColor(Color.gray);
        drawCosshair(state.mouseCoordX, state.mouseCoordY, g);
        g.setColor(Color.ORANGE);
        drawCosshair(p.xpoints[mini], p.ypoints[mini], g);
        
        
        	g.setColor(Color.red);
        	g.drawString("CollGood:"+collGood+"Bad:"+collBad,(int)x,(int)y+20);
       
        
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
		if(state.keyDown[KeyEvent.VK_F] )
		{
			if(!frozenFlag)
			{
				frozen = !frozen;
				frozenFlag = true;
			}
		} else frozenFlag = false;
		
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
		double morphSpeed = 3;
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
		else if(!frozen) 
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
			
		double speed = 1.7;
		if(state.keyDown[KeyEvent.VK_S])
			y += speed;
		if(state.keyDown[KeyEvent.VK_W])
			y += -speed;
		if(state.keyDown[KeyEvent.VK_D])
			x += speed;
		if(state.keyDown[KeyEvent.VK_A])
			x += -speed;
		
		

		p.translate((int)x, (int)y);
		// working beyond origin
		
		// !!!!! TODO check bounds
		if((p.getBounds().x < 0 || p.getBounds().x > state.width || p.getBounds().y < 0 || p.getBounds()>state.height))
		{
			// TODO revert changes
			// -> morphing
			// -> moving
		}
		
		// collision
		collGood = 0;
		collBad = 0;
		for(int i = 0; i<state.elements.size(); i++)
			if(state.elements.get(i).getClass() == polyElement.class)
			{
				polyElement enemy = (polyElement)state.elements.get(i);
				Area a = new Area(enemy.p);
				Area b = new Area(p);
				a.intersect(b);
				if(!a.isEmpty())
				{
					if(enemy.good) collGood += 1;
					else collBad +=1;
				}
				//	collision = true; // !!! TODO...
			}

			
		//p.xpoints[0] += 1;
	}
	
	public double dist(double x, double y, double x2, double y2)
	{
		return Math.hypot(x-x2, y-y2);
	}
	
	public double distMorphed()
	{
		// TODO square/root it?
		p.translate(-(int)x, -(int)y); // TODO...
		double result = 0;
		for(int i = 0; i<p.npoints; i++)
		{
			result += dist(origin.xpoints[i],origin.ypoints[i],p.xpoints[i],p.ypoints[i]);
		}
		p.translate((int)x, (int)y);
		return result;
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
		return result/2; // -1*
	}

	
}
