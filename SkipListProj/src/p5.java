import java.util.Random;


class Node{
	public Node above;
	public Node below;
	public Node next;
	public Node prev;
	public int key;
	
	public Node(int key) {
		this.key=key;
		above=null;
		below=null;
		next=null;
		prev=null;
	}
}

class skiplist{
	private Node head;
	private Node tail;
	
	private final int NEG_INFINITY=Integer.MIN_VALUE;
	private final int POS_INFINITY=Integer.MAX_VALUE;
	
	private int heightofskiplist=0;
	
	public Random random=new Random();
	
	public skiplist() {
		head=new Node(NEG_INFINITY);
		tail=new Node(POS_INFINITY);
		head.next=tail;
		tail.prev=head;
	}
	
	public Node skipSearch(int key) {
		int searchsteps=0;
		Node n=head;
		while(n.below!=null) {
			n=n.below;
			searchsteps++;
			while(key>=n.next.key) {
				n=n.next;
				searchsteps++;
			}
		}
		System.out.println("the search steps to search for " + key + " were: " + searchsteps);
		return n;
	}
	
	public Node skipInsert(int key) {
		Node position = skipSearch(key);
		Node q;
		
		int level= -1;
		
		if(position.key==key) {
			return position;
		}
		
		do {
			level++;
			
			canIncreaseLevel(level); 
			
			q=position;
			while(position.above==null) {
				position=position.prev;
			}
			
			position=position.above;
			
			q=insertAfterAbove(position,q,key);
		}while(random.nextBoolean()==true);
		
		return q;
	}
	
	public Node delete(int key) {
		Node nodeToDelete=skipSearch(key);
		
		if(nodeToDelete.key!=key) {
			return null;
		}
		
		removeRefrancesToNode(nodeToDelete);
		
		while(nodeToDelete!=null) {
			removeRefrancesToNode(nodeToDelete);
			
			if(nodeToDelete.above!=null) {
				nodeToDelete=nodeToDelete.above;
			}
			else {
				break;
			}
		}
		
		return nodeToDelete;
	}
	
	private void removeRefrancesToNode(Node nodeToDelete) {
		Node afterNodeToBeDeleted=nodeToDelete.next;
		Node beforeNodeToBeDeleted=nodeToDelete.prev;
		
		beforeNodeToBeDeleted.next=afterNodeToBeDeleted;
		afterNodeToBeDeleted.prev=beforeNodeToBeDeleted;
	}
	
	private void canIncreaseLevel(int level) {
		if(level>=heightofskiplist) {
			heightofskiplist++;
			addEmptyLevel();
		}
	}
	
	private void addEmptyLevel() {
		Node newHeadNode=new Node(NEG_INFINITY);
		Node newTailNode=new Node(POS_INFINITY);
		
		newHeadNode.next=newTailNode;
		newHeadNode.below=head;
		newTailNode.prev=newHeadNode;
		newTailNode.below=tail;
		
		head.above=newHeadNode;
		tail.above=newTailNode;
		
		head=newHeadNode;
		tail=newTailNode;
	}
	
	private Node insertAfterAbove(Node position,Node q,int key) {
		Node newNode=new Node(key);
		Node nodeBeforeNewNode=position.below.below;
		
		setBeforeAndAfterRefrances(q,newNode);
		setAboveAndBelowRefrances(position,key,newNode,nodeBeforeNewNode);
		
		return newNode;
	}
	
	private void setBeforeAndAfterRefrances(Node q, Node newNode) {
		newNode.next=q.next;
		newNode.prev=q;
		q.next.prev=newNode;
		q.next=newNode;
	}
	
	private void setAboveAndBelowRefrances(Node position,int key,Node newNode,Node nodeBeforeNewNode) {
		if(nodeBeforeNewNode !=null) {
			while(true) {
				if(nodeBeforeNewNode.next.key!=key) {
					nodeBeforeNewNode=nodeBeforeNewNode.next;
				}
				else {
					break;
				}
			}
			
			newNode.below=nodeBeforeNewNode.next;
			nodeBeforeNewNode.next.above=newNode;
		}
		
		if(position!=null) {
			if(position.next.key==key) {
				newNode.above=position.next;
			}
		} 
	}
	
	public void printLayer(int layer) {
		int layerWeAt=heightofskiplist;
		Node n=head;
		if(layer>heightofskiplist||layer<0) {
			return;
		}
		while(layer!=layerWeAt) {
			n=n.below;
			layerWeAt--;
		}
		System.out.print("layer[" + layer + "]: ");
		while(n.next.key!=POS_INFINITY) {
			System.out.print(n.next.key + ",");
			n=n.next;
		}
		System.out.println();//prints a new line
	}
	
	public void getLayers() {
		System.out.println(heightofskiplist+1);
	}
}

public class p5 {

	public static void main(String[] args) {
		skiplist list=new skiplist();
		list.skipInsert(2);
		list.skipInsert(10);
		list.skipInsert(15);
		list.skipInsert(16);
		list.skipInsert(31);
		list.skipInsert(71);
		list.skipInsert(86);
		list.skipInsert(89);
		list.skipInsert(91);
		list.skipInsert(96);
		list.skipSearch(86);
		list.skipSearch(16);
		list.skipSearch(160);
		list.getLayers();
		list.printLayer(1);
		list.printLayer(4);
		list.printLayer(0);
	}

}
