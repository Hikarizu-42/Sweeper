public class Coords
{
    private int x;
    private int y;
    // Getters
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    // Setters
    public void setX(int newX)
    {
        this.x = newX;
    }
    public void setY(int newY)
    {
        this.y = newY;
    }
    // Constructors
    public Coords(int setX, int setY)
    {
        x = setX;
        y = setY;
    }
}
