package huffman;


import java.util.ArrayList;
import java.util.Hashtable;

public class TreeNode {

    public String nodeName;
    public TreeNode nodeParent;
    public ArrayList<TreeNode> nodeChildren;
    public Hashtable table;

    public TreeNode(String nodeName){
        this.nodeName = nodeName;
        this.nodeParent = null;
        this.nodeChildren = new ArrayList<TreeNode>();
        this.table = new Hashtable<String,String>();

    }

}
