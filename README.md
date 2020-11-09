
# README

JavaScript OSRS Jad Simulator (No download!): https://downthecrop.github.io/JS-OSRS-Jad-Simulator/

This repository includes files from the Ascend client. You can find the release thread for the source I used to build this here: https://www.rune-server.ee/runescape-development/rs2-server/downloads/678691-osrs-ascend-raids-revs-fire-surge-vorkath.html

  

### How do I get set up?

* Summary of set up

You will need the Java Runtime Environment installed. You can check to see if you have Java installed by opening a command prompt (cmd.exe) and typing "java". You'll see some output if it's installed. Just double click on `windows.bat` or `unix.sh` to start it up and wait for the client to popup.

* Configuration

The practice tool is ready to go from the second you load it up. Just click Login and you're already in TzHaar by the bank with a Blowpipe and some prayer gear. Some extra supplies and a rune Cbow are in the bank. Resizable and fullscreen work! I suggest resizable for fight caves so you can see better.

  

* Is this program safe?

  

The source code for this program is packaged along side it. If you want to be 100% sure you can run the project in Intellij IDEA or Eclipse. You're looking for `Main.java` in `com.client` This RSPS practice tool is completely offline and doesn't access or request any web resources and only stores/modifies files in its own running directory. I'm just a big nerd helping smaller nerds.

  

### Additional Items

  

The account has full admin rights. you can use the ::item command to give yourself any items you need.

  

You can find item ID's here https://everythingrs.com/tools/osrs/itemlist/238

  

format -> ::item itemID amount

ie `::item 1163` -> would give you a rune full helm.

ie `::item 995 10000` -> would give you 10k coins.

  

---

  

Repost from https://downthecrop.xyz/blog/osrs-jad-simulator-practice-tool/

  

## OSRS Jad Simulator Practice Tool

Yo.

  

Thought I should just post this here because it was something that I worked on for a few hours two weeks ago. Might be useful to other people that had or are having difficulties figuring out Fight Caves.

  

[![Jad Simulator Practice Tool for Old School RuneScape.](https://downthecrop.xyz/blog/wp-content/uploads/2019/11/jad-simulator-practice-tool-screenshot-working-2019-1024x650.png)](https://www.youtube.com/watch?v=FuPFbeX_UWw)

  

OSRS Jad Simulator Practice Tool Download Preview Waves

Jad Simulator Practice Tool for Old School RuneScape. Watch my demo video here

What is this?

  

In Old School Runescape the best in slot cape for melee excluding the Infernal cape is the Fire Cape. The Fire Cape is a reward for completing a 63 Wave Player vs Monster challenge in the TzHaar Fight Caves. Most players want to get this done as quickly as possible due to its dominance in the cape slot. Although the Fight Caves have no level requirements a prayer level of 40 for Protect from Missiles and Protect form Magic is essential for completion without utilizing advanced mechanics like Tick eating.

  

The final monster and by far the hardest part of the Fight Caves it a level 703 TzTok-Jad. Jad has both a ranged and magic attack which he switches between at random. Failing to react to Jad’s attack switch even once will be the end of your Fight Cave run. Worst of all to even have a chance to see or practice Jad you’re going to need to climb all the way to Wave 63. This can take around 2 hours depending on your combat stats. Here is a clip of me getting dumpstered last year on my 4th/5th attempt https://clips.twitch.tv/WrongLazyDootHumbleLife

  

To help other players practice the sounds and animations of Jad I created a practice tool for the OSRS Fight Caves. It’s missing a lot of things and is a really basic mod of some decent RSPS source. Jad doesn’t have healers. Collision for units is completely off for the other waves. Sound effects are only in for Jad.

  

Demo: https://www.youtube.com/watch?v=FuPFbeX_UWw

  

Download: https://github.com/downthecrop/Jad-Practice/releases

  

Forum post: https://www.rune-server.ee/runescape-development/rs2-server/downloads/679199-jad-fight-cave-practice-tool-w-sounds.html
