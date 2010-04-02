package dk.profundo.drfeeds;

import javax.swing.JFrame;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        JFrame f = new JFrame();
        f.setLocationByPlatform(true);
        f.getContentPane().add(new FeedPanel());
        f.pack();
        f.setVisible(true);
    }
}
