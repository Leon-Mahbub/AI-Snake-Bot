package de.frauas.stud;
import snakes.Bot;
import snakes.Coordinate;
import snakes.Direction;
import snakes.Snake;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.IntStream;


public class UasBot implements Bot {
    private static final Direction[] DIRECTIONS = new Direction[]{Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};


    @Override
    public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        Coordinate head = snake.getHead();


        Coordinate afterHeadNotFinal = null;
        if (snake.body.size() >= 2) {
            Iterator<Coordinate> it = snake.body.iterator();
            it.next();
            afterHeadNotFinal = it.next();
        }

        final Coordinate afterHead = afterHeadNotFinal;

        /* The only illegal move is going backwards. Here we are checking for not doing it */
        Direction[] validMoves = Arrays.stream(DIRECTIONS)
                .filter(d -> !head.moveTo(d).equals(afterHead)) // Filter out the backwards move
                .sorted()
                .toArray(Direction[]::new);

        /* Just naïve greedy algorithm that tries not to die at each moment in time */
        Direction[] notLosing = Arrays.stream(validMoves)
                .filter(d -> head.moveTo(d).inBounds(mazeSize))             // Don't leave maze
                .filter(d -> !opponent.elements.contains(head.moveTo(d)))   // Don't collide with opponent...
                .filter(d -> !snake.elements.contains(head.moveTo(d)))      // and yourself
                .sorted()
                .toArray(Direction[]::new);

        int l=0,r=0,u=0,d=0;
        for(int i=0;i<notLosing.length;i++) {
            if(notLosing[i]==Direction.DOWN.UP) u=1;
            else if (notLosing[i]==Direction.DOWN.DOWN) d=1;
            else if(notLosing[i]==Direction.DOWN.LEFT) l=1;
            else if(notLosing[i]==Direction.DOWN.RIGHT) r=1;

           // System.out.println(notLosing[i]);
        }

        if (notLosing.length > 0) {
            int ver_dif=apple.x-head.x;
            int hor_dif=apple.y-head.y;



            if(ver_dif<0 && l==1) return Direction.LEFT;
            if(ver_dif>0  && r==1) return Direction.RIGHT;

            if(hor_dif>0  && u==1) return Direction.UP;
            if(hor_dif<0  && d==1) return Direction.DOWN;
            return notLosing[0];
        }
        else return validMoves[0];

    }
}