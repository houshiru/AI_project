	/*
	 * Name: Shiro Hou : shou2015@my.fit.edu
	 * Course: CSE **** Intro to Atrificial Intelligence
	 * Project: Sliding Puzzle Solver
	 */

	import java.util.Scanner;
	import java.util.ArrayList;
	import java.util.Arrays;
	import java.util.Collections;
	
	public class SlideAstar {
		public final int MEMLIMIT = 6;
		public static ArrayList <Board> MEM = new ArrayList<Board>();
		
		public static class Board implements Comparable<Board> {
			public int[][] area;
			String msf = new String();
			int weight = 0;
			int count = 0;
			
			public void format(int[][] arin, String stin, int ct){
				this.area = new int[arin.length][arin.length];
				for (int pp = 0; pp < arin.length; pp++){
					for (int qq = 0; qq < arin.length; qq++){
						this.area[pp][qq] = arin[pp][qq];
					}
				}
				this.msf = stin;
				this.count = ct;
			}
			
			public void prntBoard(){
				int dim = this.area.length;
				for (int rr = 0; rr < dim; rr++){
					for (int ss = 0; ss < dim; ss++){
						System.out.print(area[rr][ss]);
					}
					System.out.println();
				}
			}
			
			public void addMove(String add){
				msf = msf + " " + add;
				count++;
			}
			
			public String getMoves(){
				return msf;
			}

			public void setWeight(int way){
				weight = way;
			}

			public int getweight(){
				return weight;
			}

			public int getCt(){
				return count;
			}
			public int compareTo (Board otr){
				return this.getweight() - otr.getweight();
		}
		public static boolean chkPast(Board in){
			boolean recur = false;
			int count = 0;
			int dim = in.area.length;
			for (Board x : MEM){
				for(int zz = 0; zz < dim; zz++){
					for (int aa = 0; aa < dim; aa++){
						if (x.area[zz][aa] == in.area[zz][aa]){
							count++;
						}
					}
				}
				if (count == (dim * dim)){
					recur = true;
				}
			}
			return recur;
		}
		
		public static boolean test(int dim, Board board){
			//tests and sets weight for board.
			boolean check = true;
			int weighA = 0;
			int weighB = 0;
			for(int kk = 0; kk < dim; kk++){
				for(int ll = 0; ll < dim; ll++){
					if((board.area[kk][ll] != ((dim*kk)+ll+1)) && (board.area[kk][ll] != 0)) {
						check = false;
						int num = board.area[kk][ll];
						weighA++; //Note: Number out of place.
						weighB += Math.abs((num / 3) - kk) + Math.abs((num % 3) - (ll + 1));
					}
				}
			}
			board.setWeight(weighA + weighB);
			return check;
		}

		public static boolean solverAS(int dim, Board boards){
		
			boolean check = true;
			Board tstbrdR, tstbrdD, tstbrdL, tstbrdU;
			Board[] tempLst;
			int x = 0; //Row Location
			int y = 0; //Col Location
			//get and copy boards.
			tstbrdR = boards;
			//make new objects to avoid pointer assignment issues
			tstbrdD = new Board();
			tstbrdD.format(tstbrdR.area, tstbrdR.getMoves(), tstbrdR.getCt());
			tstbrdL = new Board();
			tstbrdL.format(tstbrdD.area, tstbrdD.getMoves(), tstbrdD.getCt());
			tstbrdU = new Board();
			tstbrdU.format(tstbrdL.area, tstbrdL.getMoves(), tstbrdL.getCt());
			//List for a* evaluation.
			ArrayList<Board> blist = new ArrayList<Board>();
			String moves = tstbrdR.getMoves();

			//debugging display
			//Depth indication.
			System.out.println("Depth: " + tstbrdR.getCt());
			System.out.println(tstbrdR.getMoves());
			for (int oo = 0; oo < dim; oo++){
				for (int pp = 0; pp < dim; pp++){
					System.out.print(tstbrdR.area[oo][pp]);
				}
				System.out.println();
			}
			System.out.println("\n");
			
			//find empty '0' space.
			for(int mm = 0; mm < dim; mm++){
				for(int nn = 0; nn < dim; nn++){
					if (tstbrdR.area[mm][nn] == 0){
						x = mm;
						y = nn;
						break;
					}
				}
				if (tstbrdR.area[x][y] == 0){
					break;
				}
			}
			
			if (((y % dim) != (dim - 1)) && (!moves.endsWith("L"))){
				//move Right.
				int tmp = tstbrdR.area[x][y];
				tstbrdR.area[x][y] = tstbrdR.area[x][y+1];
				tstbrdR.area[x][y+1] = tmp;
				//add move to list.
				tstbrdR.addMove("R");
				//Check for valid solution.
				check = test(dim, tstbrdR);
				if (check == true){
						System.out.println(tstbrdR.getMoves());
						tstbrdR.prntBoard();
					return check;
				}
				if (!chkPast(tstbrdR)){
					blist.add(tstbrdR);
				}
			}
			if (((x % dim) != (dim - 1)) && (!moves.endsWith("U"))){
				//move Down.
				int tmp = tstbrdD.area[x][y];
				tstbrdD.area[x][y] = tstbrdD.area[x+1][y];
				tstbrdD.area[x+1][y] = tmp;
				//add move to list.
				tstbrdD.addMove("D");
				//check for valid solution.
				check = test(dim, tstbrdD);
				if (check == true){
					System.out.println(tstbrdD.getMoves());
					tstbrdD.prntBoard();
					return check;
				}
				if (!chkPast(tstbrdD)){
					blist.add(tstbrdD);
				}
			}
			if (((y % dim) != 0) && (!moves.endsWith("R"))){
				//move Left.
				int tmp = tstbrdL.area[x][y];
				tstbrdL.area[x][y] = tstbrdL.area[x][y-1];
				tstbrdL.area[x][y-1] = tmp;
				//add move to list.
				tstbrdL.addMove("L");
				//check for valid solution.
				check = test(dim, tstbrdL);
				if(check ==  true){
					System.out.println(tstbrdL.getMoves());
					tstbrdL.prntBoard();
					return check;
				}
				if (!chkPast(tstbrdL)){
					blist.add(tstbrdL);
				}
			}
			if (((x %dim) != 0) && (!moves.endsWith("D"))){
				//move Up.
				int tmp = tstbrdU.area[x][y];
				tstbrdU.area[x][y] = tstbrdU.area[x-1][y];
				tstbrdU.area[x-1][y] = tmp;
				//add move to list.
				tstbrdU.addMove("U");
				//check for valid solution.
				check = test(dim, tstbrdU);
				if (check == true){
					System.out.println(tstbrdU.getMoves());
					tstbrdU.prntBoard();
					return check;
				}
				if (!chkPast(tstbrdU)){
					blist.add(tstbrdU);
				}
			}
			if (MEM.size() > 6){
				MEM.remove(0);
			}
			tempLst = new Board[blist.size()];
			blist.toArray(tempLst);
			Arrays.sort(tempLst);
			MEM.add(tempLst[0]);

			if (check != true){ // && (tempLst[0].getCt() < 20))
				check = solverAS(dim, tempLst[0]);
			}
			return check;
		}

		public static void main(String[] args) {
			//Declarations
			boolean solved = false;
			String result = new String();
			int size;
			int[][] data;
			Board board = new Board();
			int temp = -1;
			Scanner stdin = new Scanner(System.in);
			
			//Load Board
			System.out.print("Enter Board Size (n x n): ");
			size = stdin.nextInt();
			data = new int[size][size];
			System.out.print("Enter Board Layout: ");
			for (int ii = 0; ii < (size); ii++){
				for (int jj = 0; jj < size; jj++){
					while ((temp < 0) || (temp > (size * size - 1))){
						temp = stdin.nextInt();
					}
					data[ii][jj]=temp;
					temp = -1;
				}
			}
			board.area=data;
			stdin.close();
			//Board loaded.
			solved = solverAS(size, board);
			result = (solved == true)? "True" : "False";
			System.out.println("Valid Solution in " + "XX" + " moves: " + result);
		}

	}
}
