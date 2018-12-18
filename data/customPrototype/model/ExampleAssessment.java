import java.util.ArrayList;

// class declaration - allows for correct classname to be checked
class ExampleAssessment
{
    private ArrayList<String> linesList;
    // Declare constant - test literal repetition
    private final int CONSTANT_NUM = 19;

    public static void main(String[] args)
    {
        int wholeNumber = Integer.parseInt(args[0]);

        ExampleAssessment ex = new ExampleAssessment();
        ex.run(wholeNumber);

        ex.printLines();
    }

    public ExampleAssessment()
    {
        initVars();
    }

    private void initVars()
    {
        linesList = new ArrayList();
    }

    public void addLine(String l)
    {
        linesList.add(l);
    }

    public void addLine(Number n)
    {
        addLine("" + n);
    }

    public void addLineRepeat()
    {
        for (int i = 0; i < 10; i++)
        {
            addLine("Repeated line");
        }
    }

    public void printLines()
    {
        // iterate and print through linesList -
        for (int i = 0; i < linesList.size(); i++)
        {
            System.out.println(linesList.get(i));
        }
    }

    public String readFile()
    {
        // TODO implement
        return "UNIMPLEMENTED FILE READING";
    }

    public void run(int wholeNumber)
    {
        // Basic output - tests string correctness
        addLine("Output:");

        // Constant addition - test constant repetition
        addLine(CONSTANT_NUM);
        addLine(2 + CONSTANT_NUM);

        // Ensure number is positive - input validation
        if (wholeNumber < 0)
        {
            addLine("You must enter a positive number!");
        }

        // Perform arithmetic - tests operators and values
        float postArithmetic = arithmetic(wholeNumber);
        addLine(postArithmetic);

        // Check if even -
        if (isEven(Math.round(postArithmetic)))
        {
            addLine("even");
        }
        else
        {
            addLine("odd");
        }

        // TODO IMPLEMENT read file - test reading correct filename
        addLine(readFile());

        // Repeatedly add same line - test statement repetition
        addLineRepeat();

        // Multi-line statement - test overly long linesList.
        addLine("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");


    }

    public float arithmetic(int input)
    {
        float f = input;
        f = f * 7;
        f += 3.5;
        int divisor = 3;
        f = f / divisor;
        f -= 2.3;
        f += CONSTANT_NUM;
        return f;
    }

    public boolean isEven(int input)
    {
        return (input % 2) == 0;
    }
}