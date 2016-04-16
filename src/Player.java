import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;


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
		p.addPoint(100, 100);
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.pink);
		if(state.mouseDown) g.setColor(Color.blue);
		//g.fillRect((int)x, (int)y, 50, 50);
		
		g.fillPolygon(p);
		
	}
	
	public void tick(GameState s)
	{
		p.xpoints[0] += 1;
	}
	
}
