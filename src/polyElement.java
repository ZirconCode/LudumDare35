import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;


public class polyElement extends Element{

	public Color clr;
	public Polygon p;
	
	public polyElement()
	{
		
	}
	
	public void render(Graphics g)
	{
		g.setColor(clr);
		g.fillPolygon(p);
	}
	
	public void tick(GameState s)
	{
		
	}
	
}
