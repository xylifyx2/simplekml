/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.profundo.drfeeds;

import dk.profundo.drpodcasts.DRPodcasts;
import dk.profundo.drpodcasts.ItemListener;
import dk.profundo.drpodcasts.PodCastItem;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.jdesktop.swingworker.SwingWorker;
import org.xml.sax.SAXException;

/**
 *
 * @author ermh
 */
public class FeedTableModel extends DefaultListModel {

    public FeedTableModel() {
       
    }

    public void load(JComponent c) {
        try {
            loadDRFeedList();
        } catch (Exception ex) {
           JOptionPane.showMessageDialog(c, ex.toString());
        }
    }

    protected void loadDRFeedList() throws SAXException, ParserConfigurationException, IOException {
        new SwingWorker<Void, PodCastItem>() {

            @Override
            protected Void doInBackground() throws Exception {
                ItemListener il = new ItemListener() {

                    public void onItem(PodCastItem item) {
                        p(item);
                    }
                };

                DRPodcasts.loadDRFeedList(il);
                return null;
            }
            PodCastItem[] singleton = new PodCastItem[1];

            private void p(PodCastItem item) {
                publish(item);
            }

            @Override
            protected void done() {
                super.done();
            }

            @Override
            protected void process(List<PodCastItem> list) {
                addToTableModel(list);
            }
            
            
        }.execute();
    }

    protected void addToTableModel(List<PodCastItem> list) {
        for (PodCastItem i : list) {
            insertElementAt(rowData(i), size());
        }
    }

    protected Object rowData(PodCastItem i) {
        return i;
    }
}
