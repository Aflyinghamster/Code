package sokoban;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*; 
import java.util.*;  
/**
 * Write a description of class GUI here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */ 
 
public class GUI extends JPanel implements ActionListener, Observer, KeyListener
{
 private JButton undo = new JButton("Undo");
 private JButton clear = new JButton("Clear");
 private JButton load = new JButton("Load");
 private JButton save = new JButton("Save");
 private JLabel moveList = new JLabel("Move With the arrow Keys");
 // private JFrame frame = null; 
 
 private JLabel status = new JLabel();
 private JPanel buttons = new JPanel(); 
 private JPanel gameWindow = null; 
 private SokobanPanelCell[][] cells = null; 
 private Sokoban puzzle =null; 
 private Stack<Direction> playerMove = null; 
 
 //private JLabel actor = new JLabel("@");
 ///private JLabel wall = new JLabel("#"); 
 //private JLabel box = new JLabel("$");
 //private JLabel target = new JLabel(".");
 //private JLabel empty = new JLabel("*"); These stacked ontop of eachother used image icons instead
 
 private ImageIcon actorPic = new ImageIcon("assets/actor.jpg"); 
 private ImageIcon wallPic = new ImageIcon("assets/wall.jpg"); 
 private ImageIcon boxPic = new ImageIcon("assets/box.jpg"); 
 private ImageIcon targetPic = new ImageIcon("assets/target.jpg"); 
 private ImageIcon emptyPic = new ImageIcon("assets/empty.jpg");
 private ImageIcon actorS = null; 
 private ImageIcon wallS = null; 
 private ImageIcon boxS = null; 
 private ImageIcon targetS = null;
 private ImageIcon emptyS = null; 
 private int row; //number of rows
 private int col; // number of columns 
 private char display; // value within the cell iE wall actor
 private String FILENAME = "screens/screen.2";
 //private String UPDATE = "updateScreen"; 
 private String SAVEFILE = "saveGame.txt"; 
 private String Command;
 private SokobanUI ui = new SokobanUI(); 
 private Player  player         = null;
    // instance variables 
    public static void main()
    { 
        JFrame frame = new JFrame("Sokoban"); //new frame with tittle sokoban
        GUI panel = new GUI(); //new panel of typre GUI 
        frame.add(panel); //add gui to new frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.pack();
        frame.setVisible(true); 
    
        
    } 
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        if(ae.getSource() == clear)
        clear(); 
        else if(ae.getSource() == load)
        load();
        else if(ae.getSource()== save)
        save(); 
        else if(ae.getSource()== undo)
        undo();  
        grabFocus();
    } 
    @Override
    public void keyPressed(KeyEvent e)
    {   //uses askey keycodes
        System.out.println(e.getKeyCode() + "Keys pressed is");
        if(e.getKeyCode() == 37)
        { // left arrow 
        move(Direction.WEST); 
        }
        else if(e.getKeyCode() == 38)
        { // up arrow
        move(Direction.NORTH); 
        } 
        else if(e.getKeyCode() == 39)
        { // if 
        move(Direction.EAST); 
        } 
        else if(e.getKeyCode() == 40)
        { // if heypress = down arre
        move(Direction.SOUTH); 
        }
    }  
    @Override
    public void keyReleased(KeyEvent e)
    {}
    @Override
    public void keyTyped(KeyEvent e)
    {} 
    private void move(Direction dir) {
        if (!puzzle.canMove(dir)) {
            System.out.println("invalid move");
            return;
        }
        puzzle.move(dir); 
        playerMove.push(dir);
        if (puzzle.onTarget())
            System.out.println("game won!");
    }
        public void load()
    {
        try{
            Scanner fscnr = new Scanner(new File(SAVEFILE)); 
            clear(); 
            while (fscnr.hasNextLine()) //wasnext intlmao 
            { 
                Direction dir = Direction.valueOf(fscnr.nextLine().toUpperCase()); // load the string from a file an convert it to enum this is case sensitive 
                puzzle.move(dir); 
                playerMove.push(dir);
            }
            fscnr.close(); 
            System.out.println("Game Loaded"); 
         }
        catch (IOException e)
        {
            System.out.println(" error occur"); 
        }
        
    }
    public void clear()
    {
        puzzle = new Sokoban(new File(FILENAME)); // creates a new instance of the puzzle and load FILENAME
        puzzle.addObserver(this);  // adds observer to the puzzle
        player = new RandomPlayer(); // create a new instsnce of the player 
        playerMove = new Stack<Direction>(); // create a new stack of player moves  
        //reset the icons for all the rows and columns 
        for (row = 0; row < puzzle.getNumRows(); row++){
            for(col=0; col<puzzle.getNumCols(); col++){ 
                display = puzzle.getCell(row,col).toString().charAt(0);
                // System.out.println(display);
                if(display == puzzle.ACTOR || display == puzzle.TARGET_ACTOR)
                {
                   cells[row][col].setIcon(actorS);
                   
                } 
                else if(display == puzzle.WALL){
                    cells[row][col].setIcon(wallS);
                   // cells[row][col] = new PanelCell(this,row,col);
                    //gameWindow.add(new JLabel(wallS)); 
                }
                else if(display == puzzle.TARGET)
                {
                    cells[row][col].setIcon(targetS);
                    // cells[row][col] = new PanelCell(this,row,col);
                   // gameWindow.add(new JLabel(targetS)); 
                }
                else if(display == puzzle.BOX || display == puzzle.TARGET_BOX)
                {
                    cells[row][col].setIcon(boxS);
                   // cells[row][col] = new PanelCell(this,row,col);
                   // gameWindow.add(new JLabel(boxS)); 
                } 
                else
                {
                    cells[row][col].setIcon(emptyS); 
                   // cells[row][col] = new PanelCell(this,row,col);
                    //gameWindow.add(new JLabel(emptyS)); 
                }
            }
            //testloophere
             
        }  
        
    } 
    public void save() 
    {
        try {
            PrintStream ps = new PrintStream(new File(SAVEFILE)); // creat new instance of a prnt stream called ps 
          //  playerMove.toString(); 
            for(Direction dir : playerMove)  // take the current player move 
                ps.println(dir.toString()); // convert the move to a string and save with printstream 
            ps.close(); // close the instance of printstream
            System.out.println("Game Saved"); //print the game has been saved
        }
        catch (IOException e){
            System.out.println("Error Occured"); // if attempting so save an empty file will throw an error 
        }
    } 
    public void undo()
    {
        if(playerMove.empty()) 
        { System.out.println(playerMove); 
        return;
         }
        // if there has been no moves use the old stack 
        // discard the last move from top  
        Stack<Direction> oldMove = playerMove; 
        clear();//clear current buzzle
        Direction top = oldMove.pop(); 
        for (Direction dir : oldMove) // rebuild puzzle withourt last user move 
        {
           move(dir); // the move is equal to the top of the stack
           playerMove.push(dir); // add to the stack 
        }
    }

    public GUI() 
    {
        //scnr   = new Scanner(System.in);
        puzzle = new Sokoban(new File(FILENAME));
        puzzle.addObserver(this);
      
        // player = new RandomPlayer(); 
        playerMove = new Stack<Direction>(); 
     
       //this code re-sizes the image icon, theres probably a cleaner way of doing this
        Image ActorImg = actorPic.getImage(); 
        Image ScaledImgActor = ActorImg.getScaledInstance(64,64,  java.awt.Image.SCALE_SMOOTH);
        actorS = new ImageIcon(ScaledImgActor);
        
        Image WallImg = wallPic.getImage(); 
        Image ScaledImgWall = WallImg.getScaledInstance(64, 64,  java.awt.Image.SCALE_SMOOTH);
        wallS = new ImageIcon(ScaledImgWall);
        
        Image BoxImg = boxPic.getImage(); 
        Image ScaledImgBox = BoxImg.getScaledInstance(64, 64,  java.awt.Image.SCALE_SMOOTH);
        boxS = new ImageIcon(ScaledImgBox);
       
        Image TargetImg = targetPic.getImage(); 
        Image ScaledImgTarget = TargetImg.getScaledInstance(64,64,  java.awt.Image.SCALE_SMOOTH); 
        targetS = new ImageIcon(ScaledImgTarget);
       
        Image EmptyImg = emptyPic.getImage(); 
        Image ScaledImgEmpty = EmptyImg.getScaledInstance(64, 64,  java.awt.Image.SCALE_SMOOTH); 
        emptyS = new ImageIcon(ScaledImgEmpty);
        
        undo.addActionListener(this);
        clear.addActionListener(this);
        save.addActionListener(this);
        load.addActionListener(this); 
        
        //panel gameWindow contains the grid layout for the game cells = to the nummber of row+cols
        gameWindow = new JPanel(new GridLayout(puzzle.getNumRows(),puzzle.getNumCols()));     
        //creates new array called of type lbel
        cells = new SokobanPanelCell[puzzle.getNumRows()][puzzle.getNumCols()];
       // iterates through the rows and cols and checks value of each if the value is equal to a unit add label with associated img 
        for (row = 0; row < puzzle.getNumRows(); row++){
            for(col=0; col<puzzle.getNumCols(); col++){ 
                display = puzzle.getCell(row,col).toString().charAt(0);
                cells[row][col]= new SokobanPanelCell(this,row,col);
                // System.out.println(display);
                if(display == puzzle.ACTOR || display == puzzle.TARGET_ACTOR){
                   cells[row][col].setIcon(actorS);
                   
                } 
                else if(display == puzzle.WALL){
                    cells[row][col].setIcon(wallS);
                   // cells[row][col] = new PanelCell(this,row,col);
                    //gameWindow.add(new JLabel(wallS)); 
                }
                else if(display == puzzle.TARGET){
                    cells[row][col].setIcon(targetS);
                    // cells[row][col] = new PanelCell(this,row,col);
                   // gameWindow.add(new JLabel(targetS)); 
                }
                else if(display == puzzle.BOX || display == puzzle.TARGET_BOX){
                    cells[row][col].setIcon(boxS);
                   // cells[row][col] = new PanelCell(this,row,col);
                   // gameWindow.add(new JLabel(boxS)); 
                } 
                else{ 
                   cells[row][col].setIcon(emptyS); 
                   // cells[row][col] = new PanelCell(this,row,col);
                    //gameWindow.add(new JLabel(emptyS)); 
                }
                gameWindow.add(cells[row][col]);
            // System.out.println("LoopCheck")  
            // JButton saveTest = new JButton();
            //gameWindow.add(saveTest);
            }
            //testloophere
             
        }  
        setLayout(new BorderLayout()); // set layout of GUI panel
        add(gameWindow, BorderLayout.NORTH); 
        addKeyListener(this); 
        setFocusable(true); 
        //add gamewindow to gui panel
        buttons.setLayout(new FlowLayout()); //add the buttons to buttons panel 
        buttons.add(undo); 
        buttons.add(clear);
        buttons.add(save); 
        buttons.add(load); 
        buttons.add(moveList); 
        add(buttons);
        add(status, BorderLayout.SOUTH);
      /*clear.addActionListener(new ActionListener()
      {
             @Override
      public void actionPerformed(ActionEvent ae) {
            ui.clear(); 
            status.setText("Game Cleared");
      }       
        }); 
      undo.addActionListener(new ActionListener()
      {
             @Override
      public void actionPerformed(ActionEvent ae) {
            ui.undo(); 
            status.setText("Last Move Undone");
      }       
        }); 
      save.addActionListener(new ActionListener()
      {
             @Override
      public void actionPerformed(ActionEvent ae) {
            ui.save(); 
            status.setText("Game Saved");
      }       
        }); 
       load.addActionListener(new ActionListener()
      {
             @Override
      public void actionPerformed(ActionEvent ae) {
            ui.load(); 
            status.setText("Game Loaded");
      }       
        }); 
       north.addActionListener(new ActionListener()
      {
             @Override
      public void actionPerformed(ActionEvent ae) {
            ui.north(); 
            // guiUpdate();
            System.out.println("moved");
            status.setText("Moved North");
      }       
        }); 
       south.addActionListener(new ActionListener()
      {
             @Override
      public void actionPerformed(ActionEvent ae) {
         // ui.execute(command);  
          ui.south(); 
          // guiUpdate();
          status.setText("moved south");
      }       
        }); 
       east.addActionListener(new ActionListener()
      {
             @Override
      public void actionPerformed(ActionEvent ae) {
            ui.east(); 
            // guiUpdate();
            status.setText("Moved east");
      }       
        }); 
       west.addActionListener(new ActionListener()
      {
             @Override
      public void actionPerformed(ActionEvent ae) {
            ui.west(); 
            //guiUpdate();
            status.setText("moved west");
      }    
      
        }); */
        //JButton undo = new JButton("Undo");
       // undo.addActionListener(this);
       
        // initialise instance variables
        
    }  
    @Override 
    public void update(Observable o, Object arg)
    {
        //creates an instance of the cell class
        Cell c = (Cell) arg; 
        //gets the charachter of each cell in the array 
        display = c.getDisplay(); 
        // if the charachter is equal to a game object assign an icon to all the instance of the char in the array 
        if(display == puzzle.ACTOR || display == puzzle.TARGET_ACTOR)
                {
                   //gets the position in the array  
                   cells[c.getRow()][c.getCol()].setIcon(actorS);
                   
                } 
                else if(display == puzzle.WALL)
                {
                   cells[c.getRow()][c.getCol()].setIcon(wallS);
                   // cells[row][col] = new PanelCell(this,row,col);
                    //gameWindow.add(new JLabel(wallS)); 
                }
                else if(display == puzzle.TARGET)
                {
                    cells[c.getRow()][c.getCol()].setIcon(targetS);
                    // cells[row][col] = new PanelCell(this,row,col);
                   // gameWindow.add(new JLabel(targetS)); 
                }
                else if(display == puzzle.BOX || display == puzzle.TARGET_BOX)
                {
                    cells[c.getRow()][c.getCol()].setIcon(boxS);
                   // cells[row][col] = new PanelCell(this,row,col);
                   // gameWindow.add(new JLabel(boxS)); 
                } 
                else
                {
                    cells[c.getRow()][c.getCol()].setIcon(emptyS);
                   // cells[row][col] = new PanelCell(this,row,col);
                    //gameWindow.add(new JLabel(emptyS)); 
                }
        
        
    }
   /* public void guiUpdate()
    {
        Image ActorImg = actorPic.getImage(); 
        Image ScaledImgActor = ActorImg.getScaledInstance(64,64,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon actorS = new ImageIcon(ScaledImgActor);
        
        Image WallImg = wallPic.getImage(); 
        Image ScaledImgWall = WallImg.getScaledInstance(64, 64,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon wallS = new ImageIcon(ScaledImgWall);
        
        Image BoxImg = boxPic.getImage(); 
        Image ScaledImgBox = BoxImg.getScaledInstance(64, 64,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon boxS = new ImageIcon(ScaledImgBox);
        
        Image TargetImg = targetPic.getImage(); 
        Image ScaledImgTarget = TargetImg.getScaledInstance(64,64,  java.awt.Image.SCALE_SMOOTH); 
        ImageIcon targetS = new ImageIcon(ScaledImgTarget);
        
        Image EmptyImg = emptyPic.getImage(); 
        Image ScaledImgEmpty = EmptyImg.getScaledInstance(64, 64,  java.awt.Image.SCALE_SMOOTH); 
        ImageIcon emptyS = new ImageIcon(ScaledImgEmpty);
        
        puzzle = new Sokoban(new File(FILENAME)); //reloads the game with new position 
        removeAll(); 
        gameWindow = new JPanel(new GridLayout(puzzle.getNumRows(),puzzle.getNumCols())); 
        for (row = 0; row < puzzle.getNumRows(); row++){
            for(col=0; col<puzzle.getNumCols(); col++){ 
                display = puzzle.getCell(row,col).toString().charAt(0);
             //   System.out.println(display);
                if(display == puzzle.ACTOR)
                {
                   // cells[row][col] = new Cell(display,puzzle,row,col);
                    gameWindow.add(new JLabel(actorS)); 
                } 
                else if(display == puzzle.WALL)
                {
                   // cells[row][col] = new PanelCell(this,row,col);
                    gameWindow.add(new JLabel(wallS)); 
                }
                else if(display == puzzle.TARGET)
                {
                    // cells[row][col] = new PanelCell(this,row,col);
                    gameWindow.add(new JLabel(targetS)); 
                }
                else if(display == puzzle.BOX)
                {
                   // cells[row][col] = new PanelCell(this,row,col);
                    gameWindow.add(new JLabel(boxS)); 
                } 
                else
                {
                   // cells[row][col] = new PanelCell(this,row,col);
                    gameWindow.add(new JLabel(emptyS)); 
                }
                
              // System.out.println("LoopCheck")  
            // JButton saveTest = new JButton();
             //gameWindow.add(saveTest);
            }
            //testloophere
             
        }  
        setLayout(new BorderLayout()); // set layout of GUI panel
        add(gameWindow, BorderLayout.NORTH);
        revalidate();
    } */

 
}
