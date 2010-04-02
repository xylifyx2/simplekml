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
	public PodCastItem() {
		
	}
	
	public PodCastItem(String link, String feed, String title,
			String description) {
		super();
		this.link = link;
		this.feed = feed;
		this.title = title;
		this.description = description;
	}

	public String link;
	public String feed;
	public String title;
	public String description;

	public String toString() {
		return "Item[" + title + "," + link + "," + feed + //
				"," + description + "]";
	}
}
