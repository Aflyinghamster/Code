package sokoban;

import java.io.*;
import java.util.*;

/**
 * A text-based user interface for a Sokoban puzzle.
 * 
 * @author Dr Mark C. Sinclair
 * @version September 2021
 */
public class SokobanUI {
    /**
     * Default constructor
     */
    public SokobanUI() {
        scnr   = new Scanner(System.in);
        puzzle = new Sokoban(new File(FILENAME));
        player = new RandomPlayer(); 
        playerMove = new Stack<Direction>(); 
    }

    /**
     * Main control loop.  This displays the puzzle, then enters a loop displaying a menu,
     * getting the user command, executing the command, displaying the puzzle and checking
     * if further moves are possible
     */
    public void menu() {
        String command = "";
        System.out.print(puzzle);
        while (!command.equalsIgnoreCase("Quit") && !puzzle.onTarget())  {
            displayMenu();
            command = getCommand();
            execute(command);
            System.out.print(puzzle);
            if (puzzle.onTarget())
                System.out.println("puzzle is complete");
            trace("onTarget: "+puzzle.numOnTarget());
        }
    }

    /**
     * Display the user menu
     */
    private void displayMenu()  {
        System.out.println("Commands are:");
        System.out.println("   Move North         [N]");
        System.out.println("   Move South         [S]");
        System.out.println("   Move East          [E]");
        System.out.println("   Move West          [W]");
        System.out.println("   Player move        [P]");
        System.out.println("   Undo move          [U]");
        System.out.println("   Restart puzzle [Clear]");
        System.out.println("   Save to file    [Save]");
        System.out.println("   Load from file  [Load]");
        System.out.println("   To end program  [Quit]");    
    }

    /**
     * Get the user command
     * 
     * @return the user command string
     */
    private String getCommand() {
        System.out.print ("Enter command: ");
        return scnr.nextLine();
    }

    /**
     * Execute the user command string
     * 
     * @param command the user command string
     */
    private void execute(String command) {
        if (command.equalsIgnoreCase("Quit")) {
            System.out.println("Program closing down"); 
            System.exit(0);
        } else if (command.equalsIgnoreCase("N")) {
            north();
        } else if (command.equalsIgnoreCase("S")) {
            south();
        } else if (command.equalsIgnoreCase("E")) {
            east();
        } else if (command.equalsIgnoreCase("W")) {
            west();
        } else if (command.equalsIgnoreCase("P")) {
            playerMove();
        } else if (command.equalsIgnoreCase("U")) {
            undo();
        } else if (command.equalsIgnoreCase("Clear")) {
            clear(); 
        } else if (command.equalsIgnoreCase("Save")) {
            save(); 
        } else if (command.equalsIgnoreCase("Load")) {
            load(); 
        } else {
            System.out.println("Unknown command (" + command + ")");
        }
    } 
    private void load()
    {
        try{
            Scanner fscnr = new Scanner(new File(SAVEFILE)); 
            clear(); 
            while (fscnr.hasNextInt())
            { 
            // dir = new Direction(fscnr.fromString()); 
            // Direction dir = new Direction(fscnr).fromString();   attempting to convert the string to an enum
            // Direction.fromString(); 
            // Direction dir = new Direction(fscnr); 
            Direction dir = Direction.valueOf(fscnr.next().toUpperCase()); // load the string from a file an convert it to enum this is case sensitive 
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
    private void clear()
    {
        puzzle = new Sokoban(new File(FILENAME)); // creates a new instance of the puzzle and load FILENAME
        player = new RandomPlayer(); // create a new instsnce of the player 
        playerMove = new Stack<Direction>(); // create a new stack of player moves  
    } 
    private void save() 
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
    private void undo()
    {
        if(playerMove.empty())
        return;  // if there has been no moves use the old stack 
        Direction top = playerMove.pop(); // discard the last move from top  
        Stack<Direction> oldMove = playerMove; // create new stack to prevent loss  
        for (Direction dir : oldMove) // rebuild puzzle withourt last user move 
        {
            puzzle.move(dir); // the move is equal to the top of the stack
            playerMove.push(dir); // add to the stack 
        }
    }

    /**
     * Move the actor north
     */
    private void north() {
        move(Direction.NORTH); 
       // save(); 
    }

    /**
     * Move the actor south
     */
    private void south() {
        move(Direction.SOUTH);
       // save(); 
    }

    /**
     * Move the actor east
     */
    private void east() {
        move(Direction.EAST);
        //save(); 
    }

    /**
     * Move the actor west
     */
    private void west() {
        move(Direction.WEST);
      //  save(); 
    }

    /**
     * Move the actor according to the computer player's choice
     */
    private void playerMove() {
        Vector<Direction> choices = puzzle.canMove();
        Direction         choice  = player.move(choices);
        move(choice);
    }  

    /**
     * If it is safe, move the actor to the next cell in a given direction
     * 
     * @param dir the direction to move
     */
    private void move(Direction dir) {
        if (!puzzle.canMove(dir)) {
            System.out.println("invalid move");
            return;
        }
        puzzle.move(dir);
        if (puzzle.onTarget())
            System.out.println("game won!");
    }

    public static void main(String[] args) {
        SokobanUI ui = new SokobanUI(); 
         
        ui.menu();
    }

    /**
     * A trace method for debugging (active when traceOn is true)
     * 
     * @param s the string to output
     */
    public static void trace(String s) {
        if (traceOn)
            System.out.println("trace: " + s);
    }

    private Scanner scnr           = null;
    private Sokoban puzzle         = null;
    private Player  player         = null;
    private static String  FILENAME = "screens/screen.2"; 
    private static String  SAVEFILE = "saveGame.txt";
    private static String UPDATE = "updateScreen";
    private Stack<Direction> playerMove = null;

    private static boolean   traceOn = false; // for debugging
}
