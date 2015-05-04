package huffman;

import java.util.ArrayList;
import java.util.Hashtable;

public class TreeNode {

    public String nodeName;
    public TreeNode nodeParent;
    public ArrayList<TreeNode> nodeChildren;
    public int frequency;

    public TreeNode(String nodeName){
        this.nodeName = nodeName;
        this.nodeParent = null;
        this.nodeChildren = new ArrayList<TreeNode>();
        this.frequency = 0;

    }

    public int compareTo(TreeNode treenode) {
        return frequency - treenode.frequency;
    }

    public TreeNode getLeft(){
        return this.nodeChildren.get(0);
    }

    public TreeNode getRight(){
        return this.nodeChildren.get(1);
    }

}
