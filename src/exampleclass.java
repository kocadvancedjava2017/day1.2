import java.awt.*;

/**
 * Created by IGMAdmin on 7/27/2017.
 */
public class exampleclass {

    int x;
    int y;

    public exampleclass(int ax, int ay){
        this.x = ax;
        y = ay;
    }


    public void draw(Graphics2D g){
        g.setColor(Color.red);
        g.fillRect(x, y, 100, 100);
    }

}
