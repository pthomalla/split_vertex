package split_vertex;

import java.util.List;

public class Split_vertex {

    
    public static void main(String[] args) {
        SplitVertex split_vertex = new SplitVertex();
        split_vertex.readData(System.in);
        split_vertex.calculate();
        System.out.print("size split_vertex: ");
        System.out.print(split_vertex.size_split_vertex());
        List<Integer> list = split_vertex.get_split_vertex();
        System.out.print("\nsplit vertex: ");
        
        list.forEach((vertex)->{
            System.out.print(vertex);
            System.out.print(", ");
        });
        System.out.print("\b\b \n");
    }
    
}
