/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.profundo.drpodcasts;

/**
 *
 * @author ermh
 */
public class PodCastItem {
    String link;
    String feed;
    String title;
    String description;

    public String toString() {
        return "Item["+title+","+link+","+feed+","+description+"]";
    }
}
