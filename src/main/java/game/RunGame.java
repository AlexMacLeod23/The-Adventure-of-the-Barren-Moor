package game;

import game.event.*;
import game.manager.Destination;
import game.character.Player;
import game.util.Input;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RunGame {

	private static final int WIN_SCORE = 5000;
	private static final int RESET_DISTANCE = 3;
	private static final int ZERO = 0;

	public static void main(String[] args) {

		System.out.println("Welcome to The Adventure of the Barren Moor!");
		System.out.println("Please enter your name below:");
		String inputName = Input.scan.nextLine();
		Player player = new Player(inputName);

		List<Event> eventList = new ArrayList<>();
		eventList.add(new SmallTreasure());
		eventList.add(new SmallTreasure());
		eventList.add(new BigTreasure());

		Destination destination = new Destination();

		introduction();

		do {
			decideNextAction(player, destination);
			if (destination.isAtOrigin()) {
				int randomIndex = ThreadLocalRandom.current().nextInt(ZERO, eventList.size());
				Event randomEvent = eventList.get(randomIndex);
				System.out.println("You can now see what the device was pointing you towards");

				randomEvent.play();

				if (!randomEvent.isCompleted()) {
					break;
				}
				eventList.remove(randomEvent);
				System.out.println("You earn " + randomEvent.getValue() + " points!");
				player.addPoints(randomEvent.getValue());
				System.out.println("You now have " + player.getScore() + " points");
				if (player.getScore() >= WIN_SCORE) {
					player.wins();
				}
			}
			if (destination.isAtOrigin() || destination.calculateDistance() > RESET_DISTANCE) {
				destination.reset();
			}

		} while (!player.isVictorious());

		Input.scan.close();
		if (player.isVictorious()) {
			System.out.println("Congratulations, " + player.getName() + "! You have won the game");
		} else {
			System.out.println("Bad luck, " + player.getName() + "! You have lost the game");
		}
	}

	private static void introduction() {
		System.out.println("You awaken to find yourself alone, lying cold and wet on a barren moor.");
		System.out.println("Slowly, you pick yourself up from the ground and take in your bleak surroundings.");
		System.out.println("All around, you are surrounded by a thick grey fog. You cannot see much around you.");
		System.out.println("You can just about see a black weathered rock in front of you, struggling against"
				+ " being swallowed up by the fog.");
		System.out.println("You feel something strange in your pocket.");

		System.out.println("You take it out and examine it.");
		System.out.println("It looks a bit like a pocket watch. However, there is only one hand, "
				+ "and it has a small display.");
		System.out.println("The markings on it suggest that it might be a compass, but it definitely doesn't point north.");
		System.out.println("The display seems to show a distance. Is this device pointing you towards something?");
	}

	private static void decideNextAction(Player player, Destination destination) {
		System.out.println("What are you going to do?");
		String commandInput = Input.scan.nextLine();
		String[] command = commandInput.split(" ");

		switch (command[0]) {
			case "check":
				try {
					if ("score".equalsIgnoreCase(command[1])) {
						System.out.println("Your current score is " + player.getScore());
					} else if ("compass".equalsIgnoreCase(command[1])) {
						System.out.println("You take a moment to check the device.");
						System.out.println("The compass is pointing " + destination.findDirection().toLowerCase() + ".");
						System.out.println("The display reads: " + destination.calculateDistance() + " miles.");
					} else {
						System.out.println("Invalid arguments, can only check compass or score");
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Invalid arguments, must specify what you are checking: score or compass");
				}

				break;
			case "walk":
				try {
					destination.walk(command[1]);
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Invalid arguments, must specify a single direction: North, South, East, or West");
				}
				break;
			case "help":
				System.out.println("Valid commands: \"check\", \"walk\", \"help\"");
				break;
			default:
				System.out.println("Did not recognise command \"" + command[0] + "\". Use the \"help\" command to see list of valid commands");
				break;
		}
	}

}
