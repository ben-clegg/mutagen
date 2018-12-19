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
        char letter = args[1].charAt(0);

        ExampleAssessment ex = new ExampleAssessment();
        ex.run(wholeNumber, letter);

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

    public boolean stringEquality()
    {
        String a = "foo";
        String b = "foo";
        String c = "bar";
        if ((a.equals(b)) || !(a.equals(c)))
        {
            return true;
        }
        return false;
    }

    public void iterateArray()
    {
        int[] arr = {2,4,6,8,10,12};
        for (int i = 0; i < 6; i++)
        {
            addLine(arr[i]);
        }
    }

    public void comparators(int n)
    {
        if (n <= 50)
        {
            addLine("less than or equal to 50");
        }
        if (n >= 3)
        {
            addLine("greater than or equal to 3");
        }
        if (n < 50)
        {
            addLine("less than 50");
        }
        if (n > 3)
        {
            addLine("greater than 3");
        }
    }

    public void logicalOperators()
    {
        String s = null;

        // Short-circuiting - does not expect a NullPointerException
        // Non-short-circuiting - expects a NullPointerException
        try
        {
            boolean b = (s != null && s.length() > 0);
            addLine("CORRECT: No NullPointerException encountered.");
        }
        catch (NullPointerException correctE)
        {
            addLine("ERROR: Unexpected NullPointerException encountered.");
        }

        // Non-short-circuiting - expects a NullPointerException
        try
        {
            if (s != null & s.length() > 0)
            {
                addLine("ERROR: No NullPointerException encountered!");
            }
        }
        catch (NullPointerException correctE)
        {
            addLine("CORRECT: NullPointerException encountered as expected.");
        }

        // TODO implement for | / ||
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

    public void vowelChecker(char ch)
    {
        switch (ch)
        {
            case 'a': case 'A':
                addLine("Vowel: A");
                break;
            case 'e': case 'E':
                addLine("Vowel: E");
                break;
            case 'i': case 'I':
                addLine("Vowel: I");
                break;
            case 'o': case 'O':
                addLine("Vowel: O");
                break;
            case 'u': case 'U':
                addLine("Vowel: U");
                break;
            default:
                addLine("Consonant");
                break;
        }
    }

    public void run(int wholeNumber, char letter)
    {
        // Basic output - tests string correctness
        addLine("Output:");

        // Vowel check - tests SwitchStmtsShouldHaveDefault and MissingBreakInSwitch
        vowelChecker(letter);

        // Run comparators - tests cmpSyn
        comparators(wholeNumber);

        // Constant addition - test constant repetition
        addLine(CONSTANT_NUM);
        addLine(2 + CONSTANT_NUM);

        // Ensure number is positive - input validation
        if (wholeNumber < 0)
        {
            addLine("The number is negative");
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

        // Check string equality - test string .equals vs ==
        if(stringEquality()) { addLine("String equality correct"); }

        // Array iteration - checks exceeding range and [] replacement
        iterateArray();

        // Logical operators - check short-circuit operator confusion
        logicalOperators();


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