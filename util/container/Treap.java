package container;

public class Treap<T extends Comparable> {
	class Node{
		T value;
		int priority;
		int size;
		Node father;
		Node leftChild;
		Node rightChild;
		Node(T value)
		{
			this.value = value;
			father = null;
			leftChild = null;
			rightChild = null;
			size = 1;
			priority = (int) Math.random();
		}
	}
	
	private Node root;
	
	public Treap(){
		
	}
	public Treap(T t)
	{
		this.root = new Node(t);
	}
	
	private void update(Node root)
	{
		root.size=1;
		if(root.leftChild!=null)
		{
			root.size+=root.leftChild.size;
		}
		if(root.rightChild!=null)
		{
			root.size+=root.rightChild.size;
		}
	}
	
	private void rotateLeft(Node p)
	{
		Node father = p.father;
		Node pLeftChild = p.leftChild;
		p.leftChild = pLeftChild.rightChild;
		if(pLeftChild.rightChild != null) pLeftChild.rightChild.father = p;
		pLeftChild.rightChild = p;
		p.father = pLeftChild;
		if(father == null) root = pLeftChild;
		else if(p.equals(father.leftChild))
		{
			father.leftChild = pLeftChild;
		}
		else{
			father.rightChild = pLeftChild;
		}
		pLeftChild.father = father;
		update(p);
		update(pLeftChild);
	}
	
	private void rotateRight(Node p)
	{
		Node father = p.father;
		Node pRightChild = p.rightChild;
		p.rightChild = pRightChild.leftChild;
		if(pRightChild.leftChild!=null) pRightChild.leftChild.father = p;
		pRightChild.leftChild = p;
		p.father = pRightChild;
		if(father==null) root = pRightChild;
		else if(p.equals(father.leftChild)) father.leftChild = pRightChild;
		else father.rightChild = pRightChild;
		pRightChild.father = father;
		update(p);
		update(pRightChild);
	}
	
	private void insertNode(Node root,T value)
	{
		if(root == null) {
			this.root = new Node(value);
			return;
		}
		if(root.value.compareTo(value) > 0)
		{
			if(root.rightChild!=null)
				insertNode(root.rightChild,value);
			else{
				Node node = new Node(value);
				root.rightChild = node;
				node.father = root;
			}
			if(root.priority < root.rightChild.priority){
				rotateRight(root);
			}
		}
		else{
			if(root.leftChild!=null)
				insertNode(root.leftChild,value);
			else{
				Node node = new Node(value);
				root.leftChild = node;
				node.father = root;
			}
			if(root.priority < root.leftChild.priority){
				rotateLeft(root);
			}
		}
		update(root);
	}
	
	private T getKth(Node node,int k)
	{
		int tem = (node.leftChild==null?0:node.leftChild.size);
		if(k== tem+1)
		{
			return node.value;
		}
		else if(k < tem+1){
			return getKth(node.leftChild,k);
		}
		else{
			return getKth(node.rightChild,k-tem-1);
		}
	}
	
	private Node find(Node node,T value){
		if(node == null) return null;
		if(node.value.equals(value)){
			return node;
		}
		else if(value.compareTo(node.value)>0){
			return find(node.rightChild,value);
		}
		else {
			return find(node.leftChild,value);
		}
			
	}
	
	private void remove(Node node){
		if(node != null){
			Node father = node.father;
			if(node.leftChild == null){
				if(father != null){
					if(node.equals(father.leftChild)) father.leftChild = node.rightChild;
					else father.rightChild = node.rightChild;
				}
				else{
					this.root = node.rightChild;
				}
				if(node.rightChild != null) node.rightChild.father = father;
			}
			else if(node.rightChild == null){
				if(father != null){
					if(node.equals(father.leftChild)) father.leftChild = node.leftChild;
					else father.rightChild = node.leftChild;
				}
			}
			else{
				if(node.leftChild.priority > node.rightChild.priority){
					rotateLeft(node);
				}else{
					rotateRight(node);
				}
				remove(node);
			}
			if(father!=null)
				update(father);
			else update(this.root);
		}
	}
	
	public void add(T value){
		insertNode(root,value);
	}
	
	public T getKth(int k)
	{
		return getKth(root,k);
	}
	
	public boolean contains(T value)
	{
		Node node = find(root,value);
		if(node == null) return false;
		else return true;
	}
	
	public void remove(T value)
	{
		Node node = find(root,value);
		remove(node);
	}
	
	public int size()
	{
		if(root==null) return 0;
		return root.size;
	}
}
