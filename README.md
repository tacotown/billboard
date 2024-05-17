### Billboard

This tool allows you to edit Pole Position arcade roms to change the billboard
signs in the game.

Each billboard in Pole Position is paired with another sign. The following table
provides the information necessary to edit the desired sign(s):

| ROM Version   | MAME ROMs               | Primary                 | Secondary      | editor    |
|---------------|-------------------------|-------------------------|----------------|-----------|
| Atari (v1&v2) | poleposa1<br/>poleposa2 | Namco Amusement Creator | Atari Logo     | namco     |
| Atari (v1&v2) | poleposa1<br/>poleposa2 | Dig Dug                 | Pole Position  | digdug    |
| Atari (v1&v2) | poleposa1<br/>poleposa2 | Centipede               | USA            | centipede |
| World, Japan  | polepos<br/>poleposj    | Marlboro                | S.E.V. Marchal | marlboro  |
| World, Japan  | polepos<br/>poleposj    | Martini                 | Champion       | martini   |
| World, Japan  | polepos<br/>poleposj    | Pepsi                   | Agip           | pepsi     |

The billboards in Pole Position are rendered using two differently sized signs.
When you
approach
a sign in the game, you are seeing the smaller, quarter sized sign that is
scaled down initially,
and scales up until you get closer. Then, the image switches to the larger sign
scaled down,
and scales up until your car is right in front of it.

This tool allows you to edit and draw new images for the signs. 

NOTE: You cannot draw whatever images you like. You are limited to the following:

- Each sign only has a set amount of colors available, as shown by the color picker for that sign:

| Sign                    | Colors                               |
|-------------------------|--------------------------------------|
| Namco Amusement Creator | BLACK, BLUE, DARK RED, RED, WHITE    |
| Atari                   | PURPLE, YELLOW                       |
| Dig Dug                 | RED, WHITE                           |
| Pole Position           | BLACK, CYAN, DARK RED, ORANGE, WHITE |
| Centipede               | BLACK, GREEN, RED, WHITE             |
| USA                     | BLUE, RED, WHITE                     |
| Marlboro                | BLACK, RED, WHITE                    |
| S.E.V. Marchal          | BLACK, WHITE, YELLOW                 |
| Martini                 | BLACK, RED, WHITE, YELLOW            |
| Champion                | BLACK, RED, WHITE                    |
| Pepsi                   | CYAN, BLUE, RED, WHITE               |
| Agip                    | BLACK, RED, YELLOW                   |

- Each sign is paired with primary/secondary as show in the first table above.
- The World/Japan signs have additional drawing limitations due to the colors
of their palettes not having a "paired color" on the other sign. For
example, in the Marlboro/S.E.V. Marchal sign, the S.E.V. Marchal cat's eyes (YELLOW) overlaps
with the WHITE and BLACK on the Marlboro sign. However, RED never overlaps the YELLOW area,
and there is no binary representation available for the mapping of that color combination. 
Therefore, if you draw YELLOW on the S.E.V. Marchal sign, be aware you won't be able to draw RED over that area
on the Marlboro sign (and vice versa). None of the Atari ROM signs have this issue.

## Prerequisites

- In order to use the tool, you must have one of the ROM sets above.
- You must also have a java runtime on your computer. The project is built with
  Java 1.8 to provide the lowest barrier of entry, but anything greater than 1.8
  should work.

## To Download

An executable with the latest build can be found a https://www.darkstararcade.com/

## To build

To build yourself, you'll need a Java SDK 1.8 or above installed and configured.
Download the code using either ``git clone`` or downloading it directly.

type:
```mvnw package```

This will build the executable billboard.jar in the ``target`` folder.

## To run

You will need to specify values for the following flags:

- ``--romloc`` - the location of the rom directory from 'MAME ROM set' column in the table
  above (example: C:\Users\Bob\mame\ROMS\poleposj)
- ``--editor`` - the name of the billboard group to edit from the 'editor' column in the table
  above (example: To modify the Atari Logo sign, specify 'namco')

Change your directory to location of the executable jar (``target`` directory if you built it yourself)

To run, type
```java -jar billboard.jar --romloc <mame romset location> --editor <editor>```

Example:
```java -jar billboard.jar --romloc C:\Users\Bob\mame\ROMS\poleposj --editor martini```

This will bring up a graphical user interface displaying four tabs: the primary
billboard,
the secondary billboard, the primary smaller sign auto-generated, and the
secondary smaller sign
auto-generated.
Switch between the 'Primary' and 'Secondary' tabs to modify either sign or both.

The color picker on the toolbar allows you to pick the color to draw with. This palette is limited to the
colors of the original sign, so before drawing, consider which sign's palette
best fits
the goals of your design.

The toolbar also has minus and plus signs that allow you to change the
magnification
of the image to make it easier to see and edit.

