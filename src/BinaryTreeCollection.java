import java.util.AbstractCollection;
import java.util.Iterator;

public class BinaryTreeCollection extends AbstractCollection {

    private Node root;
    private int size;

    public static class Node {
        private Integer value;
        private Node left;
        private Node right;

        public Node(Integer value, Node left, Node right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        void setLeft(Node left) {
            this.left = left;
        }

        void setRight(Node right) {
            this.right = right;
        }

        public Integer getValue() {
            return value;
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("Значение: " + value)
                    .append(left != null ? " -> Левый: " + left.value : "")
                    .append(right != null ? " -> Правый: " + right.value : "")
                    .toString();
        }
    }

    public BinaryTreeCollection(Integer value) {
        root = new Node(value, null, null);
        size++;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator iterator() {
        return new Iterator() {
            java.util.ArrayList<Node> list = new java.util.ArrayList<>();
            int index = 0;
            Node lastReturned = null;

            {
                inorderTraversal(root);
            }

            private void inorderTraversal(Node node) {
                if (node != null) {
                    inorderTraversal(node.left);
                    list.add(node);
                    inorderTraversal(node.right);
                }
            }

            @Override
            public boolean hasNext() {
                return index < list.size();
            }

            @Override
            public Object next() {
                lastReturned = list.get(index);
                index++;
                return lastReturned;
            }

            @Override
            public void remove() {
                BinaryTreeCollection.this.remove(lastReturned.value);
                lastReturned = null;
            }
        };
    }

    @Override
    public boolean add(Object value) {
        Node node = new Node((Integer) value, null, null);
        if (root == null) {
            root = node;
            size++;
            return true;
        }

        Node current = root;
        while (true) {
            if ((Integer) value < current.value) {
                if (current.left == null) {
                    current.left = node;
                    size++;
                    return true;
                }
                current = current.left;
            } else if ((Integer) value > current.value) {
                if (current.right == null) {
                    current.right = node;
                    size++;
                    return true;
                }
                current = current.right;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean remove(Object o) {
        Integer value = (Integer) o;
        Node parent = null;
        Node current = root;

        while (current != null) {
            if (value < current.value) {
                parent = current;
                current = current.left;
            } else if (value > current.value) {
                parent = current;
                current = current.right;
            } else {
                break;
            }
        }

        if (current == null) {
            return false;
        }

        if (current.left == null && current.right == null) {
            if (parent == null) {
                root = null;
            } else if (parent.left == current) {
                parent.left = null;
            } else {
                parent.right = null;
            }
        } else if (current.left == null) {
            if (parent == null) {
                root = current.right;
            } else if (parent.left == current) {
                parent.left = current.right;
            } else {
                parent.right = current.right;
            }
        } else if (current.right == null) {
            if (parent == null) {
                root = current.left;
            } else if (parent.left == current) {
                parent.left = current.left;
            } else {
                parent.right = current.left;
            }
        } else {
            Node successorParent = current;
            Node successor = current.right;
            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }
            current.value = successor.value;
            if (successorParent.left == successor) {
                successorParent.left = successor.right;
            } else {
                successorParent.right = successor.right;
            }
        }
        size--;
        return true;
    }

    @Override
    public void clear() {
        super.clear();
        size = 0;
        root = null;
    }

    @Override
    public String toString() {
        if (root == null) {
            return "Дерево пустое";
        }
        StringBuilder sb = new StringBuilder();
        Iterator it = iterator();
        while (it.hasNext()) {
            Node node = (Node) it.next();
            sb.append(node.getValue());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}