package sat_solver;

import java.util.ArrayList;

public class SAT_solver {
     

    public static void main(String[] args) {
       
        String b = "(a>b&b>c&c>q)";
        MakeCNF a = new MakeCNF(b);

        if (a.syntax()) {
            String finalExpression = a.makeCNF();
            if(finalExpression != ""){
                solver solver = new solver(finalExpression);
                solver.extractPropositions();
                ArrayList<Integer> results = solver.ComputeExpression();
                solver.checkTautology(results);
                solver.checkContradiction(results);
                solver.checkContigency();
                if(solver.contradiction){
                     System.out.println("This expression is Contradiction");
                }
                else if(solver.tautology){
                     System.out.println("This expression is Tautology");
                }
                else if(solver.contigency){
                     System.out.println("This expression is contigency");
                }
                
 
                System.out.println(results);
            }
            else
                System.out.println("THE GIVEN EXPRESSION IS NOT IN \"CNF\" FORMAT");
                
        } else {
            System.out.println("GIVEN EXPRESSION IS : " + b);
            System.out.println("THIS SYNTAX IS NOT TRUE");
        }
    }
   
}
