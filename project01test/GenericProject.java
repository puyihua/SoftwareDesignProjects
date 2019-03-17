/**
 * GenericProject 
 * Name : Yihua Pu 
 * Netid: ypu5 
 * Email: ypu5@uic.edu
 */
public class GenericProject {

    public static void main(String[] args) {

    }

}

class Node<T>{
    T data;
    Node<T> next;

    Node(T data){
        this.data = data;
        this.next = null;
    }

}

abstract class GenericList<I>{

    Node<I> head;
    private int length;

    GenericList(){
        this.head = null;
        this.length = 0;
    }

    int getlength(){
        return this.length;
    }

    void setlength(int length){
        this.length = length;
    }

    void lengthPlus(){
        this.length = this.length + 1;
    }

    void print() {
        Node<I> tmp = this.head;
        while (tmp != null) {
            System.out.println(tmp.data);
            tmp = tmp.next;
        }
    }

    abstract void add(I data);

    I delete(){
        if(this.head == null){
            return null;
        }
        I tmp = this.head.data;
        this.head = this.head.next;
        this.length--;
        return tmp;
    }
}

class GenericQueue<I> extends GenericList<I> {
    
    GenericQueue(I data){
        this.head = new Node<I>(data);
        this.lengthPlus();
    }
    
    void add(I data){
        Node<I> tmp = new Node<I>(data);
        Node<I> cur =  this.head;
    
        while(cur.next != null){
            cur = cur.next;
        }
        cur.next = tmp;
        this.lengthPlus();
    }
    
    void enqueue(I data){
        this.add(data);
    }

    I dequeue(){
        return this.delete();
    }

}

class GenericStack<I> extends GenericList<I> {

    GenericStack(I data){
        this.head = new Node<I>(data);
        this.lengthPlus();
    }
    void add(I data){
        Node<I> tmp = new Node<I>(data);
        tmp.next = this.head;
        this.head = tmp;
        this.lengthPlus();
    }

    void push(I data){
        this.add(data);
    }

    I pop(){
        return this.delete();
    }

}

