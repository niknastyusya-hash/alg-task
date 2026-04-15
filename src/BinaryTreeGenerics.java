import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinaryTreeGenerics<T extends Comparable<T>> extends AbstractCollection<T> {

    private Node root;
    private int size;

    private class Node {
        private T value;
        private Node left;
        private Node right;

        public Node(T value, Node left, Node right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        public T getValue() {
            return value;
        }
    }

    public BinaryTreeGenerics(T value) {
        root = new Node(value, null, null);
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(T value) {
        if (value == null) {
            throw new NullPointerException("Null values not allowed");
        }

        Node newNode = new Node(value, null, null);
        if (root == null) {
            root = newNode;
            size++;
            return true;
        }

        Node current = root;
        while (true) {
            int cmp = value.compareTo(current.value);
            if (cmp < 0) {
                if (current.left == null) {
                    current.left = newNode;
                    size++;
                    return true;
                }
                current = current.left;
            } else if (cmp > 0) {
                if (current.right == null) {
                    current.right = newNode;
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
    public Iterator<T> iterator() {
        return new Iterator<T>() {
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
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                lastReturned = list.get(index);
                index++;
                return lastReturned.value;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                BinaryTreeGenerics.this.remove(lastReturned.value);
                lastReturned = null;
            }
        };
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;

        T value;
        try {
            value = (T) o;
        } catch (ClassCastException e) {
            return false;
        }

        Node parent = null;
        Node current = root;

        while (current != null) {
            int cmp = value.compareTo(current.value);
            if (cmp < 0) {
                parent = current;
                current = current.left;
            } else if (cmp > 0) {
                parent = current;
                current = current.right;
            } else {
                break;
            }
        }

        if (current == null) return false;
        size--;
        return true;
    }

    @Override
    public String toString() {
        if (root == null) return "Дерево пустое";
        StringBuilder sb = new StringBuilder();
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) sb.append(", ");
        }
        return sb.toString();
    }
}
