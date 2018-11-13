package mutagen.cli;

public class OptionNotSetException extends Exception
{
    public OptionNotSetException(String msg)
    {
        super(msg);
    }
}
