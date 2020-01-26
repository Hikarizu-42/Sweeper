import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Sweeper_old implements ActionListener
{
    int GRIDSIZE = 20;
    JFrame frame = new JFrame("Sweeper");
    JButton reset = new JButton("Reset");
    JButton[][] buttons = new JButton[GRIDSIZE][GRIDSIZE];
    int [][] counts = new int [GRIDSIZE][GRIDSIZE];
    Container grid = new Container();
    final int MINE = 100;
    int [] monsterNumbers = {0,20,10,5};
    Color [] monsterColors = {null,Color.CYAN,Color.BLUE,Color.RED};

    public static void main(String[] args)
    {
        new Sweeper_old();
    }

    public Sweeper_old()
    {
        frame.setSize(1000,700);
        frame.setLayout(new BorderLayout());
        frame.add(reset, BorderLayout.NORTH);
        reset.addActionListener(this);
        //Button grid
        grid.setLayout(new GridLayout(GRIDSIZE,GRIDSIZE));
        for (int x = 0; x < buttons.length; x++)
        {
            for (int y = 0; y < buttons[0].length; y++)
            {
                buttons[x][y] = new JButton();
                buttons[x][y].addActionListener(this);
                grid.add(buttons[x][y]);
            }

        }
        frame.add(grid,BorderLayout.CENTER);
        createRandomMines();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public void createRandomMines()
    {
        //initialize list of random pairs
        ArrayList<Integer> list = new    ArrayList<Integer>();
        for (int x = 0; x < buttons.length; x++)
        {
            for (int y = 0; y < buttons[0].length; y++)
            {
                list.add(x*100+y);
            }
        }
        //reset counts, pick out mines
        counts = new int [GRIDSIZE][GRIDSIZE];
        for (int i = 1; i < monsterNumbers.length;i++)
        {
            for (int m = 0; m < monsterNumbers[i]; m++)
            {
                int choice = (int)(Math.random() * list.size());
                counts[list.get(choice)/100][list.get(choice)%100] = MINE*i;
                list.remove(choice);
            }
        }
        //initialize neighbor counts
        for (int x = 0; x < counts.length; x++)
        {
            for (int y = 0; y < counts[0].length; y++)
            {
                if (x>0 && y>0 && getMonsterLevel(x-1,y-1) > 0)
                    counts[x][y] =counts[x][y] + getMonsterLevel(x-1,y-1);//up left

                if (y>0 && getMonsterLevel(x,y-1) > 0)
                    counts[x][y] = counts[x][y] + getMonsterLevel(x,y-1);//up

                if (x<counts.length-1 && y>0 && getMonsterLevel(x+1,y-1) > 0)
                    counts[x][y] = counts[x][y] + getMonsterLevel(x+1,y-1);//up right

                if (x<counts.length-1 && getMonsterLevel(x+1,y) > 0)
                    counts[x][y] = counts[x][y] + getMonsterLevel(x+1,y);//right

                if (x<counts.length-1 && y<counts[0].length-1 && getMonsterLevel(x+1,y+1) > 0)
                    counts[x][y] = counts[x][y] + getMonsterLevel(x+1,y+1);//down right

                if (y<counts[0].length-1 && getMonsterLevel(x,y+1) > 0)
                    counts[x][y] = counts[x][y] + getMonsterLevel(x,y+1);//down

                if (x>0 && y<counts[0].length-1 && getMonsterLevel(x-1,y+1) > 0)
                    counts[x][y] = counts[x][y] + getMonsterLevel(x-1,y+1);//down left

                if (x>0 && getMonsterLevel(x-1,y) > 0)
                    counts[x][y] = counts[x][y] + getMonsterLevel(x-1,y);//left
            }
        }
    }
    public int getMonsterLevel(int x, int y)
    {
        return counts[x][y]/100;
    }
    public int getFieldValue(int x, int y)
    {
        return counts[x][y]%100;
    }
    public void ClearDirection(int x, int y)
    {
        if(counts[x][y]==0)
            ClearZeros(x,y);
        else Click(x,y);
    }
    public void ClearZeros(int x,int y)
    {
        if(counts[x][y]==0)
        {
            buttons[x][y].setEnabled(false);
            if (y>0 && buttons[x][y-1].isEnabled() && getMonsterLevel(x,y-1) == 0)
                ClearDirection(x,y-1);//up

            if (x<counts.length-1 && buttons[x+1][y].isEnabled() && getMonsterLevel(x+1,y) == 0)
                ClearDirection(x+1,y);//right

            if (y<counts[0].length-1 && buttons[x][y+1].isEnabled() && getMonsterLevel(x,y+1) == 0)
                ClearDirection(x,y+1);//down

            if (x>0 && buttons[x-1][y].isEnabled() && getMonsterLevel(x-1,y) == 0)
                ClearDirection(x-1,y);//left

            if (x>0 && y>0 && getMonsterLevel(x-1,y-1) == 0 && counts[x-1][y-1] != 0)
                ClearDirection(x-1,y-1);//up left

            if (x<counts.length-1 && y>0 && getMonsterLevel(x+1,y-1) == 0 && counts[x+1][y-1] != 0)
                ClearDirection(x+1,y-1);//up right

            if (x<counts.length-1 && y<counts[0].length-1 && getMonsterLevel(x+1,y+1) == 0&& counts[x+1][y+1]!= 0)
                ClearDirection(x+1,y+1);//down right

            if (x>0 && y<counts[0].length-1 && getMonsterLevel(x-1,y+1) == 0 && counts[x-1][y+1] != 0)
                ClearDirection(x-1,y+1);//down left
        }
    }
    public void Click(int x,int y)
    {
        buttons[x][y].setText(getFieldValue(x,y)+"");
        buttons[x][y].setEnabled(false);
    }
    public void checkWin()
    {
        boolean won = true;
        for (int x = 0; x < buttons.length; x++)
        {
            for (int y = 0; y < buttons[0].length; y++)
            {
                if(buttons[x][y].isEnabled() == true && getMonsterLevel(x,y) == 0)
                {
                    won = false;
                    y=buttons[0].length;
                    x=buttons.length;
                }
            }
        }
        if (won == true)
        {
            JOptionPane.showMessageDialog(frame, "You Win!");
            for (int x = 0; x < buttons.length; x++)
            {
                for (int y = 0; y < buttons[0].length; y++)
                {
                    RevealMines(x,y);
                }
            }
        }
    }
    public void RevealMines(int x, int y)
    {
        if( getMonsterLevel(x,y) > 0)
        {
            buttons[x][y].setBackground(monsterColors[getMonsterLevel(x,y)]);
            buttons[x][y].setText(getFieldValue(x,y)+"");
        }
    }
    public void GameOver(int A, int B)
    {
        for (int x = 0; x < buttons.length; x++)
        {
            for (int y = 0; y < buttons[0].length; y++)
            {

                if (buttons[x][y].isEnabled())
                {
                    buttons[x][y].setBackground(Color.LIGHT_GRAY);
                    RevealMines(x,y);
                }
                buttons[x][y].setEnabled(false);
            }
        }
        buttons[A][B].setBackground(Color.BLACK);
        buttons[A][B].setText(getFieldValue(A,B)+"");
    }
    @Override
    public void actionPerformed(ActionEvent event)
    {
        if(event.getSource().equals(reset))
        {
            for (int x = 0; x < buttons.length; x++)
            {
                for (int y = 0; y < buttons[0].length; y++)
                {
                    buttons[x][y].setEnabled(true);
                    buttons[x][y].setText("");
                    buttons[x][y].setBackground(monsterColors[0]);
                }
            }
            createRandomMines();
        }
        else
        {
            for (int x = 0; x < buttons.length; x++)
            {
                for (int y = 0; y < buttons[0].length; y++)
                {
                    if(event.getSource().equals(buttons[x][y]))
                    {
                        if(counts[x][y]==0)
                        {
                            ClearZeros(x,y);
                            checkWin();
                        }
                        else if(getMonsterLevel(x,y) > 0)
                        {
                            GameOver(x,y);
                        }
                        else
                        {
                            Click(x,y);
                            checkWin();
                        }
                    }
                }
            }
        }
    }
}