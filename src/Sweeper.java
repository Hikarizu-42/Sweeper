import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Sweeper implements ActionListener
{
    public final static int ARRAY_WIDTH = 20;
    public final static int ARRAY_HEIGHT = 20;
    public final static int FRAME_WIDTH = 1000;
    public final static int FRAME_HEIGHT = 700;
    public final static int [] MONSTER_NUMBERS = {0,20,10,5};
    public final static Color [] MONSTER_COLORS = {null,Color.CYAN,Color.BLUE,Color.RED};
    public final static Color GAME_OVER_COLOR = Color.BLACK;
    public final static Color UNOPENED_FIELDS_COLOR = Color.LIGHT_GRAY;
    public final static String DO_NOT_DISPLAY = "0";
    JFrame frame = new JFrame("Sweeper");
    JButton reset = new JButton("Reset");
    JButton[][] buttons = new JButton[ARRAY_WIDTH][ARRAY_HEIGHT];
    Cell[][] cells = new Cell[ARRAY_WIDTH][ARRAY_HEIGHT];
    Container grid = new Container();

    public static void main(String[] args)
    {
        new Sweeper();
    }

    //Constructor initializing the frame, button grid and reset button
    public Sweeper()
    {
        frame.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        frame.setLayout(new BorderLayout());
        frame.add(reset, BorderLayout.NORTH);
        reset.addActionListener(this);
        //Button grid
        grid.setLayout(new GridLayout(ARRAY_WIDTH, ARRAY_HEIGHT));
        for (int x = 0; x < ARRAY_WIDTH; x++)
            for (int y = 0; y < ARRAY_HEIGHT; y++)
            {
                buttons[x][y] = new JButton();
                buttons[x][y].addActionListener(this);
                grid.add(buttons[x][y]);
            }
        frame.add(grid,BorderLayout.CENTER);
        createRandomMonsters();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /// Chooses location and power of monsters
    private void createRandomMonsters()
    {
        //initialize list of all positions
        ArrayList<Coords> positions = new ArrayList<Coords>();
        for (int x = 0; x < ARRAY_WIDTH; x++)
            for (int y = 0; y < ARRAY_HEIGHT; y++)
            {
                Coords myCoords = new Coords(x, y);
                positions.add(myCoords);
            }
        //reset cells, pick out position of monsters from list of all positions
        cells = new Cell [ARRAY_WIDTH][ARRAY_HEIGHT];
        for (int strength = 1; strength < MONSTER_NUMBERS.length; strength++)
            for (int m = 0; m < MONSTER_NUMBERS[strength]; m++)
            {
                int choice = (int) (Math.random() * positions.size());
                Coords myCoords = positions.get(choice);
                Cell myCell = new Cell(strength);
                int x = myCoords.getX();
                int y = myCoords.getY();
                cells[x][y] = myCell;
                positions.remove(choice);
            }
        CalculateDisplay();
    }

    //calculates the displayed value shown to the player
    public void CalculateDisplay()
    {
        for (int x = 0; x < ARRAY_WIDTH; x++)
            for (int y = 0; y < ARRAY_HEIGHT; y++)
            {
                int counts = 0;
                if (x > 0 && y > 0 && cells[x - 1][y - 1] != null && cells[x - 1][y - 1].getMonsterStr() > 0)
                    counts = counts + cells[x - 1][y - 1].getMonsterStr();//up left

                if (y > 0 && cells[x][y - 1] != null && cells[x][y - 1].getMonsterStr() > 0)
                    counts = counts + cells[x][y - 1].getMonsterStr();//up

                if (x < (ARRAY_WIDTH - 1) && y > 0 && cells[x + 1][y - 1] != null && cells[x + 1][y - 1].getMonsterStr() > 0)
                    counts = counts + cells[x + 1][y - 1].getMonsterStr();//up right

                if (x < (ARRAY_WIDTH - 1) && cells[x + 1][y] != null && cells[x + 1][y].getMonsterStr() > 0)
                    counts = counts + cells[x + 1][y].getMonsterStr();//right

                if (x < (ARRAY_WIDTH - 1) && y < (ARRAY_HEIGHT - 1) && cells[x + 1][y + 1] != null && cells[x + 1][y + 1].getMonsterStr() > 0)
                    counts = counts + cells[x + 1][y + 1].getMonsterStr();//down right

                if (y < (ARRAY_HEIGHT - 1) && cells[x][y + 1] != null && cells[x][y + 1].getMonsterStr() > 0)
                    counts = counts + cells[x][y + 1].getMonsterStr();//down

                if (x > 0 && y < (ARRAY_HEIGHT - 1) && cells[x - 1][y + 1] != null && cells[x - 1][y + 1].getMonsterStr() > 0)
                    counts = counts + cells[x - 1][y + 1].getMonsterStr();//down left

                if (x > 0 && cells[x - 1][y] != null && cells[x - 1][y].getMonsterStr() > 0)
                    counts = counts + cells[x - 1][y].getMonsterStr();//left

                if (cells[x][y] == null)
                {
                    Cell myCell;
                    myCell = new Cell(0, counts + "");
                    cells[x][y] = myCell;
                }   else
                {
                    cells[x][y].setDisplayVal(counts + "");
                }
            }
    }

    // reveals neighbouring cells
    public void ClearZeros(int x,int y)
    {
        if(cells[x][y].checkIsEmpty())
        {
            buttons[x][y].setEnabled(false);
            if (y>0 && buttons[x][y-1].isEnabled() && cells[x][y-1].getMonsterStr() == 0)
                ClearNeighbours(x,y-1);//up

            if (x<cells.length-1 && buttons[x+1][y].isEnabled() && cells[x+1][y].getMonsterStr() == 0)
                ClearNeighbours(x+1,y);//right

            if (y<cells[0].length-1 && buttons[x][y+1].isEnabled() && cells[x][y+1].getMonsterStr() == 0)
                ClearNeighbours(x,y+1);//down

            if (x>0 && buttons[x-1][y].isEnabled() && cells[x-1][y].getMonsterStr() == 0)
                ClearNeighbours(x-1,y);//left

            if (x>0 && y>0 && cells[x-1][y-1].getMonsterStr() == 0 && !cells[x - 1][y - 1].checkIsEmpty())
                ClearNeighbours(x-1,y-1);//up left

            if (x<cells.length-1 && y>0 && cells[x+1][y-1].getMonsterStr() == 0 && !cells[x + 1][y - 1].checkIsEmpty())
                ClearNeighbours(x+1,y-1);//up right

            if (x<cells.length-1 && y<cells[0].length-1 && cells[x+1][y+1].getMonsterStr() == 0&& !cells[x + 1][y + 1].checkIsEmpty())
                ClearNeighbours(x+1,y+1);//down right

            if (x>0 && y<cells[0].length-1 && cells[x-1][y+1].getMonsterStr() == 0 && !cells[x - 1][y + 1].checkIsEmpty())
                ClearNeighbours(x-1,y+1);//down left
        }
    }

    //reveals cell if it has a display otherwise cell is empty and recurse revealing it's neighbours
    public void ClearNeighbours(int x, int y)
    {
        if(cells[x][y].checkIsEmpty())
        {
            ClearZeros(x,y);
        } else Click(x,y);
    }

    //reveal cell
    public void Click(int x,int y)
    {
        String display = cells[x][y].getDisplayVal();
        if(!DO_NOT_DISPLAY.equals(display))
            buttons[x][y].setText(display);
        buttons[x][y].setEnabled(false);
    }

    public void checkWin()
    {
        boolean isWon = true;
        for (int x = 0; x < ARRAY_WIDTH; x++)
            for (int y = 0; y < ARRAY_HEIGHT; y++)
                if (buttons[x][y].isEnabled() && cells[x][y].getMonsterStr() == 0)
                {
                    isWon = false;
                    x = ARRAY_WIDTH;
                    y = ARRAY_HEIGHT;
                }
        if (isWon)
        {
            JOptionPane.showMessageDialog(frame, "You Win!");
            for (int x = 0; x < ARRAY_WIDTH; x++)
                for (int y = 0; y < ARRAY_HEIGHT; y++)
                    RevealMonsters(x, y);
        }
    }

    public void RevealMonsters(int x, int y)
    {
        if( cells[x][y].getMonsterStr() > 0)
        {
            buttons[x][y].setBackground(MONSTER_COLORS[cells[x][y].getMonsterStr()]);
            buttons[x][y].setText(cells[x][y].getDisplayVal());
        }
    }

    public void GameOver(int A, int B)
    {
        for (int x = 0; x < ARRAY_WIDTH; x++)
            for (int y = 0; y < ARRAY_HEIGHT; y++)
            {
                if (buttons[x][y].isEnabled())
                {
                    buttons[x][y].setBackground(UNOPENED_FIELDS_COLOR);
                    RevealMonsters(x, y);
                }
                buttons[x][y].setEnabled(false);
            }
        buttons[A][B].setBackground(GAME_OVER_COLOR);
        buttons[A][B].setText(cells[A][B].getDisplayVal());
    }
    @Override
    public void actionPerformed(ActionEvent event)
    {
        if(event.getSource().equals(reset))
        {
            for (int x = 0; x < ARRAY_WIDTH; x++)
            {
                for (int y = 0; y < ARRAY_HEIGHT; y++)
                {
                    buttons[x][y].setEnabled(true);
                    buttons[x][y].setText("");
                    buttons[x][y].setBackground(MONSTER_COLORS[0]);
                }
            }
            createRandomMonsters();
        }
        else
        {
            for (int x = 0; x < ARRAY_WIDTH; x++)
                for (int y = 0; y < ARRAY_HEIGHT; y++)
                {
                    if (event.getSource().equals(buttons[x][y]))
                    {
                        if (cells[x][y].checkIsEmpty())
                        {
                            ClearZeros(x, y);
                            checkWin();
                        } else if (cells[x][y].getMonsterStr() > 0)
                        {
                            GameOver(x, y);
                        } else
                        {
                            Click(x, y);
                            checkWin();
                        }
                    }
                }
        }
    }
}