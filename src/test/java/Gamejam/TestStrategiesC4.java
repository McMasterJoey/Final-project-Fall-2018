package Gamejam;


import org.junit.Test;

import connectFour.ConnectFourAI;
import connectFour.ConnectFourEasyAI;
import connectFour.ConnectFourHardAI;
import connectFour.ConnectFourModel;

public class TestStrategiesC4 {
	  @Test
	  public void run100ConnectFourGames() {
	    // This is currently an infinite loop since the two strategies
	    // always return the same Point with .x and .y
	    ConnectFourAI randomBot = new ConnectFourAI();
	    randomBot.setStrategy(new ConnectFourEasyAI());
	    ConnectFourAI IntermediateBot = new ConnectFourAI();
	    IntermediateBot.setStrategy(new ConnectFourHardAI());

	    int randomPlayerWins = 0;
	    int IntermediatePlayerWins = 0;
	    int ties = 0;

	    for (int game = 1; game <= 100; game++) {
	      char winner = playOneGame(IntermediateBot, randomBot);
	      if (winner == 'R')
	        IntermediatePlayerWins++;
	      if (winner == 'Y')
	        randomPlayerWins++;
	      if (winner == 'T')
	        ties++;
	    }

	    System.out.println("IntermediateAI strategy should have many more wins than the");
	    System.out.println("RandomAI strategy. This tournament has the Intermediate");
	    System.out.println("strategy choose first.  Ties can certainly happen.");
	    System.out.println("===========================================");
	    System.out.println("Intermediate win percentage: " + IntermediatePlayerWins / 10. + "%");
	    System.out.println("Random win percentage: " + randomPlayerWins/ 10. + "%");
	    System.out.println("Ties happened " + ties/ 10. + "% of the games");
	  }
	  
	  private char playOneGame( ConnectFourAI first, ConnectFourAI second) {
		    ConnectFourModel theGame = new ConnectFourModel();

		    while (true) {
		      int firstsMove = first.nextMove(theGame);
		      theGame.humanMove(firstsMove, true);

		      if (theGame.tied())
		        return 'T';

		      if (theGame.won('R'))
		        return 'R';
		      if (theGame.won('Y'))
		        return 'Y';

		      int secondsMove = second.nextMove(theGame);
		      theGame.computerMove(secondsMove);

		      if (theGame.tied())
		        return 'T';

		      if (theGame.won('R'))
		        return 'R';
		      if (theGame.won('Y'))
		        return 'Y';
		    }
		  }
}
