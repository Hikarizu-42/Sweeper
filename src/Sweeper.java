import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Sweeper implements ActionListener
{
    int WIDTH = 20;
    int HEIGHT = 20;
    JFrame frame = new JFrame("Sweeper");
    JButton reset = new JButton("Reset");
    JButton[][] buttons = new JButton[WIDTH][HEIGHT];
    Cell[][] cells = new Cell[WIDTH][HEIGHT];
    Container grid = new Container();
    int [] monsterNumbers = {0,20,10,5};
    Color [] monsterColors = {null,Color.CYAN,Color.BLUE,Color.RED};

    public static void main(String[] args)
    {
        new Sweeper();
    }

    public Sweeper()
    {
        frame.setSize(1000,700);
        frame.setLayout(new BorderLayout());
        frame.add(reset, BorderLayout.NORTH);
        reset.addActionListener(this);
        //Button grid
        grid.setLayout(new GridLayout(WIDTH,HEIGHT));
        for (int x = 0; x < WIDTH; x++)
            for (int y = 0; y < HEIGHT; y++)
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
    private void createRandomMonsters()
    {
        //initialize list of all positions
        ArrayList<Coords> positions = new ArrayList<Coords>();
        for (int x = 0; x < WIDTH; x++)
            for (int y = 0; y < HEIGHT; y++) {
                Coords myCoords = new Coords(x, y);
                positions.add(myCoords);
            }
        //reset cells, pick out position of monsters from list of all positions
        cells = new Cell [WIDTH][HEIGHT];
        for (int strength = 1; strength < monsterNumbers.length;strength++)
            for (int m = 0; m < monsterNumbers[strength]; m++)
            {
                int choice = (int) (Math.random() * positions.size());
                Coords myCoords = positions.get(choice);
                Cell myCell = new Cell(strength);
                int x = myCoords.getX();
                int y = myCoords.getY();
                cells[x][y] = myCell;
                positions.remove(choice);
            }

        //calculate neighbor cells power
        for (int x = 0; x < WIDTH; x++)
            for (int y = 0; y < HEIGHT; y++)
            {
                int counts = 0;
                if (x > 0 && y > 0 && cells[x - 1][y - 1] != null && cells[x - 1][y - 1].getMonsterStr() > 0)
                    counts = counts + cells[x - 1][y - 1].getMonsterStr();//up left

                if (y > 0 && cells[x][y - 1] != null && cells[x][y - 1].getMonsterStr() > 0)
                    counts = counts + cells[x][y - 1].getMonsterStr();//up

                if (x < (WIDTH - 1) && y > 0 && cells[x + 1][y - 1] != null && cells[x + 1][y - 1].getMonsterStr() > 0)
                    counts = counts + cells[x + 1][y - 1].getMonsterStr();//up right

                if (x < (WIDTH - 1) && cells[x + 1][y] != null && cells[x + 1][y].getMonsterStr() > 0)
                    counts = counts + cells[x + 1][y].getMonsterStr();//right

                if (x < (WIDTH - 1) && y < (HEIGHT - 1) && cells[x + 1][y + 1] != null && cells[x + 1][y + 1].getMonsterStr() > 0)
                    counts = counts + cells[x + 1][y + 1].getMonsterStr();//down right

                if (y < (HEIGHT - 1) && cells[x][y + 1] != null && cells[x][y + 1].getMonsterStr() > 0)
                    counts = counts + cells[x][y + 1].getMonsterStr();//down

                if (x > 0 && y < (HEIGHT - 1) && cells[x - 1][y + 1] != null && cells[x - 1][y + 1].getMonsterStr() > 0)
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
    public void ClearZeros(int x,int y)
    {
        if(cells[x][y].checkIsEmpty())
        {
            buttons[x][y].setEnabled(false);
            if (y>0 && buttons[x][y-1].isEnabled() && cells[x][y-1].getMonsterStr() == 0)
                ClearDirection(x,y-1);//up

            if (x<cells.length-1 && buttons[x+1][y].isEnabled() && cells[x+1][y].getMonsterStr() == 0)
                ClearDirection(x+1,y);//right

            if (y<cells[0].length-1 && buttons[x][y+1].isEnabled() && cells[x][y+1].getMonsterStr() == 0)
                ClearDirection(x,y+1);//down

            if (x>0 && buttons[x-1][y].isEnabled() && cells[x-1][y].getMonsterStr() == 0)
                ClearDirection(x-1,y);//left

            if (x>0 && y>0 && cells[x-1][y-1].getMonsterStr() == 0 && !cells[x - 1][y - 1].checkIsEmpty())
                ClearDirection(x-1,y-1);//up left

            if (x<cells.length-1 && y>0 && cells[x+1][y-1].getMonsterStr() == 0 && !cells[x + 1][y - 1].checkIsEmpty())
                ClearDirection(x+1,y-1);//up right

            if (x<cells.length-1 && y<cells[0].length-1 && cells[x+1][y+1].getMonsterStr() == 0&& !cells[x + 1][y + 1].checkIsEmpty())
                ClearDirection(x+1,y+1);//down right

            if (x>0 && y<cells[0].length-1 && cells[x-1][y+1].getMonsterStr() == 0 && !cells[x - 1][y + 1].checkIsEmpty())
                ClearDirection(x-1,y+1);//down left
        }
    }
    public void ClearDirection(int x, int y)
    {
        if(cells[x][y].checkIsEmpty())
        {
            ClearZeros(x,y);
        } else Click(x,y);
    }
    public void Click(int x,int y)
    {
        String display = cells[x][y].getDisplayVal();
        if(!"0".equals(display))
            buttons[x][y].setText(display);
        buttons[x][y].setEnabled(false);
    }
    public void checkWin()
    {
        boolean isWon = true;
        for (int x = 0;x < WIDTH; x++)
            for (int y = 0; y < HEIGHT; y++)
                if (buttons[x][y].isEnabled() && cells[x][y].getMonsterStr() == 0)
                {
                    isWon = false;
                    x = WIDTH;
                    y = HEIGHT;
                }
        if (isWon)
        {
            JOptionPane.showMessageDialog(frame, "You Win!");
            for (int x = 0; x < WIDTH; x++)
                for (int y = 0; y < HEIGHT; y++)
                    RevealMonsters(x, y);
        }
    }
    public void RevealMonsters(int x, int y)
    {
        if( cells[x][y].getMonsterStr() > 0)
        {
            buttons[x][y].setBackground(monsterColors[cells[x][y].getMonsterStr()]);
            buttons[x][y].setText(cells[x][y].getDisplayVal());
        }
    }
    public void GameOver(int A, int B)
    {
        for (int x = 0; x < WIDTH; x++)
            for (int y = 0; y < HEIGHT; y++)
            {
                if (buttons[x][y].isEnabled())
                {
                    buttons[x][y].setBackground(Color.LIGHT_GRAY);
                    RevealMonsters(x, y);
                }
                buttons[x][y].setEnabled(false);
            }
        buttons[A][B].setBackground(Color.BLACK);
        buttons[A][B].setText(cells[A][B].getDisplayVal());
    }
    @Override
    public void actionPerformed(ActionEvent event)
    {
        if(event.getSource().equals(reset))
        {
            for (int x = 0; x < WIDTH; x++)
            {
                for (int y = 0; y < HEIGHT; y++)
                {
                    buttons[x][y].setEnabled(true);
                    buttons[x][y].setText("");
                    buttons[x][y].setBackground(monsterColors[0]);
                }
            }
            createRandomMonsters();
        }
        else
        {
            for (int x = 0; x < WIDTH; x++)
                for (int y = 0; y < HEIGHT; y++)
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