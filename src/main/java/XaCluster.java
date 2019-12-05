import QB.quantumbiology.HtmlClustering;
import com.google.common.collect.MapDifference;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by nathanliu on 26/07/2016.
 */
public class XaCluster {

    private List<Element> OriginalClusters = new ArrayList<>();
    private List<Element> ValidClusters = new ArrayList<>();
    private List<String> compareList;
    private double thresholdValue = 0.25;

    public XaCluster() {

    }

    public XaCluster(List<String> compareList, double thresholdValue) {
        this.compareList = compareList;
        this.thresholdValue = thresholdValue;
    }

    public List<Element> CreateClusters(String url) {

        ValidClusters.clear();

        try {

            List<Element> ClusterSet = HtmlClustering.getClusterFromUrl(url);
            OriginalClusters = ClusterSet;
            ValidateCluster(ClusterSet);
            //PrintCluster(ValidClusters);
            UpdateList(ValidClusters);

            System.out.println("------------");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ValidClusters;
    }

    public List<Element> CreateClusters(String url, List<String> compareList) {
        ValidClusters.clear();
        try {

            List<Element> ClusterSet = HtmlClustering.getClusterFromUrl(url);
            OriginalClusters = ClusterSet;
            ValidateCluster(ClusterSet, compareList);
            //PrintCluster(ValidClusters);
            UpdateList(ValidClusters);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ValidClusters;
    }

    private void ValidateCluster(List<Element> ClusterSet, List<String> compList) {
        Check(ClusterSet, compList);
    }

    private void ValidateCluster(List<Element> ClusterSet) {
        Check(ClusterSet,Assistant.SubjectList);
    }


    private void Check(List<Element> ClusterSet, List<String> values) {
        for (int i = 0 ; i < ClusterSet.size(); i++) {

            double trueVals = 0;

            List<Element> tags = ClusterSet.get(i).getElementsByAttribute("href");

            for (Element e : tags) {

                if (Assistant.CompareSubject(e.text().toUpperCase(), values)) {

                    trueVals++;
                    double testVal = trueVals/tags.size();

                    if (testVal >= thresholdValue) {

                        ValidClusters.add(ClusterSet.get(i));
                        ClusterSet = RemoveChildren(ClusterSet, ClusterSet.get(i));
                        break;
                    }
                }
            }
        }
    }

    private List<Element> RemoveChildren(List<Element> clusterSet, Element elem) {

        List<Element> removeChildren = HtmlClustering.getClusterFromElement(elem);

        for (Element child : removeChildren) {
            if (clusterSet.contains(child)) {
                clusterSet.remove(child);
            }
        }

        return clusterSet;

    }

    private void PrintCluster(List<Element> list) {

        for (Element a : list) {

            for (Element link : a.getElementsByAttribute("href")) {

                System.out.println(link.text());

            }

        }

    }

    private void UpdateList(List<Element> ClusterSet) {

        for (Element e : ClusterSet) {

            for (Element link : e.getElementsByAttribute("href")) {

                Assistant.SubjectList.add(link.text().toUpperCase());

            }

        }

        Collections.sort(Assistant.SubjectList);

//        for (String s : Assistant.SubjectList) {
//
//            System.out.println(s.toUpperCase());
//
//        }

    }

    public List<Element> getValidClusters() {
        return ValidClusters;
    }

    public List<String> getCompareList() {
        return compareList;
    }

    public List<Element> getOriginalClusters() {
        return OriginalClusters;
    }

    //    public static void ValidateCluster() {
//        for (Cluster cluster : ClusterSet) {
//            double trueVals = 0;
//
//            for (WebElement a : cluster.getCluster()) {
//                if (Assistant.CompareSubject(a.getText().toUpperCase())) {
//                    trueVals++;
//                    double testVal = trueVals/cluster.getCluster().size();
//                    //System.out.println(testVal);
//                    if (testVal >= 0.25) {
//                        ValidClusters.add(cluster);
//                        break;
//                    }
//                }
//            }
//        }
//    }
//
//    public static void UpdateList() {
//        for (Cluster cluster : ValidClusters) {
//            for (WebElement a : cluster.getCluster()) {
//                Assistant.SubjectList.add(a.getText());
//            }
//        }
//
//        Collections.sort(Assistant.SubjectList);
//
//        for (String str : Assistant.SubjectList) {
//            System.out.println(str.toUpperCase());
//        }
//    }
}
