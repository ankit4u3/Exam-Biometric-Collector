/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package csbc.attendence;

/**
 *
 * @author Ankit
 */
import java.util.concurrent.Callable;

import java.util.concurrent.ExecutionException;

import java.util.concurrent.FutureTask;

 

public class FutureTaskExample {

 

        public static void main(String[] args) {

                FutureTask<String> future = new FutureTask<>(new CallableTask());
    FutureTask<String> future2 = new FutureTask<>(new CallableTask2());
                //Cancelling code before run

                /*boolean b = future.cancel(true);

                System.out.println("Cancelled="+b);*/

                future.run();
                future2.run();

          //Cancelling code after run

                /*boolean b = future.cancel(true);

                System.out.println("Cancelled="+b);*/

                try {

            String result = future2.get();

            System.out.println("Result2="+result);

    } catch (InterruptedException | ExecutionException e) {

            System.out.println("EXCEPTION2!!!");

        e.printStackTrace();

    }

                           try {

            String result = future.get();

            System.out.println("Result="+result);

    } catch (InterruptedException | ExecutionException e) {

            System.out.println("EXCEPTION!!!");

        e.printStackTrace();

    }
        }

}

 

class CallableTask implements Callable<String>{

        @Override

  public String call() throws Exception {

          System.out.println("Executing call() !!!");

          for( int i=0;i<999;i++)
          
          {
            System.out.println("Executing call() !!!" + i);
            
          }
          /*if(1==1)

                throw new java.lang.Exception("Thrown from call()");*/

          return "success";

  }

  
}

class CallableTask2 implements Callable<String>{

        @Override

  public String call() throws Exception {

          System.out.println("Executing call() !!!");

          for( int i=0;i<999;i++)
          
          {
            System.out.println("Executing call() !!!" + i);
            
          }
          /*if(1==1)

                throw new java.lang.Exception("Thrown from call()");*/

          return "success";

  }

  
}



