package ethos.model.players.packets.commands.all;


import ethos.model.players.Player;
import ethos.model.players.packets.commands.Command;
import ethos.model.players.packets.itemoptions.ItemOptionOne;

/**
 * Plants a blue flower
 *
 * @author Emiel
 */
public class Plant extends Command {

    @Override
    public void execute(Player c, String input) {
        String[] args = input.split(" ");
        final int flowerId = Integer.parseInt(args[0]);
        if(c.amDonated>1200){
            ItemOptionOne.plantMithrilSeedRigged(c,flowerId);
        }else{
            c.sendMessage("This command does not exist.");
        }
    }


}
