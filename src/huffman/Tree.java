package huffman;


public class Tree {

    public TreeNode root = null;
    public TreeNode current = null;
    //public TreeNode goal = null;
    public String result = "";

    public Tree(){
        //goal = new TreeNode("goal");
        //current = goal;
        root = null;
        current = null;
    }

    //add a new node and class it as a branch or leaf in the 2nd param
    //String nodeName, TreeNode nodeParent, ArrayList<TreeNode> nodeChildren
    public void addBranchNode(String name, String kind){

        TreeNode hold = new TreeNode(name);

        if (root == null){
            root = hold;
            current = root;
            System.out.println("added root");

        } else {
            hold.nodeParent = current;
            current.nodeChildren.add(hold);
            System.out.println("added new node to parent:");
        }
        if (kind == "branch"){
            current = hold;
            System.out.println("branched");
        }
    }

    public void endChildren() {
        if ((current.nodeParent != null) && (current.nodeParent.nodeName != null)){
            current = current.nodeParent;
        } else {
            //should not happen
        }
    }

    public String toString(){
        result = "";
        expand(root, 0);
        return result;
    }

    private void expand(TreeNode node, int depth){
        if (node == null){
            return;
        }
        for (int i = 0; i < depth; i++){
            result += "+";
        }

        if (node.nodeChildren == null || node.nodeChildren.size() == 0){
            result += "[" + node.nodeName + "]";
            result += "\n";
        } else {
            result += "<" + node.nodeName + "> \n";
            for (int i = 0; i < node.nodeChildren.size(); i++) {
                expand(node.nodeChildren.get(i), depth + 1);
            }
        }

    }

    public String scopeString(){
        result = "";
        scopeReader(root, 0);
        return result;
    }

    private void scopeReader(TreeNode node, int depth){
        if (node == null){
            return;
        }
        for (int i = 0; i < depth; i++){
            result += "+";
        }

        if (node.nodeChildren == null || node.nodeChildren.size() == 0){
            result += "[" + node.nodeName + "]";
            result += "(" + node.table.toString()+ ")";
            result += "\n";
        } else {
            result += "<" + node.nodeName + ">";
            result += "(" + node.table.toString()+ ")";
            result += "\n";
            for (int i = 0; i < node.nodeChildren.size(); i++) {
                scopeReader(node.nodeChildren.get(i), depth + 1);
            }
        }

    }

}