public class Cell
{
    private String displayVal;
    private int monsterStr;
    // Getters
    public String getDisplayVal()
    {
        return displayVal;
    }
    public int getMonsterStr()
    {
        return monsterStr;
    }
    // Setters
    public void setDisplayVal(String newDisplayVal)
    {
        this.displayVal = newDisplayVal;
    }
    public void setMonsterStr(int newMonsterStr)
    {
        this.monsterStr = newMonsterStr;
    }
    // Constructors
    public Cell(int monster,String display)
    {
        monsterStr = monster;
        displayVal = display;
    }
    public Cell(int monster)
    {
        displayVal = null;
        monsterStr = monster;
    }
    public boolean checkIsEmpty()
    {
        if(monsterStr == 0 && "0".equals(displayVal))
            return true;
        else return false;
    }
}