There is also a clear
button that will set all the billboard pixels to the current color selected *if* there
is a mapping for that color. 

NOTE: For the World/Japan ROM billboards, if you try to draw a color and it doesn't seem
to be working, most likely the color on the other board doesn't have a mapping for that
color. This is best seen when you try to clear a color and a portion of the image does
not appear to clear.

## Import and Save Options

The drawing ability of the editor isn't that great, so there is an import available
so you can initially draw with the image editor of your choice instead. 

On the File menu, select "Import GIF" to choose a GIF image and import it into the editor.
The image size of the GIF must match the size of the billboard you are
trying to
edit. For a normal billboard, this will be 96 pixels wide by 64 pixels high. Due
to the
palette limitation, the import process will automatically match the closest
color from
the original image to the nearest color available in the palette for that sign.

There is also a "Save GIF" option that will save the current tab you have selected
as a GIF file.

Lastly, there is a "Save ROM" option that will save the edited images to ROM format. The ROMS
will be saved with ``_new`` as a suffix, leaving the original files untouched.

## Shrunken Imags

On the two remaining tabs, you'll find shrunken down versions of the larger
images. Remember, two signs are used to provide the "getting closer to a sign" effect.
Because the smaller sign is nothing more than the larger sign scaled down, this
tool
will automatically generate the smaller sign from the larger sign using a
simplistic
scale down algorithm. Therefore, you can be as creative as you want with the
larger
scale sign and not have to worry about redrawing the image at a smaller scale.
You can
see what these will look like by navigating to the "shrunken" tabs in the
editor.

However, if, for some reason you aren't happy with the smaller generated image,
you can directly edit the smaller version of the image by running the
application again,
but with a new flag:

```java -jar billboard.jar --romloc <mame romset location> --editor <editor> --mini```

This will allow you to edit the smaller (mid scaled version) of the image. However, the
benefits of additional edits are very hard to notice during game play, so doing further editing
of the smaller image doesn't have much of a payoff.

Once you are happy with what you've edited, you can save the images. The tool
will create new files with the suffix ``_new`` so that the original roms stay
intact.
I recommend backing up the original directory, then copying the original ROMS in your working directory with ``_orig`` suffix, so you have a
backup in case an error occurs (and to be able to use the scripts mentioned above).

## A few notes

- You'll notice you can't edit the outer edge of the billboards. I felt that
  since those parts were part of the overall sign structure, they should stay,
  so
  you still
  get that "angled sign" look. Because of this, images you import will have
  those areas of their imported images ignored; take that into account when
  designing an image to be imported. So, if you are importing a GIF, you actually only have about 88 x 60 pixes to work with.

- I left out editing the arrows signs. If you look at the code, it wouldn't be
  too
  hard to add the two classes needed to support them, but since they are
  critical to navigating the raceway,
  I didn't want people to be
  able to mess with them.

- Each billboard has its own limited palette, so you can't just make any image.
  I'm sure there's code somewhere else controlling the palettes, but
  figuring that out is beyond me. And even if you figured it out, you'd have to
  make some sort of "palette builder" and determine it before you could start
  drawing.
- The ROM checksums are not repaired after editing, therefore hardware or MAME may
complain about ROM checksums being incorrect.

- The application could be modified to further edit some of the smaller signs (eg.
Namco, Canon, Atari),
but, as the signs are smaller and don't give you as much room for creativity, I
left
them out for now, as the payoff wasn't large enough.

## Helpful tools

I created two sets of scripts in the 'scripts' directory for promoting edited images
for testing in MAME, and rolling back edits. I found thees very helpful in my
editing billboards workflow; you can use these or make your own.

## Typical workflow

Steps I typically follow when editing a billboard. I typically work with the Atari roms
so adjust as needed:
1. Figure out what sign meets my palette needs best and load it into the editor
2. Clear the image to the background I want
3. Edit the image (either edit in the program, or save as a .gif, edit it another program, and import the edited image).
4. Save the ROMS
5. Run commands (either by hand or script) to copy "_new" files in place of actual roms
6. Run MAME to view the results.

## TODO

If someone wants to submit some pull requests and contribute, here's some ideas:

- add more/better editing tools (shapes, cut and paste, move, etc.)
- support more signs or the smaller signs
- add checksum repair
- add "hidden" colors (some of the palette maps appear incomplete)
- Figure out the palette map encoding to see if different colors can be made to appear on different signs.

## BUG?

The in the World/Japan roms, the Pepsi sign has a CYAN dot in the blue that matches to a yellow pixel inside
the "P" in Agip. I can't help but think that was either a mistake or possibly a bad
ROM dump; the Pepsi pixel was meant to be BLUE and the pixel on the Agip sign is meant to be BLACK.



