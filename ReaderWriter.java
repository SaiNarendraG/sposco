import java.util.concurrent.Semaphore;
import java.util.Scanner;

public class ReaderWriter{
    static Semaphore reader= new Semaphore(1);
    static Semaphore writer=new Semaphore(1);
    static int rc=0;
    static String message=" ";
    Scanner sc=new Scanner(System.in);

    static class Writer  implements Runnable{
        public void run(){
            try{
               writer.acquire(); //To wait on a semaphore
               message+="1";
               System.out.println("Thread Writer"+Thread.currentThread().getName()+" is writing  :"+ message);
               Thread.sleep(2500);
               System.out.println("Thread Writer"+Thread.currentThread().getName()+" done with writing");
               writer.release();  // To signal on a semaphore: 
            }

            catch(InterruptedException e){
                System.out.println(e.getMessage());
            }
        }
    }
    static class Reader  implements Runnable{
        public void run(){
            try{
                reader.acquire();
                rc++;
                if(rc==1) writer.acquire(); // To wait on a semaphore: 
                reader.release();           // To signal on a semaphore: 
                System.out.println("Thread Reader"+Thread.currentThread().getName()+" is reading ");
                Thread.sleep(2500);
                System.out.println("Thread Reader"+Thread.currentThread().getName()+" done reading");
                reader.acquire();
                rc--;
                
                if(rc==0) writer.release(); // To signal on a semaphore: 
                reader.release();
            }
            catch(InterruptedException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception{

        Reader reader=new Reader();
        
        Thread []r=new Thread[3];
        for(int i=0;i<3;i++){
            r[i]= new Thread(reader);
           
        }

        Writer writer=new Writer();
        Thread []w=new Thread[2];
        for(int i=0;i<2;i++){
            w[i]= new Thread(writer);
            
        }

        r[0].start();
        r[1].start();
        w[0].start();
        w[1].start();
        r[2].start();
    r[3].start();
    }

}
 