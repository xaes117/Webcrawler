import org.openqa.selenium.WebElement;

import java.util.LinkedList;

/**
 * Created by nathanliu on 26/07/2016.
 */
public class Cluster {

    private LinkedList<WebElement> cluster;
    private int clusterID;

    public Cluster(int clusterID) {
        this.cluster = new LinkedList<>();
        this.clusterID = clusterID;
    }

    public LinkedList<WebElement> getCluster() {
        return cluster;
    }

    public void setCluster(LinkedList<WebElement> cluster) {
        this.cluster = cluster;
    }

    public int getClusterID() {
        return clusterID;
    }

    public void add(WebElement e) {
        cluster.add(e);
    }
}
