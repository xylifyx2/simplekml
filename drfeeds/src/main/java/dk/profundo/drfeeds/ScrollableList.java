/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.profundo.drfeeds;

import javax.swing.JList;
import javax.swing.Scrollable;

/**
 *
 * @author ermh
 */
public class ScrollableList extends JList implements Scrollable {

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }
    
}
