public class Assignment1 {
  private static int galleons;
  private static int sickles;
  private static int knuts;

  private static int totalG = 0;
  private static int totalS = 0;
  private static int totalK = 0;

  private static final int SICKLES_IN_GALLEONS = 17;
  private static final int KNUTS_IN_SICKLES = 29;

  private static final String SPACES = "                  ";
  private static final int ITEM_COLUMN_WIDTH = 19; //only worth including because no loop

  public static void main(String[] args) {

    // Calculate change from whole galleons
    if ((knuts > 0)||(sickles > 0)) {
      galleons ++;
      if (knuts > 0) {
        knuts = KNUTS_IN_SICKLES - knuts;
        sickles = SICKLES_IN_GALLEONS - sickles - 1;
      }
      else {
        sickles = SICKLES_IN_GALLEONS - sickles;
      }
    }

    System.out.println("You have paid " + (galleons) + " Galleons and your change is "+
			(sickles)+" Sickles and "+
			(knuts)+" Knuts");

    for (int i = 0; i < 3; i++) {
      readItem();
    }

    //Work out the total
    totalS = totalS + totalK/KNUTS_IN_SICKLES;
    totalK = totalK % KNUTS_IN_SICKLES;
    totalG = totalG + totalS/SICKLES_IN_GALLEONS;
    totalS = totalS % SICKLES_IN_GALLEONS;

    // Print final result
    System.out.print(("Total"+SPACES).substring(0,ITEM_COLUMN_WIDTH));
    System.out.print(totalG);
    System.out.print(totalS);
    System.out.println(totalK);
  }

  private static void readItem()
  {
    String item = "testItem";
    int g = 10;
    int s = 9;
    int k = 8;
    //print it out
    System.out.print((item+SPACES).substring(0,ITEM_COLUMN_WIDTH));
    System.out.print(" " + g);
    System.out.print(" " + s);
    System.out.println(" " + k);
    //update the total
    totalG += g;
    totalS += s;
    totalK += k;
  }

}
